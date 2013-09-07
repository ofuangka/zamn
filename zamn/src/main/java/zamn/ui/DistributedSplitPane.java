package zamn.ui;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JSplitPane;

public class DistributedSplitPane extends JSplitPane {

	private static final long serialVersionUID = 7986545220487833886L;

	private double distribution;
	private JComponent left;
	private JComponent right;
	private Dimension spriteSize;

	public DistributedSplitPane(JComponent left, JComponent right,
			Dimension spriteSize, double distribution) {
		super(JSplitPane.HORIZONTAL_SPLIT, left, right);
		this.left = left;
		this.right = right;
		this.spriteSize = spriteSize;
		this.distribution = distribution;
		setOneTouchExpandable(false);
		setDividerSize(0);
		setBorder(BorderFactory.createEmptyBorder());

	}

	@Override
	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(preferredSize);
		left.setPreferredSize(new Dimension((int) (preferredSize.width
				* distribution / spriteSize.width)
				* spriteSize.width, preferredSize.height));
		right.setPreferredSize(new Dimension(preferredSize.width
				- left.getPreferredSize().width, preferredSize.height));
	}

}
