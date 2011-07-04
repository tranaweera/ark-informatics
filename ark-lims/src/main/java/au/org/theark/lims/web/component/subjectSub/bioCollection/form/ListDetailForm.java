package au.org.theark.lims.web.component.subjectSub.bioCollection.form;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.security.ArkSecurity;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.component.listeditor.AbstractListEditor;
import au.org.theark.core.web.component.listeditor.AjaxListDeleteButton;
import au.org.theark.core.web.component.listeditor.ListItem;
import au.org.theark.core.web.form.AbstractListDetailForm;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.subject.form.ContainerForm;
import au.org.theark.lims.web.component.subjectSub.DetailModalWindow;
import au.org.theark.lims.web.component.subjectSub.bioCollection.DetailPanel;
import au.org.theark.lims.web.component.subjectSub.bioCollection.ListDetailPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ListDetailForm extends AbstractListDetailForm<LimsVO>
{
	/**
	 * 
	 */
	private static final long						serialVersionUID		= 5402491244761953070L;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>				iArkCommonService;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService								iLimsService;

	private LinkSubjectStudy						linkSubjectStudy;
	private Label									idLblFld;
	private Label									nameLblFld;
	private Label									commentsLblFld;
	private Label									collectionDateLblFld;
	private Label									surgeryDateLblFld;
	private ListDetailPanel							listDetailPanel;
	private DetailPanel 							detailPanel;
	private DetailModalWindow						modalWindow;
	
	public ListDetailForm(String id, FeedbackPanel feedbackPanel, ContainerForm containerForm,  DetailModalWindow modalWindow, ListDetailPanel listDetailPanel)
	{
		super(id);
		this.feedbackPanel = feedbackPanel;
		this.containerForm = containerForm;
		this.modalWindow = modalWindow;
		this.listDetailPanel = listDetailPanel;
		
		this.detailPanel = new DetailPanel("content", modalWindow, containerForm);
		
		String subjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
		
		if(subjectUID != null)
		{
			try
			{
				linkSubjectStudy = iArkCommonService.getSubjectByUID(subjectUID);
			}
			catch (EntityNotFoundException e)
			{
			}
		}
	}

	@Override
	protected AbstractListEditor<BioCollection> initialiseListEditor()
	{
		listEditor = new AbstractListEditor<BioCollection>("listDetails", new PropertyModel(containerForm.getModelObject(), "bioCollectionList"))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -1972072222086035973L;

			@Override
			protected void onPopulateItem(final ListItem<BioCollection> item)
			{
				item.setOutputMarkupId(true);
				item.setModel(new CompoundPropertyModel(item.getModel()));
				final BioCollection bioCollection = item.getModelObject();

				AjaxButton listEditButton = new AjaxButton("listEditButton", new StringResourceModel("editKey", this, null))
				{
					/**
					 * 
					 */
					private static final long	serialVersionUID	= -6032731528995858376L;

					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form)
					{
						// Refresh any feedback
						target.addComponent(feedbackPanel);
						
						// Set selected item into model.context, then show modalWindow for editing
						Form<LimsVO> listDetailsForm = (Form<LimsVO>) form;
						
						BioCollection bioCollectionSelected = bioCollection;
						containerForm.getModelObject().setBioCollection(bioCollectionSelected);
						
						showModalWindow(target, listDetailsForm);
					}
					
					@Override
					public boolean isVisible()
					{
						return ArkSecurity.isActionPermitted(au.org.theark.core.Constants.EDIT);
					}
				};
				
				item.addOrReplace(listEditButton);
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);

				idLblFld = new Label("id", item.getModelObject().getId().toString());
				nameLblFld = new Label("name", item.getModelObject().getName());
				collectionDateLblFld = new Label("collectionDate", simpleDateFormat.format(item.getModelObject().getCollectionDate()));
				surgeryDateLblFld = new Label("surgeryDate", simpleDateFormat.format(item.getModelObject().getCollectionDate()));
				commentsLblFld = new Label("comments");

				ArkDatePicker datePicker = new ArkDatePicker();
				datePicker.bind(collectionDateLblFld);
				collectionDateLblFld.add(datePicker);

				ArkDatePicker datePicker2 = new ArkDatePicker();
				datePicker2.bind(surgeryDateLblFld);
				surgeryDateLblFld.add(datePicker2);

				item.add(idLblFld);
				item.add(nameLblFld);
				item.add(collectionDateLblFld);
				item.add(surgeryDateLblFld);
				item.add(commentsLblFld);

				AjaxListDeleteButton deleteButton = new AjaxListDeleteButton("listDeleteButton", new StringResourceModel("confirmDelete", this, null),
						new StringResourceModel(Constants.DELETE, this, null))
				{
					/**
					 * 
					 */
					private static final long	serialVersionUID	= -585048033031888283L;

					@Override
					protected void onDeleteConfirmed(AjaxRequestTarget target, Form<?> form)
					{
						containerForm.getModelObject().setBioCollection(bioCollection);
						
						try
						{
							iLimsService.deleteBioCollection(containerForm.getModelObject());
							this.info("Biospecimen collection " + bioCollection.getName() + " was deleted successfully");
						}
						catch(org.hibernate.NonUniqueObjectException noe)
						{
							this.error(noe.getMessage());
						}

						// Display delete confirmation message
						target.addComponent(feedbackPanel);
						target.addComponent(form);
					}

					@Override
					public boolean isEnabled()
					{
						//return (bioCollection.getId() != null);
						return (!iLimsService.hasBiospecimens(bioCollection));
					}
					
					@Override
					public boolean isVisible()
					{
						return ArkSecurity.isActionPermitted(au.org.theark.core.Constants.DELETE);
					}
				};
				item.addOrReplace(deleteButton);
				
				item.add(new AttributeModifier(Constants.CLASS, true, new AbstractReadOnlyModel() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1710579139006313949L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));

				attachValidators();
			}
		};
		
		return (AbstractListEditor<BioCollection>) listEditor;
	}

	public void initialiseList()
	{
		java.util.List<BioCollection> bioCollectionList = new ArrayList<BioCollection>(0);
		try
		{
			BioCollection bioCollection = new BioCollection();
			bioCollection.setLinkSubjectStudy(linkSubjectStudy);
			bioCollectionList = iLimsService.searchBioCollection(bioCollection);
			getModelObject().setBioCollectionList(bioCollectionList);
		}
		catch (ArkSystemException e)
		{
			error(e.getMessage());
		}
	}

	protected void attachValidators()
	{
	}
	
	/**
	 * @param linkSubjectStudy
	 *           the linkSubjectStudy to set
	 */
	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy)
	{
		this.linkSubjectStudy = linkSubjectStudy;
	}

	/**
	 * @return the linkSubjectStudy
	 */
	public LinkSubjectStudy getLinkSubjectStudy()
	{
		return linkSubjectStudy;
	}

	/**
	 * @return the listDetailPanel
	 */
	public ListDetailPanel getListDetailPanel()
	{
		return listDetailPanel;
	}

	/**
	 * @param listDetailPanel the listDetailPanel to set
	 */
	public void setListDetailPanel(ListDetailPanel listDetailPanel)
	{
		this.listDetailPanel = listDetailPanel;
	}

	protected void saveOnErrorProcess(AjaxRequestTarget target)
	{
		target.addComponent(feedbackPanel);
		target.addComponent(this);
	}

	protected void processErrors(AjaxRequestTarget target)
	{
		target.addComponent(feedbackPanel);
		target.addComponent(this);
	}

	@Override
	protected void onNew(AjaxRequestTarget target, Form<LimsVO> form)
	{
		// refresh the feedback messages 
		target.addComponent(feedbackPanel);
		
		Form<LimsVO> listDetailsForm = (Form<LimsVO>) form;
		
		// Set new BioCollection into model, then show modalWindow to save
		containerForm.getModelObject().setBioCollection(new BioCollection());
		if(linkSubjectStudy != null)
		{
			containerForm.getModelObject().getBioCollection().setLinkSubjectStudy(linkSubjectStudy);
			containerForm.getModelObject().getBioCollection().setStudy(linkSubjectStudy.getStudy());
		}
		else
		{
			containerForm.getModelObject().getBioCollection().setLinkSubjectStudy(containerForm.getModelObject().getLinkSubjectStudy());
			containerForm.getModelObject().getBioCollection().setStudy(containerForm.getModelObject().getLinkSubjectStudy().getStudy());
		}
		
		showModalWindow(target, listDetailsForm);
	}
	
	protected void showModalWindow(AjaxRequestTarget target, Form<LimsVO> form)
	{
		// Set the modalWindow title and content
		modalWindow.setTitle("Collection Detail");
		modalWindow.setContent(detailPanel);
		modalWindow.setListDetailPanel(listDetailPanel);
		modalWindow.setListDetailForm(form);
		modalWindow.show(target);
	}
}