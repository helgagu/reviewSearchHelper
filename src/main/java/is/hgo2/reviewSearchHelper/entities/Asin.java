/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package is.hgo2.reviewSearchHelper.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Helga
 */
@Entity
@Table(name = "asin")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Asin.findAll", query = "SELECT a FROM Asin a"),
    @NamedQuery(name = "Asin.findByIdasin", query = "SELECT a FROM Asin a WHERE a.idasin = :idasin"),
    @NamedQuery(name = "Asin.findByAsin", query = "SELECT a FROM Asin a WHERE a.asin = :asin"),
    @NamedQuery(name = "Asin.findByTimestamp", query = "SELECT a FROM Asin a WHERE a.timestamp = :timestamp"),
    @NamedQuery(name = "Asin.findByUpdatedTimestamp", query = "SELECT a FROM Asin a WHERE a.updatedTimestamp = :updatedTimestamp")})
public class Asin implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idasin")
    private Integer idasin;
    @Basic(optional = false)
    @Column(name = "asin")
    private String asin;
    @Basic(optional = false)
    @Column(name = "Timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Column(name = "UpdatedTimestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTimestamp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idasin")
    private Collection<Books> booksCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idasin")
    private Collection<BrowsenodesAsin> browsenodesAsinCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "asinIdasin")
    private Collection<Notaccessibleitems> notaccessibleitemsCollection;

    public Asin() {
    }

    public Asin(Integer idasin) {
        this.idasin = idasin;
    }

    public Asin(Integer idasin, String asin, Date timestamp) {
        this.idasin = idasin;
        this.asin = asin;
        this.timestamp = timestamp;
    }

    public Integer getIdasin() {
        return idasin;
    }

    public void setIdasin(Integer idasin) {
        this.idasin = idasin;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(Date updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    @XmlTransient
    public Collection<Books> getBooksCollection() {
        return booksCollection;
    }

    public void setBooksCollection(Collection<Books> booksCollection) {
        this.booksCollection = booksCollection;
    }

    @XmlTransient
    public Collection<BrowsenodesAsin> getBrowsenodesAsinCollection() {
        return browsenodesAsinCollection;
    }

    public void setBrowsenodesAsinCollection(Collection<BrowsenodesAsin> browsenodesAsinCollection) {
        this.browsenodesAsinCollection = browsenodesAsinCollection;
    }

    @XmlTransient
    public Collection<Notaccessibleitems> getNotaccessibleitemsCollection() {
        return notaccessibleitemsCollection;
    }

    public void setNotaccessibleitemsCollection(Collection<Notaccessibleitems> notaccessibleitemsCollection) {
        this.notaccessibleitemsCollection = notaccessibleitemsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idasin != null ? idasin.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Asin)) {
            return false;
        }
        Asin other = (Asin) object;
        if ((this.idasin == null && other.idasin != null) || (this.idasin != null && !this.idasin.equals(other.idasin))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bla.Asin[ idasin=" + idasin + " ]";
    }
    
}
