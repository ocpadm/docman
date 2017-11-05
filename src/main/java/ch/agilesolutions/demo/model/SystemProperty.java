package ch.agilesolutions.demo.model;

import com.google.gson.annotations.Expose;
import ch.agilesolutions.jboss.annotations.Attribute;
import ch.agilesolutions.jboss.annotations.WidgetType;


public class SystemProperty {
	@Expose(serialize = true)
	private Integer id;
	@Expose(serialize = true)
	private String name;
	@Expose(serialize = true)
	private Integer version;
	@Expose(serialize = true)
	private String value;

	@Attribute(order = 1, length = 60, required=true, type = WidgetType.INPUT)	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getDescription() {
		return String.format("name = %s and value = %s", name,value);
	}
	@Attribute(order = 2, length = 60, required=true, type = WidgetType.INPUT)	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.name;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SystemProperty other = (SystemProperty) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	

}
