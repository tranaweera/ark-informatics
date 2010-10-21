package au.org.theark.gdmi.model.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

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

import org.apache.wicket.util.io.IOUtils;
import org.hibernate.Hibernate;

/**
 * EncodedData entity. @author MyEclipse Persistence Tools
 */
@Entity(name="au.org.theark.gdmi.model.entity.EncodedData")
@Table(name = "ENCODED_DATA", schema = "GDMI")
public class EncodedData implements java.io.Serializable {

	// Fields

	private Long id;
	private Collection collection;
	private Long subjectId;
	private Blob encodedBit1;
	private Blob encodedBit2;

	// Constructors

	/** default constructor */
	public EncodedData() {
	}

	/** minimal constructor */
	public EncodedData(Long id, Collection collection, Long subjectId) {
		this.id = id;
		this.collection = collection;
		this.subjectId = subjectId;
	}

	/** full constructor */
	private EncodedData(Long id, Collection collection, Long subjectId,
			Blob encodedBit1, Blob encodedBit2) {
		this.id = id;
		this.collection = collection;
		this.subjectId = subjectId;
		this.encodedBit1 = encodedBit1;
		this.encodedBit2 = encodedBit2;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name="Encoded_Data_PK_Seq",sequenceName="GDMI.ENCODED_DATA_PK_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO,generator="Encoded_Data_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COLLECTION_ID", nullable = false)
	public Collection getCollection() {
		return this.collection;
	}

	public void setCollection(Collection collection) {
		this.collection = collection;
	}

	@Column(name = "SUBJECT_ID", nullable = false, precision = 22, scale = 0)
	public Long getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}
	
	@Column(name = "ENCODED_BIT1")
	public Blob getEncodedBit1() {
		return this.encodedBit1;
	}

	public void setEncodedBit1(Blob encodedBit1) {
		this.encodedBit1 = encodedBit1;
	}

//	public void getEncodedBit1AsStream(OutputStream os) throws SQLException, IOException
//	{
//		InputStream blobStream = this.encodedBit1.getBinaryStream();
//		IOUtils.copy(blobStream, os);
//		blobStream.close();
//	}
//	
//	public void setEncodedBit1AsStream(InputStream is) throws IOException
//	{
//		this.encodedBit1 = Hibernate.createBlob(is);
//	}

	@Column(name = "ENCODED_BIT2")
	public Blob getEncodedBit2() {
		return this.encodedBit2;
	}

	public void setEncodedBit2(Blob encodedBit2) {
		this.encodedBit2 = encodedBit2;
	}

//	public void getEncodedBit2AsStream(OutputStream os) throws SQLException, IOException
//	{
//		InputStream blobStream = this.encodedBit2.getBinaryStream();
//		IOUtils.copy(blobStream, os);		//there is a 2GB limit the return value
//		blobStream.close();
//	}
//	
//	public void setEncodedBit2AsStream(InputStream is) throws IOException
//	{
//		this.encodedBit2 = Hibernate.createBlob(is);
//	}
	
}