/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.lims.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.model.lims.entity.InvColRowType;
import au.org.theark.core.model.lims.entity.InvFreezer;
import au.org.theark.core.model.lims.entity.InvRack;
import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.core.model.lims.entity.StudyInvSite;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.lims.model.vo.BiospecimenLocationVO;
import au.org.theark.lims.model.vo.LimsVO;

@SuppressWarnings("unchecked")
@Repository("inventoryDao")
public class InventoryDao extends HibernateSessionDao implements IInventoryDao {
	private static final Logger	log	= LoggerFactory.getLogger(InventoryDao.class);

	public void createInvSite(InvSite invSite) {
		getSession().save(invSite);
	}

	public void createInvFreezer(InvFreezer invFreezer) {
		getSession().save(invFreezer);
	}

	public void createInvRack(InvRack invRack) {
		getSession().save(invRack);
	}

	public void createInvBox(InvBox invBox) {
		getSession().save(invBox);
	}

	public void deleteInvBox(InvBox invBox) {
		getSession().delete(invBox);
	}

	public void deleteInvSite(InvSite invSite) {
		getSession().delete(invSite);
	}

	public void deleteInvFreezer(InvFreezer invFreezer) {
		getSession().delete(invFreezer);
	}

	public void deleteInvRack(InvRack invRack) {
		getSession().delete(invRack);
	}

	public List<InvSite> searchInvSite(InvSite invSite) throws ArkSystemException {
		Criteria criteria = getSession().createCriteria(InvSite.class);

		if (invSite.getId() != null) {
			criteria.add(Restrictions.eq("id", invSite.getId()));
		}

		if (invSite.getName() != null) {
			criteria.add(Restrictions.eq("name", invSite.getName()));
		}

		if (invSite.getContact() != null) {
			criteria.add(Restrictions.eq("contact", invSite.getContact()));
		}

		if (invSite.getAddress() != null) {
			criteria.add(Restrictions.eq("address", invSite.getAddress()));
		}

		if (invSite.getPhone() != null) {
			criteria.add(Restrictions.eq("phone", invSite.getPhone()));
		}

		List<InvSite> list = criteria.list();
		return list;
	}

	public List<InvSite> searchInvSite(InvSite invSite, List<Study> studyList) throws ArkSystemException {
		List<InvSite> invSiteList = new ArrayList<InvSite>(0);
		
		if(studyList==null || studyList.isEmpty()){
			return invSiteList;
		}
		
		Criteria criteria = getSession().createCriteria(StudyInvSite.class);

		/*
		 * if (invSite.getId() != null) { criteria.add(Restrictions.eq("id", invSite.getId())); }
		 * 
		 * if (invSite.getName() != null) { criteria.add(Restrictions.eq("name", invSite.getName())); }
		 * 
		 * if (invSite.getContact() != null) { criteria.add(Restrictions.eq("contact", invSite.getContact())); }
		 * 
		 * if (invSite.getAddress() != null) { criteria.add(Restrictions.eq("address", invSite.getAddress())); }
		 * 
		 * if (invSite.getPhone() != null) { criteria.add(Restrictions.eq("phone", invSite.getPhone())); }
		 */
/*if you have an empty grouping, hibernate will do this sort of this;
 * select
        this_.INV_SITE_ID as y0_ 
    from
        lims.study_inv_site this_ 
    where
        this_.STUDY_ID in (
            
        ) 
    group by
        this_.INV_SITE_ID
        ...therefore always null check before checking if something is in a group of nothing
         */
		criteria.add(Restrictions.in("study", studyList));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("invSite"), "invSite");
		criteria.setProjection(projectionList);

		// List<StudyInvSite> list = criteria.list();
		invSiteList = criteria.list();
		/*
		 * for(StudyInvSite studyInvSite : list){ invSiteList.add(studyInvSite.getInvSite()); }
		 */
		return invSiteList;
	}

	public void updateInvSite(InvSite invSite) {
		getSession().merge(invSite);
		getSession().refresh(invSite);
	}

	public void updateInvFreezer(InvFreezer invFreezer) {
		getSession().merge(invFreezer);
	}

	public void updateInvRack(InvRack invRack) {
		getSession().merge(invRack);
	}

	public void updateInvBox(InvBox invBox) {
		getSession().merge(invBox);
	}

	public InvSite getInvSite(Long id) {
		InvSite invSite = new InvSite();
		Criteria criteria = getSession().createCriteria(InvSite.class);

		if (id != null) {
			criteria.add(Restrictions.eq("id", id));
		}

		List<InvSite> list = criteria.list();
		if (!list.isEmpty()) {
			invSite = (InvSite) list.get(0);
		}
		return invSite;
	}

	
	public InvCell getInvCell(InvBox invBox, long rowno, long colno) {
		InvCell invCell = null; //new InvCell();
		Criteria criteria = getSession().createCriteria(InvCell.class);

		if (invBox != null) {
			criteria.add(Restrictions.eq("invBox", invBox));
		}

		criteria.add(Restrictions.eq("rowno", rowno));
		criteria.add(Restrictions.eq("colno", colno));

		List<InvCell> list = criteria.list();
		if (!list.isEmpty()) {
			invCell = (InvCell) list.get(0);
		}
		return invCell;
	}

	public Biospecimen getBiospecimenByInvCell(InvCell invCell) {
		Biospecimen biospecimen = null;
		Criteria criteria = getSession().createCriteria(InvCell.class);
		criteria.add(Restrictions.eq("id", invCell.getId()));
		List<InvCell> list = criteria.list();
		if (!list.isEmpty()) {
			biospecimen = (Biospecimen) list.get(0).getBiospecimen();
		}
		return biospecimen;
	}

	public InvBox getInvBox(Long id) {
		InvBox invBox = new InvBox();
		Criteria criteria = getSession().createCriteria(InvBox.class);

		if (id != null) {
			criteria.add(Restrictions.eq("id", id));
		}

		List<InvBox> list = criteria.list();
		if (!list.isEmpty()) {
			invBox = (InvBox) list.get(0);
		}

		if (invBox == null) {
			log.error("InvBox with ID " + id + " is no longer in the database");
		}
		return invBox;
	}

	/** TODO ASAP TEST THIS */
	public List<InvCell> getCellAndBiospecimenListByBox(InvBox invBox) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select cell FROM  InvCell AS cell ");
		sb.append(" LEFT JOIN fetch cell.biospecimen ");
		sb.append(" WHERE cell.invBox.id = :invBoxId ");
		sb.append(" ORDER BY cell.rowno, cell.colno ");

		Query query = getSession().createQuery(sb.toString());
		query.setParameter("invBoxId", invBox.getId());

		List<InvCell> invCellList = query.list();
		return invCellList;
		
/*
		List<InvCell> invCellList = new ArrayList<InvCell>();

		StringBuffer sb = new StringBuffer();
		sb.append(" FROM  InvCell AS cell ");
		sb.append("LEFT JOIN cell.biospecimen as biospecimenList ");
		sb.append("  WHERE cell.invBox.id = :invBoxId");
		sb.append(" ORDER BY cell.rowno, cell.colno");

		Query query = getSession().createQuery(sb.toString());
		query.setParameter("invBoxId", invBox.getId());

		List<Object[]> listOfObjects = query.list();
		for (Object[] objects : listOfObjects) {
			InvCell invCell = new InvCell();
			Biospecimen biospecimen = new Biospecimen();

			if (objects.length > 0 && objects.length >= 1) {
				invCell = (InvCell) objects[0];
				if (objects[1] != null) {
					biospecimen = (Biospecimen) objects[1];
					invCell.setBiospecimen(biospecimen);
				}
				invCellList.add(invCell);
			}
		}

		return invCellList;*/
	}

	public List<InvColRowType> getInvColRowTypes() {
		Criteria criteria = getSession().createCriteria(InvColRowType.class);
		List<InvColRowType> list = criteria.list();
		return list;
	}

	public void createInvCell(InvCell invCell) {
		getSession().save(invCell);
	}

	public void updateInvCell(InvCell invCell) {
		getSession().update(invCell);
	}

	public void deleteInvCell(InvCell invCell) {
		getSession().delete(invCell);
	}

	public InvFreezer getInvFreezer(Long id) {
		InvFreezer invFreezer = new InvFreezer();
		Criteria criteria = getSession().createCriteria(InvFreezer.class);

		if (id != null) {
			criteria.add(Restrictions.eq("id", id));
		}

		List<InvFreezer> list = criteria.list();
		if (!list.isEmpty()) {
			invFreezer = (InvFreezer) list.get(0);
		}

		if (invFreezer == null) {
			log.error("InvFreezer with ID " + id + " is no longer in the database");
		}
		return invFreezer;
	}

	public InvRack getInvRack(Long id) {
		InvRack invRack = new InvRack();
		Criteria criteria = getSession().createCriteria(InvRack.class);

		if (id != null) {
			criteria.add(Restrictions.eq("id", id));
		}

		List<InvRack> list = criteria.list();
		if (!list.isEmpty()) {
			invRack = (InvRack) list.get(0);
		}

		if (invRack == null) {
			log.error("InvRack with ID " + id + " is no longer in the database");
		}
		return invRack;
	}

	public InvCell getInvCellByBiospecimen(Biospecimen biospecimen) {
		InvCell invCell = new InvCell();
		Criteria criteria = getSession().createCriteria(InvCell.class);

		if (biospecimen != null) {
			criteria.add(Restrictions.eq("biospecimen", biospecimen));
		}

		List<InvCell> list = criteria.list();
		if (!list.isEmpty()) {
			invCell = (InvCell) list.get(0);
		}

		if (invCell == null) {
			log.error("InvCell with biospecimen " + biospecimen.getId() + " is no longer in the database");
		}
		return invCell;
	}

	public InvCell getInvCell(Long id) {
		InvCell invCell = new InvCell();
		Criteria criteria = getSession().createCriteria(InvCell.class);

		if (id != null) {
			criteria.add(Restrictions.eq("id", id));
		}

		List<InvCell> list = criteria.list();
		if (!list.isEmpty()) {
			invCell = (InvCell) list.get(0);
		}

		if (invCell == null) {
			log.error("InvRack with ID " + id + " is no longer in the database");
		}
		return invCell;
	}

	public List<InvRack> searchInvRack(InvRack invRack, List<Study> studyListForUser) throws ArkSystemException {
		StringBuilder hqlString = new StringBuilder();
		hqlString.append("FROM InvRack AS rack \n");
		hqlString.append("WHERE invFreezer.id IN (SELECT id FROM InvFreezer AS freezer \n");
		hqlString.append("								WHERE freezer.invSite.id IN (SELECT invSite.id FROM StudyInvSite \n");
		hqlString.append("																		WHERE study IN (:studies)))");

		Query q = getSession().createQuery(hqlString.toString());
		q.setParameterList("studies", studyListForUser);

		List<InvRack> list = q.list();
		return list;
	}

	public List<InvFreezer> searchInvFreezer(InvFreezer invFreezer, List<Study> studyListForUser) throws ArkSystemException {
		StringBuilder hqlString = new StringBuilder();
		hqlString.append("FROM InvFreezer AS freezer \n");
		hqlString.append("WHERE freezer.invSite.id IN (SELECT invSite.id FROM StudyInvSite \n");
		hqlString.append("							WHERE study IN (:studies))");

		Query q = getSession().createQuery(hqlString.toString());
		q.setParameterList("studies", studyListForUser);

		List<InvFreezer> list = q.list();
		return list;
	}

	public List<InvBox> searchInvBox(InvBox invBox) throws ArkSystemException {
		Criteria criteria = getSession().createCriteria(InvBox.class);

		if (invBox.getId() != null) {
			criteria.add(Restrictions.eq("id", invBox.getId()));
		}

		if (invBox.getName() != null) {
			criteria.add(Restrictions.eq("name", invBox.getName()));
		}

		List<InvBox> list = criteria.list();
		return list;
	}

	public BiospecimenLocationVO getBiospecimenLocation(Biospecimen biospecimen) {
		BiospecimenLocationVO biospecimenLocationVo = new BiospecimenLocationVO();

		StringBuilder hqlString = new StringBuilder();
		hqlString
				.append("SELECT site.name AS siteName, freezer.name as freezerName, rack.name AS rackName, box.name AS boxName, cell.colno AS column, cell.rowno AS row, box.colnotype.name AS colNoType, box.rownotype.name AS rowNoType \n");
		hqlString.append("FROM InvCell AS cell \n");
		hqlString.append("LEFT JOIN cell.invBox AS box \n");
		hqlString.append("LEFT JOIN box.invRack AS rack \n");
		hqlString.append("LEFT JOIN rack.invFreezer AS freezer \n");
		hqlString.append("LEFT JOIN freezer.invSite AS site \n");
		hqlString.append("WHERE cell.biospecimen = :biospecimen");

		Query q = getSession().createQuery(hqlString.toString());
		q.setParameter("biospecimen", biospecimen);
		Object[] result = (Object[]) q.uniqueResult();

		if (result != null) {
			biospecimenLocationVo.setIsAllocated(true);
			biospecimenLocationVo.setSiteName(result[0].toString());
			biospecimenLocationVo.setFreezerName(result[1].toString());
			biospecimenLocationVo.setRackName(result[2].toString());
			biospecimenLocationVo.setBoxName(result[3].toString());

			Long colno = new Long((Long) result[4]);
			Long rowno = new Long((Long) result[5]);
			biospecimenLocationVo.setColumn(colno);
			biospecimenLocationVo.setRow(rowno);

			String colNoType = result[6].toString();
			String rowNoType = result[7].toString();

			String colLabel = new String();
			if (colNoType.equalsIgnoreCase("ALPHABET")) {
				char character = (char) (colno + 64);
				colLabel = new Character(character).toString();
			}
			else {
				colLabel = new Integer(colno.intValue()).toString();
			}
			biospecimenLocationVo.setColLabel(colLabel);

			String rowLabel = new String();
			if (rowNoType.equalsIgnoreCase("ALPHABET")) {
				char character = (char) (rowno + 64);
				rowLabel = new Character(character).toString();
			}
			else {
				rowLabel = new Integer(rowno.intValue()).toString();
			}
			biospecimenLocationVo.setRowLabel(rowLabel);
		}
		return biospecimenLocationVo;
	}

	public BiospecimenLocationVO getInvCellLocation(InvCell invCell) throws ArkSystemException {
		BiospecimenLocationVO biospecimenLocationVo = new BiospecimenLocationVO();

		StringBuilder hqlString = new StringBuilder();
		hqlString
				.append("SELECT site.name AS siteName, freezer.name as freezerName, rack.name AS rackName, box.name AS boxName, cell.colno AS column, cell.rowno AS row, box.colnotype.name AS colNoType, box.rownotype.name AS rowNoType \n");
		hqlString.append("FROM InvCell AS cell \n");
		hqlString.append("LEFT JOIN cell.invBox AS box \n");
		hqlString.append("LEFT JOIN box.invRack AS rack \n");
		hqlString.append("LEFT JOIN rack.invFreezer AS freezer \n");
		hqlString.append("LEFT JOIN freezer.invSite AS site \n");
		hqlString.append("WHERE cell.id = :id");

		Query q = getSession().createQuery(hqlString.toString());
		q.setParameter("id", invCell.getId());
		Object[] result = (Object[]) q.uniqueResult();

		if (result != null) {
			biospecimenLocationVo.setSiteName(result[0].toString());
			biospecimenLocationVo.setFreezerName(result[1].toString());
			biospecimenLocationVo.setRackName(result[2].toString());
			biospecimenLocationVo.setBoxName(result[3].toString());

			Long colno = new Long((Long) result[4]);
			Long rowno = new Long((Long) result[5]);
			biospecimenLocationVo.setColumn(colno);
			biospecimenLocationVo.setRow(rowno);

			String colNoType = result[6].toString();
			String rowNoType = result[7].toString();

			String colLabel = new String();
			if (colNoType.equalsIgnoreCase("ALPHABET")) {
				char character = (char) (colno + 64);
				colLabel = new Character(character).toString();
			}
			else {
				colLabel = new Integer(colno.intValue()).toString();
			}
			biospecimenLocationVo.setColLabel(colLabel);

			String rowLabel = new String();
			if (rowNoType.equalsIgnoreCase("ALPHABET")) {
				char character = (char) (rowno + 64);
				rowLabel = new Character(character).toString();
			}
			else {
				rowLabel = new Integer(rowno.intValue()).toString();
			}
			biospecimenLocationVo.setRowLabel(rowLabel);
		}
		return biospecimenLocationVo;
	}

	public boolean boxesExist() {
		Criteria criteria = getSession().createCriteria(InvBox.class);
		criteria.setProjection(Projections.count("id"));
		Long count = (Long) criteria.uniqueResult();
		return count > 0L;
	}

	public boolean hasAllocatedCells(InvBox invBox) {
		Criteria criteria = getSession().createCriteria(InvCell.class);
		criteria.add(Restrictions.eq("invBox", invBox));
		criteria.add(Restrictions.isNotNull("biospecimen"));
		criteria.setProjection(Projections.count("id"));
		Long count = (Long) criteria.uniqueResult();
		return count > 0L;
	}

	public InvCell getInvCellByLocationNames(String siteName, String freezerName, String rackName, String boxName, String row, String column) throws ArkSystemException {
		InvCell invCell = new InvCell();

		if((siteName == null || freezerName == null || rackName == null || boxName == null || row == null || column == null) ||
				(siteName.isEmpty() || freezerName.isEmpty() || rackName.isEmpty() || boxName.isEmpty() || row.isEmpty() || column.isEmpty())) {
			return invCell;
		}
		
		Long rowno;
		Long colno;

		try {
			rowno = new Long(row);
		}
		catch (NumberFormatException e) {
			char c = (char) row.charAt(0);
			rowno = (long) (c - 64);
		}

		try {
			colno = new Long(column);
		}
		catch (NumberFormatException e) {
			char c = (char) column.charAt(0);
			colno = (long) (c - 64);
		}

		Criteria criteria = getSession().createCriteria(InvCell.class);
		criteria.createAlias("invBox", "box", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("box.invRack", "rack", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("rack.invFreezer", "freezer", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("freezer.invSite", "site", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("biospecimen", "b", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("site.name", siteName));
		criteria.add(Restrictions.eq("freezer.name", freezerName));
		criteria.add(Restrictions.eq("rack.name", rackName));
		criteria.add(Restrictions.eq("box.name", boxName));
		criteria.add(Restrictions.eq("rowno", rowno));
		criteria.add(Restrictions.eq("colno", colno));

		invCell = (InvCell) criteria.uniqueResult();
		return invCell;
	}

	public void batchUpdateInvCells(List<InvCell> updateInvCells) {
		// StatelessSession session = getStatelessSession();
		// Transaction tx = session.beginTransaction();

		for (InvCell invCell : updateInvCells) {
			// session.update(invCell);
			getSession().update(invCell);
		}
		// tx.commit();
		// session.close();
	}

	/*
	 * TODO Clean up catch
	 * 
	 * Bigger TODO  remove all this logic of empty cells and such cells should just be null/nothing/non-existent 
	 * 
	 */
	public String fillOutAllBoxesWithEmptyInvCellsToCapacity(Study study){
		int count = 0;
		if(study != null){
			InvRack emptySearchRack = new InvRack();
			List<Study> studyListForUser = new ArrayList<Study>();
			studyListForUser.add(study);
			
			try {
				List<InvRack> allRacks = searchInvRack(emptySearchRack, studyListForUser);
			
				for (InvRack rack : allRacks){
					List<InvBox> boxes = rack.getInvBoxes();
				
					for(InvBox box : boxes){
						int nocols = box.getNoofcol();
						int norows = box.getNoofrow();
						
						for (long rowno=1; rowno<=norows ; rowno++){
							for(long colno=1; colno<=nocols ; colno++){
								
								if(getInvCell(box, rowno, colno)==null){
									InvCell invCell = new InvCell();
									invCell.setColno(colno);
									invCell.setRowno(rowno);
									invCell.setInvBox(box);
									
									//invCell.setDeleted(0L);
									invCell.setStatus("Empty");
									getSession().save(invCell);
									count++;
								}
							}
						}
					}
				}
			} catch (ArkSystemException e) {
				log.error("you madea mistake trying to insert / fill empty box for study " + study.getName());
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "failed - " + e.getMessage();
			}
		}
		else{
			log.error("give me a study name or I will refuse to enter empty cells");
			return "Without a study name we cannot complete empty cells";
		}
		return "successfully completed " + count + " cells";
	}
	
	public InvCell getNextAvailableInvCell(InvBox invBox) {
		Criteria criteria = getSession().createCriteria(InvCell.class);
		criteria.add(Restrictions.eq("invBox", invBox));
		criteria.add(Restrictions.isNull("biospecimen"));
		criteria.setMaxResults(1);
		return (InvCell) criteria.list().get(0);
	}

	public Integer countAvailableCellsForBox(InvBox invBox) {
		int total = 0;
		total = ((Long) getSession().createQuery("SELECT COUNT(*) FROM InvCell WHERE invBox = :invBox and biospecimen IS NULL").setParameter("invBox", invBox).iterate().next()).intValue();
		return total;
	}

	public void createStudyInvSite(StudyInvSite studyInvSite) {
		getSession().save(studyInvSite);
	}

	public void deleteStudyInvSite(StudyInvSite studyInvSite) {
		getSession().delete(studyInvSite);
	}

	
	
	public void updateInvSite(LimsVO modelObject) {
		
        InvSite invSite = modelObject.getInvSite();
        Session session = getSession();
        session.update(invSite);
        session.flush();
        session.refresh(invSite);
        List<StudyInvSite> existingInvSites = invSite.getStudyInvSites();

        //for (StudyInvSite sis : modelObject.getInvSite().getStudyInvSites()) {
        //      session.delete(sis);
        //      session.flush();
        //}



        List<Long> selectedAndExistingStudies = new ArrayList<Long>();
        List<Study> selectedStudies = modelObject.getSelectedStudies();

        for (Study selectedStudy : selectedStudies) {
                boolean studyAlreadyLinked = false;
                log.info("selected =" + selectedStudy.getId());

                for(StudyInvSite sis: existingInvSites){
                        Study existingStudy = sis.getStudy();
                        log.info("  existing=" + selectedStudy.getId());
                        if(existingStudy.getId().equals(selectedStudy.getId())){
                        		log.info("found a match for " + selectedStudy.getId());
                                studyAlreadyLinked = true;
                                selectedAndExistingStudies.add(selectedStudy.getId());
                                break; // leave it along
                        }
                }

                if(!studyAlreadyLinked){
                        log.info("about to create" + selectedStudy.getId());
                        StudyInvSite studyInvSite = new StudyInvSite();
                        studyInvSite.setStudy(selectedStudy);
                        studyInvSite.setInvSite(invSite);
                        session.save(studyInvSite);
                }

        }

        for(StudyInvSite sis: existingInvSites){
        		log.info("about to investigate for deletion existing study " + sis.getStudy().getId());
                boolean deletePreviouslyExistingSiteAsItWasNotSelected = true;
                for(Long selectedId : selectedAndExistingStudies){
                		log.info("compare it to selected " + selectedId);
                        if(selectedId.equals(sis.getStudy().getId())){
                        		log.info("recommending you don't delete");
                                deletePreviouslyExistingSiteAsItWasNotSelected = false;
                        }
                        else{
                        	log.info("match not found.");
                        }
                }
                if(deletePreviouslyExistingSiteAsItWasNotSelected){
                	log.info("deleting " + sis.getStudy().getId());
                	session.delete(sis);
                }
        }
        session.flush();
        session.refresh(invSite);

		
		
		
		
		//List<StudyInvSite> existingInvSites = invSite.getStudyInvSites();
		
		//for (StudyInvSite sis : modelObject.getInvSite().getStudyInvSites()) {
		//	session.delete(sis);
		//	session.flush();
		//}
		
		/*
		
		List<Study> selectedAndExistingStudies = new ArrayList<Study>();
		List<Study> selectedStudies = modelObject.getSelectedStudies();
		
		for (Study selectedStudy : selectedStudies) {
			boolean studyAlreadyLinked = false;
			for(StudyInvSite sis: existingInvSites){
				Study existingStudy = sis.getStudy();
				if(existingStudy.equals(selectedStudy)){
					studyAlreadyLinked = true;
					selectedAndExistingStudies.add(selectedStudy);
					break; // leave it along
				}
			}
			
			if(!studyAlreadyLinked){
				StudyInvSite studyInvSite = new StudyInvSite();
				studyInvSite.setStudy(selectedStudy);
				studyInvSite.setInvSite(modelObject.getInvSite());
				session.save(studyInvSite);
			}
			
		}
		
		for(StudyInvSite sis: existingInvSites){
			if(!selectedAndExistingStudies.contains(sis.getStudy())){
				session.delete(sis);
			}
		}
		*/
	}

	public void unallocateBox(InvBox invBox) {
		Session session = getSession();
		for(InvCell invCell : invBox.getInvCells()) {
			invCell.setBiospecimen(null);
			session.update(invCell);
		}
		session.refresh(invBox);
	}
}
