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
import javax.persistence.JoinColumns;
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
@Table(name = "browsenodes_asin")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BrowsenodesAsin.findAll", query = "SELECT b FROM BrowsenodesAsin b"),
    @NamedQuery(name = "BrowsenodesAsin.findByIdbrowsenodesAsin", query = "SELECT b FROM BrowsenodesAsin b WHERE b.browsenodesAsinPK.idbrowsenodesAsin = :idbrowsenodesAsin"),
    @NamedQuery(name = "BrowsenodesAsin.findByAsin", query = "SELECT b FROM BrowsenodesAsin b WHERE b.asin = :asin"),
    @NamedQuery(name = "BrowsenodesAsin.findByTimestamp", query = "SELECT b FROM BrowsenodesAsin b WHERE b.timestamp = :timestamp"),
    @NamedQuery(name = "BrowsenodesAsin.findByUpdatedTimestamp", query = "SELECT b FROM BrowsenodesAsin b WHERE b.updatedTimestamp = :updatedTimestamp"),
    @NamedQuery(name = "BrowsenodesAsin.findByIdbrowsenodes", query = "SELECT b FROM BrowsenodesAsin b WHERE b.browsenodesAsinPK.idbrowsenodes = :idbrowsenodes"),
    @NamedQuery(name = "BrowsenodesAsin.findByIdbinsearchResults", query = "SELECT b FROM BrowsenodesAsin b WHERE b.browsenodesAsinPK.idbinsearchResults = :idbinsearchResults"),
    @NamedQuery(name = "BrowsenodesAsin.findByIdasin", query = "SELECT b FROM BrowsenodesAsin b WHERE b.browsenodesAsinPK.idasin = :idasin")})
public class BrowsenodesAsin implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BrowsenodesAsinPK browsenodesAsinPK;
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
    @JoinColumn(name = "idasin", referencedColumnName = "idasin", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Asin asin1;
    @JoinColumns({
        @JoinColumn(name = "idbrowsenodes", referencedColumnName = "idbrowsenodes", insertable = false, updatable = false),
        @JoinColumn(name = "idbinsearch_results", referencedColumnName = "idbinsearch_results", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Browsenodes browsenodes;

    public BrowsenodesAsin() {
    }

    public BrowsenodesAsin(BrowsenodesAsinPK browsenodesAsinPK) {
        this.browsenodesAsinPK = browsenodesAsinPK;
    }

    public BrowsenodesAsin(BrowsenodesAsinPK browsenodesAsinPK, String asin, Date timestamp) {
        this.browsenodesAsinPK = browsenodesAsinPK;
        this.asin = asin;
        this.timestamp = timestamp;
    }

    public BrowsenodesAsin(int idbrowsenodesAsin, int idbrowsenodes, int idbinsearchResults, int idasin) {
        this.browsenodesAsinPK = new BrowsenodesAsinPK(idbrowsenodesAsin, idbrowsenodes, idbinsearchResults, idasin);
    }

    public BrowsenodesAsinPK getBrowsenodesAsinPK() {
        return browsenodesAsinPK;
    }

    public void setBrowsenodesAsinPK(BrowsenodesAsinPK browsenodesAsinPK) {
        this.browsenodesAsinPK = browsenodesAsinPK;
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

    public Asin getAsin1() {
        return asin1;
    }

    public void setAsin1(Asin asin1) {
        this.asin1 = asin1;
    }

    public Browsenodes getBrowsenodes() {
        return browsenodes;
    }

    public void setBrowsenodes(Browsenodes browsenodes) {
        this.browsenodes = browsenodes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (browsenodesAsinPK != null ? browsenodesAsinPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BrowsenodesAsin)) {
            return false;
        }
        BrowsenodesAsin other = (BrowsenodesAsin) object;
        if ((this.browsenodesAsinPK == null && other.browsenodesAsinPK != null) || (this.browsenodesAsinPK != null && !this.browsenodesAsinPK.equals(other.browsenodesAsinPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "reviewsearchhelperentity.BrowsenodesAsin[ browsenodesAsinPK=" + browsenodesAsinPK + " ]";
    }
    
}
