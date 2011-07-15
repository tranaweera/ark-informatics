package au.org.theark.core.web.component;

import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPostprocessingCallDecorator;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.model.IModel;

/**
 * AjaxButton that disables onSubmit, and renables once processing completed
 */
public abstract class ArkAjaxButton extends AjaxButton {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4620255487709221599L;

	public ArkAjaxButton(String id) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
	}

	public ArkAjaxButton(String id, IModel<String> model) {
		super(id, model, null);
		setOutputMarkupPlaceholderTag(true);
	}

	@Override
	protected IAjaxCallDecorator getAjaxCallDecorator() {
		return new AjaxPostprocessingCallDecorator(super.getAjaxCallDecorator()) {
			private static final long	serialVersionUID	= 1L;

			@Override
			public CharSequence postDecorateScript(CharSequence script) {
				return script + "document.getElementById('" + getMarkupId() + "').disabled = true;";
			}

			@Override
			public CharSequence postDecorateOnFailureScript(CharSequence script) {
				return script + "document.getElementById('" + getMarkupId() + "').disabled = false;";
			}

			@Override
			public CharSequence postDecorateOnSuccessScript(CharSequence script) {
				return script + "document.getElementById('" + getMarkupId() + "').disabled = false;";
			}
		};
	}
}