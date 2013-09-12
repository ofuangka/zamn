package zamn.creation;

public class BoardDefinition {
	private String boardId;
	private CritterDefinition[] critterDefinitions;
	private Integer[][] entrances;
	private ExitDefinition[] exits;
	private TileDefinition[][] tiles;

	public String getBoardId() {
		return boardId;
	}

	public CritterDefinition[] getCritterDefinitions() {
		return critterDefinitions;
	}

	public Integer[][] getEntrances() {
		return entrances;
	}

	public ExitDefinition[] getExits() {
		return exits;
	}

	public TileDefinition[][] getTiles() {
		return tiles;
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}

	public void setCritterDefinitions(CritterDefinition[] critterDefinitions) {
		this.critterDefinitions = critterDefinitions.clone();
	}

	public void setEntrances(Integer[][] entrances) {
		this.entrances = entrances;
	}

	public void setExits(ExitDefinition[] exits) {
		this.exits = exits.clone();
	}

	public void setTiles(TileDefinition[][] tiles) {
		this.tiles = tiles.clone();
	}

}
