package au.org.theark.core.web.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.StringResourceModel;

import au.org.theark.core.Constants;
import au.org.theark.core.vo.ArkCrudContainerVO;

/**
 * <p>
 * Abstract class for ListDetailForm sub-classes. It provides some common functionality that sub-classes inherit. Provides the skeleton methods for
 * onSave,onDelete,onCancel etc.Defines the core buttons like save,delete, cancel, and edit. Provides method to toggle the view from read only to edit
 * mode which is usually a common behavior the sub-classes can re-use.
 * </p>
 * 
 * @author cellis
 * @param <T>
 * 
 */
public abstract class AbstractListDetailForm<T> extends AbstractDetailForm<T>
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8486268113733956635L;

	public AbstractListDetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, Form<T> containerForm)
	{
		super(id, feedBackPanel, arkCrudContainerVO, containerForm);
	}

	protected void initialiseForm(Boolean isArkCrudContainerVOPattern)
	{

		cancelButton = new AjaxButton(Constants.CANCEL, new StringResourceModel("cancelKey", this, null))
		{
			private static final long	serialVersionUID	= 1684005199059571017L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				if (isNew())
				{
					editCancelProcess(target, true);
				}
				else
				{
					editCancelProcessForUpdate(target);
				}
			}

		};

		saveButton = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -423605230448635419L;

			@Override
			public boolean isVisible()
			{
				return isActionPermitted(Constants.SAVE);
			}

			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onSave(containerForm, target);
				target.addComponent(arkCrudContainerVO.getDetailPanelContainer());
			}

			@SuppressWarnings("unchecked")
			public void onError(AjaxRequestTarget target, Form<?> form)
			{

				saveOnErrorProcess(target);
			}
		};

		deleteButton = new AjaxButton(Constants.DELETE, new StringResourceModel("deleteKey", this, null))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -2430231894703055744L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				// target.addComponent(detailPanelContainer);
				onDelete(containerForm, target);

			}

			@Override
			public boolean isVisible()
			{
				return isActionPermitted(Constants.DELETE);
			}
		};

		editButton = new AjaxButton("edit", new StringResourceModel("editKey", this, null))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -6282464357368710796L;

			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				editButtonProcess(target);
				// Add the sub-class functionality
				onEditButtonClick();

			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				processErrors(target);
			}

			@Override
			public boolean isVisible()
			{
				return isActionPermitted(Constants.EDIT);
			}
		};

		editCancelButton = new AjaxButton("editCancel", new StringResourceModel("editCancelKey", this, null))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 5457464178392550628L;

			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				editCancelProcess(target, true);
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				processErrors(target);
			}

		};

		selectModalWindow = initialiseModalWindow();

		addComponentsToForm(true);
	}
	
	/**
	 * Abstract method that allows sub-classes to implement specific functionality for onEditButtonClick event.
	 */
	public abstract void onEditButtonClick();
}