package ch.agilesolutions.demo.model;

/**
 * 
 * JCT supported deployment environments.
 *
 * @author u24279
 * @version  $Revision$, $Date$
 */
public enum Environment {
	DEV("Development"), LAB("Laboratory"), SIT("System Integration Test"), AT("Assembly Test"), UAT("User Acceptance Test"), PRD("Production"), EDU("Education");

	private String value;

	private Environment(String value) {

		this.value = value;

	}
	
	public String value() {
		return value;
	}

};
