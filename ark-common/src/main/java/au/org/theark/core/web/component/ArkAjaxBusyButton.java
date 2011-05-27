package au.org.theark.core.web.component;

import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPostprocessingCallDecorator;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.model.IModel;

/**
 * AjaxButton that disables whole web page, and re-enables once processing completed, 
 * also has an pop-up loading/busy indicator. Can set whether or not to actually turn on busy indicator (eg buttons that don't submit page)
 */
public abstract class ArkAjaxBusyButton extends IndicatingAjaxButton{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6243098370244180405L;
	private boolean displayBusyIndicator = true;
	private String setBusyIndicatorOn = "document.getElementById('busyIndicator').style.display ='inline';\n" +
										"overlay = document.getElementById(\"overlay\");\n" +
										"overlay.style.visibility = \"visible\";";
	
	private String setBusyIndicatorOff = "document.getElementById('busyIndicator').style.display ='none';\n" +
										 "overlay = document.getElementById(\"overlay\");\n" +
										 "overlay.style.visibility = \"hidden\";";

	public ArkAjaxBusyButton(String id) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
	}
	
	public ArkAjaxBusyButton(String id, IModel<String> model)
	{
		super(id, model, null);
		setOutputMarkupPlaceholderTag(true);
	}
	
    @Override
    protected IAjaxCallDecorator getAjaxCallDecorator() {
        return new AjaxPostprocessingCallDecorator(super.getAjaxCallDecorator()) {
            private static final long serialVersionUID = 1L;
 
            @Override
            public CharSequence postDecorateScript(CharSequence script) {
            	if(displayBusyIndicator){
            		return script + setBusyIndicatorOn; 
            	}
            	return script;
            }
            
            @Override
            public CharSequence postDecorateOnFailureScript(CharSequence script) {
            	if(displayBusyIndicator){
            		return script + setBusyIndicatorOff; 
            	}
            	return script;
            }
            
            @Override
            public CharSequence postDecorateOnSuccessScript(CharSequence script) {
            	if(displayBusyIndicator){
            		return script + setBusyIndicatorOff;
            	}
            	return script;
            }
        };
    }
    
    public boolean isDisplayBusyIndicator() {
		return displayBusyIndicator;
	}

	public void setDisplayBusyIndicator(boolean displayBusyIndicator) {
		this.displayBusyIndicator = displayBusyIndicator;
	}
}