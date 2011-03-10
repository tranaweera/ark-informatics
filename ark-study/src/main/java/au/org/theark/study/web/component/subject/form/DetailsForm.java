/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.subject.form;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.DateValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.CountryState;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.PersonContactMethod;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 *
 */
public class DetailsForm extends AbstractSubjectDetailForm<SubjectVO>{

	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	@SpringBean( name =  au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	private WebMarkupContainer arkContextMarkupContainer;

	private TextField<String> firstNameTxtFld;
	private TextField<String> middleNameTxtFld;
	private TextField<String> lastNameTxtFld;
	private TextField<String> previousLastNameTxtFld;
	private TextField<String> preferredNameTxtFld;
	private TextField<String> subjectUIDTxtFld;
	
	private DateTextField dateOfBirthTxtFld;
	private DateTextField dateOfDeathTxtFld;
	private TextField<String> causeOfDeathTxtFld;
	
	//Custom Fields and Consents at Subject Study Level
	private TextField<String> amdrifIdTxtFld;
	private DateTextField studyApproachDate;
	private TextField<Long> yearOfFirstMamogramTxtFld;
	private TextField<String> yearOfRecentMamogramTxtFld;
	private TextField<String> totalNumberOfMamogramsTxtFld;
	private DropDownChoice<YesNo> consentToActiveContactDdc;
	private DropDownChoice<YesNo> consentToUseDataDdc;
	private DropDownChoice<YesNo> consentToPassDataGatheringDdc;
	
	//Address Stuff comes here 
	private TextField<String> streetAddressTxtFld;
	private TextField<String> cityTxtFld;
	private TextField<String> postCodeTxtFld;
	private DropDownChoice<Country> countryChoice;
	private DropDownChoice<CountryState> stateChoice;
	private WebMarkupContainer countryStateSelector;
	private TextField<String> preferredEmailTxtFld;
	private TextField<String> otherEmailTxtFld;
	
	//Reference Data 
	private DropDownChoice<TitleType> titleTypeDdc;
	private DropDownChoice<VitalStatus> vitalStatusDdc;
	private DropDownChoice<GenderType> genderTypeDdc;
	private DropDownChoice<SubjectStatus> subjectStatusDdc;
	private DropDownChoice<MaritalStatus> maritalStatusDdc;
	private DropDownChoice<PersonContactMethod> personContactMethodDdc;
	
	private WebMarkupContainer wmcPreferredEmailContainer;
	private WebMarkupContainer wmcDeathDetailsContainer;
	
	private Study study;
	
	
	public DetailsForm(	String id,
						FeedbackPanel feedBackPanel,
						WebMarkupContainer resultListContainer,
						WebMarkupContainer detailPanelContainer,
						WebMarkupContainer detailPanelFormContainer,
						WebMarkupContainer searchPanelContainer,
						WebMarkupContainer viewButtonContainer,
						WebMarkupContainer editButtonContainer,
						WebMarkupContainer arkContextContainer,
						ContainerForm containerForm				) {
		
	
			super(id,feedBackPanel,resultListContainer,detailPanelContainer,detailPanelFormContainer,searchPanelContainer,viewButtonContainer,editButtonContainer,containerForm);
			this.arkContextMarkupContainer = arkContextContainer;
	}

		
	 public void initialiseDetailForm(){
	
		firstNameTxtFld = new TextField<String>(Constants.PERSON_FIRST_NAME);
		middleNameTxtFld = new TextField<String>(Constants.PERSON_MIDDLE_NAME);
		lastNameTxtFld = new TextField<String>(Constants.PERSON_LAST_NAME);
		previousLastNameTxtFld = new TextField<String>(Constants.SUBJECT_PREVIOUS_LAST_NAME);
		previousLastNameTxtFld.setEnabled(false);
		preferredNameTxtFld = new TextField<String>(Constants.PERSON_PREFERRED_NAME);
		subjectUIDTxtFld = new TextField<String>(Constants.SUBJECT_UID);
		
		preferredEmailTxtFld = new TextField<String>(Constants.PERSON_PREFERRED_EMAIL);
		otherEmailTxtFld = new TextField<String>(Constants.PERSON_OTHER_EMAIL);
		
		
		dateOfBirthTxtFld = new DateTextField(Constants.PERSON_DOB,au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker dobDatePicker = new ArkDatePicker();
		dobDatePicker.bind(dateOfBirthTxtFld);
		dateOfBirthTxtFld.add(dobDatePicker);
		
		dateOfDeathTxtFld = new DateTextField(Constants.PERSON_DOD,au.org.theark.core.Constants.DD_MM_YYYY);
		
		causeOfDeathTxtFld = new TextField<String>(Constants.PERSON_CAUSE_OF_DEATH);
		ArkDatePicker dodDatePicker = new ArkDatePicker();
		dodDatePicker.bind(dateOfDeathTxtFld);
		dateOfDeathTxtFld.add(dodDatePicker);
		
		wmcDeathDetailsContainer = new  WebMarkupContainer("deathDetailsContainer");
		wmcDeathDetailsContainer.setOutputMarkupPlaceholderTag(true);
		// Default death details to disabled (enable onChange of vitalStatus)
		setDeathDetailsContainer();
		
		//Initialise Drop Down Choices 
		//Title We can also have the reference data populated on Application start and refer to a static list instead of hitting the database
		Collection<TitleType> titleTypeList = iArkCommonService.getTitleType();
		ChoiceRenderer<TitleType> defaultChoiceRenderer = new ChoiceRenderer<TitleType>(Constants.NAME,Constants.ID);
		titleTypeDdc = new DropDownChoice<TitleType>(Constants.PERSON_TYTPE_TYPE,(List)titleTypeList,defaultChoiceRenderer);

		Collection<VitalStatus> vitalStatusList = iArkCommonService.getVitalStatus();
		ChoiceRenderer<VitalStatus> vitalStatusRenderer = new ChoiceRenderer<VitalStatus>(Constants.NAME, Constants.ID);
		vitalStatusDdc = new DropDownChoice<VitalStatus>(Constants.PERSON_VITAL_STATUS,(List<VitalStatus>)vitalStatusList,vitalStatusRenderer);
		vitalStatusDdc.add(new AjaxFormComponentUpdatingBehavior("onchange"){
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				setDeathDetailsContainer();
				target.addComponent(wmcDeathDetailsContainer);
			}
		});
		
		
		Collection<GenderType> genderTypeList = iArkCommonService.getGenderType(); 
		ChoiceRenderer<GenderType> genderTypeRenderer = new ChoiceRenderer<GenderType>(Constants.NAME,Constants.ID);
		genderTypeDdc = new DropDownChoice<GenderType>(Constants.PERSON_GENDER_TYPE,(List<GenderType>)genderTypeList,genderTypeRenderer);
		
		
		Collection<SubjectStatus> subjectStatusList = iArkCommonService.getSubjectStatus();
		ChoiceRenderer<SubjectStatus> subjectStatusRenderer = new ChoiceRenderer<SubjectStatus>(Constants.NAME,Constants.SUBJECT_STATUS_ID);
		subjectStatusDdc = new DropDownChoice<SubjectStatus>(Constants.SUBJECT_STATUS,(List)subjectStatusList,subjectStatusRenderer);
		
		Collection<MaritalStatus> maritalStatusList = iArkCommonService.getMaritalStatus(); 
		ChoiceRenderer<MaritalStatus> maritalStatusRender = new ChoiceRenderer<MaritalStatus>(Constants.NAME,Constants.ID);
		maritalStatusDdc = new DropDownChoice<MaritalStatus>(Constants.PERSON_MARITAL_STATUS,(List) maritalStatusList, maritalStatusRender);
		
		Collection<PersonContactMethod> contactMethodList = iArkCommonService.getPersonContactMethodList(); 
		ChoiceRenderer<PersonContactMethod> contactMethodRender = new ChoiceRenderer<PersonContactMethod>(Constants.NAME,Constants.ID);
		
		wmcPreferredEmailContainer = new  WebMarkupContainer("preferredEmailContainer");
		wmcPreferredEmailContainer.setOutputMarkupPlaceholderTag(true);
		// Depends on preferredContactMethod
		setPreferredEmailContainer();
		
		personContactMethodDdc= new DropDownChoice<PersonContactMethod>(Constants.PERSON_CONTACT_METHOD,(List) contactMethodList, contactMethodRender);
		personContactMethodDdc.add(new AjaxFormComponentUpdatingBehavior("onchange"){
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				//Check what was selected and then toggle
				setPreferredEmailContainer();
				target.addComponent(wmcPreferredEmailContainer);
			}
		});
		
		String subjectPreviousLastname = studyService.getPreviousLastname(containerForm.getModelObject().getSubjectStudy().getPerson());
		containerForm.getModelObject().setSubjectPreviousLastname(subjectPreviousLastname);
		
		initCustomFields();
		
		attachValidators();
		addDetailFormComponents();
	}
	 
	private void setDeathDetailsContainer()
	{
		VitalStatus vitalStatus = containerForm.getModelObject().getSubjectStudy().getPerson().getVitalStatus();
		if(vitalStatus != null){
			String vitalStatusName  = vitalStatus.getName();
			
			if(vitalStatusName.equalsIgnoreCase("DECEASED")){
				dateOfDeathTxtFld.setEnabled(true);
				causeOfDeathTxtFld.setEnabled(true);
			}
			else{
				dateOfDeathTxtFld.setEnabled(false);
				causeOfDeathTxtFld.setEnabled(false);
			}
		}
	}
	
	private void setPreferredEmailContainer(){
		PersonContactMethod personContactMethod = containerForm.getModelObject().getSubjectStudy().getPerson().getPersonContactMethod();
		
		if(personContactMethod != null){
			String personContactMethodName  = personContactMethod.getName();
			if(personContactMethodName.equalsIgnoreCase("EMAIL")){
				// Add a validator making preferredEmail required
				preferredEmailTxtFld.setRequired(true).setLabel(new StringResourceModel("subject.preferredEmail.required", null));
			}
			else{
				preferredEmailTxtFld.setRequired(false);
				
			}
		}
	}
	 
	private void initCustomFields(){
		amdrifIdTxtFld = new TextField<String>("subjectStudy.amdrifId");
		
		studyApproachDate = new DateTextField("subjectStudy.studyApproachDate", au.org.theark.core.Constants.DD_MM_YYYY);
		
		ArkDatePicker dobStudyApproachDatePicker = new ArkDatePicker();
		
		dobStudyApproachDatePicker.bind(studyApproachDate);
		studyApproachDate.add(dobStudyApproachDatePicker);
		
		yearOfFirstMamogramTxtFld =  new TextField<Long>("subjectStudy.yearOfFirstMamogram",Long.class);
		yearOfRecentMamogramTxtFld =  new TextField<String>("subjectStudy.yearOfRecentMamogram");
		totalNumberOfMamogramsTxtFld = new TextField<String>("subjectStudy.totalNumberOfMamograms");
		
		streetAddressTxtFld = new TextField<String>("subjectStudy.siteAddress");
		cityTxtFld = new TextField<String>("subjectStudy.city");
		postCodeTxtFld = new TextField<String>("subjectStudy.postCode");
		
		initialiseCountryDropDown();
		initialiseCountrySelector();
		
		Collection<YesNo> yesNoList = iArkCommonService.getYesNoList(); 
		ChoiceRenderer<YesNo> yesnoRenderer = new ChoiceRenderer<YesNo>(Constants.NAME,Constants.ID);
		consentToActiveContactDdc = new DropDownChoice<YesNo>("subjectStudy.consentToActiveContact",(List)yesNoList,yesnoRenderer);
		
		consentToUseDataDdc = new DropDownChoice<YesNo>("subjectStudy.consentToUseData",(List)yesNoList,yesnoRenderer);
		
		consentToPassDataGatheringDdc  = new DropDownChoice<YesNo>("subjectStudy.consentToPassiveDataGathering",(List)yesNoList,yesnoRenderer);
		
	}
	
	private void initialiseCountrySelector(){
		
		countryStateSelector = new WebMarkupContainer("countryStateSelector");
		countryStateSelector.setOutputMarkupPlaceholderTag(true);
		//Get the value selected in Country
		Country selectedCountry  = countryChoice.getModelObject();
		
		//If there is no country selected, back should default to current country and pull the states
		List<CountryState> countryStateList  = iArkCommonService.getStates(selectedCountry);
		ChoiceRenderer<CountryState> defaultStateChoiceRenderer = new ChoiceRenderer<CountryState>("state", Constants.ID);
		stateChoice = new DropDownChoice<CountryState>("subjectStudy.state",countryStateList,defaultStateChoiceRenderer);
		//Add the Country State Dropdown into the WebMarkupContainer - countrySelector
		countryStateSelector.add(stateChoice);
	}
	
	private void initialiseCountryDropDown(){
		
		final List<Country> countryList = iArkCommonService.getCountries();
		ChoiceRenderer<Country> defaultChoiceRenderer = new ChoiceRenderer<Country>(Constants.NAME, Constants.ID);
		
		countryChoice = new DropDownChoice<Country>("subjectStudy.country", countryList, defaultChoiceRenderer);
		//Attach a behavior, so when it changes it does something
		countryChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				updateCountryStateChoices(countryChoice.getModelObject());
				target.addComponent(countryStateSelector);
			}
		});
	
	}
	
	/**
	 * A method that will refresh the choices in the State drop down choice based on
	 * what was selected in the Country Dropdown. It uses the country as the argument and invokes
	 * the back-end to fetch relative states.
	 */
	private void updateCountryStateChoices(Country country){
		
		List<CountryState> countryStateList = iArkCommonService.getStates(country);
		stateChoice.getChoices().clear();
		stateChoice.setChoices(countryStateList);
	}
	
	public void addDetailFormComponents(){

		detailPanelFormContainer.add(subjectUIDTxtFld);
		detailPanelFormContainer.add(titleTypeDdc);
		detailPanelFormContainer.add(firstNameTxtFld);
		detailPanelFormContainer.add(middleNameTxtFld);
		detailPanelFormContainer.add(lastNameTxtFld);
		detailPanelFormContainer.add(previousLastNameTxtFld);
		detailPanelFormContainer.add(preferredNameTxtFld);
		detailPanelFormContainer.add(dateOfBirthTxtFld);
		detailPanelFormContainer.add(vitalStatusDdc);
		
		// Death deatils only editable when vital status set to deceased
		wmcDeathDetailsContainer.add(dateOfDeathTxtFld);
		wmcDeathDetailsContainer.add(causeOfDeathTxtFld);
		detailPanelFormContainer.add(wmcDeathDetailsContainer);
		
		detailPanelFormContainer.add(genderTypeDdc);
		detailPanelFormContainer.add(subjectStatusDdc);
		detailPanelFormContainer.add(maritalStatusDdc);
		detailPanelFormContainer.add(personContactMethodDdc);
		
		// Prerred email becomes required when selected as preferred contact method
		wmcPreferredEmailContainer.add(preferredEmailTxtFld);
		detailPanelFormContainer.add(wmcPreferredEmailContainer);
		detailPanelFormContainer.add(otherEmailTxtFld);
		
		//Add the supposed-to-be custom controls into the form container.
		detailPanelFormContainer.add(amdrifIdTxtFld);
		detailPanelFormContainer.add(studyApproachDate);
		detailPanelFormContainer.add(yearOfFirstMamogramTxtFld);
		detailPanelFormContainer.add(yearOfRecentMamogramTxtFld);
		detailPanelFormContainer.add(totalNumberOfMamogramsTxtFld);
		detailPanelFormContainer.add(streetAddressTxtFld);
		detailPanelFormContainer.add(cityTxtFld);
		detailPanelFormContainer.add(postCodeTxtFld);
		detailPanelFormContainer.add(countryChoice);
		detailPanelFormContainer.add(countryStateSelector);//This contains the drop-downn for State
		detailPanelFormContainer.add(consentToActiveContactDdc);
		detailPanelFormContainer.add(consentToUseDataDdc);
		detailPanelFormContainer.add(consentToPassDataGatheringDdc);
	}


	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.addComponent(feedBackPanel);
	}
	
	protected  void onCancel(AjaxRequestTarget target){
		subjectUIDTxtFld.setEnabled(true);
		SubjectVO subjectVO = new SubjectVO();
		containerForm.setModelObject(subjectVO);
		onCancelPostProcess(target);
		
	}
	

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		subjectUIDTxtFld.setRequired(true).setLabel(new StringResourceModel("subject.uid.required", this, null));
		dateOfBirthTxtFld.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("dob.range", this, null));
		studyApproachDate.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("approach.date", this, null));
		
		titleTypeDdc.setRequired(true).setLabel(new StringResourceModel("title.type.required", this, null));
		vitalStatusDdc.setRequired(true).setLabel(new StringResourceModel("vital.status.required", this, null));
		genderTypeDdc.setRequired(true).setLabel(new StringResourceModel("gender.type.required", this, null));
		subjectStatusDdc.setRequired(true).setLabel(new StringResourceModel("subject.subjectStatus.required", this, null));
		
		preferredEmailTxtFld.add(EmailAddressValidator.getInstance());
		otherEmailTxtFld.add(EmailAddressValidator.getInstance());
	}

	private boolean validateCustomFields(Long fieldToValidate,String message, AjaxRequestTarget target){
		boolean validFlag=true;
		Calendar calendar = Calendar.getInstance();
		int calYear = calendar.get(Calendar.YEAR);
		if(fieldToValidate > calYear){
			validFlag=false;
			this.error(message);
			processErrors(target);
		}
		
		return validFlag;
	}

	
	private void saveUpdateProcess(SubjectVO subjectVO,AjaxRequestTarget target){
		
		if(subjectVO.getSubjectStudy().getPerson().getId() == null || 		containerForm.getModelObject().getSubjectStudy().getPerson().getId() == 0){
	
			subjectVO.getSubjectStudy().setStudy(study);
			try
			{
				studyService.createSubject(subjectVO);
				StringBuffer sb = new StringBuffer();
				sb.append("The Subject with Subject UID: ");
				sb.append(subjectVO.getSubjectStudy().getSubjectUID());
				sb.append(" has been created successfully and linked to the study in context " );
				sb.append(study.getName());
				onSavePostProcess(target);
				this.info(sb.toString());
				
			}
			catch (ArkUniqueException ex)
			{
				this.error("Subject UID must be unique.");
			}

		}else{

			try {
				studyService.updateSubject(subjectVO);
				StringBuffer sb = new StringBuffer();
				sb.append("The Subject with Subject UID: ");
				sb.append(subjectVO.getSubjectStudy().getSubjectUID());
				sb.append(" has been updated successfully and linked to the study in context " );
				sb.append(study.getName());
				onSavePostProcess(target);
				this.info(sb.toString());
			} catch (ArkUniqueException e) {
				this.error("Subject UID must be unique.");
			}
		}
		processErrors(target);
	}
	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<SubjectVO> containerForm,	AjaxRequestTarget target) {
		boolean firstMammogramFlag =false;
		boolean recentMamogramFlag = false;
		target.addComponent(detailPanelContainer);
		
		Long studyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if(studyId == null){
			//No study in context
			this.error("There is no study in Context. Please select a study to manage a subject.");
			processErrors(target);
		}
		else{
			
			study = iArkCommonService.getStudy(studyId);
			Long yearOfFirstMammogram = containerForm.getModelObject().getSubjectStudy().getYearOfFirstMamogram();
			Long yearOfRecentMammogram =containerForm.getModelObject().getSubjectStudy().getYearOfRecentMamogram();	
			//validate if the fields were supplied
			if(yearOfFirstMammogram != null){
				firstMammogramFlag = validateCustomFields(containerForm.getModelObject().getSubjectStudy().getYearOfFirstMamogram(),
						"Year of Fist Mammogram cannot be in the future.",
						target);
			}
			 
			if(yearOfRecentMammogram != null){
				 recentMamogramFlag =validateCustomFields(containerForm.getModelObject().getSubjectStudy().getYearOfRecentMamogram(),
							"Year of recent Mammogram cannot be in the future.",
							target);
			}
			
			//When both the year fields were supplied, save only if they are valid
			if( (yearOfFirstMammogram != null && firstMammogramFlag)  && (yearOfRecentMammogram != null && recentMamogramFlag)){
				saveUpdateProcess(containerForm.getModelObject(), target);
			}
			else if((yearOfFirstMammogram != null && firstMammogramFlag)  && (yearOfRecentMammogram == null)){//when only yearOfFirstMammogram was supplied
				saveUpdateProcess(containerForm.getModelObject(), target);
			}
			else if((yearOfFirstMammogram == null )  && (yearOfRecentMammogram != null && recentMamogramFlag)){
				saveUpdateProcess(containerForm.getModelObject(), target);
			}else if(yearOfFirstMammogram == null && yearOfRecentMammogram == null){
				//When other
				saveUpdateProcess(containerForm.getModelObject(), target);
			}
			
			//String subjectPreviousLastname = iArkCommonService.getPreviousLastname(containerForm.getModelObject().getSubjectStudy().getPerson());
			//containerForm.getModelObject().setSubjectPreviousLastname(subjectPreviousLastname);
			
			ContextHelper contextHelper = new ContextHelper();
			contextHelper.resetContextLabel(target, arkContextMarkupContainer);
			contextHelper.setStudyContextLabel(target, study.getName(), arkContextMarkupContainer);
			contextHelper.setSubjectContextLabel(target,containerForm.getModelObject().getSubjectStudy().getSubjectUID(), arkContextMarkupContainer);
			
			SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID, containerForm.getModelObject().getSubjectStudy().getPerson().getId());
			//We specify the type of person here as Subject
			SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_TYPE, au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT);
			detailPanelContainer.setVisible(true);
		}	
	}
}