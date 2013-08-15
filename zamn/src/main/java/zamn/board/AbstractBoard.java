package zamn.board;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;

import zamn.board.controlmode.Action;
import zamn.ui.IDelegatingKeySink;
import zamn.ui.ILayer;

/**
 * This class should be used to access the 2 dimension Tile array and can place
 * pieces
 * 
 * @author ofuangka
 * 
 */
public abstract class AbstractBoard extends JComponent implements ILayer,
		IDelegatingKeySink {

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

	private Dimension spriteSize;

	protected Tile[][] tiles;

	@Override
	public void backspace() {
		getCurrentKeySink().backspace();
	}

	@Override
	public void down() {
		getCurrentKeySink().down();
	}

	@Override
	public void enter() {
		getCurrentKeySink().enter();
	}

	@Override
	public void esc() {
		getCurrentKeySink().esc();
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

	@Override
	public void left() {
		getCurrentKeySink().left();
	}

	/**
	 * This method calls repaint
	 * 
	 * @param piece
	 * @param x
	 * @param y
	 */
	public void placePiece(AbstractBoardPiece piece, int x, int y) {
		removePiece(piece);
		if (tiles[x][y] == null) {
			throwNullTileException(x, y);
		} else {
			tiles[x][y].add(piece);
			piece.setXY(x, y);
			repaint();
		}

	}

	public void placePieceInGeneralArea(AbstractBoardPiece piece, int x, int y) {
		if (tiles[x][y] == null) {
			throwNullTileException(x, y);
		} else {
			List<Tile> queue = new ArrayList<Tile>();
			queue.add(tiles[x][y]);
			if (!placePieceInGeneralAreaHelper(piece, queue)) {
				throw new IllegalStateException(
						"Unable to place piece on board");
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected boolean placePieceInGeneralAreaHelper(AbstractBoardPiece piece,
			List<Tile> queue) {
		for (Tile tile : queue) {
			if (tile != null) {
				if (!tile.isOccupied() && tile.isWalkable()) {
					placePiece(piece, tile.getX(), tile.getY());
					return true;
				} else {
					queue.addAll(CollectionUtils.arrayToList(tile
							.getAdjacentTiles()));
					return placePieceInGeneralAreaHelper(piece, queue);
				}
			}
		}
		return false;
	}

	/**
	 * This method calls repaint
	 * 
	 * @param piece
	 */
	public void removePiece(AbstractBoardPiece piece) {
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

	@Override
	public void right() {
		getCurrentKeySink().right();
	}

	@Required
	public void setSpriteSize(Dimension spriteSize) {
		this.spriteSize = spriteSize;
	}

	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}

	@Override
	public void space() {
		getCurrentKeySink().space();
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
	public boolean tryMove(AbstractBoardPiece piece, Action dir) {
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
			if (nextTile.isWalkable() && nextTile.isEnabled()
					&& !nextTile.isOccupied()) {
				placePiece(piece, nextX, nextY);
				return true;

			}
		}
		return false;
	}

	@Override
	public void up() {
		getCurrentKeySink().up();
	}

	@Override
	public void x() {
		getCurrentKeySink().x();
	}
}
