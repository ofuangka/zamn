package zamn.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import zamn.board.Critter;
import zamn.framework.event.IEventContext;

public class GameInterface extends JScrollPane {

	private static final long serialVersionUID = 4716145778679751281L;

	private Map<Critter, CritterSticker> items = new HashMap<Critter, CritterSticker>();

	private JLabel spacer = new JLabel();

	public GameInterface(IEventContext eventContext) {
		super(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setViewportView(new JPanel(new GridBagLayout()));
	}

	public void addCritter(Critter critter) {
		CritterSticker critterItem = new CritterSticker(critter);
		items.put(critter, critterItem);
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = items.size() - 1;
		getView().add(critterItem, gc);
		refreshSpacer();
		revalidate();
		repaint();
	}

	public void clearCritters() {
		Set<Critter> critters = items.keySet();
		for (Critter critter : critters) {
			getView().remove(items.get(critter));
		}
		items.clear();
		revalidate();
		repaint();
	}

	protected JPanel getView() {
		return ((JPanel) getViewport().getView());
	}

	protected void refreshSpacer() {
		getView().remove(spacer);
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = items.size();
		gc.weighty = 1;
		getView().add(spacer, gc);
	}

	public void removeCritter(Critter critter) {
		getView().remove(items.remove(critter));
		revalidate();
		repaint();
	}

}
