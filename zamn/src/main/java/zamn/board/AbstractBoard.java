package zamn.board;

import java.awt.Dimension;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import zamn.board.controlmode.Action;
import zamn.creation.board.BoardLoader;
import zamn.ui.IKeySink;
import zamn.ui.ILayer;

/**
 * This class should be used to access the 2 dimension Tile array and can place
 * pieces
 * 
 * @author ofuangka
 * 
 */
public abstract class AbstractBoard extends JComponent implements ILayer,
		IKeySink {

	public static final int INVALID_X = -1;
	public static final int INVALID_Y = -1;

	private static final Logger LOG = Logger.getLogger(AbstractBoard.class);
	private static final long serialVersionUID = -2949485746939158973L;

	/**
	 * Convenience method checks if the given coordinates are within the bounds
	 * also given
	 * 
	 * @param x
	 * @param y
	 * @param minX
	 * @param minY
	 * @param maxX
	 * @param maxY
	 * @return
	 */
	public static boolean isInBounds(int x, int y, int minX, int minY, int dx,
			int dy) {
		return !(x < minX) && !(y < minY) && (x < minX + dx) && (y < minY + dy);
	}

	private BoardLoader boardLoader;
	protected List<Critter> critters = new ArrayList<Critter>();
	protected List<Decoration> decorations = new ArrayList<Decoration>();
	protected int[][] entrances;
	protected Map<String, String[]> exits = new HashMap<String, String[]>();

	private Dimension spriteSize;

	protected Tile[][] tiles;

	public void addBoardPiece(BoardPiece boardPiece, int x, int y) {
		Class<?> boardPieceClass = boardPiece.getClass();
		if (Critter.class.isAssignableFrom(boardPieceClass)) {
			placePieceInGeneralArea(boardPiece, x, y);
			addCritter((Critter) boardPiece);
		} else {
			movePiece(boardPiece, x, y);
			if (Decoration.class.isAssignableFrom(boardPieceClass)) {
				addDecoration((Decoration) boardPiece);
			}
		}
	}

	protected void addCritter(Critter critter) {
		critters.add(critter);
	}

	protected void addDecoration(Decoration decoration) {
		decorations.add(decoration);
	}

	public void addExit(int x, int y, Action dir, String[] boardIdAndEntrance) {
		exits.put(getExitKey(x, y, dir), boardIdAndEntrance);
	}

	public List<Critter> getCritters() {
		return critters;
	}

	public List<Decoration> getDecorations() {
		return decorations;
	}

	public int[][] getEntrances() {
		return entrances;
	}

	public String getExitKey(int x, int y, Action dir) {
		return x + "," + y + "," + dir.toString();
	}

	public Map<String, String[]> getExits() {
		return exits;
	}

	public Dimension getSpriteSize() {
		return spriteSize;
	}

	public Tile getTile(int x, int y) {
		return tiles[x][y];
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	protected void forceClearBoardState() {
		critters.clear();
		decorations.clear();
		exits.clear();
		tiles = null;
	}

	public boolean isAdjacent(int x1, int y1, int x2, int y2) {
		List<Tile> adjacentTiles = Arrays.asList(getTile(x1, y1)
				.getAdjacentTiles());
		return adjacentTiles.contains(getTile(x2, y2));
	}

	/**
	 * The Board layer is never empty
	 * 
	 * @return
	 */
	@Override
	public boolean isEmpty() {
		return false;
	}

	/**
	 * Checks if the given coordinates are within the bounds of the board
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isInBounds(int x, int y) {
		return !(x < 0)
				&& x < tiles.length
				&& AbstractBoard.isInBounds(x, y, 0, 0, tiles.length,
						tiles[x].length);
	}

	protected boolean isTileOpen(Tile tile) {
		return true;
	}

	public void load(URI id) throws IOException {
		boardLoader.load(id, this);
	}

	/**
	 * This method moves a piece without affecting the board state
	 * 
	 * @param piece
	 * @param x
	 * @param y
	 */
	public void movePiece(BoardPiece piece, int x, int y) {
		removePiece(piece);
		if (tiles[x][y] == null) {
			throwNullTileException(x, y);
		} else {
			tiles[x][y].add(piece);
			piece.setXY(x, y);
			repaint();
		}

	}

	protected void placePieceInGeneralArea(BoardPiece piece, int x, int y) {
		if (tiles[x][y] == null) {
			throwNullTileException(x, y);
		} else {
			List<Tile> queue = new ArrayList<Tile>();
			List<Tile> consumed = new ArrayList<Tile>();
			queue.add(tiles[x][y]);
			if (!placePieceInGeneralAreaHelper(piece, queue, consumed)) {
				throw new IllegalStateException(
						"Unable to place piece on board");
			}
		}
	}

	protected boolean placePieceInGeneralAreaHelper(BoardPiece piece,
			List<Tile> queue, List<Tile> consumed) {
		while (!queue.isEmpty()) {
			Tile tile = queue.remove(0);
			if (tile != null) {
				if (!tile.isOccupied() && !tile.isSolid()) {
					movePiece(piece, tile.getX(), tile.getY());
					return true;
				} else {
					consumed.add(tile);
					Tile[] adjacentTiles = tile.getAdjacentTiles();
					for (int i = 0; i < adjacentTiles.length; i++) {
						if (!consumed.contains(adjacentTiles[i])) {
							queue.add(adjacentTiles[i]);
						}
					}
					return placePieceInGeneralAreaHelper(piece, queue, consumed);
				}
			}
		}
		return false;
	}

	/**
	 * This method removes a piece from the board without affecting the board
	 * state
	 * 
	 * @param piece
	 */
	protected void removePiece(BoardPiece piece) {
		int x = piece.getX();
		int y = piece.getY();

		if (isInBounds(x, y)) {
			if (tiles[x][y] == null) {
				throwNullTileException(x, y);
			} else {
				tiles[x][y].remove(piece);
				piece.setXY(INVALID_X, INVALID_Y);
			}

			repaint();
		}
		// else do nothing (removing a piece that's not on the board)
	}

	public void removeBoardPiece(BoardPiece piece) {
		removePiece(piece);
		critters.remove(piece);
		decorations.remove(piece);
	}

	@Required
	public void setBoardLoader(BoardLoader boardLoader) {
		this.boardLoader = boardLoader;
	}

	public void setEntrances(int[][] entrances) {
		this.entrances = entrances;
	}

	@Required
	public void setSpriteSize(Dimension spriteSize) {
		this.spriteSize = spriteSize;
	}

	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}

	/**
	 * Convenience method that throws an NullPointerException with the
	 * coordinates of the null Tile
	 * 
	 * @param x
	 * @param y
	 */
	private void throwNullTileException(int x, int y) {
		throw new IllegalArgumentException("Tile was null at coordinates (" + x
				+ ", " + y + ")!");
	}

	/**
	 * Moves an arbitrary piece one space in the given direction unless the Tile
	 * in the direction is invalid, disabled or occupied. Returns true if the
	 * move was successful. This method calls repaint transitively through
	 * placePiece
	 * 
	 * @param piece
	 * @param dir
	 */
	public boolean tryMove(BoardPiece piece, Action dir) {
		LOG.debug("Attempting to move piece " + piece + " " + dir);
		int nextX = piece.getX();
		int nextY = piece.getY();
		switch (dir) {
		case UP: {
			nextY--;
			break;
		}
		case RIGHT: {
			nextX++;
			break;
		}
		case DOWN: {
			nextY++;
			break;
		}
		case LEFT: {
			nextX--;
			break;
		}
		default: {
			break;
		}
		}
		if (isInBounds(nextX, nextY)) {
			Tile nextTile = getTile(nextX, nextY);
			if (isTileOpen(nextTile)) {
				movePiece(piece, nextX, nextY);
				return true;

			}
		}
		return false;
	}

}
