package fr.univnantes.alma.hadl.m2.component;

import fr.univnantes.alma.hadl.m2.configuration.ComponentConfiguration;

public abstract class AtomicComponent extends Component {

	public AtomicComponent(final String label, ComponentConfiguration composite) {
		super(label, composite);
	}

	public AtomicComponent(final String label) {
		super(label);
	}
}
