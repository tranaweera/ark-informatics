package au.org.theark.lims.web.component.subjectLims.lims.biospecimen.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPreprocessingCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.util.BiospecimenIdGenerator;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.subjectLims.lims.biospecimen.BiospecimenModalDetailPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "unchecked", "rawtypes" })
public class BiospecimenListForm extends Form<LimsVO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SpringBean( name =  au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService iLimsService;

	protected CompoundPropertyModel<LimsVO> cpModel;
	protected FeedbackPanel feedbackPanel;
	protected AbstractDetailModalWindow modalWindow;

	private Label idLblFld;
	private Label nameLblFld;
	private Label sampleTypeLblFld;
	private Label collectionLblFld;
	private Label commentsLblFld;
	private Label quantityLblFld;
	// private BioCollectionListPanel bioCollectionListPanel;
	private Panel modalContentPanel;
	protected AjaxButton newButton;

	protected WebMarkupContainer dataViewListWMC;
	private DataView<Biospecimen> dataView;
	private ArkDataProvider<Biospecimen, ILimsService> biospecimenProvider;

	public BiospecimenListForm(String id, FeedbackPanel feedbackPanel, AbstractDetailModalWindow modalWindow, CompoundPropertyModel<LimsVO> cpModel) {
		super(id, cpModel);
		this.cpModel = cpModel;
		this.feedbackPanel = feedbackPanel;
		this.modalWindow = modalWindow;
	}

	public void initialiseForm() {
		modalContentPanel = new EmptyPanel("content");

		initialiseDataView();
		initialiseNewButton();
		
		add(modalWindow);
	}
	
	@Override
	public void onBeforeRender() {
		// Reset the Biospecimen (for criteria) in LimsVO
		// This prevents the manual modal "X" close button from not reseting the criteria
		cpModel.getObject().setBiospecimen(new Biospecimen());

		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		String sessionSubjectUID = (String)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);

		if ((sessionStudyId != null) && (sessionSubjectUID != null)) {
			LinkSubjectStudy linkSubjectStudy = null;
			Study study = null;
			boolean contextLoaded = false;
			try {
				study = iArkCommonService.getStudy(sessionStudyId);
				linkSubjectStudy = iArkCommonService.getSubjectByUID(sessionSubjectUID);
				if (study != null && linkSubjectStudy != null) {
					contextLoaded = true;
				}
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (contextLoaded) {
				// Successfully loaded from backend
				cpModel.getObject().setLinkSubjectStudy(linkSubjectStudy);
				cpModel.getObject().getBiospecimen().setLinkSubjectStudy(linkSubjectStudy);
				cpModel.getObject().getBiospecimen().setStudy(study);
			}
		}
		super.onBeforeRender();
	}
	
	private void initialiseDataView() {
		dataViewListWMC = new WebMarkupContainer("dataViewListWMC");
		dataViewListWMC.setOutputMarkupId(true);
		// Data provider to paginate resultList
		biospecimenProvider = new ArkDataProvider<Biospecimen, ILimsService>(iLimsService) {

			public int size() {
				return service.getBiospecimenCount(model.getObject());
			}

			public Iterator<Biospecimen> iterator(int first, int count) {
				List<Biospecimen> listSubjects = new ArrayList<Biospecimen>();
				if (ArkPermissionHelper
						.isActionPermitted(au.org.theark.core.Constants.SEARCH)) {
					listSubjects = service.searchPageableBiospecimens(model
							.getObject(), first, count);
				}
				return listSubjects.iterator();
			}
		};
		// Set the criteria into the data provider's model
		biospecimenProvider.setModel(new LoadableDetachableModel<Biospecimen>() {
			@Override
			protected Biospecimen load() {
				return cpModel.getObject().getBiospecimen();
			}
		});
		
		dataView = buildDataView(biospecimenProvider);
		dataView.setItemsPerPage(au.org.theark.core.Constants.ROWS_PER_PAGE);

		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", dataView) {
			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.addComponent(dataViewListWMC);
			}
		};
		dataViewListWMC.add(pageNavigator);
		dataViewListWMC.add(dataView);
		add(dataViewListWMC);

	}

	private void initialiseNewButton() {
		newButton = new AjaxButton("listNewButton", new StringResourceModel("listNewKey", this, null)) {
			/*
			 * When any AjaxButton in the form is clicked on, it eventually calls form.inputChanged().
			 * This then goes through all its children to check/call isVisible() and isEnabled().
			 * Thus, it is best to keep the isVisible() and isEnabled() very light-weight
			 * (e.g. avoid hitting the database to work this out)
			 */
			@Override
			public boolean isVisible()
			{
//				// Needs CREATE permission AND a BioCollection to select from
//				boolean hasBioCollections = false;
//
//				hasBioCollections = iLimsService.hasBioCollections(cpModel.getObject().getLinkSubjectStudy());
//
//				if(!hasBioCollections)
//				{
//					hasBioCollections = false;
//					this.error("No Biospecimen Collections exist. Please create at least one Collection.");
//				}
//				else
//				{
//					hasBioCollections = true;
//				}
//				return (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.NEW) && hasBioCollections);
				return ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.NEW);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onNew(target);	
			}
		};
		newButton.setDefaultFormProcessing(false);

		add(newButton);
	}

	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of BioCollection
	 */
	public DataView<Biospecimen> buildDataView(
			ArkDataProvider<Biospecimen, ILimsService> biospecimenProvider) {

		DataView<Biospecimen> biospecimenDataView = new DataView<Biospecimen>(
				"biospecimenList", biospecimenProvider) {
			@Override
			protected void populateItem(final Item<Biospecimen> item) {
				item.setOutputMarkupId(true);
				// DO NOT store the item.getModelObject! Checking it is ok...
				final Biospecimen biospecimen = item.getModelObject();

				WebMarkupContainer rowEditWMC = new WebMarkupContainer("rowEditWMC", item.getModel());
				/*
				 * When any AjaxButton in the form is clicked on, it eventually calls form.inputChanged().
				 * This then goes through all its children to check/call isVisible() and isEnabled().
				 * By avoiding the use of AjaxButtons when not required, no form submit is caused and thus
				 * less processing is required.
				 */
//				AjaxButton listEditButton = new AjaxButton("listEditButton", new StringResourceModel("editKey", this, null)) {
//					/**
//					 * 
//					 */
//					private static final long serialVersionUID = -6032731528995858376L;
//
//					@Override
//					protected void onSubmit(AjaxRequestTarget target,
//							Form<?> form) {
//						// Refresh any feedback
//						target.addComponent(feedbackPanel);
//
//						// // Set selected item into model.context, then show
//						// modalWindow for editing
//						// Form<LimsVO> listDetailsForm = (Form<LimsVO>) form;
//						//						
//						Biospecimen b = (Biospecimen)(getParent().getDefaultModelObject());
//						cpModel.getObject().setBiospecimen(b);							
//						showModalWindow(target, cpModel);
//					}
//
//					@Override
//					public boolean isVisible()
//					{
//						return ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.EDIT);
//					}
//				};
				AjaxLink listEditLink = new AjaxLink("listEditLink") {

					@Override
					public void onClick(AjaxRequestTarget target) {
						Biospecimen b = (Biospecimen)(getParent().getDefaultModelObject());
						CompoundPropertyModel<LimsVO> newModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
						newModel.getObject().getBiospecimen().setId(b.getId());
						showModalWindow(target, newModel);
					}
					
					@Override
					public boolean isVisible()
					{
						return ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.EDIT);
					}
				};
				
//				listEditButton.setDefaultFormProcessing(false);
//				rowEditWMC.add(listEditButton);
				Label nameLinkLabel = new Label("lblEditLink", "Edit");
				listEditLink.add(nameLinkLabel);
				rowEditWMC.add(listEditLink);
				item.add(rowEditWMC);

//				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
//						au.org.theark.core.Constants.DD_MM_YYYY);

				idLblFld = new Label("biospecimen.id", String.valueOf(biospecimen.getId()));
				nameLblFld = new Label("biospecimen.biospecimenId", biospecimen.getBiospecimenId());
				sampleTypeLblFld = new Label("biospecimen.sampleType.name", biospecimen.getSampleType().getName());
				collectionLblFld = new Label("biospecimen.bioCollection.name", biospecimen.getBioCollection().getName());
				commentsLblFld = new Label("biospecimen.comments", biospecimen.getComments());
				if (biospecimen.getQuantity() == null) { 
					quantityLblFld = new Label("biospecimen.quantity", "0");
				}
				else {
					quantityLblFld = new Label("biospecimen.quantity", biospecimen.getQuantity().toString());
				}

				item.add(idLblFld);
				item.add(nameLblFld);
				item.add(sampleTypeLblFld);
				item.add(collectionLblFld);
				item.add(commentsLblFld);
				item.add(quantityLblFld);

				WebMarkupContainer rowDeleteWMC = new WebMarkupContainer("rowDeleteWMC", item.getModel());
				AjaxButton deleteButton = new AjaxButton("listDeleteButton",
						new StringResourceModel(Constants.DELETE, this, null)) {
					IModel confirm = new StringResourceModel("confirmDelete", this, null);
					/**
					 * 
					 */
					private static final long serialVersionUID = -585048033031888283L;
					
					@Override
					public boolean isVisible() {
						return ArkPermissionHelper
								.isActionPermitted(au.org.theark.core.Constants.DELETE);
					}

					@Override
					protected IAjaxCallDecorator getAjaxCallDecorator() {
						return new AjaxPreprocessingCallDecorator(super.getAjaxCallDecorator()) {
							private static final long serialVersionUID = 7495281332320552876L;

							@Override
							public CharSequence preDecorateScript(
									CharSequence script) {
								StringBuffer sb = new StringBuffer();
								sb.append("if(!confirm('");
								sb.append(confirm.getObject());
								sb.append("'))");
								sb.append("{ ");
								sb.append("	return false ");
								sb.append("} else { ");
								sb.append("	this.disabled = true;" );
								sb.append("};");
								sb.append(script);
								return sb;
							}
						};
					}

					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						target.addComponent(form);
						onDeleteConfirmed(target, form);
					}

					protected void onDeleteConfirmed(AjaxRequestTarget target, Form<?> form) {
						// Leave the cpModel's BioCollection as-is
						LimsVO bioCollectionSelectdLimsVO = new LimsVO();
						Biospecimen biospecimenSelected = (Biospecimen)(getParent().getDefaultModelObject());
						bioCollectionSelectdLimsVO.setBiospecimen(biospecimenSelected);

						iLimsService.deleteBiospecimen(bioCollectionSelectdLimsVO);
						this.info("Biospecimen " + biospecimenSelected.getBiospecimenId() + " was deleted successfully");
				
						// Display delete confirmation message
						target.addComponent(feedbackPanel);
						target.addComponent(form);						
					}

				};
				deleteButton.setDefaultFormProcessing(false);
				rowDeleteWMC.add(deleteButton);
				item.add(rowDeleteWMC);

				item.add(new AttributeModifier(Constants.CLASS, true,
						new AbstractReadOnlyModel() {

							@Override
							public String getObject() {
								return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
							}
						}));
			}

		};

		return biospecimenDataView;
	}

	protected void onNew(AjaxRequestTarget target) {
		// Needs CREATE permission AND a BioCollection to select from
		boolean hasBioCollections = false;

		hasBioCollections = iLimsService.hasBioCollections(cpModel.getObject().getLinkSubjectStudy());

		if (hasBioCollections) {
			// Set new Biospecimen into model, then show modalWindow to save
			CompoundPropertyModel<LimsVO> newModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
	//		newModel.getObject().setBiospecimen(new Biospecimen());
			newModel.getObject().getBiospecimen().setLinkSubjectStudy(getModelObject().getLinkSubjectStudy());
			newModel.getObject().getBiospecimen().setStudy(getModelObject().getLinkSubjectStudy().getStudy());
	
			// Create new BiospecimenUID
			newModel.getObject().getBiospecimen().setBiospecimenId(BiospecimenIdGenerator.generateBiospecimenId());
			
			showModalWindow(target, newModel); // listDetailsForm);
		}
		else {
			this.error("No Biospecimen Collections exist. Please create at least one Collection.");
		}
		// refresh the feedback messages
		target.addComponent(feedbackPanel);
	}

	protected void showModalWindow(AjaxRequestTarget target, CompoundPropertyModel<LimsVO> cpModel) {
		modalContentPanel = new BiospecimenModalDetailPanel("content", modalWindow, cpModel);

		// Set the modalWindow title and content
		modalWindow.setTitle("Biospecimen Detail");
		modalWindow.setContent(modalContentPanel);
		modalWindow.show(target);
	}

}