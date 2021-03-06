/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package is.hgo2.reviewSearchHelper.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    @NamedQuery(name = "Browsenodes.findByIdbrowsenodes", query = "SELECT b FROM Browsenodes b WHERE b.idbrowsenodes = :idbrowsenodes"),
    @NamedQuery(name = "Browsenodes.findByBinName", query = "SELECT b FROM Browsenodes b WHERE b.binName = :binName"),
    @NamedQuery(name = "Browsenodes.findByBinItemCount", query = "SELECT b FROM Browsenodes b WHERE b.binItemCount = :binItemCount"),
    @NamedQuery(name = "Browsenodes.findByBrowseNodeId", query = "SELECT b FROM Browsenodes b WHERE b.browseNodeId = :browseNodeId"),
    @NamedQuery(name = "Browsenodes.findByParentBrowseNode", query = "SELECT b FROM Browsenodes b WHERE b.parentBrowseNode = :parentBrowseNode"),
    @NamedQuery(name = "Browsenodes.findByKeyword", query = "SELECT b FROM Browsenodes b WHERE b.keyword = :keyword"),
    @NamedQuery(name = "Browsenodes.findByExclusionReason", query = "SELECT b FROM Browsenodes b WHERE b.exclusionReason = :exclusionReason"),
    @NamedQuery(name = "Browsenodes.findByTimestamp", query = "SELECT b FROM Browsenodes b WHERE b.timestamp = :timestamp"),
    @NamedQuery(name = "Browsenodes.findByUpdatedTimestamp", query = "SELECT b FROM Browsenodes b WHERE b.updatedTimestamp = :updatedTimestamp")})
    @NamedQuery(name = "Browsenodes.findByBrowseNodeIdKeyword", query = "SELECT b FROM Browsenodes b WHERE b.browseNodeId = :browseNodeId and b.keyword = :keyword")
public class Browsenodes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idbrowsenodes")
    private Integer idbrowsenodes;
    @Basic(optional = false)
    @Column(name = "binName")
    private String binName;
    @Basic(optional = false)
    @Column(name = "BinItemCount")
    private long binItemCount;
    @Basic(optional = false)
    @Column(name = "BrowseNodeId")
    private String browseNodeId;
    @Column(name = "parentBrowseNode")
    private String parentBrowseNode;
    @Basic(optional = false)
    @Column(name = "keyword")
    private String keyword;
    @Column(name = "exclusionReason")
    private String exclusionReason;
    @Basic(optional = false)
    @Column(name = "Timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Column(name = "UpdatedTimestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTimestamp;
    @OneToMany(mappedBy = "idbrowsenodes")
    private Collection<Childbrowsenodestosearch> childbrowsenodestosearchCollection;
    @JoinColumn(name = "idbinsearch_results", referencedColumnName = "idbinsearch_results")
    @ManyToOne(optional = false)
    private BinsearchResults idbinsearchResults;

    public Browsenodes() {
    }

    public Browsenodes(Integer idbrowsenodes) {
        this.idbrowsenodes = idbrowsenodes;
    }

    public Browsenodes(Integer idbrowsenodes, String binName, long binItemCount, String browseNodeId, String keyword, Date timestamp) {
        this.idbrowsenodes = idbrowsenodes;
        this.binName = binName;
        this.binItemCount = binItemCount;
        this.browseNodeId = browseNodeId;
        this.keyword = keyword;
        this.timestamp = timestamp;
    }

    public Integer getIdbrowsenodes() {
        return idbrowsenodes;
    }

    public void setIdbrowsenodes(Integer idbrowsenodes) {
        this.idbrowsenodes = idbrowsenodes;
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

    public String getParentBrowseNode() {
        return parentBrowseNode;
    }

    public void setParentBrowseNode(String parentBrowseNode) {
        this.parentBrowseNode = parentBrowseNode;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getExclusionReason() {
        return exclusionReason;
    }

    public void setExclusionReason(String exclusionReason) {
        this.exclusionReason = exclusionReason;
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
    public Collection<Childbrowsenodestosearch> getChildbrowsenodestosearchCollection() {
        return childbrowsenodestosearchCollection;
    }

    public void setChildbrowsenodestosearchCollection(Collection<Childbrowsenodestosearch> childbrowsenodestosearchCollection) {
        this.childbrowsenodestosearchCollection = childbrowsenodestosearchCollection;
    }

    public BinsearchResults getIdbinsearchResults() {
        return idbinsearchResults;
    }

    public void setIdbinsearchResults(BinsearchResults idbinsearchResults) {
        this.idbinsearchResults = idbinsearchResults;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idbrowsenodes != null ? idbrowsenodes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Browsenodes)) {
            return false;
        }
        Browsenodes other = (Browsenodes) object;
        if ((this.idbrowsenodes == null && other.idbrowsenodes != null) || (this.idbrowsenodes != null && !this.idbrowsenodes.equals(other.idbrowsenodes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitites.Browsenodes[ idbrowsenodes=" + idbrowsenodes + " ]";
    }
    
}
