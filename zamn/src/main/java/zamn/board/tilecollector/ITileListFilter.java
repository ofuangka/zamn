package zamn.board.tilecollector;

import java.util.List;

import zamn.board.Tile;

public interface ITileListFilter {
	boolean apply(Tile tile);
	List<Tile> filter(List<Tile> unfiltered);
}
