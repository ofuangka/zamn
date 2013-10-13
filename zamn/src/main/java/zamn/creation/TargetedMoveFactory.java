package zamn.creation;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.core.io.Resource;

import zamn.board.Critter;
import zamn.board.Tile;
import zamn.board.Critter.Stat;
import zamn.board.controlmode.AbstractEffect;
import zamn.board.controlmode.StatDrivenStatEffect;
import zamn.board.controlmode.TargetedMove;
import zamn.board.tilecollector.AbstractDecayingTileCollector;
import zamn.board.tilecollector.ITileCollector;
import zamn.board.tilecollector.TileCollectorType;
import zamn.board.tilecollector.TileListFilter;
import zamn.framework.event.IEventContext;

public class TargetedMoveFactory {

	private final IEventContext eventContext;
	private final Map<String, TargetedMoveDefinition> targetedMoveDefinitions;

	public TargetedMoveFactory(Resource resource, ObjectMapper objectMapper,
			IEventContext eventContext) {
		try {
			targetedMoveDefinitions = objectMapper.readValue(resource.getURI()
					.toURL(),
					new TypeReference<Map<String, TargetedMoveDefinition>>() {
					});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.eventContext = eventContext;
	}

	public TargetedMove get(String id, Critter critter) {
		TargetedMoveDefinition targetedMoveDefinition = targetedMoveDefinitions
				.get(id);
		if (targetedMoveDefinition == null) {
			throw new IllegalArgumentException(
					"Unable to find move definition with ID: '" + id + "'");
		}
		ITileCollector targetedRange = getTileCollector(targetedMoveDefinition
				.getTargetingRange());
		TileListFilter actualRangeFilter = TileListFilter
				.valueOf(targetedMoveDefinition.getActualRangeFilter());
		ITileCollector areaOfEffect = getTileCollector(targetedMoveDefinition
				.getAreaOfEffect());
		AbstractEffect effect = getEffect(targetedMoveDefinition.getEffect());
		effect.setCritter(critter);
		return new TargetedMove(targetedMoveDefinition.getName(),
				targetedRange, actualRangeFilter, areaOfEffect, effect,
				targetedMoveDefinition.getMpCost(),
				targetedMoveDefinition.getSoundClassPath());

	}

	protected AbstractEffect getEffect(EffectDefinition def) {
		return new StatDrivenStatEffect(Stat.valueOf(def.getDrivingStat()),
				Stat.valueOf(def.getAffectedStat()), def.getMin(),
				def.getMax(), def.isPositive(), eventContext);
	}

	protected ITileCollector getTileCollector(final TileCollectorDefinition def) {
		ITileCollector ret = null;
		switch (TileCollectorType.valueOf(def.getType())) {
		case DECAYING: {
			ret = new AbstractDecayingTileCollector(TileListFilter.valueOf(def
					.getFilter())) {

				@Override
				protected int getCostThreshold() {
					return def.getRange();
				}

				@Override
				protected int getTileCost(Tile tile) {
					return 1;
				}

				@Override
				protected boolean isInclusive() {
					return def.isInclusive();
				}

				@Override
				protected boolean isTileValid(Tile tile) {
					return tile != null;
				}

			};
		}
		}
		return ret;
	}
}
