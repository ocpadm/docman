package ch.agilesolutions.demo.view;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.enterprise.context.SessionScoped;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItems;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.tree.Tree;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.util.BeanUtils;

import ch.agilesolutions.demo.annotations.Attribute;
import ch.agilesolutions.demo.annotations.WidgetType;
import ch.agilesolutions.demo.data.DocumentDao;
import ch.agilesolutions.demo.model.Profile;
import io.undertow.servlet.api.Deployment;

/**
 * managed bean handling JBoss profile page CRUD.
 * 
 * @author as
 */
@Named
@SessionScoped
public class DocumentController extends AbstractController implements Serializable {

	// --- Fields

	private static final long serialVersionUID = 1L;

	private static final String STAGING_DIR = System.getProperty("service.odo.staging") + "/staging";

	@Inject
	private DocumentDao domainDao;

	@Inject
	private SearchDao searchDao;

	private List<Profile> profiles;

	private Profile domain = new Profile();

	private Profile profile = new Profile();

	private Class<?> classifier = null;

	private Object classifierInstance = null;

	private TreeNode root = new DefaultTreeNode(new Tree("name", "description", Profile.class), null);

	private TreeNode selectedNode = new DefaultTreeNode(new Tree("name", "description", Profile.class), null);


	private String profileName = "";



	private String title = "";


	// --- Constructors

	public DocumentController() {
		System.out.println("profile controller constructed");
	}

	@PostConstruct
	private void populateProfiles() {

		profiles = new ArrayList<Profile>();

		try {

			jiraProjects = jiraDao.getProjects();

			profiles = domainDao.findAll();
}

			root = createTree(profiles);

			domainNames = searchDao.get();

			referenceData = referenceDataDao.get();

		} catch (Exception e) {
			submitMessage(FacesMessage.SEVERITY_ERROR, "Failure : Exception occured while populating profile view...", e, false);
			return;
		}
		submitMessage(FacesMessage.SEVERITY_INFO, profiles.size() + " record(s) retrieved...", true);
	}

	// Action methods

	private Method getAggregateMethod(Object instance) {

		for (Method method : Reflect.getAggregateMethods(Profile.class)) {

			// http://www.javacodegeeks.com/2013/12/advanced-java-generics-retreiving-generic-type-arguments.html
			Type myclass = method.getGenericReturnType();

			ParameterizedType parameterizedType = (ParameterizedType) myclass;

			Class<?> rob = (Class<?>) parameterizedType.getActualTypeArguments()[0];

			if (instance.getClass().getSimpleName().equals(rob.getSimpleName())) {
				return method;
			}

		}

		return null;

	}

	/**
	 * http://www.primefaces.org/showcase/ui/data/treetable/contextMenu.xhtml
	 * 
	 * @param profiles
	 * @return
	 */
	public TreeNode createTree(List<Profile> profiles) {

		TreeNode root = new DefaultTreeNode(new Tree("name", "description", Profile.class), null);

		for (Profile domain : profiles) {

			TreeNode domainNode = new DefaultTreeNode(domain, root);

			// in case of empty domain
			if (domain.getProfiles().size() == 0) {
				TreeNode profileNode = new DefaultTreeNode(new Tree("profiles", "-", Profile.class), domainNode);

			} else {

				for (Profile profile : domain.getProfiles()) {

					TreeNode profileNode = new DefaultTreeNode(profile, domainNode);
					// process datasources
					Reflect.getComponents(Profile.class).stream().forEach(m -> {

						// http://www.javacodegeeks.com/2013/12/advanced-java-generics-retreiving-generic-type-arguments.html
						Type myclass = m.getGenericReturnType();

						ParameterizedType parameterizedType = (ParameterizedType) myclass;

						Class<?> rob = (Class<?>) parameterizedType.getActualTypeArguments()[0];

						TreeNode propertiesNode = new DefaultTreeNode(new Tree(rob.getSimpleName(), "-", rob), profileNode);

						try {
							List<Object> components = (List<Object>) m.invoke(profile);

							if (components != null) {
								components.stream().forEach(c -> {
									TreeNode propertyNode = new DefaultTreeNode(c, propertiesNode);
								});
							}

						} catch (Exception e) {
							e.printStackTrace();
						}

					});

				}

			}

		}

		return root;
	}

	public void onNodeSelect(NodeSelectEvent event) {

		script = "";

		classifierInstance = event.getTreeNode().getData();

		classifier = classifierInstance.getClass();

		FacesMessage message = null;

		if (classifierInstance instanceof Profile) {
			domain = (Profile) classifierInstance;
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected domain ", domain.getName());
		} else if (classifierInstance instanceof Profile) {
			domain = (Profile) event.getTreeNode().getParent().getData();
			profile = (Profile) classifierInstance;
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected profile ", profile.getName());
		} else if (classifierInstance instanceof Tree) {
			classifier = ((Tree) classifierInstance).getClassifier();
			classifierInstance = event.getTreeNode().getData();
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected " + ((Tree) classifierInstance).getName(),
			                ((Tree) classifierInstance).getName());
		} else if (classifierInstance instanceof Deployment) {
			deployment = (Deployment) classifierInstance;
			classifierInstance = event.getTreeNode().getData();
			profile = (Profile) event.getTreeNode().getParent().getParent().getData();
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected " + classifier.getSimpleName(), classifierInstance.toString());
		} else {

			classifierInstance = event.getTreeNode().getData();
			profile = (Profile) event.getTreeNode().getParent().getParent().getData();
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected " + classifier.getSimpleName(), classifierInstance.toString());

		}

		FacesContext.getCurrentInstance().addMessage(null, message);

	}

	public void deleteComponent() {

		String message = "";

		if (profile == null) {
			submitMessage(FacesMessage.SEVERITY_INFO, "Please select a Profile first...", true);
			return;
		}

		try {
			if (classifierInstance instanceof Profile) {

				domainDao.delete((Profile) classifierInstance);

				message = String.format("Profile %s removed", domain.getName());
			} else if (classifierInstance instanceof Profile) {

				domain.getProfiles().remove(profile);

				this.domain = domainDao.save(this.domain,
				                String.format("profile %s removed from domain %s", profile.getName(), domain.getName()));

				message = String.format("Profile %s removed from domain %s", profile.getName(), domain.getName());

			} else {
				Method method = getAggregateMethod(classifierInstance);

				List<Object> list = (List<Object>) method.invoke(profile, null);

				list.remove(classifierInstance);

				this.domain = domainDao.save(this.domain, String.format("%s removed from domain %s",
				                classifierInstance.getClass().getSimpleName(), domain.getName()));

				message = String.format("%s removed from domain %s", classifierInstance.getClass().getSimpleName(), domain.getName());
			}

		} catch (Exception e) {
			submitMessage(FacesMessage.SEVERITY_ERROR, "Exception occured during component removal...", e, false);
			return;
		}

		refreshProfiles();

		submitMessage(FacesMessage.SEVERITY_INFO, message, true);

	}


	public void addComponent() {

		if (profile == null) {
			submitMessage(FacesMessage.SEVERITY_INFO, "Please select a Profile first...", true);
			return;
		}

		if (profiles.size() == 0) {
			classifier = Profile.class;
			Profile tmp = new Profile();
			tmp.setDescription("NA");
			tmp.setGitBranch("NA");
			tmp.setName("NA");
			tmp.setProfiles(Collections.EMPTY_LIST);
			tmp.setVersion(1);
			classifierInstance = tmp;

		} else {

			try {
				classifierInstance = Class.forName(classifier.getName()).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		Map<String, Object> options = new HashMap<String, Object>();
		options.put("draggable", true);
		options.put("resizable", false);
		options.put("closable", true);
		options.put("contentWidth", 790);

		title = "add new " + classifier.getSimpleName().toLowerCase();

		if (classifierInstance instanceof Deployment) {
			options.put("contentHeight", 600);
			deployment = new Deployment();
			showDeployment();

		} else if (classifierInstance instanceof Profile) {
			options.put("contentHeight", 540);
			profile = new Profile();
			setDefaultLimits();
			profile.setTemplate("eap70-template");
			profile.setImage("registry.access.redhat.com/jboss-eap-7/eap70-openshift:latest");
			profile.setJiraKey("NA");
			profile.setName("Entre new name...");
			showProfile();

		} else {
			options.put("contentHeight", Reflect.getAttributes(classifier).size() * 60);
			RequestContext.getCurrentInstance().openDialog("viewComponent", options, null);

		}

	}

	public void updateComponent() {

		Map<String, Object> options = new HashMap<String, Object>();
		options.put("draggable", true);
		options.put("resizable", false);
		options.put("closable", true);
		options.put("contentWidth", 790);

		title = "update " + classifier.getSimpleName().toLowerCase();

		if (classifierInstance instanceof Deployment) {

			try {
				BeanUtils.copyProperties(deployment, classifierInstance);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			options.put("contentHeight", 600);

			showDeployment();

		} else if (classifierInstance instanceof Profile) {
			options.put("contentHeight", 540);
			setDefaultLimits();

			showProfile();

		} else {
			options.put("contentHeight", Reflect.getAttributes(classifier).size() * 60);
			RequestContext.getCurrentInstance().openDialog("viewComponent", options, null);

		}

	}

	/**
	 * 
	 * 
	 * http://stackoverflow.com/questions/8509270/programmatically-getting- uicomponents-of-a-jsf-view-in-beans-constructor
	 * 
	 * preRenderComponent preRenderView postAddToView preValidate postValidate
	 */
	public void renderForm() {

		UIViewRoot root = FacesContext.getCurrentInstance().getViewRoot();
		UIComponent component = getUIComponentOfId(root, "injectPoint");

		if (component.getChildren().size() > 0) {
			return;
		}

		List<Method> methods = Reflect.getAttributes(classifier);

		// render panel grid content
		methods.stream().forEach(m -> {
			// set the label
			String name = WordUtils.uncapitalize(m.getName().substring(3));
			OutputLabel label = new OutputLabel();
			label.setValue(name);
			component.getChildren().add(label);

			try {

				WidgetType type = m.getAnnotation(Attribute.class).type();

				if (type == WidgetType.INPUT) {
					UIComponent ip = new UIInput();
					ip.getAttributes().put("size", Reflect.getLength(m));
					ip.getAttributes().put("required", Reflect.isRequired(m));
					ip.setValueExpression("value",
					                createValueExpression(String.format("#{profileController.classifierInstance.%s}", name), String.class));
					component.getChildren().add(ip);
				}
				if (type == WidgetType.OUTPUT) {
					UIComponent op = new UIInput();
					op.getAttributes().put("size", Reflect.getLength(m));
					op.setValueExpression("value",
					                createValueExpression(String.format("#{profileController.classifierInstance.%s}", name), String.class));
					component.getChildren().add(op);
				}
				if (type == WidgetType.LIST) {
					SelectOneMenu ls = new SelectOneMenu();
					ls.setValueExpression("value",
					                createValueExpression(String.format("#{profileController.classifierInstance.%s}", name), String.class));
					UISelectItems items = new UISelectItems();
					items.setValueExpression("value", createValueExpression(String.format("#{profileController.%ss}", name), List.class));
					;
					ls.getChildren().add(items);

					component.getChildren().add(ls);
				}
				if (type == WidgetType.AUTOCOMPLETE) {
					AutoComplete ac = new AutoComplete();
					ac.setValueExpression("value",
					                createValueExpression(String.format("#{profileController.classifierInstance.%s}", name), String.class));
					// http://forum.primefaces.org/viewtopic.php?f=3&t=19348
					MethodExpression completeMethod = createMethodExpression(
					                String.format("#{profileController.complete%s}", m.getName().substring(3)), List.class, String.class);
					ac.setCompleteMethod(completeMethod);
					ac.setAutocomplete("on");

					component.getChildren().add(ac);
				}
				if (type == WidgetType.PASSWORD) {
					UIInput pw = new HtmlInputSecret();
					pw.setConverter(new PasswordConverter());
					pw.setValueExpression("value",
					                createValueExpression(String.format("#{profileController.classifierInstance.%s}", name), String.class));

					component.getChildren().add(pw);
				}
				if (type == WidgetType.CHECKBOX) {
					SelectBooleanCheckbox chk = new SelectBooleanCheckbox();
					chk.getAttributes().put("size", Reflect.getLength(m));
					chk.setValueExpression("value",
					                createValueExpression(String.format("#{profileController.classifierInstance.%s}", name), String.class));
					component.getChildren().add(chk);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		});

	}

	public void closeView() {
		RequestContext.getCurrentInstance().closeDialog("viewComponent");

	}



	public void closeProfileDetails() {

		RequestContext.getCurrentInstance().closeDialog("viewProfileDetails");

	}

	public void updateProfile() {

		try {

			if (classifierInstance instanceof Profile) {

				this.domain = (Profile) classifierInstance;

			} else if (classifierInstance instanceof Profile) {

				// this.profile = (Profile) classifierInstance;

				if (!domain.getProfiles().contains(profile)) {
					domain.getProfiles().add(profile);

				}

			} else {

				Method method = getAggregateMethod(classifierInstance);

				List<Object> list = (List<Object>) method.invoke(profile, null);

				if (!list.contains(classifierInstance)) {
					list.add(classifierInstance);

				}

			}

			this.domain = domainDao.save(this.domain, String.format("Profile %s updated", domain.getName()));

		} catch (Exception e) {
			submitMessage(FacesMessage.SEVERITY_ERROR, "Exception occured during profile  update...", e, false);
			return;
		}

		refreshProfiles();

		RequestContext.getCurrentInstance().closeDialog("viewComponent");

		clearMessages();

		submitMessage(FacesMessage.SEVERITY_INFO, String.format("Profile %s updated...", profile.getName()), true);

	}


	public void removeProfile() {
		clearMessages();
		try {
			domainDao.delete(this.domain);
			submitMessage(FacesMessage.SEVERITY_INFO, this.domain.getDescription() + " removed from the database...", true);
		} catch (Exception e) {
			submitMessage(FacesMessage.SEVERITY_ERROR, "Failure : Exception occured while removing profile from database...", e, false);
			return;
		}
		// refresh table
		refreshProfiles();

	}

	public void refreshProfiles() {

		try {


				profiles = domainDao.findAll();


			root = createTree(profiles);

		} catch (Exception e) {
			submitMessage(FacesMessage.SEVERITY_ERROR, "Failure : Exception occured while refreshing profiles from database...", e, false);
		}
	}

	public void updateProfile() {

		FacesMessage message = null;

		clearMessages();

		try {

			// domainNames.getInstances().put(domainShortName, domainShortName);
			//
			// searchDao.save(domainNames);

			message = new FacesMessage(FacesMessage.SEVERITY_INFO, String.format("Profile %s saved", domainShortName), domainShortName);

			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (Exception e) {
			submitMessage(FacesMessage.SEVERITY_ERROR, "Failure : Exception occured while updating domain...", e, false);
		}

	}



	public List<Profile> getProfiles() {
		return profiles;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public TreeNode getRoot() {
		return root;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {

		this.selectedNode = selectedNode;

	}

	public void selectProfile(final AjaxBehaviorEvent event) {

		String selection = (String) event.getComponent().getAttributes().get("value");

		try {


				profiles = domainDao.findByProfile(selection);

			root = createTree(profiles);

		} catch (Exception e) {
			submitMessage(FacesMessage.SEVERITY_ERROR, "Failure : Exception occured while refreshing profiles from database...", e, false);
		}

	}


	public String getTitle() {
		return title;
	}

	public Object getClassifierInstance() {
		return classifierInstance;
	}

	public void setClassifierInstance(Object classifierInstance) {
		this.classifierInstance = classifierInstance;
	}

	public Class<?> getClassifier() {
		return classifier;
	}



	// Helpers
	// -----------------------------------------------------------------------------------
	private ValueExpression createValueExpression(String valueExpression, Class<?> valueType) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return facesContext.getApplication().getExpressionFactory().createValueExpression(facesContext.getELContext(), valueExpression,
		                valueType);
	}

	private static MethodExpression createMethodExpression(String valueExpression, Class<?> expectedReturnType,
	                Class<?>... parameterTypes) {
		MethodExpression methodExpression = null;
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ExpressionFactory factory = fc.getApplication().getExpressionFactory();
			methodExpression = factory.createMethodExpression(fc.getELContext(), valueExpression, expectedReturnType, parameterTypes);
		} catch (Exception e) {
			throw new FacesException("Method expression '" + valueExpression + "' could not be created.");
		}

		return methodExpression;
	}

	private UIComponent getUIComponentOfId(UIComponent root, String id) {
		if (root.getId().equals(id)) {
			return root;
		}
		if (root.getChildCount() > 0) {
			for (UIComponent subUiComponent : root.getChildren()) {
				UIComponent returnComponent = getUIComponentOfId(subUiComponent, id);
				if (returnComponent != null) {
					return returnComponent;
				}
			}
		}
		return null;
	}



	public void updateProfileName(final AjaxBehaviorEvent event) {

		profileName = (String) event.getComponent().getAttributes().get("value");

	}



	public void showProfile() {

		if (profile.getName() == null) {
			submitMessage(FacesMessage.SEVERITY_INFO, "Select profile first before update", true);
			return;
		}

		Map<String, Object> options = new HashMap<String, Object>();
		options.put("draggable", true);
		options.put("resizable", false);
		options.put("closable", true);
		options.put("contentWidth", 790);
		options.put("contentHeight", 400);

		RequestContext.getCurrentInstance().openDialog("viewProfileDetails", options, null);
	}



	public List<String> getDrivers() {

		return profile.getDrivers().stream().map(d -> d.getName()).collect(Collectors.toList());

	}

	public List<String> getHandlers() {

		return profile.getHandlers().stream().map(d -> d.getName()).collect(Collectors.toList());

	}



}
