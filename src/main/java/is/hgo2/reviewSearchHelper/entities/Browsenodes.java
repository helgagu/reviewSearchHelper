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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "browsenodes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Browsenodes.findAll", query = "SELECT b FROM Browsenodes b"),
    @NamedQuery(name = "Browsenodes.findByIdbrowsenodes", query = "SELECT b FROM Browsenodes b WHERE b.browsenodesPK.idbrowsenodes = :idbrowsenodes"),
    @NamedQuery(name = "Browsenodes.findByBinName", query = "SELECT b FROM Browsenodes b WHERE b.binName = :binName"),
    @NamedQuery(name = "Browsenodes.findByBinItemCount", query = "SELECT b FROM Browsenodes b WHERE b.binItemCount = :binItemCount"),
    @NamedQuery(name = "Browsenodes.findByBrowseNodeId", query = "SELECT b FROM Browsenodes b WHERE b.browseNodeId = :browseNodeId"),
    @NamedQuery(name = "Browsenodes.findByTimestamp", query = "SELECT b FROM Browsenodes b WHERE b.timestamp = :timestamp"),
    @NamedQuery(name = "Browsenodes.findByUpdatedTimestamp", query = "SELECT b FROM Browsenodes b WHERE b.updatedTimestamp = :updatedTimestamp"),
    @NamedQuery(name = "Browsenodes.findByIdbinsearchResults", query = "SELECT b FROM Browsenodes b WHERE b.browsenodesPK.idbinsearchResults = :idbinsearchResults")})
public class Browsenodes implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BrowsenodesPK browsenodesPK;
    @Basic(optional = false)
    @Column(name = "binName")
    private String binName;
    @Basic(optional = false)
    @Column(name = "BinItemCount")
    private long binItemCount;
    @Basic(optional = false)
    @Column(name = "BrowseNodeId")
    private String browseNodeId;
    @Basic(optional = false)
    @Column(name = "Timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Column(name = "UpdatedTimestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTimestamp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "browsenodes")
    private Collection<BrowsenodesAsin> browsenodesAsinCollection;
    @JoinColumn(name = "idbinsearch_results", referencedColumnName = "idbinsearch_results", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private BinsearchResults binsearchResults;

    public Browsenodes() {
    }

    public Browsenodes(BrowsenodesPK browsenodesPK) {
        this.browsenodesPK = browsenodesPK;
    }

    public Browsenodes(BrowsenodesPK browsenodesPK, String binName, long binItemCount, String browseNodeId, Date timestamp) {
        this.browsenodesPK = browsenodesPK;
        this.binName = binName;
        this.binItemCount = binItemCount;
        this.browseNodeId = browseNodeId;
        this.timestamp = timestamp;
    }

    public Browsenodes(int idbrowsenodes, int idbinsearchResults) {
        this.browsenodesPK = new BrowsenodesPK(idbrowsenodes, idbinsearchResults);
    }

    public BrowsenodesPK getBrowsenodesPK() {
        return browsenodesPK;
    }

    public void setBrowsenodesPK(BrowsenodesPK browsenodesPK) {
        this.browsenodesPK = browsenodesPK;
    }

    public String getBinName() {
        return binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }

    public long getBinItemCount() {
        return binItemCount;
    }

    public void setBinItemCount(long binItemCount) {
        this.binItemCount = binItemCount;
    }

    public String getBrowseNodeId() {
        return browseNodeId;
    }

    public void setBrowseNodeId(String browseNodeId) {
        this.browseNodeId = browseNodeId;
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
    public Collection<BrowsenodesAsin> getBrowsenodesAsinCollection() {
        return browsenodesAsinCollection;
    }

    public void setBrowsenodesAsinCollection(Collection<BrowsenodesAsin> browsenodesAsinCollection) {
        this.browsenodesAsinCollection = browsenodesAsinCollection;
    }

    public BinsearchResults getBinsearchResults() {
        return binsearchResults;
    }

    public void setBinsearchResults(BinsearchResults binsearchResults) {
        this.binsearchResults = binsearchResults;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (browsenodesPK != null ? browsenodesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Browsenodes)) {
            return false;
        }
        Browsenodes other = (Browsenodes) object;
        if ((this.browsenodesPK == null && other.browsenodesPK != null) || (this.browsenodesPK != null && !this.browsenodesPK.equals(other.browsenodesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "reviewsearchhelperentity.Browsenodes[ browsenodesPK=" + browsenodesPK + " ]";
    }
    
}
