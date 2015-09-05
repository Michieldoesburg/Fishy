package main;

import javafx.scene.image.Image;

public class EnemyFish extends Entity {
	private boolean isLefty;
	
	public EnemyFish(int movespeed, boolean isLefty, Sprite sprite) {
		super(movespeed, sprite);
		this.isLefty = isLefty;
	}
	
	public static EnemyFish generateFish() {
		return new EnemyFish(5, true, new Sprite(new Image("Fish.png"), new AABB(0, 0, 128, 128)));
	}

	public boolean isLefty() {
		return isLefty;
	}

	public void setLefty(boolean isLefty) {
		this.isLefty = isLefty;
	}
}
