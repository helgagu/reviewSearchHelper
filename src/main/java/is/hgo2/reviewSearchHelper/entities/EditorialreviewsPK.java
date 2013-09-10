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
public class EditorialreviewsPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ideditorialReviews")
    private int ideditorialReviews;
    @Basic(optional = false)
    @Column(name = "idbooks")
    private int idbooks;

    public EditorialreviewsPK() {
    }

    public EditorialreviewsPK(int ideditorialReviews, int idbooks) {
        this.ideditorialReviews = ideditorialReviews;
        this.idbooks = idbooks;
    }

    public int getIdeditorialReviews() {
        return ideditorialReviews;
    }

    public void setIdeditorialReviews(int ideditorialReviews) {
        this.ideditorialReviews = ideditorialReviews;
    }

    public int getIdbooks() {
        return idbooks;
    }

    public void setIdbooks(int idbooks) {
        this.idbooks = idbooks;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideditorialReviews;
        hash += (int) idbooks;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EditorialreviewsPK)) {
            return false;
        }
        EditorialreviewsPK other = (EditorialreviewsPK) object;
        if (this.ideditorialReviews != other.ideditorialReviews) {
            return false;
        }
        if (this.idbooks != other.idbooks) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "reviewsearchhelperentity.EditorialreviewsPK[ ideditorialReviews=" + ideditorialReviews + ", idbooks=" + idbooks + " ]";
    }
    
}
