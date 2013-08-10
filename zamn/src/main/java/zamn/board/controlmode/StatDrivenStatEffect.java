package zamn.board.controlmode;

import zamn.board.piece.Critter;
import zamn.framework.event.IEventContext;

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
		return getSource().getStat(drivingStat);
	}

}
