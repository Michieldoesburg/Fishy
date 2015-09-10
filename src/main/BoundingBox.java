package main;

/**
 * An Axis-Aligned Bounding Box is a rectangle aligned with the X and Y axis, i.e. no rotation.
 * The BoundingBox is used as a hitbox for collision detection between fish.
 * @author Michiel
 *
 */
public class BoundingBox {
	private int x;
	private int y;
	private int width;
	private int height;
	
	public BoundingBox(int x, int y, int width, int height) {
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
	}
	
	public boolean intersects(BoundingBox other) {
		return !((this.getX() + this.getWidth()) < other.getX() || 
				this.getX() > (other.getX() + other.getWidth()) ||
					(this.getY() + this.getHeight()) < other.getY() ||
						this.getY() > (other.getY() + other.getHeight()));
	}
	
//-----------Getters and setters-------------------
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void updateX(int x) {
		this.x += x;
	}
	
	public void updateY(int y) {
		this.y += y;
	}
	
	public boolean equals(Object other) {
		if(!(other instanceof BoundingBox)) {
			return false;
		}
		
		if(this.x != ((BoundingBox) other).getX()) {
			return false;
		}
		
		if(this.y != ((BoundingBox) other).getY()) {
			return false;
		}
		
		if(this.width != ((BoundingBox) other).getWidth()) {
			return false;
		}
		
		if(this.height != ((BoundingBox) other).getHeight()) {
			return false;
		}
		
		return true;
	}
}