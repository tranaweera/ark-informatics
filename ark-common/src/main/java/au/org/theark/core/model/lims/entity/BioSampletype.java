package au.org.theark.core.model.lims.entity;

// Generated 15/06/2011 1:22:58 PM by Hibernate Tools 3.3.0.GA

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * BioSampletype generated by hbm2java
 */
@Entity
@Table(name = "bio_sampletype", schema = Constants.LIMS_TABLE_SCHEMA)
public class BioSampletype implements java.io.Serializable
{

	private int		id;
	private String	sampletype;
	private String	samplesubtype;

	public BioSampletype()
	{
	}

	public BioSampletype(int id, String sampletype)
	{
		this.id = id;
		this.sampletype = sampletype;
	}

	public BioSampletype(int id, String sampletype, String samplesubtype)
	{
		this.id = id;
		this.sampletype = sampletype;
		this.samplesubtype = samplesubtype;
	}

	@Id
	@SequenceGenerator(name="biosampletype_generator", sequenceName="BIOSAMPLETYPE_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "biosampletype_generator")
	public int getId()
	{
		return this.id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	@Column(name = "SAMPLETYPE", nullable = false)
	public String getSampletype()
	{
		return this.sampletype;
	}

	public void setSampletype(String sampletype)
	{
		this.sampletype = sampletype;
	}

	@Column(name = "SAMPLESUBTYPE")
	public String getSamplesubtype()
	{
		return this.samplesubtype;
	}

	public void setSamplesubtype(String samplesubtype)
	{
		this.samplesubtype = samplesubtype;
	}

}
