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
package au.org.theark.core.model.geno.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.model.Constants;

/**
 * MarkerMetaData entity. @author MyEclipse Persistence Tools
 */
@Entity(name = "au.org.theark.geno.model.entity.MarkerMetaData")
@Table(name = "MARKER_META_DATA", schema = Constants.GENO_TABLE_SCHEMA)
public class MarkerMetaData implements java.io.Serializable {




	private static final long serialVersionUID = 1L;
	private Long		id;
	private MetaData	metaData;
	private Marker		marker;
	private String		userId;
	private Date		insertTime;
	private String		updateUserId;
	private Date		updateTime;


	public MarkerMetaData() {
	}

	public MarkerMetaData(Long id, MetaData metaData, Marker marker, String userId, Date insertTime) {
		this.id = id;
		this.metaData = metaData;
		this.marker = marker;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	public MarkerMetaData(Long id, MetaData metaData, Marker marker, String userId, Date insertTime, String updateUserId, Date updateTime) {
		this.id = id;
		this.metaData = metaData;
		this.marker = marker;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
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
	@JoinColumn(name = "META_DATA_ID", nullable = false)
	public MetaData getMetaData() {
		return this.metaData;
	}

	public void setMetaData(MetaData metaData) {
		this.metaData = metaData;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MARKER_ID", nullable = false)
	public Marker getMarker() {
		return this.marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	@Column(name = "USER_ID", nullable = false)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERT_TIME", nullable = false)
	public Date getInsertTime() {
		return this.insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	@Column(name = "UPDATE_USER_ID")
	public String getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
