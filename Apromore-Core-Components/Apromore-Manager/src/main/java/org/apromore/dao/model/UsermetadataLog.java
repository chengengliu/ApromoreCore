/*-
 * #%L
 * This file is part of "Apromore Core".
 * %%
 * Copyright (C) 2018 - 2020 Apromore Pty Ltd.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
package org.apromore.dao.model;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "usermetadata_log")
@Configurable("usermetadata_log")
@Cache(expiry = 180000, size = 100, coordinationType = CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS)
public class UsermetadataLog implements Serializable {
    /**
     * ID
     */
    private Integer id;
    /**
     * FK USERMETADATA ID
     */
    private Usermetadata usermetadata;
    /**
     * FK LOG ID
     */
    private Log log;

    /**
     * Default Constructor.
     */
    public UsermetadataLog() {
    }

    /**
     * Convenient constructor.
     */
    public UsermetadataLog(Usermetadata newUsermetadata, Log newLog) {
        this.usermetadata = newUsermetadata;
        this.log = newLog;
    }

    /**
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    /**
     * ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * FK USERMETADATA ID
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usermetadata_id")
    public Usermetadata getUsermetadata() {
        return this.usermetadata;
    }

    /**
     * FK USERMETADATA ID
     */
    public void setUsermetadata(Usermetadata usermetadata) {
        this.usermetadata = usermetadata;
    }

    /**
     * FK LOG ID
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "log_id")
    public Log getLog() {
        return this.log;
    }

    /**
     * FK LOG ID
     */
    public void setLog(Log log) {
        this.log = log;
    }
}
