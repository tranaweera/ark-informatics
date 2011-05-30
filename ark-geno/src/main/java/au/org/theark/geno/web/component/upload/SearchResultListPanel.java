/**
 * 
 */
package au.org.theark.geno.web.component.upload;

import java.text.SimpleDateFormat;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.Constants;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.geno.model.entity.Upload;
import au.org.theark.geno.model.entity.UploadCollection;
import au.org.theark.geno.model.vo.UploadCollectionVO;
import au.org.theark.geno.web.component.upload.form.ContainerForm;

/**
 * @author elam
 *
 */
public class SearchResultListPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UploadContainerPanel uploadContainerPanel;
	private ContainerForm containerForm;

	public SearchResultListPanel(String id,
			UploadContainerPanel uploadContainerPanel,
			ContainerForm containerForm) {
		super(id);
		this.uploadContainerPanel = uploadContainerPanel; 
		this.containerForm = containerForm;
	}

	public PageableListView<UploadCollection> buildPageableListView(IModel iModel) {
		
		PageableListView<UploadCollection> sitePageableListView = new PageableListView<UploadCollection>("collectionList", iModel, 10)
		{
			@Override
			protected void populateItem(final ListItem<UploadCollection> item)
			{
				UploadCollection uploadCollection = item.getModelObject();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DD_MM_YYYY);

				/* The phenoCollection ID */
				if (uploadCollection.getId() != null)
				{
					// Add the id component here
					item.add(new Label(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_VO_ID, uploadCollection.getId().toString()));
				}
				else
				{
					item.add(new Label(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_VO_ID, ""));
				}

				// TODO when displaying text escape any special characters
				// Description
				Upload anUpload;
				if ((anUpload = uploadCollection.getUpload()) != null) {
					
					// Component Name Link
					item.add(buildLink(uploadCollection));
					
					// TODO when displaying text escape any special characters
					// Start Date
					if (anUpload.getFileFormat() != null)
					{
						item.add(new Label(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_VO_UPLOAD_FILEFORMAT, anUpload.getFileFormat().getName()));// the ID here must match
						// the ones in mark-up
					}
					else
					{
						item.add(new Label(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_VO_UPLOAD_FILEFORMAT, ""));// the ID here must match the ones in mark-up
					}
					
					if (anUpload.getDelimiterType() != null)
					{
						item.add(new Label(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_VO_UPLOAD_DELIMITERTYPE, anUpload.getDelimiterType().getName()));// the ID here must match
						// the ones in mark-up
					}
					else
					{
						item.add(new Label(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_VO_UPLOAD_DELIMITERTYPE, ""));// the ID here must match the ones in mark-up
					}
				
				}
		
				/* For the alternative stripes */
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel()
				{
					@Override
					public String getObject()
					{
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

			}
		};
		return sitePageableListView;
	}

	private AjaxLink buildLink(final UploadCollection uploadCollection)
	{
		ArkBusyAjaxLink link = new ArkBusyAjaxLink(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_VO_UPLOAD_FILENAME)
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				UploadCollectionVO collectionVo = containerForm.getModelObject();
				collectionVo.setUploadCollection(uploadCollection);
				
				// TODO: Place the selected upload in session context for the user (passes to Shiro) ***MAY NOT BE NECESSARY***
//				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.geno.web.Constants.SESSION_GENO_COLLECTION_ID, uploadCollection.getId());
		
				uploadContainerPanel.showViewDetail(target);
			}
		};

		// Add the label for the link
		// TODO when displaying text escape any special characters
		Label nameLinkLabel = new Label("nameLbl", uploadCollection.getUpload().getFilename());
		link.add(nameLinkLabel);
		return link;
	}

}
