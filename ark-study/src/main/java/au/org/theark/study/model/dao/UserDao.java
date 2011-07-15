package au.org.theark.study.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.PersonNotFoundException;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.EtaUser;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.StudyUpload;

/**
 * 
 * @author nivedann
 * 
 */
@Repository("userDao")
public class UserDao extends HibernateSessionDao implements IUserDao {

	public void createUser(EtaUser user) {

		getSession().save(user);

	}

	public EtaUser getUser(Long userId) {
		return (EtaUser) getSession().get(EtaUser.class, userId);
	}

	public EtaUser getUser(String userName) {
		String query = "from EtaUser eu where eu.userName = :userName";
		Session session = getSession();
		EtaUser user = null;
		user = (EtaUser) session.createQuery(query).setString("userName", userName).uniqueResult();
		return user;
	}

	public Person createPerson(Person personEntity) {

		getSession().save(personEntity);
		return personEntity;
	}

	public List<Person> searchPerson(Person personVO) throws PersonNotFoundException {
		// Return all persons
		String queryString = "from Person";
		Query query = getSession().createQuery(queryString);
		List<Person> personList = query.list();
		return personList;
	}

	/**
	 * Checks if the Ark User is present in the system. IT does no compare with a particular Study. If a person existed in the system this will return
	 * true. This must be used only during create operation. If you want to add a ArkUser to another study then another method with study must be
	 * passed in.
	 */
	public boolean isArkUserPresent(String userName) {
		boolean isPresent = false;
		Criteria criteria = getSession().createCriteria(ArkUser.class);
		if (userName != null) {
			criteria.add(Restrictions.eq("ldapUserName", userName));
		}

		criteria.setProjection(Projections.rowCount());
		Integer count = (Integer) criteria.list().get(0);
		if (count > 0) {
			isPresent = true;
		}

		return isPresent;
	}

}
