package zamn.creation.board;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.Resource;

import zamn.board.Critter;
import zamn.board.Sprite;

public class CritterFactory extends BoardPieceFactory {

	public CritterFactory(ObjectMapper objectMapper, Resource spriteMapResource)
			throws IOException {
		super(objectMapper, spriteMapResource);
	}

	@Override
	public Sprite get(SpriteDefinition spriteDefinition) {
		Critter ret = (Critter) super.get(spriteDefinition);

		CritterDefinition critterDefinition = (CritterDefinition) spriteDefinition;
		ret.setHostile(critterDefinition.isHostile());
		ret.setNmuValue(critterDefinition.getNmuValue());
		ret.setTalents(critterDefinition.getTalents());
		Map<String, Integer> statDefinitions = critterDefinition.getStats();
		Set<String> statKeySet = statDefinitions.keySet();
		for (String statKey : statKeySet) {
			ret.setStat(Critter.Stat.valueOf(statKey),
					statDefinitions.get(statKey));
		}
		return ret;
	}

	@Override
	protected Sprite getNewSprite() {
		return new Critter();
	}
}
