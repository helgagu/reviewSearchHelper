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
public class NotaccessibleitemsPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idnotAccessibleItems")
    private int idnotAccessibleItems;
    @Basic(optional = false)
    @Column(name = "asin_idasin")
    private int asinIdasin;

    public NotaccessibleitemsPK() {
    }

    public NotaccessibleitemsPK(int idnotAccessibleItems, int asinIdasin) {
        this.idnotAccessibleItems = idnotAccessibleItems;
        this.asinIdasin = asinIdasin;
    }

    public int getIdnotAccessibleItems() {
        return idnotAccessibleItems;
    }

    public void setIdnotAccessibleItems(int idnotAccessibleItems) {
        this.idnotAccessibleItems = idnotAccessibleItems;
    }

    public int getAsinIdasin() {
        return asinIdasin;
    }

    public void setAsinIdasin(int asinIdasin) {
        this.asinIdasin = asinIdasin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idnotAccessibleItems;
        hash += (int) asinIdasin;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotaccessibleitemsPK)) {
            return false;
        }
        NotaccessibleitemsPK other = (NotaccessibleitemsPK) object;
        if (this.idnotAccessibleItems != other.idnotAccessibleItems) {
            return false;
        }
        if (this.asinIdasin != other.asinIdasin) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "reviewsearchhelperentity.NotaccessibleitemsPK[ idnotAccessibleItems=" + idnotAccessibleItems + ", asinIdasin=" + asinIdasin + " ]";
    }
    
}
