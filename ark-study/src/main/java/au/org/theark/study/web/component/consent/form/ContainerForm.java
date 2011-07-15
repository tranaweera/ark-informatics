package au.org.theark.study.web.component.consent.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.web.form.AbstractContainerForm;

/**
 * @author nivedann
 * 
 */
public class ContainerForm extends AbstractContainerForm<ConsentVO> {

	/**
	 * @param id
	 * @param cpmModel
	 */
	public ContainerForm(String id, CompoundPropertyModel<ConsentVO> cpmModel) {
		super(id, cpmModel);
	}

}
