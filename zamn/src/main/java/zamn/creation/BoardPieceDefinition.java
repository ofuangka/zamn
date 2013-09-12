package zamn.creation;

public class BoardPieceDefinition extends SpriteDefinition {
	private int[] coords;
	private boolean solid;

	public void setCoords(int[] coords) {
		this.coords = coords;
	}

	public int[] getCoords() {
		return coords;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public boolean isSolid() {
		return solid;
	}
}
