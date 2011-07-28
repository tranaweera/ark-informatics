package au.org.theark.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.admin.model.dao.IAdminDao;
import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.model.vo.ArkRoleModuleFunctionVO;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkFunctionType;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkPermission;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;

/**
 * The implementation of IAdminService. We want to auto-wire and hence use the @Service annotation.
 * 
 * @author cellis
 * @param <T>
 * 
 */
@Transactional
@Service(au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
public class AdminServiceImpl<T> implements IAdminService<T> {
	private IAdminDao	adminDao;

	public IAdminDao getAdminDao() {
		return adminDao;
	}

	@Autowired
	public void setAdminDao(IAdminDao adminDao) {
		this.adminDao = adminDao;
	}

	public void createArkRolePolicyTemplate(AdminVO adminVo) {
		adminDao.createArkRolePolicyTemplate(adminVo.getArkRolePolicyTemplate());
	}

	public void updateArkRolePolicyTemplate(AdminVO adminVo) {
		adminDao.updateArkRolePolicyTemplate(adminVo.getArkRolePolicyTemplate());
	}

	public void deleteArkRolePolicyTemplate(AdminVO adminVo) {
		adminDao.deleteArkRolePolicyTemplate(adminVo.getArkRolePolicyTemplate());
	}

	public void createOrUpdateArkRolePolicyTemplate(AdminVO adminVo) {
		adminDao.createOrUpdateArkRolePolicyTemplate(adminVo.getArkRolePolicyTemplate());
	}

	public List<ArkFunction> getArkFunctionList() {
		return adminDao.getArkFunctionList();
	}

	public List<ArkModule> getArkModuleList() {
		return adminDao.getArkModuleList();
	}

	public ArkPermission getArkPermissionByName(String name) {
		return adminDao.getArkPermissionByName(name);
	}

	public List<ArkRole> getArkRoleList() {
		return adminDao.getArkRoleList();
	}

	public ArkRolePolicyTemplate getArkRolePolicyTemplate(Long id) {
		return adminDao.getArkRolePolicyTemplate(id);
	}

	public List<ArkRolePolicyTemplate> getArkRolePolicyTemplateList() {
		return adminDao.getArkRolePolicyTemplateList();
	}

	public ArkFunction getArkFunction(Long id) {
		return adminDao.getArkFunction(id);
	}

	public ArkModule getArkModule(Long id) {
		return adminDao.getArkModule(id);
	}

	public void creatOrUpdateArkFunction(AdminVO adminVo) {
		adminDao.creatOrUpdateArkFunction(adminVo.getArkFunction());
	}

	public void creatOrUpdateArkModule(AdminVO adminVo) {
		adminDao.creatOrUpdateArkModule(adminVo.getArkModule());
	}

	public void deleteArkFunction(AdminVO adminVo) {
		adminDao.deleteArkFunction(adminVo.getArkFunction());
	}

	public void deleteArkModule(AdminVO adminVo) {
		adminDao.deleteArkModule(adminVo.getArkModule());
	}

	public List<ArkFunctionType> getArkFunctionTypeList() {
		return adminDao.getArkFunctionTypeList();
	}

	public List<ArkFunction> searchArkFunction(ArkFunction arkFunction) {
		return adminDao.searchArkFunction(arkFunction);
	}

	public List<ArkModule> searchArkModule(ArkModule arkModule) {
		return adminDao.searchArkModule(arkModule);
	}

	public int getArkFunctionCount(ArkFunction arkFunctionCriteria) {
		return adminDao.getArkFunctionCount(arkFunctionCriteria);
	}

	public int getArkModuleCount(ArkModule arkModuleCriteria) {
		return adminDao.getArkModuleCount(arkModuleCriteria);
	}

	public List<ArkFunction> searchPageableArkFunctions(ArkFunction arkFunctionCriteria, int first, int count) {
		return adminDao.searchPageableArkFunctions(arkFunctionCriteria, first, count);
	}

	public List<ArkModule> searchPageableArkModules(ArkModule arkModuleCriteria, int first, int count) {
		return adminDao.searchPageableArkModules(arkModuleCriteria, first, count);
	}

	public int getArkRoleModuleFunctionVOCount(ArkRoleModuleFunctionVO arkRoleModuleFunctionVO) {
		return adminDao.getArkRoleModuleFunctionVOCount(arkRoleModuleFunctionVO);
	}

	public List<ArkRoleModuleFunctionVO> searchPageableArkRoleModuleFunctionVO(ArkRoleModuleFunctionVO arkRoleModuleFunctionVo, int first, int count) {
		return adminDao.searchPageableArkRoleModuleFunctionVO(arkRoleModuleFunctionVo, first, count);
	}

	public ArkRole getArkRoleByName(String name) {
		return adminDao.getArkRoleByName(name);
	}

	public List<ArkRoleModuleFunctionVO> getArkRoleModuleFunctionVoList(ArkRole arkRole) {
		return adminDao.getArkRoleModuleFunctionVoList(arkRole);
	}

	public List<ArkRolePolicyTemplate> getArkRolePolicyTemplateList(ArkRolePolicyTemplate arkRolePolicyTemplate) {
		return adminDao.getArkRolePolicyTemplateList(arkRolePolicyTemplate);
	}
}
