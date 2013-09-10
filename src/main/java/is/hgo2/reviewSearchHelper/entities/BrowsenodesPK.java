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
public class BrowsenodesPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idbrowsenodes")
    private int idbrowsenodes;
    @Basic(optional = false)
    @Column(name = "idbinsearch_results")
    private int idbinsearchResults;

    public BrowsenodesPK() {
    }

    public BrowsenodesPK(int idbrowsenodes, int idbinsearchResults) {
        this.idbrowsenodes = idbrowsenodes;
        this.idbinsearchResults = idbinsearchResults;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idbrowsenodes;
        hash += (int) idbinsearchResults;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BrowsenodesPK)) {
            return false;
        }
        BrowsenodesPK other = (BrowsenodesPK) object;
        if (this.idbrowsenodes != other.idbrowsenodes) {
            return false;
        }
        if (this.idbinsearchResults != other.idbinsearchResults) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "reviewsearchhelperentity.BrowsenodesPK[ idbrowsenodes=" + idbrowsenodes + ", idbinsearchResults=" + idbinsearchResults + " ]";
    }
    
}
