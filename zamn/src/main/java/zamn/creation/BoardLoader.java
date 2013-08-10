package zamn.creation;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import zamn.board.AbstractBoard;
import zamn.board.Tile;

public class BoardLoader {

	private static final String DEFAULT_PREFIX = "boards/";
	private static final String DEFAULT_SUFFIX = ".js";

	private final ObjectMapper objectMapper;
	private String prefix = DEFAULT_PREFIX;
	private Dimension spriteSize;
	private String suffix = DEFAULT_SUFFIX;
	private Map<String, Integer[]> tileSpriteMap;
	private BufferedImage tileSpriteSheet;

	public BoardLoader(ObjectMapper objectMapper, Resource tileSpriteMapResource)
			throws IOException {
		this.objectMapper = objectMapper;
		SpriteMapDefinition spriteMapDefinition = objectMapper.readValue(
				tileSpriteMapResource.getURI().toURL(),
				SpriteMapDefinition.class);
		tileSpriteSheet = ImageIO
				.read((new ClassPathResource(spriteMapDefinition
						.getSpriteSheetClassPath()).getURI().toURL()));
		tileSpriteMap = spriteMapDefinition.getSpriteMap();
	}

	protected void doLoad(BoardDefinition boardDefinition, AbstractBoard board, int entryPoint) {
		// hook for subclasses
	}

	public final void load(String boardId, int entryPoint, AbstractBoard board)
			throws IOException {
		BoardDefinition boardDefinition = objectMapper.readValue(
				new ClassPathResource(prefix + boardId + suffix).getURI()
						.toURL(), BoardDefinition.class);
		TileDefinition[][] tileDefinitions = boardDefinition.getTiles();
		Tile[][] ret = new Tile[tileDefinitions.length][];
		for (int x = 0; x < tileDefinitions.length; x++) {
			ret[x] = new Tile[tileDefinitions[x].length];
			for (int y = 0; y < tileDefinitions[x].length; y++) {
				ret[x][y] = new Tile(x, y);
				ret[x][y].setWalkable(tileDefinitions[x][y].is_());
				if (x != 0) {
					ret[x - 1][y].setRight(ret[x][y]);
					ret[x][y].setLeft(ret[x - 1][y]);
				}
				if (y != 0) {
					ret[x][y - 1].setBottom(ret[x][y]);
					ret[x][y].setTop(ret[x][y - 1]);
				}

				String tileId = tileDefinitions[x][y].getSpriteId();
				Integer[] spriteSheetXY = tileSpriteMap.get(tileId);
				if (spriteSheetXY == null) {
					throw new IllegalArgumentException(
							"Could not retrieve tile with ID: '" + tileId + "'");
				}
				ret[x][y].applySprite(tileSpriteSheet, spriteSheetXY[0],
						spriteSheetXY[1], spriteSize);
			}
		}

		board.setTiles(ret);

		doLoad(boardDefinition, board, entryPoint);
	}

	@Required
	public void setSpriteSize(Dimension spriteSize) {
		this.spriteSize = spriteSize;
	}
}
