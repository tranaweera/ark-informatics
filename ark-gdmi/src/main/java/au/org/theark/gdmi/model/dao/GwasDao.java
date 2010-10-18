package au.org.theark.gdmi.model.dao;

//import java.util.List;
import java.util.*;
import java.io.*;
import java.sql.Blob;
import java.text.SimpleDateFormat;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.lob.BlobImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.gdmi.model.entity.MetaData;
import au.org.theark.gdmi.model.entity.MetaDataField;
import au.org.theark.gdmi.model.entity.MetaDataType;
//import au.org.theark.gdmi.model.entity.Person;
import au.org.theark.gdmi.model.entity.Collection;
import au.org.theark.gdmi.model.entity.DecodeMask;
import au.org.theark.gdmi.model.entity.EncodedData;
import au.org.theark.gdmi.model.entity.Marker;
import au.org.theark.gdmi.model.entity.MarkerGroup;
import au.org.theark.gdmi.model.entity.MarkerType;
import au.org.theark.gdmi.model.entity.Status;
//TODO: Use logger (see Study)

@Repository("gwasDao")
public class GwasDao extends HibernateSessionDao implements IGwasDao
{
	static Logger log = LoggerFactory.getLogger(CollectionDao.class);
	
	//Testing the create of records
	//Each FK reference for MetaData must exist (i.e. be saved) prior to use
/*
    public void createMetaData(MetaData metaData)
    {
        System.out.println("Create method invoked");
        // getSession().save(metaData);
        MetaDataType mdt = getMetaDataType("Number");
        System.out.println("Tried to get MetaDataType(\"Number\"): " + mdt);
        Date dateNow = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String nowStr = sdf.format(dateNow);

        Session s = getSession();
        
        MetaDataField mdf = new MetaDataField();
        mdf.setName("Mass");
        mdf.setDescription("kg");
        System.out.println("Now: " + nowStr);
        mdf.setStudyId(new Long(1));
        mdf.setUserId("elam");
        mdf.setInsertTime(dateNow);
        mdf.setMetaDataType(mdt);
        s.save(mdf);
        metaData.setUserId("elam");
        metaData.setInsertTime(dateNow);
        //TODO FIX STATUS AND COLLECTION
        Status stat = getStatusByName("Active");
        Collection colEn = new Collection();
        colEn.setStudyId(100);
        colEn.setStatus(stat);
        colEn.setUserId("elam");
        colEn.setInsertTime(dateNow);
        s.save(colEn);
        metaData.setCollection(colEn);
        metaData.setMetaDataField(mdf);
        System.out.println("metaData.setMetaDataField: "+ mdf);
        s.save(metaData);
        System.out.println("Tried to create a MetaDataField(\"Number\", \"Mass\", \"kg\"): " + mdf);

//        Person p = new Person();
//        p.setFirstName("A");
//        p.setLastName("Name");
//        p.setMiddleName("M");
//        getSession().save(p);
//        s.delete(metaData);
//        System.out.println("Tried to delete the newly created MetaDataField(\"Number\", \"Mass\", \"kg\"): " + mdf);
    }

    public void createMetaData(MetaDataField mdf, String value)
    {
        System.out.println("Invoked createMetaData...");
        MetaData metaData = new MetaData();
        metaData.setMetaDataField(mdf);
        metaData.setValue(value);
        getSession().save(metaData);
        System.out.println("Created new MetaData");
    }

    public void createMetaDataField(String dataType, String name,
            String description)
    {
        System.out.println("Invoked createMetaDataField...");
        MetaDataField mdf = new MetaDataField();
        MetaDataType mdt = getMetaDataType(dataType);
        mdf.setMetaDataType(mdt);
        mdf.setName(name);
        mdf.setDescription(description);
        getSession().save(mdf);
        System.out.println("Created new MetaDataField");
    }

   
    public Collection getCollection(String name)
    {
        String hql = "from Collection where name = :name";
        Query q = getSession().createQuery(hql);
        q.setString("name", name);
        q.setMaxResults(1);
        @SuppressWarnings("unchecked")
        List<Collection> results = q.list();
        if (results.size() > 0)
        {
            return (results.get(0));
        }
        else
            return null;
    }
*/
    
    public void createEncodedData(EncodedData ed) {
    	Session session = getSession();
    	session.save(ed);
    	session.flush();
    	session.refresh(ed);
    	//TODO: flush() appears to guarantee upon return that the Blob will 
    }
    
    public EncodedData getEncodedData(Long encodedDataId) {
    	EncodedData ed = (EncodedData)getSession().get(EncodedData.class, encodedDataId);
    	return ed;
    }
    
    public MarkerType getMarkerType(String typeName) {
        Criteria crit = getSession().createCriteria(MarkerType.class);
    	crit.add(Restrictions.eq("name", typeName));
        crit.addOrder(Order.asc("id"));
        //@SuppressWarnings("unchecked")
        List<MarkerType> mdtList = crit.list();
		if (mdtList.size() > 0) {
			if (mdtList.size() > 1) {
				log.error("Backend database has non-unique MarkerType names, returned the first one");
			}
			return (mdtList.get(0));
		}
        else
        	return null;
    }
}
