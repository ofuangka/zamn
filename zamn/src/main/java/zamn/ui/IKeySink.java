package zamn.ui;

public interface IKeySink {
	void backspace();

	void down();

	void enter();

	void esc();

	void left();

	void right();

	void space();

	void up();

	void x();
	
	boolean isListening();
}
