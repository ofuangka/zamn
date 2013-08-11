package zamn.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import zamn.ui.menu.Menu;

public class InGameMenuLayer extends JPanel implements ILayer,
		IDelegatingKeySink {

	private static final Logger LOG = Logger.getLogger(InGameMenuLayer.class);

	public static final int MARGIN_HORIZONTAL = 1;
	public static final int MARGIN_VERTICAL = 1;
	
	private static final long serialVersionUID = -194016976519008438L;

	private List<Menu> menus = new ArrayList<Menu>();

	private JLabel spacer = new JLabel();

	public InGameMenuLayer() {
		super(new GridBagLayout());
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(MARGIN_VERTICAL,
				MARGIN_HORIZONTAL, MARGIN_VERTICAL, MARGIN_HORIZONTAL));
	}

	@Override
	public void backspace() {
		popMenu();
	}

	/**
	 * Removes all Menus from this layer's UI and backend
	 */
	public void clearMenus() {
		while (!menus.isEmpty()) {
			remove(menus.remove(0));
		}
		refreshSpacer();
		revalidate();
		repaint();
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
		popMenu();
	}

	@Override
	public IKeySink getCurrentKeySink() {
		if (!menus.isEmpty()) {
			return menus.get(menus.size() - 1);
		}
		throw new IllegalStateException(
				"Cannot send key presses to an empty InGameMenuLayer");
	}

	@Override
	public boolean isEmpty() {
		return menus.isEmpty();
	}

	@Override
	public void left() {
		if (menus.size() > 1) {
			popMenu();
		}
	}

	/**
	 * Removes the topmost menu (if it exists)
	 */
	public void popMenu() {
		if (menus.size() > 0) {
			remove(menus.remove(menus.size() - 1));
			refreshSpacer();
			repaint();
		}
	}

	public void pushMenu(Menu menu) {

		LOG.debug("Adding Menu " + menu);

		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = menus.size();
		gc.gridy = 0;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.NORTHWEST;
		add(menu, gc);
		menus.add(menu);

		refreshSpacer();

		revalidate();

		repaint();
	}

	/**
	 * Removes the previous spacer and adds a new one to handle any changes in
	 * UI
	 */
	private void refreshSpacer() {
		remove(spacer);
		GridBagConstraints spacerGc = new GridBagConstraints();
		spacerGc.gridx = menus.size();
		spacerGc.gridy = 0;
		spacerGc.weightx = 1;
		spacerGc.weighty = 1;
		add(spacer, spacerGc);
	}

	@Override
	public void right() {
		getCurrentKeySink().right();
	}

	@Override
	public void space() {
		right();
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
