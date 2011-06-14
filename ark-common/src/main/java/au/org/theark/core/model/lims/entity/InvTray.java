package au.org.theark.core.model.lims.entity;

// Generated Jun 14, 2011 3:39:29 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * InvTray generated by hbm2java
 */
@Entity
@Table(name = "inv_tray", catalog = "lims")
public class InvTray implements java.io.Serializable {

	private int id;
	private String timestamp;
	private InvBox invBox;
	private Integer deleted;
	private int noofcol;
	private String colnotype;
	private Integer capacity;
	private String name;
	private Integer available;
	private int noofrow;
	private String rownotype;
	private Integer transferId;
	private int type;
	private Set<InvCell> invCells = new HashSet<InvCell>(0);

	public InvTray() {
	}

	public InvTray(int id, InvBox invBox, int noofcol, String colnotype,
			int noofrow, String rownotype, int type) {
		this.id = id;
		this.invBox = invBox;
		this.noofcol = noofcol;
		this.colnotype = colnotype;
		this.noofrow = noofrow;
		this.rownotype = rownotype;
		this.type = type;
	}

	public InvTray(int id, InvBox invBox, Integer deleted, int noofcol,
			String colnotype, Integer capacity, String name, Integer available,
			int noofrow, String rownotype, Integer transferId, int type,
			Set<InvCell> invCells) {
		this.id = id;
		this.invBox = invBox;
		this.deleted = deleted;
		this.noofcol = noofcol;
		this.colnotype = colnotype;
		this.capacity = capacity;
		this.name = name;
		this.available = available;
		this.noofrow = noofrow;
		this.rownotype = rownotype;
		this.transferId = transferId;
		this.type = type;
		this.invCells = invCells;
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Version
	@Column(name = "TIMESTAMP", length = 55)
	public String getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BOX_ID", nullable = false)
	public InvBox getInvBox() {
		return this.invBox;
	}

	public void setInvBox(InvBox invBox) {
		this.invBox = invBox;
	}

	@Column(name = "DELETED")
	public Integer getDeleted() {
		return this.deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	@Column(name = "NOOFCOL", nullable = false)
	public int getNoofcol() {
		return this.noofcol;
	}

	public void setNoofcol(int noofcol) {
		this.noofcol = noofcol;
	}

	@Column(name = "COLNOTYPE", nullable = false, length = 15)
	public String getColnotype() {
		return this.colnotype;
	}

	public void setColnotype(String colnotype) {
		this.colnotype = colnotype;
	}

	@Column(name = "CAPACITY")
	public Integer getCapacity() {
		return this.capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	@Column(name = "NAME", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "AVAILABLE")
	public Integer getAvailable() {
		return this.available;
	}

	public void setAvailable(Integer available) {
		this.available = available;
	}

	@Column(name = "NOOFROW", nullable = false)
	public int getNoofrow() {
		return this.noofrow;
	}

	public void setNoofrow(int noofrow) {
		this.noofrow = noofrow;
	}

	@Column(name = "ROWNOTYPE", nullable = false, length = 15)
	public String getRownotype() {
		return this.rownotype;
	}

	public void setRownotype(String rownotype) {
		this.rownotype = rownotype;
	}

	@Column(name = "TRANSFER_ID")
	public Integer getTransferId() {
		return this.transferId;
	}

	public void setTransferId(Integer transferId) {
		this.transferId = transferId;
	}

	@Column(name = "TYPE", nullable = false)
	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "invTray")
	public Set<InvCell> getInvCells() {
		return this.invCells;
	}

	public void setInvCells(Set<InvCell> invCells) {
		this.invCells = invCells;
	}

}
