package hadl.m2;

public interface Linkable {

	void plug(final Link link);

	void receive(final Message msg);

}
