package zamn.creation.board;

public class BoardPieceDefinition extends SpriteDefinition {
	private int[] coords;
	private boolean solid;

	public int[] getCoords() {
		return coords;
	}

	public boolean isSolid() {
		return solid;
	}

	public void setCoords(int[] coords) {
		this.coords = coords;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}
}
