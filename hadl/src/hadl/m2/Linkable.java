package hadl.m2;

import hadl.m2.Message;

public interface Linkable {

	void plug(final Link link);

	void receive(final Message msg);

}
