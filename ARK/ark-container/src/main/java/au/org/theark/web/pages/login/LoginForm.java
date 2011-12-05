package au.org.theark.web.pages.login;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.web.pages.home.HomePage;
import au.org.theark.web.pages.reset.ResetPage;

/**
 * <p>
 * The <code>LoginForm</code> class that extends the {@link org.apache.wicket.markup.html.form.Form Form} class. It provides the
 * implementation of the login form of The Ark application.
 * </p>
 * 
 * @author nivedann
 * @author cellis
 */
public class LoginForm extends StatelessForm<ArkUserVO> {
	/**
	 * 
	 */
	private static final long			serialVersionUID	= -7482996457044513022L;
	private transient static Logger	log					= LoggerFactory.getLogger(LoginForm.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;
	private FeedbackPanel				feedbackPanel;
	private TextField<String>			userNameTxtFld		= new TextField<String>("userName");
	private PasswordTextField			passwordTxtFld		= new PasswordTextField("password");

	private Button						signInButton;
	private Button					forgotPasswordButton;

	/**
	 * LoginForm constructor
	 * 
	 * @param id
	 *           the Component identifier
	 */
	public LoginForm(String id, final FeedbackPanel feedbackPanel) {
		// Pass in the Model to the Form so the IFormSubmitListener can set the Model Object with values that were submitted.
		super(id, new CompoundPropertyModel<ArkUserVO>(new ArkUserVO()));
		this.feedbackPanel = feedbackPanel;

		signInButton = new Button("signInButton") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public void onSubmit() {
				ArkUserVO user = (ArkUserVO) getForm().getModelObject();
				if (authenticate(user)) {
					log.info( "\n ----" + user.getUserName() + " Logged in successfully. ---- \n");
					// Place a default use case into session
					ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_STUDY);
					// Place a default module into session
					ArkModule arkModule = iArkCommonService.getArkModuleByName(au.org.theark.core.Constants.ARK_MODULE_STUDY);

					SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_FUNCTION_KEY, arkFunction.getId());
					SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY, arkModule.getId());
					setResponsePage(HomePage.class);
				}else{
					setResponsePage(LoginPage.class);
				}
			}

			@Override
			public void onError() {

			}

		
		};
		
		forgotPasswordButton = new Button("forgotPasswordButton") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public void onSubmit() {
				setResponsePage(ResetPage.class);
			}
			
			@Override
			public void onError() {
				log.error("Error on click of forgotPasswordButton");
			}
		};
		forgotPasswordButton.setDefaultFormProcessing(false);
		
		addComponentsToForm();
	}
	

	private void addComponentsToForm() {
		add(userNameTxtFld.setRequired(true));
		add(passwordTxtFld.setRequired(true));
		add(signInButton);
		add(forgotPasswordButton);
	}

	/**
	 * Authenticate the given user
	 * 
	 * @param target
	 *           the AjaxRequestTarget
	 * @param user
	 *           the given user to authenticate
	 * @return
	 */
	public final boolean authenticate( ArkUserVO user) {
		Subject subject = SecurityUtils.getSubject();
		// Disable Remember me
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUserName(), user.getPassword(), false);

		try {
			// This will propagate to the Realm
			subject.login(usernamePasswordToken);
			log.info(user.getUserName() + " logged in.");
			return true;
		}
		catch (IncorrectCredentialsException e) {
			String errMessage = getLocalizer().getString("page.incorrect.password", LoginForm.this, "Password is incorrect");
			getSession().error(errMessage);
			error(errMessage);
			log.error(e.getMessage());
		}
		catch (UnknownAccountException e) {
			String errMessage = getLocalizer().getString("page.account.notfound", LoginForm.this, "User account not found.");
			error(errMessage);
			getSession().error(errMessage);
			log.error(e.getMessage());
		}
		catch (AuthenticationException e) {
			String errMessage = getLocalizer().getString("page.invalid.username.password", LoginForm.this, "Invalid username and/or password.");
			error(errMessage);
			getSession().error(errMessage);
			log.error(e.getMessage());
		}
		catch (Exception e) {
			String errMessage = getLocalizer().getString("page.login.failed", LoginForm.this, "Login Failed.");
			error(errMessage);
			getSession().error(errMessage);
			log.error(e.getMessage());
		}
		
		addOrReplace(feedbackPanel);
		return false;
	}
}
