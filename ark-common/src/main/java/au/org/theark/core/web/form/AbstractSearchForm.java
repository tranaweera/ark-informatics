package au.org.theark.core.web.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.Constants;
import au.org.theark.core.vo.ArkCrudContainerVO;

/**
 * <p>
 * An Abstract Form class for Search. This class contains common behaviour that the sub-classes can inherit. The sub-classes themselves can override
 * the behaviour of the abstract but can also add more specific implementation if needed. As part of this class we have defined the New,Search and
 * Reset button and their behaviour which will be common for all search functions.
 * </p>
 * 
 * @author nivedann
 * @param <T>
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractSearchForm<T> extends Form<T>
{

	protected AjaxButton				searchButton;
	protected AjaxButton				newButton;
	protected Button					resetButton;
	protected WebMarkupContainer	viewButtonContainer;
	protected WebMarkupContainer	editButtonContainer;
	protected WebMarkupContainer	detailPanelContainer;
	protected WebMarkupContainer	searchMarkupContainer;
	protected WebMarkupContainer	listContainer;
	protected WebMarkupContainer	detailFormCompContainer;
	protected FeedbackPanel			feedbackPanel;
	
	private ArkCrudContainerVO arkCrudContainerVO;
	

	/**
	 * @param id
	 * @param model
	 */
	public AbstractSearchForm(String id, CompoundPropertyModel<T> cpmModel)
	{

		super(id, cpmModel);
		initialiseForm();

	}

	public AbstractSearchForm(String id, CompoundPropertyModel<T> cpmModel, WebMarkupContainer detailPanelContainer, WebMarkupContainer detailFormCompContainer, WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer, WebMarkupContainer searchMarkupContainer, WebMarkupContainer listContainer, FeedbackPanel feedBackPanel)
	{

		super(id, cpmModel);
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailPanelContainer = detailPanelContainer;
		this.searchMarkupContainer = searchMarkupContainer;
		this.listContainer = listContainer;
		this.detailFormCompContainer = detailFormCompContainer;
		this.feedbackPanel = feedBackPanel;
		initialiseForm();

	}
	

	/**
	 * Nivedan working
	 * @param id
	 * @param cpmModel
	 */
	public AbstractSearchForm(	String id, 
								IModel<T> cpmModel, 
								FeedbackPanel feedBackPanel,
								ArkCrudContainerVO arkCrudContainerVO){
		super(id,cpmModel);
		this.feedbackPanel = feedBackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		initialiseForm();
	}

	abstract protected void onSearch(AjaxRequestTarget target);

	abstract protected void onNew(AjaxRequestTarget target);

	/* This method should be implemented by sub-classes to secure a control(New button etc..) */
	abstract protected boolean isSecure(String actionType);
	

	protected void onReset()
	{
		clearInput();
		updateFormComponentModels();
	}

	protected void initialiseForm()
	{
		searchButton = new AjaxButton(Constants.SEARCH)
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				// Make the details panel visible
				onSearch(target);
			}

			@Override
			public boolean isVisible()
			{
				return isSecure(Constants.SEARCH);
			}
			
			@Override
			 protected void onError(final AjaxRequestTarget target, Form form) {
				target.addComponent(feedbackPanel);
			} 
		};

		resetButton = new Button(Constants.RESET)
		{
			public void onSubmit()
			{
				onReset();
			}

			@Override
			public boolean isVisible()
			{
				return isSecure(Constants.RESET);
			}
		};

		newButton = new AjaxButton(Constants.NEW)
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				// Make the details panel visible, disabling delete button (if found)
				AjaxButton ajaxButton = (AjaxButton) editButtonContainer.get("delete");
				if (ajaxButton != null) {
					ajaxButton.setEnabled(false);
					target.addComponent(ajaxButton);
				}
				// Call abstract method
				onNew(target);
			}

			@Override
			public boolean isVisible()
			{
				return isSecure(Constants.NEW);
			}
			
			@Override
			protected void onError(final AjaxRequestTarget target, Form form) {
				target.addComponent(feedbackPanel);
			}
		};

		addComponentsToForm();
	}

	protected void addComponentsToForm()
	{
		add(searchButton);
		add(resetButton.setDefaultFormProcessing(false));
		add(newButton);
	}

	protected void preProcessDetailPanel(AjaxRequestTarget target)
	{

		detailPanelContainer.setVisible(true);
		listContainer.setVisible(false);
		editButtonContainer.setVisible(true);
		viewButtonContainer.setVisible(false);
		searchMarkupContainer.setVisible(false);
		detailFormCompContainer.setEnabled(true);

		target.addComponent(detailPanelContainer);
		target.addComponent(listContainer);
		target.addComponent(searchMarkupContainer);
		target.addComponent(viewButtonContainer);
		target.addComponent(editButtonContainer);
		target.addComponent(detailFormCompContainer);
	}
	
	/**
	 * Overloaded Method that uses the VO to set the WMC's
	 * @param target
	 * @param flag
	 */
	protected void preProcessDetailPanel(AjaxRequestTarget target, boolean flag)
	{
		arkCrudContainerVO.getDetailPanelFormContainer().setVisible(true);
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
		arkCrudContainerVO.getEditButtonContainer().setVisible(true);
		arkCrudContainerVO.getViewButtonContainer().setVisible(false);
		arkCrudContainerVO.getSearchPanelContainer().setVisible(false);
		arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(true);
		detailFormCompContainer.setEnabled(true);
	}

	protected void disableSearchButtons(Long sessionId, String errorMessage)
	{	
		if (sessionId == null)
		{
			searchButton.setEnabled(false);
			newButton.setEnabled(false);
			resetButton.setEnabled(false);
			this.error(errorMessage);
		}
		else
		{
			newButton.setEnabled(true);
			searchButton.setEnabled(true);
			resetButton.setEnabled(true);
		}
	}
	
	
	
	
}