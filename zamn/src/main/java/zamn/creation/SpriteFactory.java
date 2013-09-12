package zamn.creation;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import zamn.board.Sprite;

public class SpriteFactory {

	private BufferedImage spriteSheet;
	private Map<String, int[]> spriteMap;
	private Dimension spriteSize;

	public SpriteFactory(ObjectMapper objectMapper, Resource spriteMapResource)
			throws IOException {
		SpriteMapDefinition spriteMapDefinition = objectMapper.readValue(
				spriteMapResource.getURL(), SpriteMapDefinition.class);
		spriteSheet = ImageIO.read((new ClassPathResource(spriteMapDefinition
				.getSpriteSheetClassPath())).getURL());
		spriteMap = spriteMapDefinition.getSpriteMap();
	}

	public Sprite get(SpriteDefinition spriteDefinition) {
		Sprite ret = getNewSprite();

		// get the sprite information
		int[] spriteSheetXY = getSpriteMap()
				.get(spriteDefinition.getSpriteId());

		ret.drawSprite(getSpriteSheet(), spriteSheetXY[0], spriteSheetXY[1],
				getSpriteSize());

		return ret;
	}

	public BufferedImage getSpriteSheet() {
		return spriteSheet;
	}

	public Map<String, int[]> getSpriteMap() {
		return spriteMap;
	}

	@Required
	public void setSpriteSize(Dimension spriteSize) {
		this.spriteSize = spriteSize;
	}

	public Dimension getSpriteSize() {
		return spriteSize;
	}

	protected Sprite getNewSprite() {
		return new Sprite();
	}
}
