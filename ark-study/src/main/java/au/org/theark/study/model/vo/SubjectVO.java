/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import au.org.theark.study.model.entity.Address;
import au.org.theark.study.model.entity.EmailAccount;
import au.org.theark.study.model.entity.LinkSubjectStudy;
import au.org.theark.study.model.entity.Person;
import au.org.theark.study.model.entity.Phone;
import au.org.theark.study.model.entity.Study;
import au.org.theark.study.model.entity.SubjectStatus;

/**
 * @author nivedann
 *
 */
@SuppressWarnings("serial")
public class SubjectVO implements Serializable{
	
	protected String subjectFullName;
	protected Person person;
	protected Phone phone;
	protected Address address;
	protected EmailAccount emailAccount;
	protected Study study;
	protected Long linkSubjectStudyId;
	protected Collection<SubjectVO> subjectList;
	protected SubjectStatus subjectStatus;
	
	/** A List of phone numbers linked to this person/subject*/
	protected List<Phone> phoneList;
	/** A List of Address linked to this person/subject*/
	protected List<Address> addressList;
	/** A List of Email account linked to this person/subject*/
	protected List<EmailAccount> emailAccountList;
	
	/**
	 * Constructor
	 */
	public SubjectVO(){
		person = new Person();
		phone = new Phone();
		address = new Address();
		emailAccount = new EmailAccount();
		
		phoneList = new ArrayList<Phone>();
		addressList = new ArrayList<Address>();
		emailAccountList = new ArrayList<EmailAccount>();
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Phone getPhone() {
		return phone;
	}

	public void setPhone(Phone phone) {
		this.phone = phone;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public EmailAccount getEmailAccount() {
		return emailAccount;
	}

	public void setEmailAccount(EmailAccount emailAccount) {
		this.emailAccount = emailAccount;
	}

	public List<Phone> getPhoneList() {
		return phoneList;
	}

	public void setPhoneList(List<Phone> phoneList) {
		this.phoneList = phoneList;
	}

	public List<Address> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<Address> addressList) {
		this.addressList = addressList;
	}

	public List<EmailAccount> getEmailAccountList() {
		return emailAccountList;
	}

	public void setEmailAccountList(List<EmailAccount> emailAccountList) {
		this.emailAccountList = emailAccountList;
	}

	public SubjectStatus getSubjectStatus() {
		return subjectStatus;
	}

	public void setSubjectStatus(SubjectStatus subjectStatus) {
		this.subjectStatus = subjectStatus;
	}

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public Collection<SubjectVO> getSubjectList() {
		return subjectList;
	}

	public void setSubjectList(Collection<SubjectVO> subjectList) {
		this.subjectList = subjectList;
	}

	public String getSubjectFullName() {
		return subjectFullName;
	}

	public void setSubjectFullName(String subjectFullName) {
		this.subjectFullName = subjectFullName;
	}


	public Long getLinkSubjectStudyId() {
		return linkSubjectStudyId;
	}

	public void setLinkSubjectStudyId(Long linkSubjectStudyId) {
		this.linkSubjectStudyId = linkSubjectStudyId;
	}



}
