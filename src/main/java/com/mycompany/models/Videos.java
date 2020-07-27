/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author michellanet
 */
@Entity
@Table(name = "VIDEOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Videos.findAll", query = "SELECT v FROM Videos v"),
    @NamedQuery(name = "Videos.findById", query = "SELECT v FROM Videos v WHERE v.id = :id"),
    @NamedQuery(name = "Videos.findByTitle", query = "SELECT v FROM Videos v WHERE v.title = :title"),
    @NamedQuery(name = "Videos.findByType", query = "SELECT v FROM Videos v WHERE v.type = :type"),
    @NamedQuery(name = "Videos.findByGenre", query = "SELECT v FROM Videos v WHERE v.genre = :genre"),
    @NamedQuery(name = "Videos.findByYear", query = "SELECT v FROM Videos v WHERE v.year = :year")})
public class Videos implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "TITLE")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "TYPE")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "GENRE")
    private String genre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "YEAR")
    private BigInteger year;
    @Lob
    @Column(name = "IMAGE")
    private byte[] image;
    @JoinColumn(name = "STAR_ACTOR_FK", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private StarActor starActorFk;

    public Videos() {
    }

    public Videos(BigDecimal id) {
        this.id = id;
    }

    public Videos(BigDecimal id, String title, String type, String genre, BigInteger year) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.genre = genre;
        this.year = year;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public BigInteger getYear() {
        return year;
    }

    public void setYear(BigInteger year) {
        this.year = year;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public StarActor getStarActorFk() {
        return starActorFk;
    }

    public void setStarActorFk(StarActor starActorFk) {
        this.starActorFk = starActorFk;
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
        if (!(object instanceof Videos)) {
            return false;
        }
        Videos other = (Videos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.models.Videos[ id=" + id + " ]";
    }
    
}
