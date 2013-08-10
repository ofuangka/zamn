package zamn.ui;

import java.awt.Dimension;

public interface ILayer extends IKeySink {
	boolean isEmpty();

	boolean isVisible();

	void setBounds(int x1, int y1, int x2, int y2);
	
	void setPreferredSize(Dimension preferredSize);
}
