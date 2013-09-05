package zamn.mapeditor;

import javax.swing.BorderFactory;
import javax.swing.JSplitPane;

public class MapEditorScreen extends JSplitPane {

	private static final long serialVersionUID = -5292580814707061790L;

	public MapEditorScreen(MapEditorBoard board,
			MapEditorInterface mapEditorInterface) {
		super(JSplitPane.HORIZONTAL_SPLIT, board, mapEditorInterface);
		setDividerSize(0);
		setOneTouchExpandable(false);
		setBorder(BorderFactory.createEmptyBorder());
	}
}
