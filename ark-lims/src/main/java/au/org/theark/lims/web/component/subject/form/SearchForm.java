/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.lims.web.component.subject.form;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.CountryState;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.lims.web.Constants;

/**
 * @author nivedann
 *
 */
public class SearchForm extends AbstractSearchForm<SubjectVO>{


	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5853988156214275754L;

	@SpringBean( name =  au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void> iArkCommonService;

	private TextField<String> subjectUIDTxtFld;
	private TextField<String> firstNameTxtFld;
	private TextField<String> middleNameTxtFld;
	private TextField<String> lastNameTxtFld;
	private DropDownChoice<VitalStatus> vitalStatusDdc;
	private DropDownChoice<GenderType> genderTypeDdc;
	private DropDownChoice<SubjectStatus> subjectStatusDdc;
	private DateTextField dateOfBirthTxtFld;
	private CompoundPropertyModel<SubjectVO> cpmModel;
	
	/**
	 * @param id
	 * @param cpmModel
	 */
	public SearchForm(	String id, 
						CompoundPropertyModel<SubjectVO> cpmModel,
						PageableListView<SubjectVO> listView, 
						FeedbackPanel feedBackPanel,
						WebMarkupContainer listContainer,
						WebMarkupContainer searchMarkupContainer,
						WebMarkupContainer detailsContainer,
						WebMarkupContainer detailPanelFormContainer,
						WebMarkupContainer viewButtonContainer,
						WebMarkupContainer editButtonContainer	) {
		
		//super(id, cpmModel);
		super	(id,
				cpmModel,
				detailsContainer,
				detailPanelFormContainer,
				viewButtonContainer,
				editButtonContainer,
				searchMarkupContainer,
				listContainer,
				feedBackPanel);
		
		this.cpmModel = cpmModel;
		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study in context. Please select a study");
		
		// Disable New button in LIMS
		newButton.setVisible(false);
	}
	
	protected void addSearchComponentsToForm(){
		add(subjectUIDTxtFld);
		add(firstNameTxtFld);
		add(middleNameTxtFld);
		add(lastNameTxtFld);
		add(vitalStatusDdc);
		add(subjectStatusDdc);
		add(genderTypeDdc);
		add(dateOfBirthTxtFld);
	}
	
	protected void initialiseSearchForm(){
		subjectUIDTxtFld = new TextField<String>(Constants.SUBJECT_UID);
		firstNameTxtFld = new TextField<String>(Constants.PERSON_FIRST_NAME);
		middleNameTxtFld = new TextField<String>(Constants.PERSON_MIDDLE_NAME);
		lastNameTxtFld = new TextField<String>(Constants.PERSON_LAST_NAME);
		initVitalStatusDdc();
		initSubjectStatusDdc();
		initGenderTypeDdc();
		
		dateOfBirthTxtFld = new DateTextField(Constants.PERSON_DOB,au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker dobDatePicker = new ArkDatePicker();
		dobDatePicker.bind(dateOfBirthTxtFld);
		dateOfBirthTxtFld.add(dobDatePicker);
	}

	private void initVitalStatusDdc(){
		CompoundPropertyModel<SubjectVO> subjectCpm = cpmModel;
		PropertyModel<LinkSubjectStudy> linkSubjectStudyPm = new PropertyModel<LinkSubjectStudy>(subjectCpm,"linkSubjectStudy");
		PropertyModel<Person> personPm = new PropertyModel<Person>(linkSubjectStudyPm,"person");
		PropertyModel<VitalStatus> vitalStatusPm = new PropertyModel<VitalStatus>(personPm,Constants.VITAL_STATUS);
		Collection<VitalStatus> vitalStatusList = iArkCommonService.getVitalStatus();
		ChoiceRenderer vitalStatusRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		vitalStatusDdc = new DropDownChoice<VitalStatus>(Constants.VITAL_STATUS,vitalStatusPm,(List)vitalStatusList,vitalStatusRenderer);
	}
	
	private void initSubjectStatusDdc(){
		CompoundPropertyModel<SubjectVO> subjectCpm = cpmModel;
		PropertyModel<LinkSubjectStudy> linkSubjectStudyPm = new PropertyModel<LinkSubjectStudy>(subjectCpm,"linkSubjectStudy");
		PropertyModel<SubjectStatus> subjectStatusPm = new PropertyModel<SubjectStatus>(linkSubjectStudyPm,"subjectStatus");
		List<SubjectStatus> subjectStatusList = iArkCommonService.getSubjectStatus();
		ChoiceRenderer subjectStatusRenderer = new ChoiceRenderer(Constants.NAME,Constants.SUBJECT_STATUS_ID);
		subjectStatusDdc = new DropDownChoice<SubjectStatus>(Constants.SUBJECT_STATUS,subjectStatusPm,subjectStatusList,subjectStatusRenderer);
	}
	
	private void initGenderTypeDdc(){
		CompoundPropertyModel<SubjectVO> subjectCpm = cpmModel;
		PropertyModel<LinkSubjectStudy> linkSubjectStudyPm = new PropertyModel<LinkSubjectStudy>(subjectCpm,"linkSubjectStudy");
		PropertyModel<Person> personPm = new PropertyModel<Person>(linkSubjectStudyPm,Constants.PERSON);
		PropertyModel<GenderType> genderTypePm = new PropertyModel<GenderType>(personPm,Constants.GENDER_TYPE);
		Collection<GenderType> genderTypeList = iArkCommonService.getGenderType(); 
		ChoiceRenderer genderTypeRenderer = new ChoiceRenderer(Constants.NAME,Constants.ID);
		genderTypeDdc = new DropDownChoice<GenderType>(Constants.GENDER_TYPE,genderTypePm, (List)genderTypeList,genderTypeRenderer);
	}
	
	@SuppressWarnings("unchecked")
	protected void onNew(AjaxRequestTarget target) {
		// ARK-108:: no longer do full reset to VO
		// Set a default Country on new when the Country field is empty
		if (getModelObject().getLinkSubjectStudy().getCountry() == null) {
			final List<Country> countryList = iArkCommonService.getCountries();
			//getModelObject().getSubjectStudy().setCountry(countryList.get(0));
			
			getModelObject().getLinkSubjectStudy().setCountry(iArkCommonService.getCountry(au.org.theark.core.Constants.DEFAULT_COUNTRY_CODE));
		}
		updateDetailFormPrerender(getModelObject().getLinkSubjectStudy());
		
		WebMarkupContainer wmc = (WebMarkupContainer) detailFormCompContainer;
		
		// Disable SubjectUID if auto-generation set
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study studyInContext = iArkCommonService.getStudy(sessionStudyId);
		if ((studyInContext != null) && (studyInContext.getAutoGenerateSubjectUid()))
		{
			TextField<String> subjectUIDTxtFld = (TextField<String>) detailFormCompContainer.get(Constants.SUBJECT_UID);
			getModelObject().getLinkSubjectStudy().setSubjectUID(Constants.SUBJECT_AUTO_GENERATED);
			subjectUIDTxtFld.setEnabled(false);
			target.addComponent(subjectUIDTxtFld);
		}
		else
		{
			TextField<String> subjectUIDTxtFld = (TextField<String>) detailFormCompContainer.get(Constants.SUBJECT_UID);
			subjectUIDTxtFld.setEnabled(true);
			target.addComponent(subjectUIDTxtFld);
		}

		preProcessDetailPanel(target);
	}

	public void updateDetailFormPrerender(LinkSubjectStudy linkSubjectStudy){
		List<CountryState> countryStateList = iArkCommonService.getStates(linkSubjectStudy.getCountry());
		WebMarkupContainer wmcStateSelector = (WebMarkupContainer) detailFormCompContainer.get(Constants.COUNTRY_STATE_SELECTOR_WMC);
		DropDownChoice<CountryState> stateChoice = (DropDownChoice<CountryState> )wmcStateSelector.get(Constants.SUBJECT_STATE);
		TextField<String> otherState = (TextField<String>)wmcStateSelector.get(Constants.SUBJECT_OTHER_STATE);
		
		if(countryStateList != null && countryStateList.size() > 0){
			stateChoice.getChoices().clear();
			stateChoice.setChoices(countryStateList);
			stateChoice.setVisible(true);
			otherState.setVisible(false);
		}else{
			//hide it
			stateChoice.setVisible(false);
			otherState.setVisible(true);
		}
	
	}

	protected void onSearch(AjaxRequestTarget target){
		
		target.addComponent(feedbackPanel);
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		getModelObject().getLinkSubjectStudy().setStudy(iArkCommonService.getStudy(sessionStudyId));
		
		int count = iArkCommonService.getStudySubjectCount(cpmModel.getObject());
		if(count == 0){
			this.info("There are no subjects with the specified criteria.");
			target.addComponent(feedbackPanel);
		}
		
		listContainer.setVisible(true);//Make the WebMarkupContainer that houses the search results visible
		target.addComponent(listContainer);//For ajax this is required so
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractSearchForm#isSecure(java.lang.String)
	 */
	@Override
	protected boolean isSecure(String actionType) {
		SecurityManager securityManager =  ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();		
		boolean flag = false;
		
		if(actionType.equalsIgnoreCase(Constants.NEW)){
			if(		
					securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.SUPER_ADMIN) ||
					securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN) ||
					securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN)){
				flag = true;
			}
			
		}else if (actionType.equalsIgnoreCase(Constants.SEARCH)){
			flag = true;
		}else{
			flag = true;
		}
		return flag;
	}
}