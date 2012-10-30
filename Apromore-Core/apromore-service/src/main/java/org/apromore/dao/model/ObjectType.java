package org.apromore.dao.model;
// Generated 12/06/2012 2:46:45 PM by Hibernate Tools 3.2.4.GA


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.factory.annotation.Configurable;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * ObjectType generated by hbm2java
 */
@Entity
@Table(name = "object_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Configurable("objectType")
public class ObjectType implements java.io.Serializable {

    private Integer id;
    private String name;
    private String type;
    private String configurable;

    private ProcessModelVersion processModelVersion;
    private Set<ObjectTypeAttribute> objectTypeAttributes = new HashSet<ObjectTypeAttribute>(0);
    private Set<ObjectRefType> objectRefTypes = new HashSet<ObjectRefType>(0);

    /**
     * Public Constructor.
     */
    public ObjectType() { }


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

    public void setName(final String newName) {
        this.name = newName;
    }

    @Column(name = "type", length = 50)
    public String getType() {
        return this.type;
    }

    public void setType(final String newType) {
        this.type = newType;
    }

    @Column(name = "configurable", length = 1)
    public String getConfigurable() {
        return this.configurable;
    }

    public void setConfigurable(final String newConfigurable) {
        this.configurable = newConfigurable;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processModelVersionId")
    public ProcessModelVersion getProcessModelVersion() {
        return this.processModelVersion;
    }

    public void setProcessModelVersion(final ProcessModelVersion newProcessModelVersion) {
        this.processModelVersion = newProcessModelVersion;
    }


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "objectType")
    public Set<ObjectTypeAttribute> getObjectTypeAttributes() {
        return this.objectTypeAttributes;
    }

    public void setObjectTypeAttributes(Set<ObjectTypeAttribute> objectTypeAttributes) {
        this.objectTypeAttributes = objectTypeAttributes;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "objectType")
    public Set<ObjectRefType> getObjectRefTypes() {
        return this.objectRefTypes;
    }

    public void setObjectRefTypes(Set<ObjectRefType> objectRefTypes) {
        this.objectRefTypes = objectRefTypes;
    }

}


