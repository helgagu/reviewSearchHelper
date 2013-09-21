/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package is.hgo2.reviewSearchHelper.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "childbrowsenodestosearch")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Childbrowsenodestosearch.findAll", query = "SELECT c FROM Childbrowsenodestosearch c"),
    @NamedQuery(name = "Childbrowsenodestosearch.findByIdchildBrowseNodesToSearch", query = "SELECT c FROM Childbrowsenodestosearch c WHERE c.idchildBrowseNodesToSearch = :idchildBrowseNodesToSearch"),
    @NamedQuery(name = "Childbrowsenodestosearch.findByBinName", query = "SELECT c FROM Childbrowsenodestosearch c WHERE c.binName = :binName"),
    @NamedQuery(name = "Childbrowsenodestosearch.findByBinItemCount", query = "SELECT c FROM Childbrowsenodestosearch c WHERE c.binItemCount = :binItemCount"),
    @NamedQuery(name = "Childbrowsenodestosearch.findByBrowseNodeId", query = "SELECT c FROM Childbrowsenodestosearch c WHERE c.browseNodeId = :browseNodeId"),
    @NamedQuery(name = "Childbrowsenodestosearch.findByParentBrowseNode", query = "SELECT c FROM Childbrowsenodestosearch c WHERE c.parentBrowseNode = :parentBrowseNode"),
    @NamedQuery(name = "Childbrowsenodestosearch.findByKeyword", query = "SELECT c FROM Childbrowsenodestosearch c WHERE c.keyword = :keyword"),
    @NamedQuery(name = "Childbrowsenodestosearch.findByEndpoint", query = "SELECT c FROM Childbrowsenodestosearch c WHERE c.endpoint = :endpoint"),
    @NamedQuery(name = "Childbrowsenodestosearch.findByResultsFetched", query = "SELECT c FROM Childbrowsenodestosearch c WHERE c.resultsFetched = :resultsFetched"),
    @NamedQuery(name = "Childbrowsenodestosearch.findByTimestamp", query = "SELECT c FROM Childbrowsenodestosearch c WHERE c.timestamp = :timestamp"),
    @NamedQuery(name = "Childbrowsenodestosearch.findByBrowseNodeIdKeywordEndpoint", query = "SELECT c FROM Childbrowsenodestosearch c WHERE c.browseNodeId = :browseNodeId and c.keyword = :keyword and c.endpoint = :endpoint")})
public class Childbrowsenodestosearch implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idchildBrowseNodesToSearch")
    private Integer idchildBrowseNodesToSearch;
    @Column(name = "binName")
    private String binName;
    @Column(name = "binItemCount")
    private BigInteger binItemCount;
    @Basic(optional = false)
    @Column(name = "browseNodeId")
    private String browseNodeId;
    @Column(name = "parentBrowseNode")
    private String parentBrowseNode;
    @Basic(optional = false)
    @Column(name = "keyword")
    private String keyword;
    @Basic(optional = false)
    @Column(name = "endpoint")
    private String endpoint;
    @Basic(optional = false)
    @Column(name = "resultsFetched")
    private int resultsFetched;
    @Column(name = "Timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idchildBrowseNodesToSearch")
    private Collection<BrowsenodesAsin> browsenodesAsinCollection;
    @JoinColumn(name = "idbrowsenodes", referencedColumnName = "idbrowsenodes")
    @ManyToOne
    private Browsenodes idbrowsenodes;

    public Childbrowsenodestosearch() {
    }

    public Childbrowsenodestosearch(Integer idchildBrowseNodesToSearch) {
        this.idchildBrowseNodesToSearch = idchildBrowseNodesToSearch;
    }

    public Childbrowsenodestosearch(Integer idchildBrowseNodesToSearch, String browseNodeId, String keyword, String endpoint, int resultsFetched) {
        this.idchildBrowseNodesToSearch = idchildBrowseNodesToSearch;
        this.browseNodeId = browseNodeId;
        this.keyword = keyword;
        this.endpoint = endpoint;
        this.resultsFetched = resultsFetched;
    }

    public Integer getIdchildBrowseNodesToSearch() {
        return idchildBrowseNodesToSearch;
    }

    public void setIdchildBrowseNodesToSearch(Integer idchildBrowseNodesToSearch) {
        this.idchildBrowseNodesToSearch = idchildBrowseNodesToSearch;
    }

    public String getBinName() {
        return binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }

    public BigInteger getBinItemCount() {
        return binItemCount;
    }

    public void setBinItemCount(BigInteger binItemCount) {
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

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public int getResultsFetched() {
        return resultsFetched;
    }

    public void setResultsFetched(int resultsFetched) {
        this.resultsFetched = resultsFetched;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @XmlTransient
    public Collection<BrowsenodesAsin> getBrowsenodesAsinCollection() {
        return browsenodesAsinCollection;
    }

    public void setBrowsenodesAsinCollection(Collection<BrowsenodesAsin> browsenodesAsinCollection) {
        this.browsenodesAsinCollection = browsenodesAsinCollection;
    }

    public Browsenodes getIdbrowsenodes() {
        return idbrowsenodes;
    }

    public void setIdbrowsenodes(Browsenodes idbrowsenodes) {
        this.idbrowsenodes = idbrowsenodes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idchildBrowseNodesToSearch != null ? idchildBrowseNodesToSearch.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Childbrowsenodestosearch)) {
            return false;
        }
        Childbrowsenodestosearch other = (Childbrowsenodestosearch) object;
        if ((this.idchildBrowseNodesToSearch == null && other.idchildBrowseNodesToSearch != null) || (this.idchildBrowseNodesToSearch != null && !this.idchildBrowseNodesToSearch.equals(other.idchildBrowseNodesToSearch))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitites.Childbrowsenodestosearch[ idchildBrowseNodesToSearch=" + idchildBrowseNodesToSearch + " ]";
    }
    
}
