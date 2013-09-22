package zamn.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MessageLayer extends JPanel implements ILayer {

	private static final long serialVersionUID = 1148334419406112709L;

	private JLabel message;
	private JLabel spacerLeft;
	private JLabel spacerBottom;

	public MessageLayer() {
		super(new GridBagLayout());
		setOpaque(false);
		createAndAddMessageAndSpacers();
		setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
	}

	private void createAndAddMessageAndSpacers() {
		message = new JLabel();
		spacerLeft = new JLabel();
		spacerBottom = new JLabel();
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 1;
		add(spacerLeft, gc);
		gc = new GridBagConstraints();
		gc.gridx = 1;
		gc.gridy = 1;
		gc.weighty = 1;
		add(spacerBottom, gc);
		gc = new GridBagConstraints();
		gc.gridx = 1;
		gc.gridy = 0;
		add(message, gc);
		hideMessage();

	}

	public void hideMessage() {
		message.setVisible(false);
	}

	@Override
	public void backspace() {
		hideMessage();
	}

	@Override
	public void down() {
		hideMessage();
	}

	@Override
	public void enter() {
		hideMessage();
	}

	@Override
	public void esc() {
		hideMessage();
	}

	@Override
	public boolean isListening() {
		return true;
	}

	@Override
	public void left() {
		hideMessage();
	}

	@Override
	public void right() {
		hideMessage();
	}

	@Override
	public void space() {
		hideMessage();
	}

	@Override
	public void up() {
		hideMessage();
	}

	@Override
	public void x() {
		hideMessage();
	}

	@Override
	public boolean isEmpty() {
		return !message.isVisible();
	}

	public String getStyledMessage(String messageText) {
		return "<html><p style=\"color: white; background-color: black; padding: 5px;\">"
				+ messageText + "</p></html>";
	}

	public void showMessage(String messageText) {
		message.setText(getStyledMessage(messageText));
		message.setVisible(true);
	}

}
