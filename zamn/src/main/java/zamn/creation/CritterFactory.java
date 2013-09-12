package zamn.creation;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import zamn.board.piece.Critter;

public class CritterFactory {
	private Map<String, Integer[]> critterSpriteMap;
	private BufferedImage critterSpriteSheet;
	private Dimension spriteSize;

	public CritterFactory(ObjectMapper objectMapper,
			Resource critterSpriteMapResource) {

		try {
			SpriteMapDefinition spriteMapDefinition = objectMapper.readValue(
					critterSpriteMapResource.getURI().toURL(),
					SpriteMapDefinition.class);
			critterSpriteSheet = ImageIO.read(new ClassPathResource(
					spriteMapDefinition.getSpriteSheetClassPath()).getURI()
					.toURL());
			critterSpriteMap = spriteMapDefinition.getSpriteMap();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Critter get(String critterId, CritterDefinition critterDefinition) {
		Critter ret = new Critter();
		ret.setCritterId(critterId);
		String critterSpriteId = critterDefinition.getSpriteId();
		Integer[] spriteSheetXY = critterSpriteMap.get(critterSpriteId);
		ret.setSpriteId(critterSpriteId);
		ret.drawSprite(critterSpriteSheet, spriteSheetXY[0], spriteSheetXY[1],
				spriteSize);
		ret.setHostile(critterDefinition.isHostile());
		ret.setTalents(critterDefinition.getTalents());
		Map<String, Integer> statDefinitions = critterDefinition.getStats();
		Set<String> statKeySet = statDefinitions.keySet();
		for (String statKey : statKeySet) {
			ret.setStat(Critter.Stat.valueOf(statKey),
					statDefinitions.get(statKey));
		}
		return ret;
	}

	@Required
	public void setSpriteSize(Dimension spriteSize) {
		this.spriteSize = spriteSize;
	}
}
