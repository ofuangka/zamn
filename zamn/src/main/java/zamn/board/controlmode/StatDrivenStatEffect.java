package zamn.board.controlmode;

import zamn.board.Critter;
import zamn.framework.event.IEventContext;

/**
 * An AbstractStatEffect whose effect modifier is driven by a Critter.Stat value
 * of the source
 * 
 * @author ofuangka
 * 
 */
public class StatDrivenStatEffect extends AbstractStatEffect {

	private final Critter.Stat drivingStat;

	public StatDrivenStatEffect(Critter.Stat drivingStat,
			Critter.Stat affectedStat, int min, int max, boolean positive,
			IEventContext eventContext) {
		super(affectedStat, min, max, positive, eventContext);
		this.drivingStat = drivingStat;
	}

	@Override
	public int getModifier() {
		return getCritter().getStat(drivingStat);
	}

}
