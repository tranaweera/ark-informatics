/**
 * 
 */
package au.org.theark.admin.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;
import au.org.theark.core.model.study.entity.Study;

/**
 * @author cellis
 * 
 */
public class AdminVO implements Serializable
{
	/**
	 * 
	 */
	private static final long				serialVersionUID	= -3939245546324873647L;

	private ArkRole							arkRole;
	private ArkModule							arkModule;
	private ArkFunction						arkFunction;
	private ArkRolePolicyTemplate			arkRolePolicyTemplate;
	private Study								study;
	private List<Study>						studyList;
	private List<ArkRolePolicyTemplate>	arkRolePolicyTemplateList;
	private List<ArkModule>					arkModuleList;
	private List<ArkFunction>				arkFunctionList;
	private Boolean							arkCreatePermission;
	private Boolean							arkReadPermission;
	private Boolean							arkUpdatePermission;
	private Boolean							arkDeletePermission;

	public AdminVO()
	{
		this.arkRole = new ArkRole();
		this.arkModule = new ArkModule();
		this.arkFunction = new ArkFunction();
		this.arkRolePolicyTemplate = new ArkRolePolicyTemplate();
		this.study = new Study();
		this.studyList = new ArrayList<Study>(0);
		this.arkRolePolicyTemplateList = new ArrayList<ArkRolePolicyTemplate>(0);
		this.arkModuleList = new ArrayList<ArkModule>(0);
		this.arkFunctionList = new ArrayList<ArkFunction>(0);
		this.arkCreatePermission = new Boolean("False");
		this.arkReadPermission = new Boolean("False");
		this.arkUpdatePermission = new Boolean("False");
		this.arkDeletePermission = new Boolean("False");
	}

	/**
	 * @return the arkRole
	 */
	public ArkRole getArkRole()
	{
		return arkRole;
	}

	/**
	 * @param arkRole
	 *           the arkRole to set
	 */
	public void setArkRole(ArkRole arkRole)
	{
		this.arkRole = arkRole;
	}

	/**
	 * @return the arkModule
	 */
	public ArkModule getArkModule()
	{
		return arkModule;
	}

	/**
	 * @param arkModule
	 *           the arkModule to set
	 */
	public void setArkModule(ArkModule arkModule)
	{
		this.arkModule = arkModule;
	}

	/**
	 * @return the arkFunction
	 */
	public ArkFunction getArkFunction()
	{
		return arkFunction;
	}

	/**
	 * @param arkFunction
	 *           the arkFunction to set
	 */
	public void setArkFunction(ArkFunction arkFunction)
	{
		this.arkFunction = arkFunction;
	}

	/**
	 * @param arkRolePolicyTemplate
	 *           the arkRolePolicyTemplate to set
	 */
	public void setArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate)
	{
		this.arkRolePolicyTemplate = arkRolePolicyTemplate;
	}

	/**
	 * @return the arkRolePolicyTemplate
	 */
	public ArkRolePolicyTemplate getArkRolePolicyTemplate()
	{
		return arkRolePolicyTemplate;
	}

	/**
	 * @param study
	 *           the study to set
	 */
	public void setStudy(Study study)
	{
		this.study = study;
	}

	/**
	 * @return the study
	 */
	public Study getStudy()
	{
		return study;
	}

	/**
	 * @param studyList
	 *           the studyList to set
	 */
	public void setStudyList(List<Study> studyList)
	{
		this.studyList = studyList;
	}

	/**
	 * @return the studyList
	 */
	public List<Study> getStudyList()
	{
		return studyList;
	}

	/**
	 * @param arkRolePolicyTemplateList
	 *           the arkRolePolicyTemplateList to set
	 */
	public void setArkRolePolicyTemplateList(List<ArkRolePolicyTemplate> arkRolePolicyTemplateList)
	{
		this.arkRolePolicyTemplateList = arkRolePolicyTemplateList;
	}

	/**
	 * @return the arkRolePolicyTemplateList
	 */
	public List<ArkRolePolicyTemplate> getArkRolePolicyTemplateList()
	{
		return arkRolePolicyTemplateList;
	}

	/**
	 * @param arkModuleList
	 *           the arkModuleList to set
	 */
	public void setArkModuleList(List<ArkModule> arkModuleList)
	{
		this.arkModuleList = arkModuleList;
	}

	/**
	 * @return the arkModuleList
	 */
	public List<ArkModule> getArkModuleList()
	{
		return arkModuleList;
	}

	/**
	 * @param arkFunctionList
	 *           the arkFunctionList to set
	 */
	public void setArkFunctionList(List<ArkFunction> arkFunctionList)
	{
		this.arkFunctionList = arkFunctionList;
	}

	/**
	 * @return the arkFunctionList
	 */
	public List<ArkFunction> getArkFunctionList()
	{
		return arkFunctionList;
	}

	/**
	 * @return the arkCreatePermission
	 */
	public Boolean getArkCreatePermission()
	{
		return arkCreatePermission;
	}

	/**
	 * @param arkCreatePermission
	 *           the arkCreatePermission to set
	 */
	public void setArkCreatePermission(Boolean arkCreatePermission)
	{
		this.arkCreatePermission = arkCreatePermission;
	}

	/**
	 * @return the arkReadPermission
	 */
	public Boolean getArkReadPermission()
	{
		return arkReadPermission;
	}

	/**
	 * @param arkReadPermission
	 *           the arkReadPermission to set
	 */
	public void setArkReadPermission(Boolean arkReadPermission)
	{
		this.arkReadPermission = arkReadPermission;
	}

	/**
	 * @return the arkUpdatePermission
	 */
	public Boolean getArkUpdatePermission()
	{
		return this.arkUpdatePermission;
	}

	/**
	 * @param arkUpdatePermission
	 *           the arkUpdatePermission to set
	 */
	public void setArkUpdatePermission(Boolean arkUpdatePermission)
	{
		this.arkUpdatePermission = arkUpdatePermission;
	}

	/**
	 * @return the arkDeletePermission
	 */
	public Boolean getArkDeletePermission()
	{
		return this.arkDeletePermission;
	}

	/**
	 * @param arkDeletePermission
	 *           the arkDeletePermission to set
	 */
	public void setArkDeletePermission(Boolean arkDeletePermission)
	{
		this.arkDeletePermission = arkDeletePermission;
	}
}