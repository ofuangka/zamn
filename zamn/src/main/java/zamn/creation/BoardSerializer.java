package zamn.creation;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import zamn.board.AbstractBoard;
import zamn.board.Tile;
import zamn.board.piece.Critter;

public class BoardSerializer {

	private ObjectMapper objectMapper;

	public BoardSerializer(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	protected BoardDefinition getBoardDefinition() {
		return new BoardDefinition();
	}

	public String serialize(AbstractBoard board) throws IOException {
		BoardDefinition boardDefinition = getBoardDefinition();
		Tile[][] tiles = board.getTiles();
		int widthInTiles = tiles.length;
		TileDefinition[][] tileDefinitions = new TileDefinition[widthInTiles][];
		for (int x = 0; x < widthInTiles; x++) {
			int heightInTiles = tiles[x].length;
			tileDefinitions[x] = new TileDefinition[heightInTiles];
			for (int y = 0; y < heightInTiles; y++) {
				tileDefinitions[x][y] = new TileDefinition();
				tileDefinitions[x][y].set_(!tiles[x][y].isSolid());
				tileDefinitions[x][y].setSpriteId(tiles[x][y].getSpriteId());
			}
		}

		List<Critter> critters = board.getCritters();
		if (critters != null) {
			int numCritters = critters.size();
			CritterPositionDefinition[] critterPositions = new CritterPositionDefinition[numCritters];
			for (int i = 0; i < numCritters; i++) {
				Critter critter = critters.get(i);
				critterPositions[i] = new CritterPositionDefinition();
				critterPositions[i].setCritterId(critter.getCritterId());
				critterPositions[i].setSeedX(critter.getX());
				critterPositions[i].setSeedY(critter.getY());
			}
			boardDefinition.setCritterPositions(critterPositions);
		}

		boardDefinition.setTiles(tileDefinitions);
		boardDefinition.setEntrances(board.getEntrances());
		return objectMapper.writeValueAsString(boardDefinition);
	}
}
