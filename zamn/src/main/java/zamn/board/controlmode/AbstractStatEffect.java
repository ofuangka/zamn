package zamn.board.controlmode;

import java.util.Random;

import org.apache.log4j.Logger;

import zamn.board.AbstractBoardPiece;
import zamn.board.Tile;
import zamn.board.piece.Critter;
import zamn.board.piece.Critter.Stat;
import zamn.framework.event.GameEventContext;
import zamn.framework.event.IEventContext;

/**
 * This class represents an HP reducing attack whose damage is driven by a
 * Critter.Stat
 * 
 * @author ofuangka
 * 
 */
public abstract class AbstractStatEffect extends AbstractEffect {

	private static final Logger LOG = Logger
			.getLogger(AbstractStatEffect.class);

	private final Critter.Stat affectedStat;
	private IEventContext eventContext;
	private final int max;
	private final int min;
	private final boolean positive;

	/**
	 * The attack damage is calculated a follows: driverValue + random(0,
	 * damageRange) + modifier
	 * 
	 * @param driver
	 * @param affectedStat
	 *            - The target stat to affect
	 * @param effectRange
	 *            - An int representing the range of possible damage values
	 * @param modifier
	 *            - An int that is always added to the resulting calculation
	 * @param eventContext
	 */
	public AbstractStatEffect(Stat affectedStat, int min, int max,
			boolean positive, IEventContext eventContext) {
		this.affectedStat = affectedStat;
		this.min = min;
		this.max = max;
		this.positive = positive;
		this.eventContext = eventContext;
	}

	public abstract int getModifier();

	/**
	 * This method checks that a Critter is occupying the target tile. If so, it
	 * calculates the damage done by the attack and subtracts from the target's
	 * HP. If the target has died, it fires a CRITTER_DEATH event
	 */
	@Override
	public void interact(Tile targetTile) {

		if (targetTile.isOccupied()) {

			AbstractBoardPiece targetPiece = targetTile.getOccupant();

			if (Critter.class.isAssignableFrom(targetPiece.getClass())) {

				Critter target = (Critter) targetPiece;

				Random random = eventContext.getRandom();
				int roll = (min + random.nextInt(max - min) + getModifier())
						* ((positive) ? -1 : 1);

				int maxValue = -1;
				int newValue = target.getStat(affectedStat) - roll;

				if (Critter.Stat.HP.equals(affectedStat)) {
					maxValue = target.getStat(Critter.Stat.MAXHP);
				}

				if (maxValue != -1 && maxValue < newValue) {
					newValue = maxValue;
				}

				LOG.debug("Target '" + target + "'." + affectedStat + " = "
						+ newValue);

				target.setStat(affectedStat, newValue);

				if (target.getStat(Critter.Stat.HP) <= 0) {
					eventContext.fire(
							GameEventContext.GameEventType.CRITTER_DEATH,
							target);
				}
			} else {
				LOG.debug("Occupant is not a Critter");
			}
		} else {
			LOG.debug("No occupant in Tile " + targetTile);
		}
	}
}