package zamn.creation.board;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;

import zamn.board.AbstractBoard;
import zamn.board.Critter;
import zamn.board.Decoration;
import zamn.board.Tile;

public class BoardSerializer {

	private ObjectMapper objectMapper;

	public BoardSerializer(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	protected BoardDefinition getBoardDefinition() {
		return new BoardDefinition();
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
				tileDefinitions[x][y].setSolid(tiles[x][y].isSolid());
				tileDefinitions[x][y].setSpriteId(tiles[x][y].getSpriteId());
			}
		}

		// add decorations
		List<Decoration> decorations = board.getDecorations();
		if (decorations != null) {
			int numDecorations = decorations.size();
			DecorationDefinition[] decorationDefinitions = new DecorationDefinition[numDecorations];
			for (int i = 0; i < numDecorations; i++) {
				Decoration decoration = decorations.get(i);
				DecorationDefinition decorationDefinition = new DecorationDefinition();
				decorationDefinition.setSpriteId(decoration.getSpriteId());
				decorationDefinition.setCoords(new int[] { decoration.getX(),
						decoration.getY() });
				decorationDefinitions[i] = decorationDefinition;
			}
			boardDefinition.setDecorationDefinitions(decorationDefinitions);
		}

		// add critters
		List<Critter> critters = board.getCritters();
		if (critters != null) {
			int numCritters = critters.size();
			CritterDefinition[] critterDefinitions = new CritterDefinition[numCritters];
			for (int i = 0; i < numCritters; i++) {
				Critter critter = critters.get(i);
				CritterDefinition critterDefinition = new CritterDefinition();
				critterDefinition.setSpriteId(critter.getSpriteId());
				critterDefinition.setCoords(new int[] { critter.getX(),
						critter.getY() });
				critterDefinition.setHostile(true);
				Map<String, Integer> stats = new HashMap<String, Integer>();
				Critter.Stat[] statKeys = Critter.Stat.values();
				for (Critter.Stat key : statKeys) {
					stats.put(key.toString(), key.getDefaultValue());
				}
				critterDefinition.setNmuValue(critter.getNmuValue());
				critterDefinition.setStats(stats);
				critterDefinition.setTalents(new ArrayList<String>());
				critterDefinitions[i] = critterDefinition;
			}
			boardDefinition.setCritterDefinitions(critterDefinitions);
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
			boardDefinition.setExitDefinitions(exitDefinitions);
		}

		// add entrances
		boardDefinition.setEntranceDefinitions(board.getEntrances());
		boardDefinition.setTileDefinitions(tileDefinitions);
		return objectMapper.writeValueAsString(boardDefinition);
	}
}
