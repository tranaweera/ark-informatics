package au.org.theark.core.web.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.Constants;

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
	protected ISearchEventHandler searchEventHandler;
	
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

	/**
	 * @param id
	 * @param model
	 */
	public AbstractSearchForm(String id, CompoundPropertyModel<T> cpmModel)
	{

		super(id, cpmModel);
		this.searchEventHandler = null;
		initialiseForm();

	}

	/**
	 * Constructor for AbstractSearchForm class (based on AbstractCRUDPanel)
	 * @param id
	 * @param cpmModel
	 * @param searchEventHandler
	 */
	public AbstractSearchForm(String id, CompoundPropertyModel<T> cpmModel, 
								ISearchEventHandler searchEventHandler)
	{

		super(id, cpmModel);
		this.searchEventHandler = searchEventHandler;
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

	abstract protected void onSearch(AjaxRequestTarget target);

	abstract protected void onNew(AjaxRequestTarget target);

	/* This method should be implemented by sub-classes to secure a control(New button etc..) */
	abstract protected boolean isSecure();

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
				if (searchEventHandler != null)
					searchEventHandler.onSearch(target);
				else
					onSearch(target);	//backwards compatibility
			}

			@Override
			public boolean isVisible()
			{
				return isSecure();
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
				return isSecure();
			}
		};

		newButton = new AjaxButton(Constants.NEW)
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				// Make the details panel visible
				if (searchEventHandler != null) {
					searchEventHandler.onNew(target);
					preProcessDetailPanel(target);		//TODO: Moved from the onNew() implementation
				} else {
					onNew(target);	//backwards compatibility
				}
			}

			@Override
			public boolean isVisible()
			{
				return isSecure();
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

	//backwards compatibility
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