package org.wager.dataimport.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import neuragenix.bio.utilities.BiospecimenUtilities;
import neuragenix.dao.DALSecurityQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.wager.biogenix.types.BioTransactions;
import org.wager.biogenix.types.Biospecimen;
import org.wager.biogenix.types.Cell;

public class BiospecimenDAO {
	  
	   SessionFactory sf;
	public BiospecimenDAO() {
		  sf = new Configuration().configure().buildSessionFactory();
	}

	@SuppressWarnings("unchecked")
	public List<Biospecimen> getUpdateList(String ids[]) {
		
		Session hib_session = sf.openSession();
		  List<Biospecimen> biospecsForUpdate = new ArrayList<Biospecimen>();
		String inclauses[] = getListInSQL(ids);
		for (int i=0; i< inclauses.length; i++)
		biospecsForUpdate.addAll(hib_session.createQuery(
	    "from Biospecimen bio where bio.deleted = 0 and bio.biospecimenid in "+ inclauses[i]).list());
		hib_session.close();
		return  biospecsForUpdate;

	}
	
	
	private String[] getListInSQL(String ids[]) {
		StringBuffer sb;
		if (ids == null) {
			return null;
		}
		else {
			int ll;
			int ul;
			int il = ids.length;
			int n = il/500;
			if (il % 500 != 0) 
				n++;
				
			String result[] = new String[n];
			for (int i = 0; i < n; i++) {
				ll = i*500;
				sb = new StringBuffer();
				sb.append(" (");
				if (i == n-1) ul=il; else ul= (i+1)*500;
				for (int j = ll; j < ul; j++){
					sb.append("'"+ids[j]+"'");
					if (j < ul-1) {
						sb.append(",");
					}
				}
				
				sb.append(")");
				result[i] = sb.toString();
			}
			return result;
		}
			
	}
	
	public long saveNewBiospecimen(Biospecimen b) {
		Session hib_session = sf.openSession();
		Transaction tx = hib_session.beginTransaction();
		long biospecId = ((Long) hib_session.save(b)).longValue();
		tx.commit();
		hib_session.close();
		return biospecId;
		
	}
	public long saveTransaction(BioTransactions b) {
		Session hib_session = sf.openSession();
		Transaction tx = hib_session.beginTransaction();
		long biospecId = ((Long) hib_session.save(b)).longValue();
		tx.commit();
		hib_session.close();
		return biospecId;
		
	}
	
	
	public void updateBiospecimen(Biospecimen b) {
		Transaction tx = null;
		try {
			Session hib_session = sf.openSession();
			tx = hib_session.beginTransaction();
			hib_session.update(b);
			tx.commit();
			hib_session.close();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e; // or display error message
		}

	}
	
	

	
}
