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
@Table(name = "editorialreviews")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Editorialreviews.findAll", query = "SELECT e FROM Editorialreviews e"),
    @NamedQuery(name = "Editorialreviews.findByIdeditorialReviews", query = "SELECT e FROM Editorialreviews e WHERE e.ideditorialReviews = :ideditorialReviews"),
    @NamedQuery(name = "Editorialreviews.findBySource", query = "SELECT e FROM Editorialreviews e WHERE e.source = :source"),
    @NamedQuery(name = "Editorialreviews.findByTimestamp", query = "SELECT e FROM Editorialreviews e WHERE e.timestamp = :timestamp"),
    @NamedQuery(name = "Editorialreviews.findByUpdatedTimestamp", query = "SELECT e FROM Editorialreviews e WHERE e.updatedTimestamp = :updatedTimestamp")})
public class Editorialreviews implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ideditorialReviews")
    private Integer ideditorialReviews;
    @Lob
    @Column(name = "editorialReview")
    private byte[] editorialReview;
    @Column(name = "source")
    private String source;
    @Basic(optional = false)
    @Lob
    @Column(name = "originalResponse")
    private byte[] originalResponse;
    @Basic(optional = false)
    @Column(name = "Timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Column(name = "UpdatedTimestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTimestamp;
    @JoinColumn(name = "idbooks", referencedColumnName = "idbooks")
    @ManyToOne(optional = false)
    private Books idbooks;

    public Editorialreviews() {
    }

    public Editorialreviews(Integer ideditorialReviews) {
        this.ideditorialReviews = ideditorialReviews;
    }

    public Editorialreviews(Integer ideditorialReviews, byte[] originalResponse, Date timestamp) {
        this.ideditorialReviews = ideditorialReviews;
        this.originalResponse = originalResponse;
        this.timestamp = timestamp;
    }

    public Integer getIdeditorialReviews() {
        return ideditorialReviews;
    }

    public void setIdeditorialReviews(Integer ideditorialReviews) {
        this.ideditorialReviews = ideditorialReviews;
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

    public byte[] getOriginalResponse() {
        return originalResponse;
    }

    public void setOriginalResponse(byte[] originalResponse) {
        this.originalResponse = originalResponse;
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

    public Books getIdbooks() {
        return idbooks;
    }

    public void setIdbooks(Books idbooks) {
        this.idbooks = idbooks;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideditorialReviews != null ? ideditorialReviews.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Editorialreviews)) {
            return false;
        }
        Editorialreviews other = (Editorialreviews) object;
        if ((this.ideditorialReviews == null && other.ideditorialReviews != null) || (this.ideditorialReviews != null && !this.ideditorialReviews.equals(other.ideditorialReviews))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitites.Editorialreviews[ ideditorialReviews=" + ideditorialReviews + " ]";
    }
    
}
