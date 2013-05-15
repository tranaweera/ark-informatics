package au.org.theark.study.model.capsule;

import java.io.Serializable;

import com.x5.util.DataCapsule;

public class RelativeCapsule implements DataCapsule,Serializable{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private String familyId;
	private String individualId;
	private String gender;
	private String father;
	private String mother;
	private String deceased;
	private String proband;
	private String dob;
	private String mzTwin;
	private String dzTwin;
	private String sampled;
	private String affected;
	
	public RelativeCapsule(){
		
	}

	public RelativeCapsule(String familyId, String individualId, String gender,
			String father, String mother, String deceased, String proband,
			String dob, String mzTwin, String dzTwin, String sampled,
			String affected) {
		this.familyId = familyId;
		this.individualId = individualId;
		this.gender = gender;
		this.father = father;
		this.mother = mother;
		this.deceased = deceased;
		this.proband = proband;
		this.dob = dob;
		this.mzTwin = mzTwin;
		this.dzTwin = dzTwin;
		this.sampled = sampled;
		this.affected = affected;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public String getIndividualId() {
		return individualId;
	}

	public void setIndividualId(String individualId) {
		this.individualId = individualId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public String getMother() {
		return mother;
	}

	public void setMother(String mother) {
		this.mother = mother;
	}

	public String getDeceased() {
		return deceased;
	}

	public void setDeceased(String deceased) {
		this.deceased = deceased;
	}

	public String getProband() {
		return proband;
	}

	public void setProband(String proband) {
		this.proband = proband;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getMzTwin() {
		return mzTwin;
	}

	public void setMzTwin(String mzTwin) {
		this.mzTwin = mzTwin;
	}

	public String getDzTwin() {
		return dzTwin;
	}

	public void setDzTwin(String dzTwin) {
		this.dzTwin = dzTwin;
	}

	public String getSampled() {
		return sampled;
	}

	public void setSampled(String sampled) {
		this.sampled = sampled;
	}

	public String getAffected() {
		return affected==null?".":affected;
	}

	public void setAffected(String affected) {
		this.affected = affected;
	}

	public String[] getExports() {
		  return new String[]{
				     "getFamilyId",
				     "getIndividualId",
				     "getGender",
				     "getFather",
				     "getMother",
				     "getDeceased",
				     "getProband",
				     "getDob",
				     "getMzTwin",
				     "getDzTwin",
				     "getSampled",
				     "getAffected",
				     };
	}

	public String getExportPrefix() {
		// TODO Auto-generated method stub
		return "relative";
	}

	@Override
	public String toString() {
		return "RelativeCapsule [familyId=" + familyId + ", individualId=" + individualId + ", gender=" + gender + ", father=" + father + ", mother=" + mother + ", deceased=" + deceased + ", proband="
				+ proband + ", dob=" + dob + ", mzTwin=" + mzTwin + ", dzTwin=" + dzTwin + ", sampled=" + sampled + ", affected=" + affected + "]";
	}

}
