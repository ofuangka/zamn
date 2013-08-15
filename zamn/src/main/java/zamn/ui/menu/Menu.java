package zamn.ui.menu;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import zamn.ui.IKeySink;

public class Menu extends JPanel implements IKeySink {

	public static final Color BG_COLOR = Color.black;

	private static final long serialVersionUID = 272208021126912914L;

	private int currentIndex;
	private List<AbstractMenuItem> items = new ArrayList<AbstractMenuItem>();

	public Menu() {
		super(new GridBagLayout());
		setOpaque(true);
		setBackground(BG_COLOR);
	}

	public final void addItem(AbstractMenuItem item) {
		items.add(item);
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = items.size() - 1;
		gc.fill = GridBagConstraints.HORIZONTAL;
		add(item, gc);
	}

	@Override
	public void backspace() {

	}

	public final void clearItems() {
		if (items != null) {
			while (!items.isEmpty()) {
				remove(items.remove(0));
			}
		}
	}

	public void down() {
		if (items != null && !items.isEmpty()) {
			items.get(currentIndex).uiDeselect();
			if (currentIndex == items.size() - 1) {
				currentIndex = 0;
			} else {
				currentIndex++;
			}
			items.get(currentIndex).uiSelect();
		}
	}

	@Override
	public void enter() {
		executeCurrentItem();
	}

	@Override
	public void esc() {

	}

	protected void executeCurrentItem() {
		if (items != null && !items.isEmpty()) {
			items.get(currentIndex).execute();
		} else {
			throw new IllegalStateException(
					"Calling execute with null items list");
		}
	}

	@Override
	public void left() {

	}

	public final void reset() {
		currentIndex = 0;
		if (items != null) {
			items.get(0).uiSelect();
			for (int i = 1; i < items.size(); i++) {
				items.get(i).uiDeselect();
			}
		}
	}

	@Override
	public void right() {
		AbstractMenuItem currentlySelectedItem = items.get(currentIndex);
		if (AbstractMenuItemWithSubMenu.class.isAssignableFrom(currentlySelectedItem
				.getClass())) {
			currentlySelectedItem.execute();
		}
	}

	public final void setItems(List<AbstractMenuItem> items) {
		if (items != null && !items.isEmpty()) {
			clearItems();
			for (AbstractMenuItem item : items) {
				addItem(item);
			}
			reset();
			items.get(0).uiSelect();
		} else {
			throw new IllegalArgumentException(
					"Must have at least one MenuItem");
		}
	}

	@Override
	public void space() {

	}

	public String toString() {
		return items.toString();
	}

	public void up() {
		if (items != null & !items.isEmpty()) {
			items.get(currentIndex).uiDeselect();
			if (currentIndex == 0) {
				currentIndex = items.size() - 1;
			} else {
				currentIndex--;
			}
			items.get(currentIndex).uiSelect();
		}
	}
	
	@Override
	public void x() {

	}

	@Override
	public boolean isListening() {
		return true;
	}

}
