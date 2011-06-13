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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.Constants;

/**
 * @author nivedann
 *
 */
@Entity
@Table(name = "ARK_ROLE", schema = Constants.STUDY_SCHEMA)
public class ArkRole implements Serializable{

	private Long id;
	private String name;
	private String description;
	private Set<ArkRolePermission> arkRolePermission  = new HashSet<ArkRolePermission>(0);
	
	public ArkRole(){
		
	}
	
	@Id
	@SequenceGenerator(name="role_generator", sequenceName="ROLE_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "role_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "arkRole")
	public Set<ArkRolePermission> getArkRolePermission() {
		return arkRolePermission;
	}

	public void setArkRolePermission(Set<ArkRolePermission> arkRolePermission) {
		this.arkRolePermission = arkRolePermission;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArkRole other = (ArkRole) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
