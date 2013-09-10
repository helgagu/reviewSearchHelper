/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package is.hgo2.reviewSearchHelper.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Helga
 */
@Entity
@Table(name = "editorialreviews")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Editorialreviews.findAll", query = "SELECT e FROM Editorialreviews e"),
    @NamedQuery(name = "Editorialreviews.findByIdeditorialReviews", query = "SELECT e FROM Editorialreviews e WHERE e.editorialreviewsPK.ideditorialReviews = :ideditorialReviews"),
    @NamedQuery(name = "Editorialreviews.findBySource", query = "SELECT e FROM Editorialreviews e WHERE e.source = :source"),
    @NamedQuery(name = "Editorialreviews.findByTimestamp", query = "SELECT e FROM Editorialreviews e WHERE e.timestamp = :timestamp"),
    @NamedQuery(name = "Editorialreviews.findByUpdatedTimestamp", query = "SELECT e FROM Editorialreviews e WHERE e.updatedTimestamp = :updatedTimestamp"),
    @NamedQuery(name = "Editorialreviews.findByIdbooks", query = "SELECT e FROM Editorialreviews e WHERE e.editorialreviewsPK.idbooks = :idbooks")})
public class Editorialreviews implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EditorialreviewsPK editorialreviewsPK;
    @Lob
    @Column(name = "editorialReview")
    private byte[] editorialReview;
    @Column(name = "source")
    private String source;
    @Basic(optional = false)
    @Lob
    @Column(name = "originalRequest")
    private byte[] originalRequest;
    @Basic(optional = false)
    @Column(name = "Timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Column(name = "UpdatedTimestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTimestamp;
    @JoinColumn(name = "idbooks", referencedColumnName = "idbooks", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Books books;

    public Editorialreviews() {
    }

    public Editorialreviews(EditorialreviewsPK editorialreviewsPK) {
        this.editorialreviewsPK = editorialreviewsPK;
    }

    public Editorialreviews(EditorialreviewsPK editorialreviewsPK, byte[] originalRequest, Date timestamp) {
        this.editorialreviewsPK = editorialreviewsPK;
        this.originalRequest = originalRequest;
        this.timestamp = timestamp;
    }

    public Editorialreviews(int ideditorialReviews, int idbooks) {
        this.editorialreviewsPK = new EditorialreviewsPK(ideditorialReviews, idbooks);
    }

    public EditorialreviewsPK getEditorialreviewsPK() {
        return editorialreviewsPK;
    }

    public void setEditorialreviewsPK(EditorialreviewsPK editorialreviewsPK) {
        this.editorialreviewsPK = editorialreviewsPK;
    }

    public byte[] getEditorialReview() {
        return editorialReview;
    }

    public void setEditorialReview(byte[] editorialReview) {
        this.editorialReview = editorialReview;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public byte[] getOriginalRequest() {
        return originalRequest;
    }

    public void setOriginalRequest(byte[] originalRequest) {
        this.originalRequest = originalRequest;
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

    public Books getBooks() {
        return books;
    }

    public void setBooks(Books books) {
        this.books = books;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (editorialreviewsPK != null ? editorialreviewsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Editorialreviews)) {
            return false;
        }
        Editorialreviews other = (Editorialreviews) object;
        if ((this.editorialreviewsPK == null && other.editorialreviewsPK != null) || (this.editorialreviewsPK != null && !this.editorialreviewsPK.equals(other.editorialreviewsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "reviewsearchhelperentity.Editorialreviews[ editorialreviewsPK=" + editorialreviewsPK + " ]";
    }
    
}
