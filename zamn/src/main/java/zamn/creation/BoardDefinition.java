package zamn.creation;

public class BoardDefinition {
	private String boardId;
	private CritterDefinition[] critterDefinitions;
	private int[][] entrances;
	private ExitDefinition[] exits;
	private BoardPieceDefinition[][] tiles;

	public String getBoardId() {
		return boardId;
	}

	public CritterDefinition[] getCritterDefinitions() {
		return critterDefinitions;
	}

	public int[][] getEntrances() {
		return entrances;
	}

	public ExitDefinition[] getExits() {
		return exits;
	}

	public BoardPieceDefinition[][] getTiles() {
		return tiles;
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}

	public void setCritterDefinitions(CritterDefinition[] critterDefinitions) {
		this.critterDefinitions = critterDefinitions.clone();
	}

	public void setEntrances(int[][] entrances) {
		this.entrances = entrances;
	}

	public void setExits(ExitDefinition[] exits) {
		this.exits = exits.clone();
	}

	public void setTiles(BoardPieceDefinition[][] tiles) {
		this.tiles = tiles.clone();
	}

}
