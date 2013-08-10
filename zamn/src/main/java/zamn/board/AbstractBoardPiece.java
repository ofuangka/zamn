package zamn.board;

/**
 * Adds the capability to be able to take up space
 * 
 * @author ofuangka
 * 
 */
public abstract class AbstractBoardPiece extends PositionedSpriteSheetSprite {

	private boolean takingUpSpace;

	public boolean isTakingUpSpace() {
		return takingUpSpace;
	}

	public void setTakingUpSpace(boolean takingUpSpace) {
		this.takingUpSpace = takingUpSpace;
	}

}
