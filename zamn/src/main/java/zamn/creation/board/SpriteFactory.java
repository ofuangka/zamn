package zamn.creation.board;

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

	private Map<String, String> reverseLookupMap;
	private Map<String, int[]> spriteMap;
	private BufferedImage spriteSheet;
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

	public void drawSprite(String spriteId, Sprite sprite) {

		// get the sprite information
		int[] spriteSheetXY = getSpriteMap().get(spriteId);

		sprite.drawSprite(getSpriteSheet(), spriteSheetXY[0], spriteSheetXY[1],
				getSpriteSize());

		sprite.setSpriteId(spriteId);
	}

	public Sprite get(String spriteId) {
		Sprite ret = getNewSprite();
		drawSprite(spriteId, ret);
		return ret;
	}

	public Sprite get(SpriteDefinition spriteDefinition) {
		return get(spriteDefinition.getSpriteId());
	}

	protected Sprite getNewSprite() {
		return new Sprite();
	}

	public String getReverseLookupKey(int[] xy) {
		return xy[0] + "," + xy[1];
	}

	public Map<String, int[]> getSpriteMap() {
		return spriteMap;
	}

	public BufferedImage getSpriteSheet() {
		return spriteSheet;
	}

	public Dimension getSpriteSize() {
		return spriteSize;
	}

	public String reverseLookup(int x, int y) {
		return reverseLookupMap.get(getReverseLookupKey(new int[] { x, y }));
	}

	@Required
	public void setSpriteSize(Dimension spriteSize) {
		this.spriteSize = spriteSize;
	}
}
