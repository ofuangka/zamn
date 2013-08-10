package zamn.creation;

public class BoardDefinition {
	private CritterPositionDefinition[] critterPositions;
	private ExitDefinition[] exits;
	private String id;
	private TileDefinition[][] tiles;

	public CritterPositionDefinition[] getCritters() {
		return critterPositions;
	}

	public ExitDefinition[] getExits() {
		return exits;
	}

	public String getId() {
		return id;
	}

	public TileDefinition[][] getTiles() {
		return tiles;
	}

	public void setCritterPositions(CritterPositionDefinition[] critterPositions) {
		this.critterPositions = critterPositions.clone();
	}

	public void setExits(ExitDefinition[] exits) {
		this.exits = exits.clone();
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTiles(TileDefinition[][] tiles) {
		this.tiles = tiles.clone();
	}

}
