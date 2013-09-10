/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package is.hgo2.reviewSearchHelper.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Helga
 */
@Entity
@Table(name = "notaccessibleitems")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Notaccessibleitems.findAll", query = "SELECT n FROM Notaccessibleitems n"),
    @NamedQuery(name = "Notaccessibleitems.findByIdnotAccessibleItems", query = "SELECT n FROM Notaccessibleitems n WHERE n.notaccessibleitemsPK.idnotAccessibleItems = :idnotAccessibleItems"),
    @NamedQuery(name = "Notaccessibleitems.findByAsinIdasin", query = "SELECT n FROM Notaccessibleitems n WHERE n.notaccessibleitemsPK.asinIdasin = :asinIdasin"),
    @NamedQuery(name = "Notaccessibleitems.findByTimestamp", query = "SELECT n FROM Notaccessibleitems n WHERE n.timestamp = :timestamp"),
    @NamedQuery(name = "Notaccessibleitems.findByUpdatedTimestamp", query = "SELECT n FROM Notaccessibleitems n WHERE n.updatedTimestamp = :updatedTimestamp")})
public class Notaccessibleitems implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NotaccessibleitemsPK notaccessibleitemsPK;
    @Basic(optional = false)
    @Column(name = "Timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Column(name = "UpdatedTimestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTimestamp;
    @JoinColumn(name = "asin_idasin", referencedColumnName = "idasin", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Asin asin;

    public Notaccessibleitems() {
    }

    public Notaccessibleitems(NotaccessibleitemsPK notaccessibleitemsPK) {
        this.notaccessibleitemsPK = notaccessibleitemsPK;
    }

    public Notaccessibleitems(NotaccessibleitemsPK notaccessibleitemsPK, Date timestamp) {
        this.notaccessibleitemsPK = notaccessibleitemsPK;
        this.timestamp = timestamp;
    }

    public Notaccessibleitems(int idnotAccessibleItems, int asinIdasin) {
        this.notaccessibleitemsPK = new NotaccessibleitemsPK(idnotAccessibleItems, asinIdasin);
    }

    public NotaccessibleitemsPK getNotaccessibleitemsPK() {
        return notaccessibleitemsPK;
    }

    public void setNotaccessibleitemsPK(NotaccessibleitemsPK notaccessibleitemsPK) {
        this.notaccessibleitemsPK = notaccessibleitemsPK;
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

    public Asin getAsin() {
        return asin;
    }

    public void setAsin(Asin asin) {
        this.asin = asin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notaccessibleitemsPK != null ? notaccessibleitemsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notaccessibleitems)) {
            return false;
        }
        Notaccessibleitems other = (Notaccessibleitems) object;
        if ((this.notaccessibleitemsPK == null && other.notaccessibleitemsPK != null) || (this.notaccessibleitemsPK != null && !this.notaccessibleitemsPK.equals(other.notaccessibleitemsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "reviewsearchhelperentity.Notaccessibleitems[ notaccessibleitemsPK=" + notaccessibleitemsPK + " ]";
    }
    
}
