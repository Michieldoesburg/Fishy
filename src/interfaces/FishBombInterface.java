package interfaces;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.BoundingBox;
import main.Sprite;

/**
 * A FishBomb can be made to explode. EnemyFish within the explosion radius
 * will die.
 * 
 * @author Michiel
 */
public interface FishBombInterface {

	/**
	 * Update the x-coordinate of the FishBomb. Used for moving FishBombs over the screen.
	 *
	 * @param x - the new x-coordinate.
	 */
	void updateX(int mod);

	/**
	 * Update the y-coordinate of the FishBomb. Used for moving FishBombs over the screen.
	 *
	 * @param y - the new y-coordinate.
	 */
	void updateY(int mod);

	/**
	 * @param gc - GraphicsContext which will perform the rendering.
	 */
	void render(GraphicsContext gc);

	/**
	 * Check if the explosion radius of the FishBomb intersects a BoundingBox.
	 * @param bb.
	 * @return true if intersecting.
	 */
	boolean intersectsRectangle(BoundingBox bb);

	// --- Getters and Setters ---	

	/**
	 * @return the x-coordinate of the bomb.
	 */
	int getPosX();

	/**
	 * @param posX - the new x-coordinate.
	 */
	void setPosX(int posX);

	/**
	 * @return the y-coordinate of the bomb.
	 */
	int getPosY();

	/**
	 * @param posY - the new y-coordinate.
	 */
	void setPosY(int posY);

	/**
	 * @return the Image for the explosion.
	 */
	Image getExplosionImg();

	/**
	 * @param explosionImg - the new Image for the explosion.
	 */
	void setExplosionImg(Image explosionImg);

	/**
	 * @param sprite - the new Sprite.
	 */
	void setSprite(Sprite sprite);

	/**
	 * @return the Sprite.
	 */
	Sprite getSprite();

	/**
	 * @return the radius.
	 */
	int getRadius();

	/**
	 * @param radius - the new radius.
	 */
	void setRadius(int radius);
}
