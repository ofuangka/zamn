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

public class GameInterface extends JPanel {

	private static final long serialVersionUID = 4716145778679751281L;

	private Map<Critter, CritterSticker> items = new HashMap<Critter, CritterSticker>();

	private JScrollPane critterStickers;
	private JLabel nmuDisplay = new JLabel();
	private JLabel spacer = new JLabel();

	public GameInterface() {
		super(new GridBagLayout());
		critterStickers = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		critterStickers.setViewportView(new JPanel(new GridBagLayout()));

		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.BOTH;
		add(critterStickers, gc);
		gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 1;
		add(nmuDisplay, gc);
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
		return ((JPanel) critterStickers.getViewport().getView());
	}

	protected void refreshSpacer() {
		getView().remove(spacer);
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = items.size();
		gc.weighty = 1;
		getView().add(spacer, gc);
	}

	public void setNmus(int nmus) {
		nmuDisplay.setText(nmus + "¢");
	}

	public void removeCritter(Critter critter) {
		getView().remove(items.remove(critter));
		revalidate();
		repaint();
	}

}
