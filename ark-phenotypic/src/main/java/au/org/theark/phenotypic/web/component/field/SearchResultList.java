package au.org.theark.phenotypic.web.component.field;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.vo.FieldVO;
import au.org.theark.phenotypic.web.component.field.form.ContainerForm;

@SuppressWarnings( { "serial", "unchecked" })
public class SearchResultList extends Panel
{

	private WebMarkupContainer	detailsPanelContainer;
	private WebMarkupContainer	searchPanelContainer;
	private WebMarkupContainer	searchResultContainer;
	private ContainerForm		containerForm;
	private Detail					detailPanel;

	public SearchResultList(String id, WebMarkupContainer detailPanelContainer, WebMarkupContainer searchPanelContainer, ContainerForm studyCompContainerForm, WebMarkupContainer searchResultContainer,
			Detail detail)
	{
		super(id);
		this.detailsPanelContainer = detailPanelContainer;
		this.containerForm = studyCompContainerForm;
		this.searchPanelContainer = searchPanelContainer;
		this.searchResultContainer = searchResultContainer;
		this.setDetailPanel(detail);
	}

	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of Field
	 */
	public PageableListView<Field> buildPageableListView(IModel iModel)
	{

		PageableListView<Field> sitePageableListView = new PageableListView<Field>("fieldList", iModel, 10)
		{
			@Override
			protected void populateItem(final ListItem<Field> item)
			{
				Field field = item.getModelObject();

				/* The Field ID */
				if (field.getId() != null)
				{
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_ID, field.getId().toString()));
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_ID, ""));
				}

				// Component Name Link
				item.add(buildLink(field));

				// TODO when displaying text escape any special characters
				// Field Type
				if (field.getFieldType() != null)
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_FIELD_TYPE, field.getFieldType().getName()));// the ID here
					// must match the
					// ones in mark-up
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_FIELD_TYPE, ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// Description
				if (field.getDescription() != null)
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_DESCRIPTION, field.getDescription()));// the ID here must match
					// the ones in mark-up
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_DESCRIPTION, ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// Units
				if (field.getName() != null)
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_UNITS, field.getUnits()));// the ID here must match the ones in
					// mark-up
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_UNITS, ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// Min
				if (field.getMinValue() != null)
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_MIN_VALUE, field.getMinValue()));// the ID here must match the
					// ones in mark-up
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_MIN_VALUE, ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// Max
				if (field.getMinValue() != null)
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_MAX_VALUE, field.getMaxValue()));// the ID here must match the
					// ones in mark-up
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_MAX_VALUE, ""));// the ID here must match the ones in mark-up
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

	private AjaxLink buildLink(final Field field)
	{
		AjaxLink link = new AjaxLink("field.name")
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				FieldVO fieldVo = containerForm.getModelObject();
				fieldVo.setMode(au.org.theark.core.Constants.MODE_READ);
				
				// Sets the selected object into the model
				fieldVo.setField(field);
				
				detailsPanelContainer.setVisible(true);
				searchResultContainer.setVisible(false);
				searchPanelContainer.setVisible(false);

				detailPanel.getDetailForm().getFieldIdTxtFld().setEnabled(false);

				target.addComponent(searchResultContainer);
				target.addComponent(detailsPanelContainer);
				target.addComponent(searchPanelContainer);
			}
		};

		// Add the label for the link
		// TODO when displaying text escape any special characters
		Label nameLinkLabel = new Label("nameLbl", field.getName());
		link.add(nameLinkLabel);
		return link;
	}

	/**
	 * @param detailPanel
	 *           the detailPanel to set
	 */
	public void setDetailPanel(Detail detailPanel)
	{
		this.detailPanel = detailPanel;
	}

	/**
	 * @return the detailPanel
	 */
	public Detail getDetailPanel()
	{
		return detailPanel;
	}
}
