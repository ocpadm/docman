package ch.agilesolutions.demo.model;

import com.google.gson.annotations.Expose;

import ch.agilesolutions.demo.annotations.Attribute;
import ch.agilesolutions.demo.annotations.WidgetType;


public class Driver {
	@Expose(serialize = true)
	private String name;
	@Expose(serialize = true)
	private String description;
	@Expose(serialize = true)
	private String moduleName;
	@Expose(serialize = true)
	private Boolean distributed;
	@Expose(serialize = true)
	private String className;
	@Expose(serialize = true)
	private Integer id;
	@Expose(serialize = true)
	private Integer version;

	@Attribute(order = 1, length = 30, required=true,type = WidgetType.INPUT)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Attribute(order = 3, length = 50, required=true, type = WidgetType.INPUT)
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	@Attribute(order = 5, length = 1, required=true, type = WidgetType.CHECKBOX)
	public Boolean getDistributed() {
		return distributed;
	}
	public void setDistributed(Boolean distributed) {
		this.distributed = distributed;
	}
	@Attribute(order = 4, length = 80, required=true, type = WidgetType.INPUT)
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
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

	
	
	@Attribute(order = 2, length = 80, required=true, type = WidgetType.INPUT)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return this.name;
	}

	
}
