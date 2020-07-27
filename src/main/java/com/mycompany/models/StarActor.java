/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author michellanet
 */
@Entity
@Table(name = "STAR_ACTOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StarActor.findAll", query = "SELECT s FROM StarActor s"),
    @NamedQuery(name = "StarActor.findById", query = "SELECT s FROM StarActor s WHERE s.id = :id"),
    @NamedQuery(name = "StarActor.findByFirstname", query = "SELECT s FROM StarActor s WHERE s.firstname = :firstname"),
    @NamedQuery(name = "StarActor.findByLastname", query = "SELECT s FROM StarActor s WHERE s.lastname = :lastname")})
public class StarActor implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "FIRSTNAME")
    private String firstname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "LASTNAME")
    private String lastname;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "starActorFk")
    private Collection<Videos> videosCollection;

    public StarActor() {
    }

    public StarActor(BigDecimal id) {
        this.id = id;
    }

    public StarActor(BigDecimal id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @XmlTransient
    public Collection<Videos> getVideosCollection() {
        return videosCollection;
    }

    public void setVideosCollection(Collection<Videos> videosCollection) {
        this.videosCollection = videosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StarActor)) {
            return false;
        }
        StarActor other = (StarActor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.models.StarActor[ id=" + id + " ]";
    }
    
}
