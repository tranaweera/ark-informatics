package org.wager.lims.biodata;

// Generated 20/10/2009 12:25:59 PM by Hibernate Tools 3.2.4.GA

import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Group.
 * 
 * @see org.wager.lims.biodata.Group
 * @author Hibernate Tools
 */

public class GroupDAO {

	private static final Log log = LogFactory.getLog(GroupDAO.class);

	private EntityManager entityManager;

	public GroupDAO() {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("labPersistence");
		entityManager = emf.createEntityManager();
	}

	public void persist(Group transientInstance) {
		log.debug("persisting Group instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Group persistentInstance) {
		log.debug("removing Group instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Group merge(Group detachedInstance) {
		log.debug("merging Group instance");
		try {
			Group result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Group findById(BigDecimal id) {
		log.debug("getting Group instance with id: " + id);
		try {
			Group instance = entityManager.find(Group.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
