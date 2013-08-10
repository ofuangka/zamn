package zamn.ui;

public interface IDelegatingKeySink extends IKeySink {
	IKeySink getCurrentKeySink();
}
