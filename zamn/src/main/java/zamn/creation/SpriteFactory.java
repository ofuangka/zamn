package zamn.creation;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import zamn.board.Sprite;

public class SpriteFactory {

	private BufferedImage spriteSheet;
	private Map<String, int[]> spriteMap;
	private Map<String, String> reverseLookupMap;
	private Dimension spriteSize;

	public SpriteFactory(ObjectMapper objectMapper, Resource spriteMapResource)
			throws IOException {
		SpriteMapDefinition spriteMapDefinition = objectMapper.readValue(
				spriteMapResource.getURL(), SpriteMapDefinition.class);
		spriteSheet = ImageIO.read((new ClassPathResource(spriteMapDefinition
				.getSpriteSheetClassPath())).getURL());
		spriteMap = spriteMapDefinition.getSpriteMap();

		createReverseLookupMap();
	}

	public void createReverseLookupMap() {
		reverseLookupMap = new HashMap<String, String>();

		Set<String> spriteKeys = spriteMap.keySet();
		for (String key : spriteKeys) {
			reverseLookupMap.put(getReverseLookupKey(spriteMap.get(key)), key);
		}
	}

	public void drawSprite(SpriteDefinition spriteDefinition, Sprite sprite) {

		// get the sprite information
		int[] spriteSheetXY = getSpriteMap()
				.get(spriteDefinition.getSpriteId());

		sprite.drawSprite(getSpriteSheet(), spriteSheetXY[0], spriteSheetXY[1],
				getSpriteSize());
	}

	public Sprite get(SpriteDefinition spriteDefinition) {
		Sprite ret = getNewSprite();
		drawSprite(spriteDefinition, ret);
		return ret;
	}

	public String getReverseLookupKey(int[] xy) {
		return xy[0] + "," + xy[1];
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

	public String reverseLookup(int x, int y) {
		return reverseLookupMap.get(getReverseLookupKey(new int[] { x, y }));
	}
}
