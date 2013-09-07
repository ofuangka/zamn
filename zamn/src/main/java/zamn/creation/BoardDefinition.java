package zamn.creation;

public class BoardDefinition {
	private CritterPositionDefinition[] critterPositions;
	private Integer[][] entrances;
	private ExitDefinition[] exits;
	private String boardId;
	private TileDefinition[][] tiles;

	public CritterPositionDefinition[] getCritters() {
		return critterPositions;
	}

	public Integer[][] getEntrances() {
		return entrances;
	}

	public ExitDefinition[] getExits() {
		return exits;
	}

	public String getBoardId() {
		return boardId;
	}

	public TileDefinition[][] getTiles() {
		return tiles;
	}

	public void setCritterPositions(CritterPositionDefinition[] critterPositions) {
		this.critterPositions = critterPositions.clone();
	}

	public void setEntrances(Integer[][] entrances) {
		this.entrances = entrances;
	}

	public void setExits(ExitDefinition[] exits) {
		this.exits = exits.clone();
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}

	public void setTiles(TileDefinition[][] tiles) {
		this.tiles = tiles.clone();
	}

}
