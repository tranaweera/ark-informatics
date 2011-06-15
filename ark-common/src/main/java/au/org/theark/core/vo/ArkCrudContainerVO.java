/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.vo;

import java.io.Serializable;

import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * @author nivedann
 *
 */
public class ArkCrudContainerVO implements Serializable{
	
	protected WebMarkupContainer searchPanelContainer;
	protected WebMarkupContainer searchResultPanelContainer;
	protected WebMarkupContainer detailPanelContainer;
	protected WebMarkupContainer detailPanelFormContainer;
	protected WebMarkupContainer viewButtonContainer;
	protected WebMarkupContainer editButtonContainer;
	protected WebMarkupContainer wizardPanelContainer;
	protected WebMarkupContainer wizardPanelFormContainer;
	
	protected WebMarkupContainer wmcForarkUserAccountPanel;
	
	public ArkCrudContainerVO(){
		
		searchPanelContainer = new WebMarkupContainer("searchContainer");
		searchPanelContainer.setOutputMarkupPlaceholderTag(true);
		
		detailPanelContainer = new WebMarkupContainer("detailContainer");
		detailPanelContainer.setOutputMarkupPlaceholderTag(true);
		
		detailPanelFormContainer = new WebMarkupContainer("detailFormContainer");
		detailPanelFormContainer.setOutputMarkupPlaceholderTag(true);

		searchResultPanelContainer = new WebMarkupContainer("resultListContainer");
		searchResultPanelContainer.setOutputMarkupPlaceholderTag(true);
		
		viewButtonContainer = new WebMarkupContainer("viewButtonContainer");
		viewButtonContainer.setOutputMarkupPlaceholderTag(true);
		
		editButtonContainer = new WebMarkupContainer("editButtonContainer");
		editButtonContainer.setOutputMarkupPlaceholderTag(true);
				
		wizardPanelContainer = new WebMarkupContainer("wizardContainer");
		wizardPanelContainer.setOutputMarkupPlaceholderTag(true);
		
		//Contains the controls of the Wizard
		wizardPanelFormContainer = new WebMarkupContainer("wizardFormContainer");
		wizardPanelFormContainer.setOutputMarkupPlaceholderTag(true);
		
		//Will contain User Management related Modules and Roles Panel
		wmcForarkUserAccountPanel = new WebMarkupContainer("arkUserAccountPanelcontainer");
		wmcForarkUserAccountPanel.setOutputMarkupPlaceholderTag(true);
		detailPanelContainer.setVisible(false);
		detailPanelFormContainer.setEnabled(false);
		searchResultPanelContainer.setVisible(true);
		viewButtonContainer.setVisible(false);
		editButtonContainer.setVisible(false);
		wizardPanelContainer.setVisible(true);
		wizardPanelFormContainer.setEnabled(true);
		
	}
	
	public WebMarkupContainer getSearchPanelContainer() {
		return searchPanelContainer;
	}
	public void setSearchPanelContainer(WebMarkupContainer searchPanelContainer) {
		this.searchPanelContainer = searchPanelContainer;
	}
	public WebMarkupContainer getSearchResultPanelContainer() {
		return searchResultPanelContainer;
	}
	public void setSearchResultPanelContainer(
			WebMarkupContainer searchResultPanelContainer) {
		this.searchResultPanelContainer = searchResultPanelContainer;
	}
	public WebMarkupContainer getDetailPanelContainer() {
		return detailPanelContainer;
	}
	public void setDetailPanelContainer(WebMarkupContainer detailPanelContainer) {
		this.detailPanelContainer = detailPanelContainer;
	}
	public WebMarkupContainer getDetailPanelFormContainer() {
		return detailPanelFormContainer;
	}
	public void setDetailPanelFormContainer(
			WebMarkupContainer detailPanelFormContainer) {
		this.detailPanelFormContainer = detailPanelFormContainer;
	}
	public WebMarkupContainer getViewButtonContainer() {
		return viewButtonContainer;
	}
	public void setViewButtonContainer(WebMarkupContainer viewButtonContainer) {
		this.viewButtonContainer = viewButtonContainer;
	}
	public WebMarkupContainer getEditButtonContainer() {
		return editButtonContainer;
	}
	public void setEditButtonContainer(WebMarkupContainer editButtonContainer) {
		this.editButtonContainer = editButtonContainer;
	}

	public WebMarkupContainer getWmcForarkUserAccountPanel() {
		return wmcForarkUserAccountPanel;
	}

	public void setWmcForarkUserAccountPanel(
			WebMarkupContainer wmcForarkUserAccountPanel) {
		this.wmcForarkUserAccountPanel = wmcForarkUserAccountPanel;
	}

}
