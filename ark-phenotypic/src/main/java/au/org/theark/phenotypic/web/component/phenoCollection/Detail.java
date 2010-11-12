package au.org.theark.phenotypic.web.component.phenoCollection;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.phenotypic.model.vo.CollectionVO;
import au.org.theark.phenotypic.model.vo.FieldVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.summary.form.ContainerForm;
import au.org.theark.phenotypic.web.component.summary.form.DetailForm;

@SuppressWarnings("serial")
public class Detail extends Panel
{
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService					phenotypicService;

	private DetailForm							detailForm;
	private FeedbackPanel						feedBackPanel;
	private WebMarkupContainer					listContainer;
	private WebMarkupContainer					detailsContainer;
	private WebMarkupContainer					searchPanelContainer;
	private ContainerForm						containerForm;
	private WebMarkupContainer 				detailPanelFormContainer;
	private WebMarkupContainer 				viewButtonContainer;
	private WebMarkupContainer 				editButtonContainer;
	private ModalWindow 					selectModalWindow;

	public Detail(String id, final WebMarkupContainer listContainer, FeedbackPanel feedBackPanel, WebMarkupContainer detailsContainer, WebMarkupContainer searchPanelContainer,
			ContainerForm containerForm,
			WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer,
			WebMarkupContainer detailPanelFormContainer)
	{
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.listContainer = listContainer;
		this.detailsContainer = detailsContainer;
		this.containerForm = containerForm;
		this.searchPanelContainer = searchPanelContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
	}

	public void initialisePanel()
	{
		detailForm = new DetailForm("detailForm", this, listContainer, detailsContainer, containerForm, viewButtonContainer, editButtonContainer, detailPanelFormContainer)
		{
			protected void onSave(CollectionVO collectionVo, AjaxRequestTarget target)
			{
				//TODO Implement try catch for exception handling
				// try {
				if (collectionVo.getCollection().getId() == null)
				{
					// Save the Collection
					phenotypicService.createCollection(collectionVo.getCollection());
					this.info("Collection " + collectionVo.getCollection().getName() + " was created successfully");
					processFeedback(target);
				}
				else
				{
					// Update the Collection
					phenotypicService.updateCollection(collectionVo.getCollection());
					this.info("Collection " + collectionVo.getCollection().getName() + " was updated successfully");
					processFeedback(target);
				}
				
				postSaveUpdate(target);

				//TODO Implement Exceptions in PhentoypicService
				//  } catch (UnAuthorizedOperation e) { this.error("You are not authorised to manage study components for the given study " +
				//  study.getName()); processFeedback(target); } catch (ArkSystemException e) {
				//  this.error("A System error occured, we will have someone contact you."); processFeedback(target); }
			}

			protected void onCancel(AjaxRequestTarget target)
			{
				CollectionVO collectionVo = new CollectionVO();
				containerForm.setModelObject(collectionVo);
				searchPanelContainer.setVisible(true);
				detailsContainer.setVisible(false);
				target.addComponent(searchPanelContainer);
				target.addComponent(feedBackPanel);
				target.addComponent(detailsContainer);
			}
			
			protected void onDelete(CollectionVO collectionVo, AjaxRequestTarget target)
			{
				selectModalWindow.show(target);
				target.addComponent(selectModalWindow);
			}
			
			// On click of Edit button, allow form to be editable
			protected void onEdit(CollectionVO collectionVo, AjaxRequestTarget target)
			{
				detailPanelFormContainer.setEnabled(true);
				editButtonContainer.setVisible(true);
				viewButtonContainer.setVisible(false);
				detailForm.getDeleteButton().setEnabled(true);
				detailForm.getDeleteButton().setVisible(true);
				
				target.addComponent(feedBackPanel);
				target.addComponent(detailPanelFormContainer);
				target.addComponent(viewButtonContainer);
				target.addComponent(editButtonContainer);
			}

			protected void processFeedback(AjaxRequestTarget target)
			{
				target.addComponent(feedBackPanel);
			}
			
			protected void processErrors(AjaxRequestTarget target)
			{
				target.addComponent(feedBackPanel);
			}
		};

		detailForm.initialiseForm();
		detailPanelFormContainer.add(initialiseModalWindow());
		add(detailForm);
	}
	
	private void postSaveUpdate(AjaxRequestTarget target){
		// Button containers
		// View Collection, thus view container visible
		viewButtonContainer.setVisible(true);
		editButtonContainer.setVisible(false);
		detailPanelFormContainer.setEnabled(false);
		
		target.addComponent(feedBackPanel);
		target.addComponent(viewButtonContainer);
		target.addComponent(editButtonContainer);
	}
	
	private ModalWindow initialiseModalWindow(){
	   // The ModalWindow, showing some choices for the user to select.
		selectModalWindow = new au.org.theark.core.web.component.SelectModalWindow("modalwindow"){

	      protected void onSelect(AjaxRequestTarget target, String selection) {
	      	//TODO Implement try catch for exception handling
				// try {
	      	
	      	// Handle Delete action
	   		phenotypicService.deleteCollection(containerForm.getModelObject().getCollection());
	   		this.info("Collection " + containerForm.getModelObject().getCollection().getName() + " was deleted successfully");
	   		
	   		// Display delete confirmation message
	   		target.addComponent(feedBackPanel);
	   		
	   		//TODO Implement Exceptions in PhentoypicService
				//  } catch (UnAuthorizedOperation e) { this.error("You are not authorised to manage study components for the given study " +
				//  study.getName()); processFeedback(target); } catch (ArkSystemException e) {
				//  this.error("A System error occured, we will have someone contact you."); processFeedback(target); }
	         
	   		// Close the confirm modal window
	   		close(target);
	         
				// Move focus back to Search form
	   		CollectionVO collectionVo = new CollectionVO();
				containerForm.setModelObject(collectionVo);
				searchPanelContainer.setVisible(true);
				detailsContainer.setVisible(false);
				target.addComponent(searchPanelContainer);
				target.addComponent(detailsContainer);
	      }

	      protected void onCancel(AjaxRequestTarget target) {
	         // Handle Cancel action
	      	// Close the confirm modal window
	         close(target);
	         
	         // Go back into Edit mode (and remove feedback, if straight after "New")
	         detailPanelFormContainer.setEnabled(true);
				editButtonContainer.setVisible(true);
				viewButtonContainer.setVisible(false);
				detailForm.getDeleteButton().setEnabled(true);
				detailForm.getDeleteButton().setVisible(true);
				
				target.addComponent(detailPanelFormContainer);
				target.addComponent(viewButtonContainer);
				target.addComponent(editButtonContainer);
	      }
	  };
	  return selectModalWindow;
	}
	
	public DetailForm getDetailForm()
	{
		return detailForm;
	}

	public void setDetailForm(DetailForm detailsForm)
	{
		this.detailForm = detailsForm;
	}
}