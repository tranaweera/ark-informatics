package au.org.theark.core.model.worktracking.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.TitleType;

@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@Table(name = "RESEARCHER", schema = Constants.ADMIN_SCHEMA)
public class Researcher implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long								id;
	private String								firstName;
	private String								lastName;
	private String								organization;
	private String								address;
	private ResearcherRole						researcherRole;
	private ResearcherStatus					researcherStatus;
	private Date								createdDate;
	private String								officePhone;
	private String								mobile;
	private String								email;
	private String								fax;
	private String								comment;
	private BillingType							billingType;
	private String								accountNumber;
	private String								bsb;
	private String								bank;
	private String								accountName;
	private Long								studyId;
	
	private TitleType							titleType;
	
	public Researcher() {
	}

	public Researcher(Long id) {
		this.id = id;
	}

	public Researcher(Long id, String firstName, String lastName,
			String organization, String address, ResearcherRole researcherRole,
			ResearcherStatus researcherStatus, Date createdDate,
			String officePhone, String mobile, String email, String fax,
			String comment, BillingType billingType, String accountNumber,
			String bsb, String bank, String accountName, Long studyId,TitleType titleType) {
		
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.organization = organization;
		this.address = address;
		this.researcherRole = researcherRole;
		this.researcherStatus = researcherStatus;
		this.createdDate = createdDate;
		this.officePhone = officePhone;
		this.mobile = mobile;
		this.email = email;
		this.fax = fax;
		this.comment = comment;
		this.billingType = billingType;
		this.accountNumber = accountNumber;
		this.bsb = bsb;
		this.bank = bank;
		this.accountName = accountName;
		this.studyId=studyId;
		this.titleType=titleType;
	}

	@Id
	@SequenceGenerator(name = "reseacher_generator", sequenceName = "RESEARCHER_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "reseacher_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "FIRST_NAME", length = 30)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "LAST_NAME", length = 45)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "ORGANIZATION", length = 50)
	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	@Column(name = "ADDRESS", length = 255)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_ID")
	public ResearcherRole getResearcherRole() {
		return researcherRole;
	}

	public void setResearcherRole(ResearcherRole researcherRole) {
		this.researcherRole = researcherRole;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID")
	public ResearcherStatus getResearcherStatus() {
		return researcherStatus;
	}

	public void setResearcherStatus(ResearcherStatus researcherStatus) {
		this.researcherStatus = researcherStatus;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE", length = 7)
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "OFFICE_PHONE", length = 12)
	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	@Column(name = "MOBILE", length = 12)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "EMAIL", length = 45)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "FAX", length = 12)
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "COMMENT", length = 255)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@ManyToOne
	@JoinColumn(name = "BILLING_TYPE_ID")
	public BillingType getBillingType() {
		return billingType;
	}

	public void setBillingType(BillingType billingType) {
		this.billingType = billingType;
	}

	@Column(name = "ACCOUNT_NUMBER", length = 30)
	public String getAccountNumber() {
		return accountNumber;
	}
	
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Column(name = "BSB", length = 8)
	public String getBsb() {
		return bsb;
	}

	public void setBsb(String bsb) {
		this.bsb = bsb;
	}

	@Column(name = "BANK", length = 50)
	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	@Column(name = "ACCOUNT_NAME", length = 50)
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@Column(name = "STUDY_ID")
	public Long getStudyId() {
		return studyId;
	}

	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}
	
	@ManyToOne()
	@JoinColumn(name = "TITLE_TYPE_ID")
	public TitleType getTitleType() {
		return this.titleType;
	}

	public void setTitleType(TitleType titleType) {
		this.titleType = titleType;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accountName == null) ? 0 : accountName.hashCode());
		result = prime * result
				+ ((accountNumber == null) ? 0 : accountNumber.hashCode());
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((bank == null) ? 0 : bank.hashCode());
		result = prime * result + ((bsb == null) ? 0 : bsb.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((fax == null) ? 0 : fax.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((organization == null) ? 0 : organization.hashCode());
		result = prime * result
				+ ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result
				+ ((officePhone == null) ? 0 : officePhone.hashCode());
		result = prime * result + ((studyId == null) ? 0 : studyId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Researcher other = (Researcher) obj;
		if (accountName == null) {
			if (other.accountName != null)
				return false;
		} else if (!accountName.equals(other.accountName))
			return false;
		if (accountNumber == null) {
			if (other.accountNumber != null)
				return false;
		} else if (!accountNumber.equals(other.accountNumber))
			return false;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (bank == null) {
			if (other.bank != null)
				return false;
		} else if (!bank.equals(other.bank))
			return false;
		if (bsb == null) {
			if (other.bsb != null)
				return false;
		} else if (!bsb.equals(other.bsb))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (organization == null) {
			if (other.organization != null)
				return false;
		} else if (!organization.equals(other.organization))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		if (officePhone == null) {
			if (other.officePhone != null)
				return false;
		} else if (!officePhone.equals(other.officePhone))
			return false;
		if (studyId == null) {
			if (other.studyId != null)
				return false;
		} else if (!studyId.equals(other.studyId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Researcher [id=" + id + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", organization=" + organization
				+ ", address=" + address + ", createdDate=" + createdDate
				+ ", officePhone=" + officePhone + ", mobile=" + mobile
				+ ", email=" + email + ", fax=" + fax + ", comment=" + comment
				+ ", accountNumber=" + accountNumber + ", bsb=" + bsb
				+ ", bank=" + bank + ", accountName=" + accountName
				+ ", studyId=" + studyId + "]";
	}	
	
}
