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

	private BufferedImage spriteSheet;
	private int spriteSheetX;
	private int spriteSheetY;

	public void applySprite(BufferedImage spriteSheet, int spriteSheetX,
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

}
