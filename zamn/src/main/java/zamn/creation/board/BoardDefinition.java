package zamn.creation.board;

public class BoardDefinition {
	private String boardId;
	private CritterDefinition[] critterDefinitions;
	private DecorationDefinition[] decorationDefinitions;
	private int[][] entranceDefinitions;
	private ExitDefinition[] exitDefinitions;
	private TileDefinition[][] tileDefinitions;

	public String getBoardId() {
		return boardId;
	}

	public CritterDefinition[] getCritterDefinitions() {
		return critterDefinitions;
	}

	public DecorationDefinition[] getDecorationDefinitions() {
		return decorationDefinitions;
	}

	public int[][] getEntranceDefinitions() {
		return entranceDefinitions;
	}

	public ExitDefinition[] getExitDefinitions() {
		return exitDefinitions;
	}

	public TileDefinition[][] getTileDefinitions() {
		return tileDefinitions;
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}

	public void setCritterDefinitions(CritterDefinition[] critterDefinitions) {
		this.critterDefinitions = critterDefinitions.clone();
	}

	public void setDecorationDefinitions(
			DecorationDefinition[] decorationDefinitions) {
		this.decorationDefinitions = decorationDefinitions;
	}

	public void setEntranceDefinitions(int[][] entranceDefinitions) {
		this.entranceDefinitions = entranceDefinitions;
	}

	public void setExitDefinitions(ExitDefinition[] exitDefinitions) {
		this.exitDefinitions = exitDefinitions.clone();
	}

	public void setTileDefinitions(TileDefinition[][] tileDefinitions) {
		this.tileDefinitions = tileDefinitions.clone();
	}

}
