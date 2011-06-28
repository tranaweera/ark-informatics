package au.org.theark.lims.web.component.subject.bioCollection.form;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractModalDetailForm;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings( { "serial", "unused" })
public class DetailForm extends AbstractModalDetailForm<LimsVO>
{
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService					iLimsService;

	private int								mode;

	private TextField<String>			idTxtFld;
	private TextField<String>			nameTxtFld;
	private TextField<String>			collectionIdTxtFld;
	private TextArea<String>			commentsTxtAreaFld;
	private DateTextField				collectionDateTxtFld;
	private DateTextField				surgeryDateTxtFld;
	private ModalWindow					modalWindow;
	private WebMarkupContainer			arkContextMarkup;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVo
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVo, Form<LimsVO> containerForm)
	{
		super(id, feedBackPanel, arkCrudContainerVo, containerForm);
	}
	
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVo
	 * @param modalWindow
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVo, ModalWindow modalWindow, Form<LimsVO> containerForm)
	{
		super(id, feedBackPanel, arkCrudContainerVo, containerForm);
		this.modalWindow = modalWindow;
	}

	public void initialiseDetailForm()
	{
		idTxtFld = new TextField<String>("bioCollection.id");
		nameTxtFld = new TextField<String>("bioCollection.name");
		commentsTxtAreaFld = new TextArea<String>("bioCollection.comments");
		collectionDateTxtFld = new DateTextField("bioCollection.collectionDate", au.org.theark.core.Constants.DD_MM_YYYY);
		surgeryDateTxtFld = new DateTextField("bioCollection.surgeryDate", au.org.theark.core.Constants.DD_MM_YYYY);

		ArkDatePicker startDatePicker = new ArkDatePicker();
		startDatePicker.bind(collectionDateTxtFld);
		collectionDateTxtFld.add(startDatePicker);

		ArkDatePicker endDatePicker = new ArkDatePicker();
		endDatePicker.bind(surgeryDateTxtFld);
		surgeryDateTxtFld.add(endDatePicker);

		attachValidators();
		addComponents();
	}

	protected void attachValidators()
	{
		nameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.bioCollection.name.required", this, new Model<String>("Name")));
	}

	private void addComponents()
	{
		arkCrudContainerVO.getDetailPanelFormContainer().add(idTxtFld.setEnabled(false));
		arkCrudContainerVO.getDetailPanelFormContainer().add(nameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(commentsTxtAreaFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(collectionDateTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(surgeryDateTxtFld);
		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}

	@Override
	protected void onSave(Form<LimsVO> containerForm, AjaxRequestTarget target)
	{
		// Subject in context
		LinkSubjectStudy linkSubjectStudy = new LinkSubjectStudy();
		String subjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);

		try
		{
			linkSubjectStudy = iArkCommonService.getSubjectByUID(subjectUID);
			containerForm.getModelObject().getBioCollection().setLinkSubjectStudy(linkSubjectStudy);
			containerForm.getModelObject().getBioCollection().setStudy(linkSubjectStudy.getStudy());

			if (containerForm.getModelObject().getBioCollection().getId() == null)
			{
				// Save
				iLimsService.createBioCollection(containerForm.getModelObject());
				this.info("Biospecimen collection " + containerForm.getModelObject().getBioCollection().getName() + " was created successfully");
				processErrors(target);
			}
			else
			{
				// Update
				iLimsService.updateBioCollection(containerForm.getModelObject());
				this.info("Biospecimen collection " + containerForm.getModelObject().getBioCollection().getName() + " was updated successfully");
				processErrors(target);
			}

			onSavePostProcess(target);
		}
		catch (EntityNotFoundException e)
		{
			this.error(e.getMessage());
			processErrors(target);
		}
		
		// Refresh parent listDetailForm
		//target.addComponent(containerForm);
	}

	@Override
	protected void onCancel(AjaxRequestTarget target)
	{
		//this.info("Cancel clicked");
		
		target.addComponent(feedbackPanel);
		//target.addComponent(containerForm);
		modalWindow.close(target);
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target)
	{
		iLimsService.deleteBioCollection(containerForm.getModelObject());
		this.info("Biospecimen collection " + containerForm.getModelObject().getBioCollection().getName() + " was deleted successfully");

		// Display delete confirmation message
		target.addComponent(feedbackPanel);

		// Move focus back to Search form
		LimsVO limsVo = new LimsVO();
		containerForm.setModelObject(limsVo);
		editCancelProcess(target);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target)
	{
		target.addComponent(feedbackPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew()
	{
		/*
		if (containerForm.getModelObject().getBioCollection().getId() == null)
		{
			return true;
		}
		else
		{
			return false;
		}
		*/
		return true;
	}
}