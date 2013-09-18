/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package is.hgo2.reviewSearchHelper.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Helga
 */
@Entity
@Table(name = "excludedbrowsenodes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Excludedbrowsenodes.findAll", query = "SELECT e FROM Excludedbrowsenodes e"),
    @NamedQuery(name = "Excludedbrowsenodes.findByIdexcludedBrowseNodes", query = "SELECT e FROM Excludedbrowsenodes e WHERE e.idexcludedBrowseNodes = :idexcludedBrowseNodes"),
    @NamedQuery(name = "Excludedbrowsenodes.findByBrowseNodeId", query = "SELECT e FROM Excludedbrowsenodes e WHERE e.browseNodeId = :browseNodeId"),
    @NamedQuery(name = "Excludedbrowsenodes.findByExclusionReason", query = "SELECT e FROM Excludedbrowsenodes e WHERE e.exclusionReason = :exclusionReason")})
public class Excludedbrowsenodes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idexcludedBrowseNodes")
    private Integer idexcludedBrowseNodes;
    @Basic(optional = false)
    @Column(name = "browseNodeId")
    private String browseNodeId;
    @Basic(optional = false)
    @Column(name = "exclusionReason")
    private String exclusionReason;

    public Excludedbrowsenodes() {
    }

    public Excludedbrowsenodes(Integer idexcludedBrowseNodes) {
        this.idexcludedBrowseNodes = idexcludedBrowseNodes;
    }

    public Excludedbrowsenodes(Integer idexcludedBrowseNodes, String browseNodeId, String exclusionReason) {
        this.idexcludedBrowseNodes = idexcludedBrowseNodes;
        this.browseNodeId = browseNodeId;
        this.exclusionReason = exclusionReason;
    }

    public Integer getIdexcludedBrowseNodes() {
        return idexcludedBrowseNodes;
    }

    public void setIdexcludedBrowseNodes(Integer idexcludedBrowseNodes) {
        this.idexcludedBrowseNodes = idexcludedBrowseNodes;
    }

    public String getBrowseNodeId() {
        return browseNodeId;
    }

    public void setBrowseNodeId(String browseNodeId) {
        this.browseNodeId = browseNodeId;
    }

    public String getExclusionReason() {
        return exclusionReason;
    }

    public void setExclusionReason(String exclusionReason) {
        this.exclusionReason = exclusionReason;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idexcludedBrowseNodes != null ? idexcludedBrowseNodes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Excludedbrowsenodes)) {
            return false;
        }
        Excludedbrowsenodes other = (Excludedbrowsenodes) object;
        if ((this.idexcludedBrowseNodes == null && other.idexcludedBrowseNodes != null) || (this.idexcludedBrowseNodes != null && !this.idexcludedBrowseNodes.equals(other.idexcludedBrowseNodes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitites.Excludedbrowsenodes[ idexcludedBrowseNodes=" + idexcludedBrowseNodes + " ]";
    }
    
}
