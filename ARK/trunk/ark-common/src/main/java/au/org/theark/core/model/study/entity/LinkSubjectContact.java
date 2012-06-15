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
package au.org.theark.core.model.study.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import au.org.theark.core.Constants;

/**
 * LinkSubjectContact entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LINK_SUBJECT_CONTACT", schema = Constants.STUDY_SCHEMA)
public class LinkSubjectContact implements java.io.Serializable {




	private static final long serialVersionUID = 1L;
	private Long			id;
	private Study			study;
	private Relationship	relationship;
	private Person			personBySubjectId;
	private Person			personByContactId;


	public LinkSubjectContact() {
	}

	public LinkSubjectContact(Long id) {
		this.id = id;
	}

	public LinkSubjectContact(Long id, Study study, Relationship relationship, Person personBySubjectId, Person personByContactId) {
		this.id = id;
		this.study = study;
		this.relationship = relationship;
		this.personBySubjectId = personBySubjectId;
		this.personByContactId = personByContactId;
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return this.study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RELATIONSHIP_ID")
	public Relationship getRelationship() {
		return this.relationship;
	}

	public void setRelationship(Relationship relationship) {
		this.relationship = relationship;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_SUBJECT_ID")
	public Person getpersonBySubjectId() {
		return this.personBySubjectId;
	}

	public void setPersonBySubjectId(Person personBySubjectId) {
		this.personBySubjectId = personBySubjectId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_CONTACT_ID")
	public Person getPersonByContactId() {
		return this.personByContactId;
	}

	public void setPersonByContactId(Person personByContactId) {
		this.personByContactId = personByContactId;
	}

}
