package ch.agilesolutions.demo.model;;

public class Tree {

	private String name;

	private String description;

	private Class<?> classifier;

	public Tree(String name, String description, Class<?> classifier) {
		this.description = description;
		this.name = name;
		this.classifier = classifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public Class<?> getClassifier() {
		return classifier;
	}

	public void setClassifier(Class<?> classifier) {
		this.classifier = classifier;
	}

}
