package zamn.board;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 * This sprite is drawn from a spritesheet
 * 
 * @author ofuangka
 * 
 */
public class SpriteSheetSprite extends AbstractSprite {

	private String spriteId;
	private BufferedImage spriteSheet;
	private int spriteSheetX;
	private int spriteSheetY;

	public void drawSprite(BufferedImage spriteSheet, int spriteSheetX,
			int spriteSheetY, Dimension spriteSize) {
		this.spriteSheet = spriteSheet;
		this.spriteSheetX = spriteSheetX;
		this.spriteSheetY = spriteSheetY;
		setSpriteSize(spriteSize);
	}

	@Override
	public BufferedImage getImage() {
		Dimension spriteSize = getSpriteSize();
		return spriteSheet.getSubimage(spriteSheetX * spriteSize.width,
				spriteSheetY * spriteSize.height, spriteSize.width,
				spriteSize.height);
	}

	public String getSpriteId() {
		return spriteId;
	}

	public void setSpriteId(String spriteId) {
		this.spriteId = spriteId;
	}

}
