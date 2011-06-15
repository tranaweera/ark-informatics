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
 * BarcodeidEngine generated by hbm2java
 */
@Entity
@Table(name = "barcodeid_engine", schema = Constants.LIMS_TABLE_SCHEMA)
public class BarcodeidEngine implements java.io.Serializable
{

	private int		id;
	private int		studyId;
	private String	class_;

	public BarcodeidEngine()
	{
	}

	public BarcodeidEngine(int id, int studyId, String class_)
	{
		this.id = id;
		this.studyId = studyId;
		this.class_ = class_;
	}

	@Id
	@SequenceGenerator(name="barcodeidengine_generator", sequenceName="BARCODEIDENGINE_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "barcodeidengine_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public int getId()
	{
		return this.id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	@Column(name = "STUDY_ID", nullable = false)
	public int getStudyId()
	{
		return this.studyId;
	}

	public void setStudyId(int studyId)
	{
		this.studyId = studyId;
	}

	@Column(name = "CLASS", nullable = false, length = 100)
	public String getClass_()
	{
		return this.class_;
	}

	public void setClass_(String class_)
	{
		this.class_ = class_;
	}

}
