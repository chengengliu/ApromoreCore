package org.apromore.dao.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.factory.annotation.Configurable;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * ResourceTypeAttribute generated by hbm2java
 */
@Entity
@Table(name = "resource_type_attribute")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Configurable("resourceTypeAttribute")
public class ResourceTypeAttribute implements Serializable {

    private Integer id;
    private String name;
    private String value;

    private ResourceType resourceType;

    /**
     * Public Constructor.
     */
    public ResourceTypeAttribute() { }


    /**
     * returns the Id of this Object.
     * @return the id
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    /**
     * Sets the Id of this Object
     * @param id the new Id.
     */
    public void setId(final Integer id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Column(name = "value")
    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resourceTypeId")
    public ResourceType getResourceType() {
        return this.resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

}


