package au.org.theark.core.model.report.entity;

import java.io.Serializable;

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

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;

@Entity
@Table(name = "custom_field_display_search", schema = Constants.REPORT_SCHEMA)
public class CustomFieldDisplaySearch  implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private CustomFieldDisplay customFieldDisplay;
	private Search search;

	public CustomFieldDisplaySearch() {
	}

	public CustomFieldDisplaySearch(CustomFieldDisplay cfd, Search search2) {
		search = search2;
		customFieldDisplay = cfd;
	}

	@Id
	@SequenceGenerator(name = "custom_field_display_search_generator", sequenceName = "CUSTOM_FIELD_DISPLAY_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "custom_field_display_search_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOM_FIELD_DISPLAY_ID")
	public CustomFieldDisplay getCustomFieldDisplay() {
		return customFieldDisplay;
	}
	public void setCustomFieldDisplay(CustomFieldDisplay customFieldDisplay) {
		this.customFieldDisplay = customFieldDisplay;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEARCH_ID")
	public Search getSearch() {
		return search;
	}
	public void setSearch(Search search) {
		this.search = search;
	}
	
}
