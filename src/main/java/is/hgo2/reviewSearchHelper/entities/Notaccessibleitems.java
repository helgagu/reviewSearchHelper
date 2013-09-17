/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package is.hgo2.reviewSearchHelper.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Helga
 */
@Entity
@Table(name = "notaccessibleitems")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Notaccessibleitems.findAll", query = "SELECT n FROM Notaccessibleitems n"),
    @NamedQuery(name = "Notaccessibleitems.findByIdnotAccessibleItems", query = "SELECT n FROM Notaccessibleitems n WHERE n.idnotAccessibleItems = :idnotAccessibleItems"),
    @NamedQuery(name = "Notaccessibleitems.findByTimestamp", query = "SELECT n FROM Notaccessibleitems n WHERE n.timestamp = :timestamp"),
    @NamedQuery(name = "Notaccessibleitems.findByUpdatedTimestamp", query = "SELECT n FROM Notaccessibleitems n WHERE n.updatedTimestamp = :updatedTimestamp")})
public class Notaccessibleitems implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idnotAccessibleItems")
    private Integer idnotAccessibleItems;
    @Lob
    @Column(name = "originalResponse")
    private byte[] originalResponse;
    @Basic(optional = false)
    @Column(name = "Timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Column(name = "UpdatedTimestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTimestamp;
    @JoinColumn(name = "asin_idasin", referencedColumnName = "idasin")
    @ManyToOne(optional = false)
    private Asin asinIdasin;

    public Notaccessibleitems() {
    }

    public Notaccessibleitems(Integer idnotAccessibleItems) {
        this.idnotAccessibleItems = idnotAccessibleItems;
    }

    public Notaccessibleitems(Integer idnotAccessibleItems, Date timestamp) {
        this.idnotAccessibleItems = idnotAccessibleItems;
        this.timestamp = timestamp;
    }

    public Integer getIdnotAccessibleItems() {
        return idnotAccessibleItems;
    }

    public void setIdnotAccessibleItems(Integer idnotAccessibleItems) {
        this.idnotAccessibleItems = idnotAccessibleItems;
    }

    public byte[] getOriginalResponse() {
        return originalResponse;
    }

    public void setOriginalResponse(byte[] originalResponse) {
        this.originalResponse = originalResponse;
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

    public Asin getAsinIdasin() {
        return asinIdasin;
    }

    public void setAsinIdasin(Asin asinIdasin) {
        this.asinIdasin = asinIdasin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idnotAccessibleItems != null ? idnotAccessibleItems.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notaccessibleitems)) {
            return false;
        }
        Notaccessibleitems other = (Notaccessibleitems) object;
        if ((this.idnotAccessibleItems == null && other.idnotAccessibleItems != null) || (this.idnotAccessibleItems != null && !this.idnotAccessibleItems.equals(other.idnotAccessibleItems))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitites.Notaccessibleitems[ idnotAccessibleItems=" + idnotAccessibleItems + " ]";
    }
    
}
