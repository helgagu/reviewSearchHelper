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
import javax.persistence.Lob;
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
@Table(name = "books")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Books.findAll", query = "SELECT b FROM Books b"),
    @NamedQuery(name = "Books.findByIdbooks", query = "SELECT b FROM Books b WHERE b.idbooks = :idbooks"),
    @NamedQuery(name = "Books.findByIsbn", query = "SELECT b FROM Books b WHERE b.isbn = :isbn"),
    @NamedQuery(name = "Books.findByTitle", query = "SELECT b FROM Books b WHERE b.title = :title"),
    @NamedQuery(name = "Books.findByAuthors", query = "SELECT b FROM Books b WHERE b.authors = :authors"),
    @NamedQuery(name = "Books.findByPages", query = "SELECT b FROM Books b WHERE b.pages = :pages"),
    @NamedQuery(name = "Books.findBySalesrank", query = "SELECT b FROM Books b WHERE b.salesrank = :salesrank"),
    @NamedQuery(name = "Books.findByEdition", query = "SELECT b FROM Books b WHERE b.edition = :edition"),
    @NamedQuery(name = "Books.findByManufacturer", query = "SELECT b FROM Books b WHERE b.manufacturer = :manufacturer"),
    @NamedQuery(name = "Books.findByPublisher", query = "SELECT b FROM Books b WHERE b.publisher = :publisher"),
    @NamedQuery(name = "Books.findByPublicationDate", query = "SELECT b FROM Books b WHERE b.publicationDate = :publicationDate"),
    @NamedQuery(name = "Books.findByDetailPageUrl", query = "SELECT b FROM Books b WHERE b.detailPageUrl = :detailPageUrl"),
    @NamedQuery(name = "Books.findByEisbn", query = "SELECT b FROM Books b WHERE b.eisbn = :eisbn"),
    @NamedQuery(name = "Books.findByBinding", query = "SELECT b FROM Books b WHERE b.binding = :binding"),
    @NamedQuery(name = "Books.findByTimestamp", query = "SELECT b FROM Books b WHERE b.timestamp = :timestamp"),
    @NamedQuery(name = "Books.findByUpdatedTimestamp", query = "SELECT b FROM Books b WHERE b.updatedTimestamp = :updatedTimestamp")})
public class Books implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idbooks")
    private Integer idbooks;
    @Basic(optional = false)
    @Column(name = "isbn")
    private String isbn;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @Column(name = "authors")
    private String authors;
    @Column(name = "pages")
    private BigInteger pages;
    @Column(name = "salesrank")
    private String salesrank;
    @Column(name = "edition")
    private String edition;
    @Column(name = "manufacturer")
    private String manufacturer;
    @Column(name = "publisher")
    private String publisher;
    @Column(name = "publicationDate")
    private String publicationDate;
    @Column(name = "detailPageUrl")
    private String detailPageUrl;
    @Column(name = "eisbn")
    private String eisbn;
    @Column(name = "binding")
    private String binding;
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
    @JoinColumn(name = "idasin", referencedColumnName = "idasin")
    @ManyToOne(optional = false)
    private Asin idasin;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idbooks")
    private Collection<Editorialreviews> editorialreviewsCollection;

    public Books() {
    }

    public Books(Integer idbooks) {
        this.idbooks = idbooks;
    }

    public Books(Integer idbooks, String isbn, String title, String authors, byte[] originalRequest, Date timestamp) {
        this.idbooks = idbooks;
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.originalRequest = originalRequest;
        this.timestamp = timestamp;
    }

    public Integer getIdbooks() {
        return idbooks;
    }

    public void setIdbooks(Integer idbooks) {
        this.idbooks = idbooks;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public BigInteger getPages() {
        return pages;
    }

    public void setPages(BigInteger pages) {
        this.pages = pages;
    }

    public String getSalesrank() {
        return salesrank;
    }

    public void setSalesrank(String salesrank) {
        this.salesrank = salesrank;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getDetailPageUrl() {
        return detailPageUrl;
    }

    public void setDetailPageUrl(String detailPageUrl) {
        this.detailPageUrl = detailPageUrl;
    }

    public String getEisbn() {
        return eisbn;
    }

    public void setEisbn(String eisbn) {
        this.eisbn = eisbn;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
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

    public Asin getIdasin() {
        return idasin;
    }

    public void setIdasin(Asin idasin) {
        this.idasin = idasin;
    }

    @XmlTransient
    public Collection<Editorialreviews> getEditorialreviewsCollection() {
        return editorialreviewsCollection;
    }

    public void setEditorialreviewsCollection(Collection<Editorialreviews> editorialreviewsCollection) {
        this.editorialreviewsCollection = editorialreviewsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idbooks != null ? idbooks.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Books)) {
            return false;
        }
        Books other = (Books) object;
        if ((this.idbooks == null && other.idbooks != null) || (this.idbooks != null && !this.idbooks.equals(other.idbooks))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bla.Books[ idbooks=" + idbooks + " ]";
    }
    
}
