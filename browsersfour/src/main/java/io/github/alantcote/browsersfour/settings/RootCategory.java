package io.github.alantcote.browsersfour.settings;

/**
 * The root {@link Category}.
 */
public class RootCategory implements Category {
	/**
	 * The name of the root category.
	 */
	public static final String ROOT_NAME = "Settings";

	/**
	 * Construct a new object.
	 */
	public RootCategory() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Objects of this class always return <code>null</code>.
	 */
	@Override
	public Editor getEditor() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return ROOT_NAME;
	}
}
