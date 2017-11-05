package ch.agilesolutions.demo.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import ch.agilesolutions.demo.annotations.Attribute;
import ch.agilesolutions.demo.annotations.WidgetType;

public class Handler {
	@Expose(serialize = true)
	private Integer id;
	@Expose(serialize = true)
	private String name;
	@Expose(serialize = true)
	private String formatter;
	@Expose(serialize = true)
	private String path;
	@Expose(serialize = true)
	private String logLevel;
	@Expose(serialize = true)
	private List<Logger> loggers = new ArrayList<>();
	@Expose(serialize = true)
	private String type;
	@Expose(serialize = true)
	private String suffix;
	@Expose(serialize = true)
	private Boolean flush;
	@Expose(serialize = true)
	private Integer version;

	@Attribute(order = 1, length = 30, required=true, type = WidgetType.INPUT)	
	public String getName() {
		return name;
	}
	@Attribute(order = 2, length = 25, required=true, type = WidgetType.LIST)
	public String getLogLevel() {
		return logLevel;
	}
	@Attribute(order = 3, length = 25, required=true, type = WidgetType.LIST)
	public String getType() {
		return type;
	}
	@Attribute(order = 4, length = 60, required=true, type = WidgetType.INPUT)	
	public String getPath() {
		return path;
	}
	@Attribute(order = 5, length = 60, required=true, type = WidgetType.INPUT)	
	public String getSuffix() {
		return suffix;
	}
	@Attribute(order = 6, length = 25, required=true, type = WidgetType.LIST)
	public String getFormatter() {
		return formatter;
	}

	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	public List<Logger> getLoggers() {
		return loggers;
	}
	public void setLoggers(List<Logger> loggers) {
		this.loggers = loggers;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	

	public Boolean getFlush() {
		return flush;
	}
	public void setFlush(Boolean flush) {
		this.flush = flush;
	}
	
	public String getDescription() {
		return String.format("%s with path %s", name,path);
	}
	@Override
	public String toString() {
		return this.name;
	}

}


