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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import au.org.theark.core.audit.annotations.ArkAuditDisplay;
import au.org.theark.core.model.Constants;
import au.org.theark.core.model.lims.entity.BioCollectionCustomFieldData;
import au.org.theark.core.model.lims.entity.BiospecimenCustomFieldData;
import au.org.theark.core.model.pheno.entity.PhenoData;

/**
 * @author nivedann
 */

@Entity
@Table(name = "CUSTOM_FIELD_DISPLAY", schema = Constants.STUDY_SCHEMA)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class CustomFieldDisplay implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private CustomField customField;
	//private Category category;
	private CustomFieldGroup customFieldGroup;
	private Boolean required;
	private String requiredMessage;
	private Boolean allowMultiselect = Boolean.FALSE;
	private Long sequence;
	private Set<SubjectCustomFieldData> subjectCustomFieldData = new HashSet<SubjectCustomFieldData>();
	private Set<BioCollectionCustomFieldData> bioCollectionCustomFieldData = new HashSet<BioCollectionCustomFieldData>();
	private Set<BiospecimenCustomFieldData> biospecimenCustomFieldData = new HashSet<BiospecimenCustomFieldData>();
	private Set<PhenoData> phenoData = new HashSet<PhenoData>();
	protected String descriptiveNameIncludingCFGName;

	public CustomFieldDisplay() {
//		this.allowMultiselect = Boolean.FALSE;
	}

	@Id
	@SequenceGenerator(name = "custom_field_display_seq_gen", sequenceName = "CUSTOM_FIELD_DISPLAY_SEQ_GEN")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "custom_field_display_seq_gen")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOM_FIELD_ID", nullable = false)
	public CustomField getCustomField() {
		return customField;
	}

	public void setCustomField(CustomField customField) {
		this.customField = customField;
	}

/*
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_ID", nullable = false)
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOM_FIELD_GROUP_ID")
	public CustomFieldGroup getCustomFieldGroup() {
		return customFieldGroup;
	}

	public void setCustomFieldGroup(CustomFieldGroup customFieldGroup) {
		this.customFieldGroup = customFieldGroup;
	}

	@Column(name = "REQUIRED")
	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	@Column(name = "REQUIRED_MESSAGE")
	public String getRequiredMessage() {
		return requiredMessage;
	}

	public void setRequiredMessage(String requiredMessage) {
		this.requiredMessage = requiredMessage;
	}

	@Column(name = "SEQUENCE", precision = 22, scale = 0)
	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customFieldDisplay")
	public Set<SubjectCustomFieldData> getSubjectCustomFieldData() {
		return subjectCustomFieldData;
	}

	public void setSubjectCustomFieldData(
			Set<SubjectCustomFieldData> subjectCustomFieldData) {
		this.subjectCustomFieldData = subjectCustomFieldData;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customFieldDisplay")
	public Set<BioCollectionCustomFieldData> getBioCollectionCustomFieldData() {
		return bioCollectionCustomFieldData;
	}

	public void setBioCollectionCustomFieldData(
			Set<BioCollectionCustomFieldData> bioCollectionCustomFieldData) {
		this.bioCollectionCustomFieldData = bioCollectionCustomFieldData;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customFieldDisplay")
	public Set<BiospecimenCustomFieldData> getBiospecimenCustomFieldData() {
		return biospecimenCustomFieldData;
	}

	public void setBiospecimenCustomFieldData(
			Set<BiospecimenCustomFieldData> biospecimenCustomFieldData) {
		this.biospecimenCustomFieldData = biospecimenCustomFieldData;
	}

	//TODO: Remove NotAudited when pheno auditing is done
	@NotAudited
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customFieldDisplay")
	public Set<PhenoData> getPhenoData() {
		return phenoData;
	}

	public void setPhenoData(Set<PhenoData> phenoData) {
		this.phenoData = phenoData;
	}

	@Column(name = "ALLOW_MULTIPLE_SELECTION", precision = 1, scale = 0)
	public Boolean getAllowMultiselect() {
		return allowMultiselect;
	}

	public void setAllowMultiselect(Boolean allowMultiselect) {
		this.allowMultiselect = allowMultiselect;
	}

	@ArkAuditDisplay
	@Transient
	public String getDescriptiveNameIncludingCFGName() {
		//descriptiveNameIncludingCFGName.charAt(1);
		StringBuilder displayExpression = new StringBuilder();
		displayExpression.append(customFieldGroup==null?"UNKNOWN":customFieldGroup.getName());
		displayExpression.append(" > ");
		displayExpression.append(customField==null?"UNKNOWN":customField.getName());
		return displayExpression.toString();
	}
	public void setDescriptiveNameIncludingCFGName(
			String descriptiveNameIncludingCFGName) {
		this.descriptiveNameIncludingCFGName = descriptiveNameIncludingCFGName;
	}
}
