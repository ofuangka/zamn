package zamn.creation;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import zamn.board.piece.Critter;

public class CritterFactory {
	private Map<String, CritterDefinition> critterDefinitions;
	private Map<String, Integer[]> critterSpriteMap;
	private BufferedImage critterSpriteSheet;
	private Dimension spriteSize;

	public CritterFactory(Resource critterDefinitionResource,
			Resource critterSpriteMapResource, ObjectMapper objectMapper) {

		try {
			critterDefinitions = objectMapper.readValue(
					critterDefinitionResource.getURI().toURL(),
					new TypeReference<Map<String, CritterDefinition>>() {
					});
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

	public Critter get(String id) {
		Critter ret = new Critter();
		CritterDefinition critterDefinition = critterDefinitions.get(id);
		if (critterDefinition == null) {
			throw new IllegalArgumentException(
					"Could not find CritterDefinition with ID: '" + id + "'");
		}
		Integer[] spriteSheetXY = critterSpriteMap.get(critterDefinition
				.getSpriteId());
		ret.applySprite(critterSpriteSheet, spriteSheetXY[0], spriteSheetXY[1],
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

	public Set<String> idSet() {
		return critterDefinitions.keySet();
	}

	@Required
	public void setSpriteSize(Dimension spriteSize) {
		this.spriteSize = spriteSize;
	}
}
