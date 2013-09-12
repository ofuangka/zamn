package zamn.creation;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

		// add tiles
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

		// add critters
		List<Critter> critters = board.getCritters();
		if (critters != null) {
			int numCritters = critters.size();
			CritterDefinition[] critterPositions = new CritterDefinition[numCritters];
			for (int i = 0; i < numCritters; i++) {
				Critter critter = critters.get(i);
				critterPositions[i] = new CritterDefinition();
				critterPositions[i].setSpriteId(critter.getSpriteId());
				critterPositions[i].setSeedX(critter.getX());
				critterPositions[i].setSeedY(critter.getY());
			}
			boardDefinition.setCritterDefinitions(critterPositions);
		}

		// add exit definitions
		Map<String, String[]> exits = board.getExits();
		if (exits != null) {
			Set<String> exitKeys = exits.keySet();
			int numExits = exitKeys.size();
			ExitDefinition[] exitDefinitions = new ExitDefinition[numExits];
			Iterator<String> iter = exitKeys.iterator();
			int count = 0;
			while (iter.hasNext()) {
				String key = iter.next();
				ExitDefinition exitDefinition = new ExitDefinition();
				exitDefinition.setBoardId(exits.get(key)[0]);
				exitDefinition.setDir(getDir(key));
				exitDefinition
						.setEntryPoint(Integer.valueOf(exits.get(key)[1]));
				exitDefinition.setX(getX(key));
				exitDefinition.setY(getY(key));
				exitDefinitions[count++] = exitDefinition;
			}
			boardDefinition.setExits(exitDefinitions);
		}

		// add entrances
		boardDefinition.setEntrances(board.getEntrances());
		boardDefinition.setTiles(tileDefinitions);
		return objectMapper.writeValueAsString(boardDefinition);
	}

	protected String getDir(String key) {
		return key.split(",")[2].toLowerCase();
	}

	protected int getX(String key) {
		return Integer.valueOf(key.split(",")[0]);
	}

	protected int getY(String key) {
		return Integer.valueOf(key.split(",")[1]);
	}
}
