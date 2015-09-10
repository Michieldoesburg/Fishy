package main;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * A sprite represents the model of an entity on the screen. It has a position in X and Y coordinates,
 * a certain velocity in a direction expressed by X and Y coordinates, A width and a height, and an image to draw on the screen.
 * @author Clinton Cao, Dmity Malarev, Matthijs Halvemaan, Sunwei Wang, Michiel Doesburg.
 */
public class Sprite {
	private Image img;
	private BoundingBox boundingBox;
	
	public Sprite(Image img, BoundingBox boundingBox) {
		this.img = img;
		setAabb(boundingBox);	
	}
	
	/**
	 * Update the x coordinate of the sprite
	 * @param x integer, x coordinate
	 */
	public void updateX(int x) {
		boundingBox.updateX(x);
	}
	
	/**
	 * Update the y coordinate of the sprite
	 * @param y integer, y coordinate
	 */
	public void updateY(int y) {
		boundingBox.updateY(y);
	}
	
	/**
	 * renders the image
	 * @param gc GraphicsContext
	 */
	public void render(GraphicsContext gc) {
		gc.drawImage(img, boundingBox.getX(), boundingBox.getY());
	}
	
	/**
	 * test whether this sprite intersects with the other sprite
	 * @param s the other Sprite
	 * @return a boolean whether this sprite intersects with other sprite
	 */
	public boolean intersects(Sprite s) {
		return s.getAabb().intersects(this.getAabb());
	}
	
//------------Getters and setters-------------------------------------------
	public Image getImg() {
		return this.img;
	}
	
	public void setImg(Image img) {
		this.img = img;
	}

	public BoundingBox getAabb() {
		return boundingBox;
	}

	public void setAabb(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}

}
