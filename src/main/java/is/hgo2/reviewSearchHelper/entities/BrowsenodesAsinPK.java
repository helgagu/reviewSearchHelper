/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package is.hgo2.reviewSearchHelper.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Helga
 */
@Embeddable
public class BrowsenodesAsinPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idbrowsenodes_asin")
    private int idbrowsenodesAsin;
    @Basic(optional = false)
    @Column(name = "idbrowsenodes")
    private int idbrowsenodes;
    @Basic(optional = false)
    @Column(name = "idbinsearch_results")
    private int idbinsearchResults;
    @Basic(optional = false)
    @Column(name = "idasin")
    private int idasin;

    public BrowsenodesAsinPK() {
    }

    public BrowsenodesAsinPK(int idbrowsenodesAsin, int idbrowsenodes, int idbinsearchResults, int idasin) {
        this.idbrowsenodesAsin = idbrowsenodesAsin;
        this.idbrowsenodes = idbrowsenodes;
        this.idbinsearchResults = idbinsearchResults;
        this.idasin = idasin;
    }

    public int getIdbrowsenodesAsin() {
        return idbrowsenodesAsin;
    }

    public void setIdbrowsenodesAsin(int idbrowsenodesAsin) {
        this.idbrowsenodesAsin = idbrowsenodesAsin;
    }

    public int getIdbrowsenodes() {
        return idbrowsenodes;
    }

    public void setIdbrowsenodes(int idbrowsenodes) {
        this.idbrowsenodes = idbrowsenodes;
    }

    public int getIdbinsearchResults() {
        return idbinsearchResults;
    }

    public void setIdbinsearchResults(int idbinsearchResults) {
        this.idbinsearchResults = idbinsearchResults;
    }

    public int getIdasin() {
        return idasin;
    }

    public void setIdasin(int idasin) {
        this.idasin = idasin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idbrowsenodesAsin;
        hash += (int) idbrowsenodes;
        hash += (int) idbinsearchResults;
        hash += (int) idasin;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BrowsenodesAsinPK)) {
            return false;
        }
        BrowsenodesAsinPK other = (BrowsenodesAsinPK) object;
        if (this.idbrowsenodesAsin != other.idbrowsenodesAsin) {
            return false;
        }
        if (this.idbrowsenodes != other.idbrowsenodes) {
            return false;
        }
        if (this.idbinsearchResults != other.idbinsearchResults) {
            return false;
        }
        if (this.idasin != other.idasin) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "reviewsearchhelperentity.BrowsenodesAsinPK[ idbrowsenodesAsin=" + idbrowsenodesAsin + ", idbrowsenodes=" + idbrowsenodes + ", idbinsearchResults=" + idbinsearchResults + ", idasin=" + idasin + " ]";
    }
    
}
