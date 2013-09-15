package zamn.creation.board;

public class ExitDefinition {
	private String boardId;
	private String dir;
	private int entryPoint;
	private int x;
	private int y;

	public String getBoardId() {
		return boardId;
	}

	public String getDir() {
		return dir;
	}

	public int getEntryPoint() {
		return entryPoint;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public void setEntryPoint(int entryPoint) {
		this.entryPoint = entryPoint;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
