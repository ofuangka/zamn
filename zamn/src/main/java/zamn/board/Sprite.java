package zamn.board;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 * This sprite is drawn from a spritesheet
 * 
 * @author ofuangka
 * 
 */
public class Sprite {

	private String spriteId;

	private BufferedImage spriteSheet;

	private int spriteSheetX;

	private int spriteSheetY;
	private Dimension spriteSize;
	public void drawSprite(BufferedImage spriteSheet, int spriteSheetX,
			int spriteSheetY, Dimension spriteSize) {
		this.spriteSheet = spriteSheet;
		this.spriteSheetX = spriteSheetX;
		this.spriteSheetY = spriteSheetY;
		setSpriteSize(spriteSize);
	}
	public BufferedImage getImage() {
		Dimension spriteSize = getSpriteSize();
		return spriteSheet.getSubimage(spriteSheetX * spriteSize.width,
				spriteSheetY * spriteSize.height, spriteSize.width,
				spriteSize.height);
	}

	public String getSpriteId() {
		return spriteId;
	}

	public Dimension getSpriteSize() {
		return spriteSize;
	}

	public void setSpriteId(String spriteId) {
		this.spriteId = spriteId;
	}

	public void setSpriteSize(Dimension spriteSize) {
		this.spriteSize = spriteSize;
	}

}
