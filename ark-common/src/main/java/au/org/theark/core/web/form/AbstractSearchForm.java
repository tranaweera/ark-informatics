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
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkBusyAjaxButton;

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
public abstract class AbstractSearchForm<T> extends Form<T>
{

	/**
	 * 
	 */
	private static final long		serialVersionUID	= -408051334961302312L;
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
	 * Constructor
	 * 
	 * @param id
	 * @param model
	 */
	public AbstractSearchForm(String id, CompoundPropertyModel<T> cpmModel)
	{
		super(id, cpmModel);

		initialiseForm();
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param cpmModel
	 * @param detailPanelContainer
	 * @param detailFormCompContainer
	 * @param viewButtonContainer
	 * @param editButtonContainer
	 * @param searchMarkupContainer
	 * @param listContainer
	 * @param feedBackPanel
	 */
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
	 * 
	 * @param id
	 * @param cpmModel
	 */
	public AbstractSearchForm(String id, IModel<T> cpmModel, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO)
	{
		super(id, cpmModel);
		this.feedbackPanel = feedBackPanel;
		initialiseForm(arkCrudContainerVO);
	}

	protected void onReset()
	{
		clearInput();
		updateFormComponentModels();
	}

	protected void initialiseForm()
	{
		searchButton = new AjaxButton(Constants.SEARCH)
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -3504899640173586559L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				// Make the details panel visible
				onSearch(target);
			}

			@Override
			public boolean isVisible()
			{
				return ArkPermissionHelper.isActionPermitted(Constants.SEARCH);
			}

			@Override
			protected void onError(final AjaxRequestTarget target, Form<?> form)
			{
				target.addComponent(feedbackPanel);
			}
		};

		resetButton = new Button(Constants.RESET)
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -6785467702774902246L;

			public void onSubmit()
			{
				onReset();
			}

			@Override
			public boolean isVisible()
			{
				return ArkPermissionHelper.isActionPermitted(Constants.SEARCH);
			}
		};

		newButton = new ArkBusyAjaxButton(Constants.NEW)
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 3592424656251078184L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				// Make the details panel visible, disabling delete button (if found)
				AjaxButton ajaxButton = (AjaxButton) editButtonContainer.get("delete");
				if (ajaxButton != null)
				{
					ajaxButton.setEnabled(false);
					target.addComponent(ajaxButton);
				}
				// Call abstract method
				onNew(target);
			}

			@Override
			public boolean isVisible()
			{
				return ArkPermissionHelper.isActionPermitted(Constants.NEW);
			}

			@Override
			protected void onError(final AjaxRequestTarget target, Form<?> form)
			{
				target.addComponent(feedbackPanel);
			}
		};

		addComponentsToForm();
	}

	/**
	 * Initialise the form, utilising the common ArkCrudContainerVO object
	 * 
	 * @param arkCrudContainerVO
	 */
	protected void initialiseForm(final ArkCrudContainerVO arkCrudContainerVO)
	{
		searchButton = new AjaxButton(Constants.SEARCH)
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -8096410123770458109L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				// Make the details panel visible
				onSearch(target);
			}

			@Override
			protected void onError(final AjaxRequestTarget target, Form<?> form)
			{
				target.addComponent(feedbackPanel);
			}
		};

		resetButton = new Button(Constants.RESET)
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 5818909400695185935L;

			public void onSubmit()
			{
				onReset();
			}
		};

		newButton = new ArkBusyAjaxButton(Constants.NEW)
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1666656098281624401L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				// Make the details panel visible, disabling delete button (if found)
				// AjaxButton ajaxButton = (AjaxButton) editButtonContainer.get("delete");
				AjaxButton ajaxButton = (AjaxButton) arkCrudContainerVO.getEditButtonContainer().get("delete");
				if (ajaxButton != null)
				{
					ajaxButton.setEnabled(false);
					target.addComponent(ajaxButton);
				}
				// Call abstract method
				onNew(target);
			}

			@Override
			public boolean isVisible()
			{
				return ArkPermissionHelper.isActionPermitted(Constants.NEW);
			}

			@Override
			protected void onError(final AjaxRequestTarget target, Form<?> form)
			{
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
	 * 
	 * @param target
	 * @param flag
	 */
	protected void preProcessDetailPanel(AjaxRequestTarget target, ArkCrudContainerVO arkCrudContainerVO)
	{
		arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
		arkCrudContainerVO.getDetailPanelFormContainer().setVisible(true);
		arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(true);

		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
		arkCrudContainerVO.getEditButtonContainer().setVisible(true);
		arkCrudContainerVO.getViewButtonContainer().setVisible(false);
		arkCrudContainerVO.getSearchPanelContainer().setVisible(false);

		target.addComponent(arkCrudContainerVO.getDetailPanelFormContainer());
		target.addComponent(arkCrudContainerVO.getDetailPanelContainer());

		target.addComponent(arkCrudContainerVO.getSearchResultPanelContainer());
		target.addComponent(arkCrudContainerVO.getSearchPanelContainer());
		target.addComponent(arkCrudContainerVO.getViewButtonContainer());
		target.addComponent(arkCrudContainerVO.getEditButtonContainer());
	}

	/**
	 * Allow disabling of the search form, based on a session object in context
	 * 
	 * @param sessionId
	 * @param errorMessage
	 */
	protected void disableSearchForm(Long sessionId, String errorMessage)
	{
		if (ArkPermissionHelper.isModuleFunctionAccessPermitted())
		{
			if (sessionId == null)
			{
				searchMarkupContainer.setEnabled(false);
				this.error(errorMessage);
			}
			else
			{
				searchMarkupContainer.setEnabled(true);
			}
		}
		else
		{
			searchMarkupContainer.setEnabled(false);
			listContainer.setVisible(false);
			this.error(au.org.theark.core.Constants.MODULE_NOT_ACCESSIBLE_MESSAGE);
		}
	}

	protected void disableSearchForm(Long sessionId, String errorMessage, ArkCrudContainerVO arkCrudContainerVO)
	{
		if (ArkPermissionHelper.isModuleFunctionAccessPermitted())
		{
			if (sessionId == null)
			{
				arkCrudContainerVO.getSearchPanelContainer().setEnabled(false);
				this.error(errorMessage);
			}
			else
			{
				arkCrudContainerVO.getSearchPanelContainer().setEnabled(true);
			}
		}
		else
		{
			arkCrudContainerVO.getSearchPanelContainer().setEnabled(false);
			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
			this.error(au.org.theark.core.Constants.MODULE_NOT_ACCESSIBLE_MESSAGE);
		}
	}

	abstract protected void onSearch(AjaxRequestTarget target);

	abstract protected void onNew(AjaxRequestTarget target);
}