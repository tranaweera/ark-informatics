package au.org.theark.study.web.component.pedigree.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyPedigreeConfiguration;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.study.model.vo.PedigreeVo;
import au.org.theark.study.service.IStudyService;

public class ConfigurationForm extends Form<PedigreeVo> {

	/**
	 * 
	 */
	private static final long						serialVersionUID	= 1L;

	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService							studyService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService		iArkCommonService;
	
	protected FeedbackPanel							feedbackPanel;

	protected ArkCrudContainerVO					arkCrudContainerVO;

	protected AbstractDetailModalWindow			modalWindow;
	
	private DropDownChoice<CustomField>			effectedStatuses;

	private CompoundPropertyModel<PedigreeVo>	 cpmModel;

	private List<CustomField>						customFieldList;

	private CheckBox									dobChkBox;

	protected AjaxButton								saveButton;

	protected AjaxButton								cancelButton;

	public ConfigurationForm(String id, CompoundPropertyModel<PedigreeVo> cpmModel, FeedbackPanel feedbackPanel, ArkCrudContainerVO arkCrudContainerVO, AbstractDetailModalWindow modalWindow) {
		super(id,cpmModel);
		this.cpmModel = cpmModel;
		this.feedbackPanel = feedbackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.modalWindow = modalWindow;

		initialiseSearchForm();
		addSearchComponentsToForm();
	}

	protected void initialiseSearchForm() {
		
		final Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

//		StudyPedigreeConfiguration config = studyService.getStudyPedigreeConfiguration(studyId);
		
		Study study= iArkCommonService.getStudy(studyId);
		StudyPedigreeConfiguration config=study.getPedigreeConfiguration();
		
		cpmModel.getObject().setPedigreeConfig(config);
		
		customFieldList = studyService.getBinaryCustomFieldsForPedigreeRelativesList(studyId);
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		effectedStatuses = new DropDownChoice("pedigreeConfig.customField", this.customFieldList, defaultChoiceRenderer);
		
		dobChkBox = new CheckBox("pedigreeConfig.dobAllowed");

		saveButton = new AjaxButton(au.org.theark.core.Constants.SAVE) {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				PedigreeVo obj =(PedigreeVo)form.getModelObject();
				
				StudyPedigreeConfiguration configObject = obj.getPedigreeConfig();
//				System.out.println("Selected custom field name --- "+obj.getPedigreeConfig().getCustomField().getName());
//				System.out.println("DOB value --- "+obj.getPedigreeConfig().isDobAllowed());
//				System.out.println("---------------------------------------------------------- ");
//				System.out.println("---------------------------------------------------------- ");
						
				studyService.saveOrUpdateStudyPedigreeConfiguration(configObject);
				modalWindow.close(target);
			}
		};

		cancelButton = new AjaxButton("close") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				modalWindow.close(target);
			}
		};
	}

	protected void addSearchComponentsToForm() {
		add(effectedStatuses);
		add(dobChkBox);
		add(saveButton);
		add(cancelButton);

	}
}
