package zamn.creation;

public class ExitDefinition {
	private String boardId;
	private String dir;
	private int index;
	private int x;
	private int y;
	private int entryPoint;

	public String getBoardId() {
		return boardId;
	}

	public int getEntryPoint() {
		return entryPoint;
	}

	public String getDir() {
		return dir;
	}

	public int getIndex() {
		return index;
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

	public void setIndex(int index) {
		this.index = index;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
