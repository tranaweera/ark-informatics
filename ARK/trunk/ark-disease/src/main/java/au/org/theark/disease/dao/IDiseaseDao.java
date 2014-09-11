package au.org.theark.disease.dao;

import java.util.List;

import au.org.theark.core.model.disease.entity.Affection;
import au.org.theark.core.model.disease.entity.AffectionCustomFieldData;
import au.org.theark.core.model.disease.entity.AffectionStatus;
import au.org.theark.core.model.disease.entity.Disease;
import au.org.theark.core.model.disease.entity.Gene;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.disease.vo.AffectionListVO;
import au.org.theark.disease.vo.AffectionVO;
import au.org.theark.disease.vo.DiseaseVO;
import au.org.theark.disease.vo.GeneVO;

public interface IDiseaseDao {
	
	public int getDiseaseCount(DiseaseVO diseaseVO);
	
	public List<DiseaseVO> searchPageableDiseases(DiseaseVO object, int first, int count);
	
	public int getGeneCount(GeneVO geneVO);

	public List<GeneVO> searchPageableGenes(GeneVO object, int first, int count);
	
	public void save(Object object);
	
	public void update(Object object);
	
	public void delete(Object object);
	
	public List<Gene> getAvailableGenesForStudy(Study study);
	
	public List<Disease> getAvailableDiseasesForStudy(Study study);

	public int getAffectionCount(AffectionVO affectionVO);

	public List<AffectionVO> searchPageableAffections(AffectionVO affectionVO, int first, int count);
	
	public List<AffectionStatus> getAffectionStatus();

	public List<Affection> getPersonsAffections(Affection affection);
	
	public List<AffectionListVO> searchPageableAffectionListVOs(AffectionListVO affectionListVO, int first, int count);

	public int getAffectionCount(LinkSubjectStudy linkSubjectStudy);

	public List<AffectionCustomFieldData> getAffectionCustomFieldData(Affection affection);
	
}
