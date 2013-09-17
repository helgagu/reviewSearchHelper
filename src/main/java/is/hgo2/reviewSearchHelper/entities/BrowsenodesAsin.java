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
@Table(name = "browsenodes_asin")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BrowsenodesAsin.findAll", query = "SELECT b FROM BrowsenodesAsin b"),
    @NamedQuery(name = "BrowsenodesAsin.findByIdbrowsenodesAsin", query = "SELECT b FROM BrowsenodesAsin b WHERE b.idbrowsenodesAsin = :idbrowsenodesAsin"),
    @NamedQuery(name = "BrowsenodesAsin.findByAsin", query = "SELECT b FROM BrowsenodesAsin b WHERE b.asin = :asin"),
    @NamedQuery(name = "BrowsenodesAsin.findByTimestamp", query = "SELECT b FROM BrowsenodesAsin b WHERE b.timestamp = :timestamp"),
    @NamedQuery(name = "BrowsenodesAsin.findByUpdatedTimestamp", query = "SELECT b FROM BrowsenodesAsin b WHERE b.updatedTimestamp = :updatedTimestamp")})
public class BrowsenodesAsin implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idbrowsenodes_asin")
    private Integer idbrowsenodesAsin;
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
    @JoinColumn(name = "binsearch_results_idbinsearch_results", referencedColumnName = "idbinsearch_results")
    @ManyToOne(optional = false)
    private BinsearchResults binsearchResultsIdbinsearchResults;
    @JoinColumn(name = "idbrowsenodes", referencedColumnName = "idbrowsenodes")
    @ManyToOne(optional = false)
    private Browsenodes idbrowsenodes;
    @JoinColumn(name = "idasin", referencedColumnName = "idasin")
    @ManyToOne(optional = false)
    private Asin idasin;

    public BrowsenodesAsin() {
    }

    public BrowsenodesAsin(Integer idbrowsenodesAsin) {
        this.idbrowsenodesAsin = idbrowsenodesAsin;
    }

    public BrowsenodesAsin(Integer idbrowsenodesAsin, String asin, Date timestamp) {
        this.idbrowsenodesAsin = idbrowsenodesAsin;
        this.asin = asin;
        this.timestamp = timestamp;
    }

    public Integer getIdbrowsenodesAsin() {
        return idbrowsenodesAsin;
    }

    public void setIdbrowsenodesAsin(Integer idbrowsenodesAsin) {
        this.idbrowsenodesAsin = idbrowsenodesAsin;
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

    public BinsearchResults getBinsearchResultsIdbinsearchResults() {
        return binsearchResultsIdbinsearchResults;
    }

    public void setBinsearchResultsIdbinsearchResults(BinsearchResults binsearchResultsIdbinsearchResults) {
        this.binsearchResultsIdbinsearchResults = binsearchResultsIdbinsearchResults;
    }

    public Browsenodes getIdbrowsenodes() {
        return idbrowsenodes;
    }

    public void setIdbrowsenodes(Browsenodes idbrowsenodes) {
        this.idbrowsenodes = idbrowsenodes;
    }

    public Asin getIdasin() {
        return idasin;
    }

    public void setIdasin(Asin idasin) {
        this.idasin = idasin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idbrowsenodesAsin != null ? idbrowsenodesAsin.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BrowsenodesAsin)) {
            return false;
        }
        BrowsenodesAsin other = (BrowsenodesAsin) object;
        if ((this.idbrowsenodesAsin == null && other.idbrowsenodesAsin != null) || (this.idbrowsenodesAsin != null && !this.idbrowsenodesAsin.equals(other.idbrowsenodesAsin))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitites.BrowsenodesAsin[ idbrowsenodesAsin=" + idbrowsenodesAsin + " ]";
    }
    
}
