package au.org.theark.lims.model.vo;

import java.io.Serializable;

/**
 * Represents a Biospecimen's complete location details in one VO.
 * @author nivedan
 * @author cellis
 *
 */
public class BiospecimenLocationVO implements Serializable{
		
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	protected String siteName;
	protected Boolean isAllocated;
	protected String tankName;
	protected String trayName;
	protected String boxName;
	protected Long row;//has rows and e;
	protected Long column;
	protected String rowLabel;
	protected String colLabel;
	
	public BiospecimenLocationVO(){
		isAllocated=false;
	}
	
	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getTankName() {
		return tankName;
	}

	public void setTankName(String tankName) {
		this.tankName = tankName;
	}

	public String getTrayName() {
		return trayName;
	}

	public void setTrayName(String trayName) {
		this.trayName = trayName;
	}

	public String getBoxName() {
		return boxName;
	}

	public void setBoxName(String boxName) {
		this.boxName = boxName;
	}

	public Long getRow() {
		return row;
	}

	public void setRow(Long row) {
		this.row = row;
	}

	public Long getColumn() {
		return column;
	}

	public void setColumn(Long column) {
		this.column = column;
	}
	
	public Boolean getIsAllocated() {
		return isAllocated;
	}

	public void setIsAllocated(Boolean isAllocated) {
		this.isAllocated = isAllocated;
	}

	/**
	 * @return the rowLabel
	 */
	public String getRowLabel() {
		return rowLabel;
	}

	/**
	 * @param rowLabel the rowLabel to set
	 */
	public void setRowLabel(String rowLabel) {
		this.rowLabel = rowLabel;
	}

	/**
	 * @return the colLabel
	 */
	public String getColLabel() {
		return colLabel;
	}

	/**
	 * @param colLabel the colLabel to set
	 */
	public void setColLabel(String colLabel) {
		this.colLabel = colLabel;
	}
}
