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
public class BooksPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idbooks")
    private int idbooks;
    @Basic(optional = false)
    @Column(name = "idasin")
    private int idasin;

    public BooksPK() {
    }

    public BooksPK(int idbooks, int idasin) {
        this.idbooks = idbooks;
        this.idasin = idasin;
    }

    public int getIdbooks() {
        return idbooks;
    }

    public void setIdbooks(int idbooks) {
        this.idbooks = idbooks;
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
        hash += (int) idbooks;
        hash += (int) idasin;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BooksPK)) {
            return false;
        }
        BooksPK other = (BooksPK) object;
        if (this.idbooks != other.idbooks) {
            return false;
        }
        if (this.idasin != other.idasin) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "reviewsearchhelperentity.BooksPK[ idbooks=" + idbooks + ", idasin=" + idasin + " ]";
    }
    
}
