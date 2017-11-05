package ch.agilesolutions.demo.model;

import com.google.gson.annotations.Expose;


public class MailSession {
	@Expose(serialize = true)
	private String smtpServer;
	@Expose(serialize = true)
	private Boolean smtpSSL;
	@Expose(serialize = true)
	private String smtpUserName;
	@Expose(serialize = true)
	private Integer smtpPort;
	@Expose(serialize = true)
	private String name;
	@Expose(serialize = true)
	private String smtpPassword;
	@Expose(serialize = true)
	private Boolean debugSwitch;
	@Expose(serialize = true)
	private String fromAddress;
	@Expose(serialize = true)
	private Integer id;
	@Expose(serialize = true)
	private Integer version;
	@Expose(serialize = true)
	private String smtpName;
	@Expose(serialize = true)
	private String jndiName;

	public String getSmtpServer() {
		return smtpServer;
	}
	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}
	public Boolean getSmtpSSL() {
		return smtpSSL;
	}
	public void setSmtpSSL(Boolean smtpSSL) {
		this.smtpSSL = smtpSSL;
	}
	public String getSmtpUserName() {
		return smtpUserName;
	}
	public void setSmtpUserName(String smtpUserName) {
		this.smtpUserName = smtpUserName;
	}
	public Integer getSmtpPort() {
		return smtpPort;
	}
	public void setSmtpPort(Integer smtpPort) {
		this.smtpPort = smtpPort;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSmtpPassword() {
		return smtpPassword;
	}
	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}
	public Boolean getDebugSwitch() {
		return debugSwitch;
	}
	public void setDebugSwitch(Boolean debugSwitch) {
		this.debugSwitch = debugSwitch;
	}
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
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
	public String getSmtpName() {
		return smtpName;
	}
	public void setSmtpName(String smtpName) {
		this.smtpName = smtpName;
	}
	public String getJndiName() {
		return jndiName;
	}
	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}
	
	
}
