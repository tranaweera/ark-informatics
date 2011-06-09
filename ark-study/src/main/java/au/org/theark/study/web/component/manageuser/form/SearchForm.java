package au.org.theark.study.web.component.manageuser.form;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.study.entity.ArkModuleRole;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ArkModuleVO;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;

public class SearchForm extends AbstractSearchForm<ArkUserVO>{
	
	private Long sessionStudyId;
	private CompoundPropertyModel<ArkUserVO> cpmModel;
	private ContainerForm containerForm;
	private ArkCrudContainerVO arkCrudContainerVO;
	private FeedbackPanel feedbackPanel;

	//Form Fields
	private TextField<String> userNameTxtField =new TextField<String>(Constants.USER_NAME);
	private TextField<String> firstNameTxtField = new TextField<String>(Constants.FIRST_NAME);
	private TextField<String> lastNameTxtField = new TextField<String>(Constants.LAST_NAME);
	private TextField<String> emailTxtField = new TextField<String>(Constants.EMAIL);
	protected DropDownChoice<YesNo> usersLinkedToStudyOnlyChoice;
	
	@SpringBean( name = "userService")
	private IUserService userService;
	
	@SpringBean( name =  au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	private PageableListView<ArkUserVO> pageableListView;
	private ListView moduleRoleListView;

	/**
	 * Constructor
	 * @param id
	 * @param cpmModel
	 * @param containerForm 
	 */
	public SearchForm(String id, CompoundPropertyModel<ArkUserVO> cpmModel,ArkCrudContainerVO arkCrudContainerVO,FeedbackPanel feedbackPanel, ContainerForm containerForm, PageableListView<ArkUserVO> pageableListView,ListView moduleRoleListView) {
		
		super(id, cpmModel,feedbackPanel,arkCrudContainerVO);
		this.pageableListView = pageableListView;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
		this.feedbackPanel = feedbackPanel;
		this.moduleRoleListView = moduleRoleListView;
		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study in context. Please select a study.",arkCrudContainerVO);
	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		try {
			//Look up a Ark User's based on the filter specified by the user.
			List<ArkUserVO> userResultList = userService.searchUser(containerForm.getModelObject());

			if(userResultList != null && userResultList.size() == 0){
				this.info("User(s) with the specified criteria does not exist in the system. Please refine your search filter.");
				target.addComponent(feedbackPanel);
			}
			
			containerForm.getModelObject().setUserList(userResultList);
			pageableListView.removeAll();
			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
			target.addComponent(arkCrudContainerVO.getSearchResultPanelContainer());
		}
		catch (ArkSystemException e) {
			this.error("A System Error has occured. Please contact support");
		}
	}

	@Override
	protected void onNew(AjaxRequestTarget target) {
		
		//We want to roll over the search criteria setting into the detail one so we don't reset or refresh the whole VO
		//Set up any preloaded stuff here
		containerForm.getModelObject().setMode(Constants.MODE_NEW);
		//containerForm.getModelObject().setArkUserRoleList(arkUserRoleList)
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		//For the Given study get the linked Modules and the associated Roles for each Module as a List of ArkModuleVO 
		//Set this list into the ArkUserVO
		Collection<ArkModuleVO> listArkModuleVO = iArkCommonService.getArkModulesLinkedToStudy(study);
		containerForm.getModelObject().setArkModuleVOList((List)listArkModuleVO);
		
		
		preProcessDetailPanel(target,arkCrudContainerVO);
		
	}

	@Override
	protected boolean isSecure(String actionType) {
		// TODO Auto-generated method stub
		return false;
	}
	
	protected void initialiseSearchForm(){
		
		userNameTxtField =new TextField<String>(Constants.USER_NAME);
		firstNameTxtField = new TextField<String>(Constants.FIRST_NAME);
		lastNameTxtField = new TextField<String>(Constants.LAST_NAME);
		emailTxtField = new TextField<String>(Constants.EMAIL);
		Collection<YesNo> yesNoList = iArkCommonService.getYesNoList(); 
		ChoiceRenderer<YesNo> yesnoRenderer = new ChoiceRenderer<YesNo>(Constants.NAME,Constants.ID);
		usersLinkedToStudyOnlyChoice = new DropDownChoice<YesNo>("usersLinkedToStudyOnly",(List)yesNoList,yesnoRenderer);
	}
	
	protected void attachValidators(){
		emailTxtField.add(EmailAddressValidator.getInstance());
		firstNameTxtField.add(StringValidator.lengthBetween(2, 100));
		lastNameTxtField.add(StringValidator.lengthBetween(2, 50));
		userNameTxtField.add(StringValidator.lengthBetween(2, 50));
		userNameTxtField.add(EmailAddressValidator.getInstance());
	}
	
	private void addSearchComponentsToForm(){
		add(emailTxtField);
		add(firstNameTxtField);
		add(lastNameTxtField);
		add(userNameTxtField);
		//add(usersLinkedToStudyOnlyChoice);
	}

}
