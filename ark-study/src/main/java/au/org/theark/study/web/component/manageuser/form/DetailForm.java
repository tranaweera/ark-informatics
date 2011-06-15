package au.org.theark.study.web.component.manageuser.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.UserNameExistsException;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.ArkUserRole;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;

@SuppressWarnings("serial")
public class DetailForm extends AbstractDetailForm<ArkUserVO>{
	
	@SpringBean( name = "userService")
	private IUserService userService;
	
	@SuppressWarnings("rawtypes")
	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	
	protected TextField<String> userNameTxtField  =new TextField<String>(Constants.USER_NAME);
	protected TextField<String> firstNameTxtField = new TextField<String>(Constants.FIRST_NAME);
	protected TextField<String> lastNameTxtField = new TextField<String>(Constants.LAST_NAME);
	protected TextField<String> emailTxtField = new TextField<String>(Constants.EMAIL);
	protected PasswordTextField userPasswordField = new PasswordTextField(Constants.PASSWORD);
	protected PasswordTextField confirmPasswordField = new PasswordTextField(Constants.CONFIRM_PASSWORD);
	protected PasswordTextField oldPasswordField = new PasswordTextField(Constants.OLD_PASSWORD);
	protected WebMarkupContainer groupPasswordContainer = new WebMarkupContainer("groupPasswordContainer");
	private ArkCrudContainerVO arkCrudContainerVO;
	
	public DetailForm(String id, FeedbackPanel feedBackPanel,	ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id, feedBackPanel, arkCrudContainerVO, containerForm);
		this.arkCrudContainerVO = arkCrudContainerVO;
	}
	

	@SuppressWarnings("unchecked")
	public void initialiseDetailForm(){
		userNameTxtField  =new TextField<String>(Constants.USER_NAME);
		firstNameTxtField = new TextField<String>(Constants.FIRST_NAME);
		lastNameTxtField = new TextField<String>(Constants.LAST_NAME);
		emailTxtField = new TextField<String>(Constants.EMAIL);
		userPasswordField = new PasswordTextField(Constants.PASSWORD);
		confirmPasswordField = new PasswordTextField(Constants.CONFIRM_PASSWORD);
		oldPasswordField = new PasswordTextField(Constants.OLD_PASSWORD);
		groupPasswordContainer = new WebMarkupContainer("groupPasswordContainer");
		
		final List<ArkUserRole> rolesList = new ArrayList<ArkUserRole>();
		
		IModel<List<ArkUserRole>> iModel =  new LoadableDetachableModel() {
			private static final long serialVersionUID = 1L;
			@Override
			protected Object load() {
				System.out.println("load()");
				List<ArkUserRole> rolesList = new ArrayList<ArkUserRole>();
				return containerForm.getModelObject().getArkUserRoleList();
				
			}
		};
		 
		
		@SuppressWarnings("rawtypes")
		ListView listView = new ListView("arkUserRoleList", iModel) {
			

			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem item) {
				
				//Each item will be ArkModuleVO use that to build the Module name and the drop down
				ArkUserRole arkUserRole = (ArkUserRole)item.getModelObject();
				ArkModule arkModule = arkUserRole.getArkModule();
				//Acts as the data source for ArkRoles
				ArrayList<ArkRole> arkRoleList = iArkCommonService.getArkRoleLinkedToModule(arkModule);
				
				PropertyModel arkUserRolePm = new PropertyModel(arkUserRole,"arkRole");
				ChoiceRenderer<ArkRole> defaultChoiceRenderer = new ChoiceRenderer<ArkRole>(Constants.NAME, "id");
				
				DropDownChoice<ArkRole> ddc = new DropDownChoice<ArkRole>("arkRole",arkUserRolePm,arkRoleList,defaultChoiceRenderer);
				
				item.add(new Label("moduleName", arkModule.getName()));//arkModule within ArkUserRole
				item.add(ddc);
				
			}
		};
		
		listView.setReuseItems(true);
		arkCrudContainerVO.getWmcForarkUserAccountPanel().add(listView);
		attachValidators();
		addDetailFormComponents();
		
	}

	private void addDetailFormComponents(){
		
		arkCrudContainerVO.getDetailPanelFormContainer().add(userNameTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(firstNameTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(lastNameTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(emailTxtField);
	
		//We use this markup to hide unhide the password fields during edit. i.e. if the user selects edit password then make it visible/enabled.
		
		groupPasswordContainer.add(userPasswordField);
		groupPasswordContainer.add(confirmPasswordField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(groupPasswordContainer);
		
		arkCrudContainerVO.getDetailPanelFormContainer().add(arkCrudContainerVO.getWmcForarkUserAccountPanel());
		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}
	
	@Override
	protected void attachValidators() {

		lastNameTxtField.add(StringValidator.lengthBetween(1, 50)).setLabel(new StringResourceModel("lastName",this,null));
		lastNameTxtField.setRequired(true);

		userNameTxtField.add(StringValidator.lengthBetween(1, 50)).setLabel(new StringResourceModel("userName",this,null));
		userNameTxtField.add(EmailAddressValidator.getInstance()).setLabel(new StringResourceModel("userName.incorrectPattern",this,null));
		userNameTxtField.setRequired(true).setLabel(new StringResourceModel("userName",this,null));
		
		firstNameTxtField.setRequired(true).setLabel(new StringResourceModel("firstName",this,null));
		firstNameTxtField.add(StringValidator.lengthBetween(1, 50)).setLabel(new StringResourceModel("firstName",this,null));
		
		emailTxtField.add(StringValidator.lengthBetween(1, 50)).setLabel(new StringResourceModel("email",this,null));
		emailTxtField.add(EmailAddressValidator.getInstance()).setLabel(new StringResourceModel("email.incorrectpattern",this,null));
		emailTxtField.setRequired(true).setLabel(new StringResourceModel("email",this,null));
		
		userPasswordField.setRequired(false);
		confirmPasswordField.setRequired(false);
		
	}

	
	protected void onCancel(AjaxRequestTarget target) {
		
		ArkUserVO arkUserVO = new ArkUserVO();
		containerForm.setModelObject(arkUserVO);
	}

	@Override
	protected void onSave(Form<ArkUserVO> containerForm,AjaxRequestTarget target) {
		//Persist the user to ArkUser Group
		if(containerForm.getModelObject().getMode() == Constants.MODE_NEW){
			
			try {
				
				userService.createArkUser(containerForm.getModelObject());
				
				StringBuffer sb = new StringBuffer();
				sb.append("The user with Login/User Name " );
				sb.append(containerForm.getModelObject().getUserName());
				sb.append(" has been added successfully into the System.");
				containerForm.getModelObject().setMode(Constants.MODE_EDIT);
				onSavePostProcess(target,arkCrudContainerVO);
				this.info(sb.toString());
				
			} catch (ArkSystemException e) {
				this.error("A System error has occured. Please contact Support.");
			} catch (UserNameExistsException e) {
				this.error("The given username is already present in the Ark System. Please provide a unique username.");
			} catch (Exception e) {
				this.error("A System error has occured. Please contact Support");
			}
			
		}else if(containerForm.getModelObject().getMode() == Constants.MODE_EDIT){
			
			try {
				
				userService.updateArkUser(containerForm.getModelObject());
				
				StringBuffer sb = new StringBuffer();
				sb.append("The user with Login/User Name " );
				sb.append(containerForm.getModelObject().getUserName());
				sb.append(" has been updated successfully into the System.");
				this.info(sb.toString());
				onSavePostProcess(target,arkCrudContainerVO);
			} catch (ArkSystemException e) {
				this.error("A System error has occured. Please contact Support.");
				
			}
		}
		
		target.addComponent(feedBackPanel);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getMode() == Constants.MODE_NEW){
			return true;
		}else{
			return false;
		}
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target,	String selection, ModalWindow selectModalWindow) {
		// TODO Auto-generated method stub
		
	}

}
