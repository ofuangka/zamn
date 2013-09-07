package zamn.board;

/**
 * Adds the capability to be able to take up space
 * 
 * @author ofuangka
 * 
 */
public class BoardPiece extends SpriteSheetSprite {

	public static final int DEFAULT_X = AbstractBoard.INVALID_X;
	public static final int DEFAULT_Y = AbstractBoard.INVALID_Y;
	public static final int DEFAULT_Z = 0;

	private boolean solid;
	private int x = DEFAULT_X;
	private int y = DEFAULT_Y;

	private int z = DEFAULT_Z;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public boolean isSolid() {
		return solid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setZ(int z) {
		this.z = z;
	}

}
