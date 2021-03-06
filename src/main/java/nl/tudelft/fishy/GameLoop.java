package nl.tudelft.fishy;

import nl.tudelft.fishy.controllers.MainScreenController;
import nl.tudelft.fishy.factories.AnimationTimerFactory;
import nl.tudelft.fishy.factories.EntityFactory;
import nl.tudelft.fishy.factories.ItemFactory;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Pair;

/**
 * GameLoop contains all the data and logic pertaining to the gameloop.
 * @author Michiel
 *
 */
@SuppressWarnings("PMD")
public class GameLoop {

  private static PlayerFish playerFish;
  private static CompositeEnemyFish compositeEnemyFish = new CompositeEnemyFish();
  private static int currScore;
  private static ArrayList<String> input;
  private static EndBoss endBoss = (EndBoss) EntityFactory.getEntityFactory().getEntity("BOSS");
  private Life life;
  private static Lance lance;
  private static boolean bossMode;
  private AnimationTimer fAnimationTimer;
  private static int frames;
  private GraphicsContext gc;

  public static final double MULTIPLIER = 1.05;

  /**
   * Constructor.
   * @param gc 
   * - The graphicsContext which needs to do the rendering.
   */
  public GameLoop(GraphicsContext gc) {
    EntityFactory entityFactory = EntityFactory.getEntityFactory();
    ItemFactory itemFactory = ItemFactory.getItemFactory();
    playerFish = (PlayerFish) entityFactory.getEntity("PLAYER");
    frames = 0;
    lance = (Lance) itemFactory.createItem("LANCE", playerFish);
    playerFish.getBombs().add(
        (FishBomb) itemFactory.createItem("FISHBOMB", playerFish));
    input = new ArrayList<String>();
    frames = 0;
    if (!Game.isPlayingNewGamePlus()) {
      currScore = 0;
    }
    bossMode = false;
    life = (Life) itemFactory.createItem("LIFE", playerFish);
    setLance((Lance) itemFactory.createItem("LANCE", playerFish));
    setBossMode(false);

    if (Game.isPlayingNewGamePlus()) {
      playerFish.setScore(currScore);
    }

    this.gc = gc;
    
    AnimationTimerFactory animationTimerFactory = AnimationTimerFactory.getAnimationTimerFactory();
    fAnimationTimer = animationTimerFactory.makeAnimationTimer(compositeEnemyFish);
  }

  /**
   * 'Wrapper' method called by AnimationTimerFactory.
   * Runs all the game logic methods of GameLoop.
   */
  public void runGameLoop() {
    turnOnBossMode();

    playerWins();

    spawnLifeItem();

    playerDiesToBoss();

    MainScreenController.renderStatics(gc);

    handleBoss(gc);

    handleWeapon(gc);

    handlePlayerInput(gc);

    generateEnemyFish();

    playerPicksUpLance();

    compositeEnemyFish.removeOffScreenEnemyFish(Game.getScreenbox());

    playerIntersectsFish();     

    renderNonStatics(gc);

    updateFrames();

  }

  /**
   * Render all the non static elements, i.e. the enemy fish and the player
   * fish.
   * 
   * @param gc
   *          - the graphicsContext which needs to do the rendering.
   */
  private void renderNonStatics(GraphicsContext gc) {
    playerFish.getSprite().render(gc);
    life.getSprite().render(gc);
    life.move(5);

    compositeEnemyFish.render(gc);
    compositeEnemyFish.move();
  }

  /**
   * Handle movement, rendering and collisions of the end boss.
   * 
   * @param gc
   *          - the GraphicsContext.
   */
  private void handleBoss(GraphicsContext gc) {
    BoundingBox endBossbb = endBoss.getSprite().getBoundingBox();
    BoundingBox playerFishbb = playerFish.getSprite().getBoundingBox();

    if (bossMode) {
      endBoss.getSprite().render(gc);
      if (endBoss.isLefty()) {
        endBoss.getSprite().updateX(getEndBoss().getMoveSpeed());
      } else {
        endBoss.getSprite().updateX(-getEndBoss().getMoveSpeed());
      }

      int playerX = playerFishbb.getX();
      int endBossX = endBossbb.getX();
      boolean endBossOutsideScreenBox = !endBossbb.intersects(Game.getScreenbox());
      boolean playerLeftOfEndBoss = playerX < endBossX;
      boolean playerRightOfEndBoss = endBossX < playerX;

      if (endBossOutsideScreenBox
          && (playerLeftOfEndBoss || playerRightOfEndBoss)) {
        endBoss.switchDirection();
      }
    } else {
      endBossbb.setX(-2000);
      endBossbb.setY(-2000);
    }
  }

  /**
   * Generate Life Item and spawn it on screen.
   * Handle collisions with player and removal when going off screen.
   */
  private void spawnLifeItem() {

    BoundingBox bb = life.getSprite().getBoundingBox();

    if (frames % 600 == 0 && playerFish.getLives() < 3) {
      bb.setX(1);
      bb.setY(Game.getScreenbox().getHeight() * 3 / 4);
    }

    if (!bb.intersects(Game.getScreenbox())) {
      bb.setX(-2000);
      bb.setY(-2000);
    }

    if (playerFish.getSprite().getBoundingBox().intersects(bb)) {
      playerFish.setScore(1000);
      bb.setX(-2000);
      bb.setY(-2000);
      playerFish.incrementLives();
    }
  }

  /**
   * Check if the player has won the game.
   * 
   * @return true if the player is bigger than a certain size.
   */
  private static boolean playerHasWon() {
    return playerFish.intersects(endBoss) && playerFish.hasLance();
  }

  /**
   * Handle movement, rendering and collisions of the weapon.
   * 
   * @param gc
   *          - the GraphicsContext.
   */
  private void handleWeapon(GraphicsContext gc) {
    if (bossMode) {
      lance.getSprite().render(gc);

      if (lance.isLefty()) {
        lance.getSprite().updateX(5);
      } else {
        lance.getSprite().updateX(-5);
      }

      BoundingBox playerFishbb = playerFish.getSprite().getBoundingBox();
      BoundingBox lancebb = lance.getSprite().getBoundingBox();

      int playerX = playerFishbb.getX();
      int lanceX = lancebb.getX();
      boolean lanceOutsideScreenBox = !lancebb.intersects(Game.getScreenbox());
      boolean playerLeftOfLance = playerX < lanceX;
      boolean playerRightOfLance = lanceX < playerX;

      if (lanceOutsideScreenBox && (playerLeftOfLance || playerRightOfLance)) {
        lance.switchDirection();
      }
    }
  }

  /**
   * This method handles the WASD input of the player. And any other input, like
   * X for using item.
   */
  private void handlePlayerInput(GraphicsContext gc) {
    ArrayList<FishBomb> playerBombs = playerFish.getBombs();
    if (input.contains("A") && !playerFish.intersectsLeftScreenEdge()) {
      playerFish.getSprite().setImg(playerFish.getPlayerFishLeftImage());
      playerFish.getSprite().updateX(-playerFish.getMoveSpeed());
      for (int i = 0; i < playerBombs.size(); i++) {
        playerBombs.get(i).updateX(-playerFish.getMoveSpeed());
      }
      Game.getLogger().logKeyPress("A");
      Game.getLogger().logDirectionChange("left");

    } else if (input.contains("D") && !playerFish.intersectsRightScreenEdge()) {
      playerFish.getSprite().setImg(playerFish.getPlayerFishRightImage());
      playerFish.getSprite().updateX(playerFish.getMoveSpeed());
      for (int i = 0; i < playerBombs.size(); i++) {
        playerBombs.get(i).updateX(playerFish.getMoveSpeed());
      }
      Game.getLogger().logKeyPress("D");
      Game.getLogger().logDirectionChange("right");
    }

    if (input.contains("W") && !playerFish.intersectsUpperScreenEdge()) {
      playerFish.getSprite().updateY(-playerFish.getMoveSpeed());
      for (int i = 0; i < playerBombs.size(); i++) {
        playerBombs.get(i).updateY(-playerFish.getMoveSpeed());
      }
      Game.getLogger().logKeyPress("W");
      Game.getLogger().logDirectionChange("upwards");

    } else if (input.contains("S") && !playerFish.intersectsUnderScreenEdge()) {
      playerFish.getSprite().updateY(playerFish.getMoveSpeed());
      for (int i = 0; i < playerBombs.size(); i++) {
        playerBombs.get(i).updateY(playerFish.getMoveSpeed());
      }
      Game.getLogger().logKeyPress("S");
      Game.getLogger().logDirectionChange("downwards");
    }

    if (input.contains("X") && playerFish.getBombs().size() > 0) {
      int index = playerFish.getBombs().size() - 1;
      FishBomb fishBomb = (FishBomb) playerFish.getBombs().get(index);
      Image explosionImg = playerFish.getBombs().get(index).getExplosionImg();
      int imgPosX = (int) (fishBomb.getPosX() - 0.25 * explosionImg.getWidth());
      int imgPosY = (int) (fishBomb.getPosY() - 0.25 * explosionImg.getHeight());
      gc.drawImage(explosionImg, imgPosX, imgPosY);

      compositeEnemyFish.handleFishBomb(fishBomb);

      playerFish.getBombs().remove(index);
    }

  }

  /** 
   * Update the player's score according to the size of the Enemy Fish.
   * @param enemyFish is the enemy fish
   */
  public static void updateScore(Entity enemyFish) {
    Sprite sprite = enemyFish.getSprite();
    BoundingBox box = sprite.getBoundingBox();
    int height = box.getHeight();
    int width = box.getWidth();

    playerFish.grow(MULTIPLIER);
    int score = (height * width) / 100;
    Game.getLogger().logPlayerFishGrows(score);
    currScore = currScore + score;
    Game.getLogger().logNewScore(currScore);
    playerFish.setScore(currScore);

    if (currScore > Game.getHighScore()) {
      Game.setHighScore(currScore);
    }
  }

  /**
   * This method is being called when the player fish collide with a large enemy
   * fish, and then the game is proceed to losing screen.
   */
  private static void playerLost() {
    Game.getLogger().logPlayerFishDies();
    Game.getLogger().logGameResult("lost", currScore);
    currScore = 0;
    playerFish.setScore(currScore);

    Game.setNewGamePlusMode(false);
    Game.getMediaPlayer().stop();

    Game.switchScreen("/LosingScreen.fxml");
    Game.getLogger().logSwitchScreen("LosingScreen");
    bossMode = false;
    playerFish.setHasLance(false);

    compositeEnemyFish.clear();
  }

  /**
   * Generates a new enemy fish every 90 frames.
   */
  private void generateEnemyFish() {
    EntityFactory entityFactory = EntityFactory.getEntityFactory();
    if ((frames % 90 == 0) && !isBossMode()) {
      compositeEnemyFish.add((EnemyFish) entityFactory.getEntity("ENEMY"));
      Game.getLogger().logEdgeBump(playerFish);
    }
  }

  /**
   * Increment frames.
   */
  private void updateFrames() {
    frames++;
  }

  /**
   * Checks if the player has picked up the lance.
   * If it has, it handles the necessary steps.
   */
  private void playerPicksUpLance() {
    BoundingBox lancebb = lance.getSprite().getBoundingBox();
    Sprite pfSprite = playerFish.getSprite();
    BoundingBox pfbb = pfSprite.getBoundingBox();
    if (pfbb.intersects(lancebb)) {

      lancebb.setX(-2000);
      lancebb.setY(-2000);

      Image pfImg = pfSprite.getImg();
      int playerFishSizeX = (int) pfImg.getWidth();
      int playerFishSizeY = (int) pfImg.getHeight();

      Image leftImg = new Image("/FishKnightLeft.png", playerFishSizeX,
          playerFishSizeY, true, true);
      Image rightImg = new Image("/FishKnightRight.png", playerFishSizeX,
          playerFishSizeY, true, true);

      playerFish.setPlayerFishLeftImage(leftImg);
      playerFish.setPlayerFishRightImage(rightImg);

      playerFish.setHasLance(true);

      playerFish.getSprite().setImg(leftImg);
    }
  }

  /**
   * Checks for intersections between playerfish and enemyfish.
   */
  private void playerIntersectsFish() {
    Pair<Integer, Boolean> res = compositeEnemyFish.intersectsPlayerFish(playerFish);

    if (res.getKey() != -1) {
      if (res.getValue()) {
        fAnimationTimer.stop();
        playerLost();
      } else {
        compositeEnemyFish.remove(res.getKey());
        playerFish.grow(MULTIPLIER);
      }
    }
  }

  /**
   * Checks if the player dies to the boss.
   */
  private void playerDiesToBoss() {
    BoundingBox endBossbb = endBoss.getSprite().getBoundingBox();

    if (playerFish.intersects(endBoss) && !playerFish.hasLance()) {
      endBossbb.setX(-2000);
      endBossbb.setY(-2000);

      fAnimationTimer.stop();
      playerLost();
    }
  }

  /**
   * Checks if the player wins.
   */
  private void playerWins() {
    BoundingBox endBossbb = endBoss.getSprite().getBoundingBox();

    if (playerHasWon()) {
      fAnimationTimer.stop();
      Game.switchScreen("/WinningScreen.fxml");
      Game.getMediaPlayer().stop();
      Game.getLogger().logSwitchScreen("WinningScreen");

      bossMode = false;
      playerFish.setHasLance(false);

      endBossbb.setX(-2000);
      endBossbb.setY(-2000);
    }
  }

  /**
   * Checks if the game should enter boss mode. 
   * If it does, it handles it.
   */
  private void turnOnBossMode() {
    BoundingBox endBossbb = endBoss.getSprite().getBoundingBox();
    BoundingBox lancebb = lance.getSprite().getBoundingBox();
    Sprite pfSprite = playerFish.getSprite();
    BoundingBox pfbb = pfSprite.getBoundingBox();     

    if (pfbb.getWidth() > 90) {
      if (endBossbb.getX() == -2000) {
        endBossbb.setX(0);
        endBossbb.setY(0);
      }
      if ((lancebb.getX() == -2000) && !playerFish.hasLance()) {
        lancebb.setX(0);
        BoundingBox screenBox = Game.getScreenbox();
        lancebb.setY(screenBox.getHeight() / 4 * 3);
      }
      bossMode = true;
    }
  }

  // --- Getters and Setters ---

  public void setCurrScore(int score) {
    currScore = score;
  }

  public int getCurrScore() {
    return currScore;
  }

  public PlayerFish getPlayerFish() {
    return playerFish;
  }

  public EndBoss getEndBoss() {
    return endBoss;
  }

  public void setLance(Lance dlance) {
    lance = dlance;
  }

  public boolean isBossMode() {
    return bossMode;
  }

  public void setBossMode(boolean dbossMode) {
    bossMode = dbossMode;
  }

  public AnimationTimer getAnimationTimer() {
    return fAnimationTimer;
  }

  public static ArrayList<String> getInput() {
    return input;
  }

  public int getPlayerFishLives() {
    return playerFish.getLives();
  }

}
