package nl.tudelft.fishy;

import nl.tudelft.fishy.interfaces.PlayerFishInterface;

import java.util.ArrayList;

import javafx.scene.image.Image;

/**
 * This class represents the fish that the player controls in the game.
 * 
 * @author Clinton Cao, Michiel Doesburg, Matthijs Halvemaan, Dmitry Malarev,
 *         Sunwei Wang.
 */
public final class PlayerFish extends Entity implements PlayerFishInterface {

  private static PlayerFish singletonFish;
  private static String leftImageName = "/FishOriginal_transparent.png";
  private static String rightImageName = "/Fish_Right_Transparent.png";
  private ArrayList<FishBomb> bombs = new ArrayList<FishBomb>();
  private Image leftImage;
  private Image rightImage;
  private boolean isAlive;
  private int score;
  private int counter;
  private boolean hasLance;
  private int lives;

  /**
   * This is a private constructor now, so this ensures that 
   * PlayerFish class has only one instance.
   * 
   * @param movespeed
   *          .
   * @param isAlive
   *          .
   * @param sprite
   *          .
   * @param score
   * 
   * @see Entity#Entity(int, Sprite)
   */
  private PlayerFish(int movespeed, boolean isAlive, Sprite sprite, int score, boolean hasLance) {

    super(movespeed, sprite);
    setAlive(isAlive);
    setScore(score);

    if (sprite.getImg() != null) {

      int imgWidth = (int) sprite.getImg().getWidth();
      int imgHeight = (int) sprite.getImg().getHeight();

      setPlayerFishLeftImage(new Image(leftImageName, imgWidth, imgHeight,
          true, true));
      setPlayerFishRightImage(new Image(rightImageName, imgWidth, imgHeight,
          true, true));
    } else {

      setPlayerFishLeftImage(new Image(leftImageName, 128, 128, true, true));
      setPlayerFishRightImage(new Image(rightImageName, 128, 128, true, true));
    }
    
    lives = 1;
    
    setHasLance(hasLance);
  }

  /**
   * This method creates the fish the player controls. The image is scaled to
   * its starting size. A BoundingBox with the same dimensions is created and
   * placed at the middle of the screen.
   * This method is now private, so PlayerFish cannot be instantiate without 
   * getSingleton method.
   * 
   * @return a new PlayerFish object.
   */
  private static PlayerFish createPlayerFish() {

    Image temp = new Image(leftImageName);

    int startImageWidth = (int) (temp.getWidth() * 0.30);
    int startImageHeight = (int) (temp.getHeight() * 0.30);

    Image playerFishImage = new Image(leftImageName, startImageWidth,
        startImageHeight, true, true);

    int startPosX = Game.getScreenbox().getWidth() / 2;
    int startPosY = Game.getScreenbox().getHeight() / 2;

    BoundingBox boundingBox = new BoundingBox(startPosX, startPosY,
        startImageWidth, startImageHeight);

    Sprite sprite = new Sprite(playerFishImage, boundingBox);

    return new PlayerFish(10, true, sprite, 0, false);
  }

  /**
   * {@inheritDoc} A new scaled image is created, and the PlayerFish's
   * BoundingBox is scaled accordingly.
   */
  public void grow(double multiplier) {
    double newWidth = multiplier * this.getSprite().getImg().getWidth();
    double newHeight = multiplier * this.getSprite().getImg().getHeight();

    if (this.getSprite().getImg().equals(this.leftImage)) {
      this.setPlayerFishLeftImage(new Image(leftImageName, newWidth, newHeight, true, true));
      this.setPlayerFishRightImage(new Image(rightImageName, newWidth, newHeight, true, true));
      this.getSprite().setImg(leftImage);
    } else {
      this.setPlayerFishLeftImage(new Image(leftImageName, newWidth, newHeight, true, true));
      this.setPlayerFishRightImage(new Image(rightImageName, newWidth, newHeight, true, true));
      this.getSprite().setImg(rightImage);
    }
    BoundingBox playerFishBoundingBox = this.getSprite().getBoundingBox();

    playerFishBoundingBox.setWidth((int) this.getPlayerFishLeftImage().getWidth());
    playerFishBoundingBox.setHeight((int) this.getPlayerFishLeftImage().getHeight());
  }
  
  /**
   * Decrement life Counter.
   */
  public void decrementLives() {
	  lives--;
  }
  
  /**
   * Increment life counter.
   */
  public void incrementLives() {
	  lives++;
  }

  /**
   * {@inheritDoc} Width of the images is used for comparison.
   */
  public boolean playerDies(Entity entity) {
    return this.getSprite().getImg().getWidth() <= entity.getSprite().getImg().getWidth();
  }

  /**
   * {@inheritDoc} Check if the x-coordinate is less or equals to the 0.
   */
  public boolean intersectsLeftScreenEdge() {
    return this.getSprite().getBoundingBox().getX() <= 0;
  }

  /**
   * {@inheritDoc} Check if the x-coordinate + width is greater or equals to the
   * x-resolution.
   */
  public boolean intersectsRightScreenEdge() {
    return (this.getSprite().getBoundingBox().getX() 
             + this.getSprite().getBoundingBox().getWidth()) >= Game.getResX();
  }

  /**
   * {@inheritDoc} Check if the y-coordinate is less or equals to the 0.
   */
  public boolean intersectsUpperScreenEdge() {
    return this.getSprite().getBoundingBox().getY() <= 0;
  }

  /**
   * {@inheritDoc} Check if the y-coordinate + height is greater or equals to
   * the y-resolution.
   */
  public boolean intersectsUnderScreenEdge() {
    return (this.getSprite().getBoundingBox().getY() 
            + this.getSprite().getBoundingBox().getHeight()) >= Game.getResY();
  }

  // --- Getters and Setters ---

  public boolean isAlive() {
    return isAlive;
  }

  public void setAlive(boolean isAlive) {
    this.isAlive = isAlive;
  }

  public Image getPlayerFishLeftImage() {
    return leftImage;
  }

  public void setPlayerFishLeftImage(Image playerFishLeftImage) {
    this.leftImage = playerFishLeftImage;
  }

  public Image getPlayerFishRightImage() {
    return rightImage;
  }

  public void setPlayerFishRightImage(Image playerFishRightImage) {
    this.rightImage = playerFishRightImage;
  }

  public static String getPlayerFishLeftImageName() {
    return leftImageName;
  }

  public void setPlayerFishLeftImageName(String playerFishLeftImageName) {
    PlayerFish.leftImageName = playerFishLeftImageName;
  }

  public static String getPlayerFishRightImageName() {
    return rightImageName;
  }

  public void setPlayerFishRightImageName(String playerFishRightImageName) {
    PlayerFish.rightImageName = playerFishRightImageName;
  }

  /**
   * Set the score of the player.
   * 
   * @param number
   *          the score.
   */
  public void setScore(int number) {
    counter += (number - score);
    if (counter >= 100) {
      counter = 0;
      FishBomb bomb = FishBomb.createFishBomb(this);
      if (bombs.size() == 0) {
        bombs.add(bomb);
      }
    }
    score = number;
  }

  public int getCounter() {
	  return counter;
  }

  public int getLives() {
	  return lives;
  }

  public void setLives(int lives) {
	  this.lives = lives;
  }

  public void setCounter(int counter) {
	  this.counter = counter;
  }

  public int getScore() {
	  return score;
  }

  /**
   * @return true if player has lance.
   */
  public boolean hasLance() {
	  return hasLance;
  }

  public void setHasLance(boolean hasLance) {
	  this.hasLance = hasLance;
  }

  public ArrayList<FishBomb> getBombs() {
	  return bombs;
  }

  public void setBombs(ArrayList<FishBomb> items) {
    this.bombs = items;
  }

  /**
   * The getSingletonFish() method gives us a way to instantiate the PlayerFish
   * class and also to return an instance of it.
   * @return an instance of PlayerFish
   */
  public static synchronized PlayerFish getSingletonFish() {
    if (singletonFish == null) {
      singletonFish = createPlayerFish();
    }
    return singletonFish; 
  }
  
  
}
