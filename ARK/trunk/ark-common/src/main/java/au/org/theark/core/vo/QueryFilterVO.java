package au.org.theark.core.vo;

import java.io.Serializable;

import au.org.theark.core.model.report.entity.FieldCategory;
import au.org.theark.core.model.report.entity.QueryFilter;

public class QueryFilterVO implements Serializable {

	private static final long serialVersionUID = -1239326388214334966L;
	
	private FieldCategory fieldCategory;
	
	private QueryFilter queryFilter;
	
	public FieldCategory getFieldCategory() {
		return fieldCategory;
	}
	public void setFieldCategory(FieldCategory fieldCategory) {
		this.fieldCategory = fieldCategory;
	}
	public QueryFilter getQueryFilter() {
		return queryFilter;
	}
	public void setQueryFilter(QueryFilter queryFilter) {
		this.queryFilter = queryFilter;
	}
	
}
