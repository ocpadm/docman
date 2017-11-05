package ch.agilesolutions.demo.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import ch.agilesolutions.demo.annotations.Attribute;
import ch.agilesolutions.demo.annotations.Component;
import ch.agilesolutions.demo.annotations.Root;
import ch.agilesolutions.demo.annotations.WidgetType;

@Root
public class Profile {
	@Expose(serialize = true)
	private String name;
	@Expose(serialize = true)
	private String description;
	@Expose(serialize = true)
	private List<Logger> loggers = new ArrayList<>();
	@Expose(serialize = true)
	private List<SystemProperty> systemProperties = new ArrayList<>();
	@Expose(serialize = true)
	private List<MailSession> mailSessions = new ArrayList<>();
	@Expose(serialize = true)
	private String environment;
	@Expose(serialize = true)
	private List<QueueFactory> queueFactories = new ArrayList<>();
	@Expose(serialize = true)
	private List<Datasource> datasources = new ArrayList<>();
	@Expose(serialize = true)
	private List<Handler> handlers = new ArrayList<>();
	@Expose(serialize = true)
	private List<Driver> drivers = new ArrayList<>();

	@Attribute(order = 1, length = 30, required = true, type = WidgetType.INPUT)
	public String getName() {
		return name;
	}

	@Attribute(order = 2, length = 90, required = true, type = WidgetType.INPUT)
	public String getDescription() {
		return description;
	}

	@Attribute(order = 3, length = 30, required = true, type = WidgetType.LIST)
	public String getEnvironment() {
		return environment;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Component(order = 7)
	public List<Logger> getLoggers() {
		return loggers;
	}

	public void setLoggers(List<Logger> loggers) {
		this.loggers = loggers;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Component(order = 1)
	public List<SystemProperty> getSystemProperties() {
		return systemProperties;
	}

	public void setSystemProperties(List<SystemProperty> systemProperties) {
		this.systemProperties = systemProperties;
	}

	public List<MailSession> getMailSessions() {
		return mailSessions;
	}

	public void setMailSessions(List<MailSession> mailSessions) {
		this.mailSessions = mailSessions;
	}

	@Component(order = 4)
	public List<QueueFactory> getQueueFactories() {
		return queueFactories;
	}

	public void setQueueFactories(List<QueueFactory> queueFactories) {
		this.queueFactories = queueFactories;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	@Component(order = 2)
	public List<Datasource> getDatasources() {
		return datasources;
	}

	public void setDatasources(List<Datasource> datasources) {
		this.datasources = datasources;
	}

	@Component(order = 6)
	public List<Handler> getHandlers() {
		return handlers;
	}

	public void setHandlers(List<Handler> handlers) {
		this.handlers = handlers;
	}

	@Component(order = 3)
	public List<Driver> getDrivers() {
		return drivers;
	}

	public void setDrivers(List<Driver> drivers) {
		this.drivers = drivers;
	}

}
