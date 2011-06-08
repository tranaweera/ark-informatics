package au.org.theark.report.model.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.subject.Subject;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.model.report.entity.ReportOutputFormat;
import au.org.theark.core.model.report.entity.ReportTemplate;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.report.model.vo.ConsentDetailsReportVO;
import au.org.theark.report.model.vo.FieldDetailsReportVO;
import au.org.theark.report.service.Constants;
import au.org.theark.report.web.component.viewReport.consentDetails.ConsentDetailsDataRow;
import au.org.theark.report.web.component.viewReport.phenoFieldDetails.FieldDetailsDataRow;

@Repository("reportDao")
public class ReportDao extends HibernateSessionDao implements IReportDao {

	private static Logger log = LoggerFactory.getLogger(ReportDao.class);
	private Subject	currentUser;
	private Date		dateNow;


	public Integer getTotalSubjectCount(Study study) {
		Integer totalCount = 0;
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.setProjection(Projections.rowCount());
		totalCount = (Integer) criteria.uniqueResult();

		return totalCount;
	}

	public Map<String, Integer> getSubjectStatusCounts(Study study) {
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("study", study));
		ProjectionList projectionList = Projections.projectionList();
		criteria.createAlias("subjectStatus", "subjectStatusAlias");
		projectionList.add(Projections.groupProperty("subjectStatusAlias.name"));
		projectionList.add(Projections.rowCount());
		criteria.setProjection(projectionList);
		List results = criteria.list();
		Map<String, Integer> statusMap = new HashMap<String, Integer>();
		for(Object r: results) {
			Object[] obj = (Object[]) r;
			String statusName = (String)obj[0];
			statusMap.put(statusName, (Integer)obj[1]);
		}
		return statusMap;
	}

	public Map<String, Integer> getStudyConsentCounts(Study study) {
		Map<String, Integer> statusMap = new HashMap<String, Integer>();

		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("study", study));
		ProjectionList projectionList = Projections.projectionList();
		criteria.createAlias("consentStatus", "consentStatusAlias");
		projectionList.add(Projections.groupProperty("consentStatusAlias.name"));
		projectionList.add(Projections.rowCount());
		criteria.setProjection(projectionList);
		List results = criteria.list();
		for(Object r: results) {
			Object[] obj = (Object[]) r;
			String statusName = (String)obj[0];
			statusMap.put(statusName, (Integer)obj[1]);
		}
		
		// Tack on count of when consentStatus = undefined (NULL) 
		criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.isNull("consentStatus"));
		projectionList = Projections.projectionList();
		projectionList.add(Projections.rowCount());
		criteria.setProjection(projectionList);
		Integer undefCount = (Integer) criteria.uniqueResult();
		String statusName = Constants.NOT_CONSENTED;
		statusMap.put(statusName, undefCount);

		return statusMap;
	}

	public Map<String, Integer> getStudyCompConsentCounts(Study study,
			StudyComp studyComp) {
		Map<String, Integer> statusMap = new HashMap<String, Integer>();

		Criteria criteria = getSession().createCriteria(Consent.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("studyComp", studyComp));
		ProjectionList projectionList = Projections.projectionList();
		criteria.createAlias("consentStatus", "consentStatusAlias");
		projectionList.add(Projections.groupProperty("consentStatusAlias.name"));
		projectionList.add(Projections.rowCount());
		criteria.setProjection(projectionList);
		List results = criteria.list();
		if ((results != null) && (results.size() > 0)) {
			for(Object r: results) {
				Object[] obj = (Object[]) r;
				String statusName = (String)obj[0];
				statusMap.put(statusName, (Integer)obj[1]);
			}
		}
		else {
			statusMap.put("(none found)", new Integer(0));
		}
		return statusMap;
	}
	
	public Long getWithoutStudyCompCount(Study study) {

		/* The following HQL implements this MySQL query:
		 SELECT COUNT(*) FROM study.link_subject_study AS lss
			LEFT JOIN study.consent AS c
			ON lss.id = c.subject_id		-- this line is implicit from annotations on the entity classes
			WHERE lss.study_id = 2
			AND c.id IS NULL;
		*/
		String hqlString = "SELECT COUNT(*) FROM LinkSubjectStudy AS lss \n"
							+ "LEFT JOIN lss.consents AS c \n"
							+ "WHERE lss.study = :study \n"
							+ "AND c.id IS NULL";
		Query q = getSession().createQuery(hqlString);
//		if (hqlString.contains(":study_id")) {
//			q.setParameter("study_id", study.getId());
//		}
//		if (hqlString.contains(":study")) {
			q.setParameter("study", study);
//		}
		Long undefCount = (Long) q.uniqueResult();

		return undefCount;
	}
	
	public List<ReportTemplate> getReportsForUser(ArkUser arkUser) {
		Criteria criteria = getSession().createCriteria(ReportTemplate.class);
//		TODO : Add security here
//		criteria.add(Restrictions.eq("arkUser", arkUser));
		List<ReportTemplate> reportsAvailListing = criteria.list();

		return reportsAvailListing;
	}

	public List<ReportOutputFormat> getOutputFormats() {
		Criteria criteria = getSession().createCriteria(ReportOutputFormat.class);
		List<ReportOutputFormat> outputFormats = criteria.list();

		return outputFormats;
	}

	public List<LinkSubjectStudy> getStudyLevelConsentDetailsList(
			ConsentDetailsReportVO cdrVO) {
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);

		// Add study in context to criteria first (linkSubjectStudy on the VO should never be null)
		criteria.add(Restrictions.eq(Constants.LINKSUBJECTSTUDY_STUDY, cdrVO.getLinkSubjectStudy().getStudy()));
		if (cdrVO.getLinkSubjectStudy().getSubjectUID() != null) {
			criteria.add(Restrictions.ilike(Constants.LINKSUBJECTSTUDY_SUBJECTUID, cdrVO.getLinkSubjectStudy().getSubjectUID(), MatchMode.ANYWHERE));
		}
		if (cdrVO.getLinkSubjectStudy().getSubjectStatus() != null) {
			criteria.add(Restrictions.eq(Constants.LINKSUBJECTSTUDY_SUBJECTSTATUS, cdrVO.getLinkSubjectStudy().getSubjectStatus()));
		}
	
		// we are dealing with study-level consent
		if (cdrVO.getConsentStatus() != null) {
			if (cdrVO.getConsentStatus().getName().equals(Constants.NOT_CONSENTED)) {
				// Special-case: Treat the null FK for consentStatus as "Not Consented"
				criteria.add(Restrictions.or(
								Restrictions.eq(Constants.LINKSUBJECTSTUDY_CONSENTSTATUS, cdrVO.getConsentStatus()),
								Restrictions.isNull(Constants.LINKSUBJECTSTUDY_CONSENTSTATUS))
							);
			}
			else {
				criteria.add(Restrictions.eq(Constants.LINKSUBJECTSTUDY_CONSENTSTATUS, cdrVO.getConsentStatus()));
			}
		}
		if (cdrVO.getConsentDate() != null) {
			criteria.add(Restrictions.eq(Constants.LINKSUBJECTSTUDY_CONSENTDATE, cdrVO.getConsentDate()));
		}
		criteria.addOrder(Order.asc("consentStatus"));	//although MySQL causes NULLs to come first 
		criteria.addOrder(Order.asc("subjectUID"));

		return (List<LinkSubjectStudy>)criteria.list();
	}
	
	public List<ConsentDetailsDataRow> getStudyCompConsentList(
											ConsentDetailsReportVO cdrVO) {
		//NB: There should only ever be one Consent record for a particular Subject for a particular StudyComp
		
		List<ConsentDetailsDataRow> results = new ArrayList<ConsentDetailsDataRow>();
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class, "lss");
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("lss." + "subjectUID"), "subjectUID");

		criteria.add(Restrictions.eq("lss." + Constants.LINKSUBJECTSTUDY_STUDY, cdrVO.getLinkSubjectStudy().getStudy()));
		if (cdrVO.getLinkSubjectStudy().getSubjectUID() != null) {
			criteria.add(Restrictions.ilike("lss." + Constants.LINKSUBJECTSTUDY_SUBJECTUID, cdrVO.getLinkSubjectStudy().getSubjectUID(), MatchMode.ANYWHERE));
		}
		if (cdrVO.getLinkSubjectStudy().getSubjectStatus() != null) {
			criteria.add(Restrictions.eq("lss." + Constants.LINKSUBJECTSTUDY_SUBJECTSTATUS, cdrVO.getLinkSubjectStudy().getSubjectStatus()));
		}

		if (cdrVO.getConsentDate() != null) {
			//NB: constraint on consentDate or consentStatus automatically removes "Not Consented" state
			// So LinkSubjectStudy inner join to Consent ok for populated consentDate
			criteria.createAlias("lss." + Constants.LINKSUBJECTSTUDY_CONSENT, "c");
			criteria.createAlias("c." + Constants.CONSENT_CONSENTSTATUS, "cs");
			// constrain on studyComp
			criteria.add(Restrictions.eq("c." + Constants.CONSENT_STUDYCOMP, cdrVO.getStudyComp()));
			// constrain on consentDate 
			criteria.add(Restrictions.eq("c." + Constants.CONSENT_CONSENTDATE, cdrVO.getConsentDate()));
			// ConsentStatus is optional for this query...
			if (cdrVO.getConsentStatus() != null) {
				criteria.add(Restrictions.eq("c." + Constants.CONSENT_CONSENTSTATUS, cdrVO.getConsentStatus()));
			}
			projectionList.add(Projections.property("cs.name"), "consentStatus");
			projectionList.add(Projections.property("c." + Constants.CONSENT_CONSENTDATE), "consentDate");

		}
		else if (cdrVO.getConsentStatus() != null) {
			if (cdrVO.getConsentStatus().getName().equals(Constants.NOT_CONSENTED)) {
				// Need to handle "Not Consented" status differently (since it doesn't have a Consent record)
				// Helpful website: http://www.cereslogic.com/pages/2008/09/22/hibernate-criteria-subqueries-exists/
				
				// Build subquery to find all Consent records for a Study Comp
				DetachedCriteria consentCriteria = DetachedCriteria.forClass(Consent.class, "c");
				// Constrain on StudyComponent
				consentCriteria.add(Restrictions.eq("c." + Constants.CONSENT_STUDYCOMP, cdrVO.getStudyComp()));
				// Just in case "Not Consented" is erroneously entered into a row in the Consent table
	//			consentCriteria.add(Restrictions.ne("c." + Constants.CONSENT_CONSENTSTATUS, cdrVO.getConsentStatus()));
				// Join LinkSubjectStudy and Consent on ID FK
				consentCriteria.add(Property.forName("c.linkSubjectStudy.id").eqProperty("lss." + "id"));
				criteria.add(Subqueries.notExists(consentCriteria.setProjection(Projections.property("c.id"))));			
				
				// If Consent records follows design for "Not Consented", then:
				// - consentStatus and consentDate are not populated
			}
			else {
				//NB: constraint on consentDate or consentStatus automatically removes "Not Consented" state
				// So LinkSubjectStudy inner join to Consent ok for all recordable consentStatus
				criteria.createAlias("lss." + Constants.LINKSUBJECTSTUDY_CONSENT, "c");
				criteria.createAlias("c." + Constants.CONSENT_CONSENTSTATUS, "cs");
				// Constrain on StudyComponent
				criteria.add(Restrictions.eq("c." + Constants.CONSENT_STUDYCOMP, cdrVO.getStudyComp()));
				// ConsentStatus is NOT optional for this query!
				criteria.add(Restrictions.eq("c." + Constants.CONSENT_CONSENTSTATUS, cdrVO.getConsentStatus()));
				if (cdrVO.getConsentDate() != null) {
					criteria.add(Restrictions.eq("c." + Constants.CONSENT_CONSENTDATE, cdrVO.getConsentDate()));
				}
				projectionList.add(Projections.property("cs.name"), "consentStatus");
				projectionList.add(Projections.property("c." + Constants.CONSENT_CONSENTDATE), "consentDate");
			}
		}
		else {
			// Should not attempt to run this query with no consentDate nor consentStatus criteria provided
			log.error("reportDao.getStudyCompConsentList(..) is missing consentDate or consentStatus parameters in the VO");
			return null;
		}
			
		criteria.addOrder(Order.asc("lss." + "subjectUID"));
		criteria.setProjection(projectionList);
	    criteria.setResultTransformer(Transformers.aliasToBean(ConsentDetailsDataRow.class));
	    // This gives a list of subjects matching the specific studyComp and consentStatus
	    results = criteria.list();

		return results;
	}
	
	public Address getBestAddress(LinkSubjectStudy subject) {
		Address result = null;
		// Attempt to get the preferred address first
		Criteria criteria = getSession().createCriteria(Address.class);
		criteria.add(Restrictions.eq("person", subject.getPerson()));
		criteria.add(Restrictions.eq("preferredMailingAddress", true));
		criteria.setMaxResults(1);
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("streetAddress"), "streetAddress");
		projectionList.add(Projections.property("city"), "city");
		projectionList.add(Projections.property("country"), "country");
		projectionList.add(Projections.property("countryState"), "countryState");
		projectionList.add(Projections.property("otherState"), "otherState");
		projectionList.add(Projections.property("postCode"), "postCode");
		criteria.setProjection(projectionList);	// only return fields required for report
	    criteria.setResultTransformer(Transformers.aliasToBean(Address.class));
	    
	    if (criteria.uniqueResult() != null) {
			result = (Address) criteria.uniqueResult();
		}
		else {
			// Get any address
			criteria = getSession().createCriteria(Address.class);
			criteria.add(Restrictions.eq("person", subject.getPerson()));
			criteria.setMaxResults(1);
			criteria.setProjection(projectionList);	// only return fields required for report
		    criteria.setResultTransformer(Transformers.aliasToBean(Address.class));
		    
		    result = (Address) criteria.uniqueResult();
		}
		return result;
	}
	
	public Phone getWorkPhone(LinkSubjectStudy subject) {
		Phone result = null;
		Criteria criteria = getSession().createCriteria(Phone.class);
		criteria.add(Restrictions.eq("person", subject.getPerson()));
		criteria.createAlias("phoneType", "pt");
		criteria.add(Restrictions.eq("pt.name", "Work"));
		criteria.setMaxResults(1);

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("areaCode"), "areaCode");
		projectionList.add(Projections.property("phoneNumber"), "phoneNumber");
		criteria.setProjection(projectionList);	// only return fields required for report
	    criteria.setResultTransformer(Transformers.aliasToBean(Phone.class));
		
		if (criteria.uniqueResult() != null) {
			result = (Phone) criteria.uniqueResult();
		}
		return result;
	}
	
	public Phone getHomePhone(LinkSubjectStudy subject) {
		Phone result = null;
		Criteria criteria = getSession().createCriteria(Phone.class);
		criteria.add(Restrictions.eq("person", subject.getPerson()));
		criteria.createAlias("phoneType", "pt");
		criteria.add(Restrictions.eq("pt.name", "Home"));
		criteria.setMaxResults(1);
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("areaCode"), "areaCode");
		projectionList.add(Projections.property("phoneNumber"), "phoneNumber");
		criteria.setProjection(projectionList);	// only return fields required for report
	    criteria.setResultTransformer(Transformers.aliasToBean(Phone.class));
		
		if (criteria.uniqueResult() != null) {
			result = (Phone) criteria.uniqueResult();
		}
		return result;
	}

	public List<LinkSubjectStudy> getSubjects(ConsentDetailsReportVO cdrVO) {
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq(Constants.LINKSUBJECTSTUDY_STUDY, cdrVO.getLinkSubjectStudy().getStudy()));
		if (cdrVO.getLinkSubjectStudy().getSubjectUID() != null) {
			criteria.add(Restrictions.ilike(Constants.LINKSUBJECTSTUDY_SUBJECTUID, cdrVO.getLinkSubjectStudy().getSubjectUID(), MatchMode.ANYWHERE));
		}
		if (cdrVO.getLinkSubjectStudy().getSubjectStatus() != null) {
			criteria.add(Restrictions.eq(Constants.LINKSUBJECTSTUDY_SUBJECTSTATUS, cdrVO.getLinkSubjectStudy().getSubjectStatus()));
		}
		criteria.addOrder(Order.asc("subjectUID"));
		List<LinkSubjectStudy> results = criteria.list();
		return results;
	}
	
	public Consent getStudyCompConsent(Consent consent)
	{
		// Note: Should never be possible to have more than one Consent record for a
		// given a particular subject and study component
		Criteria criteria = getSession().createCriteria(Consent.class);
		if (consent != null)
		{
			criteria.add(Restrictions.eq("study.id", consent.getStudy().getId()));
			//must only get consents for subject in context
			criteria.add(Restrictions.eq("linkSubjectStudy.id", consent.getLinkSubjectStudy().getId()));
			//must only get consents for specific studyComp
			criteria.add(Restrictions.eq("studyComp.id", consent.getStudyComp().getId()));
			// Do NOT constrain against consentStatus or consentDate here, because we want to be able to
			// tell if they are "Not Consented" vs "Consented" with different consentStatus or consentDate.
//			if (consent.getConsentStatus() != null)
//			{
//				criteria.add(Restrictions.eq("consentStatus.id", consent.getConsentStatus().getId()));
//			}
//
//			if (consent.getConsentDate() != null)
//			{
//				criteria.add(Restrictions.eq("consentDate", consent.getConsentDate()));
//			}

		}
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("studyComp"), "studyComp");
		projectionList.add(Projections.property("consentStatus"), "consentStatus");
		projectionList.add(Projections.property("consentDate"), "consentDate");
		criteria.setProjection(projectionList);
		criteria.setMaxResults(1);
	    criteria.setResultTransformer(Transformers.aliasToBean(Consent.class));
	    Consent result = (Consent) criteria.uniqueResult();
		return result;
	}

	public List<FieldDetailsDataRow> getPhenoFieldDetailsList(
			FieldDetailsReportVO fdrVO) {
		// TODO Auto-generated method stub
		return null;
	}
}
