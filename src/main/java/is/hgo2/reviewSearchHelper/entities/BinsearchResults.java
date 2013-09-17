/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package is.hgo2.reviewSearchHelper.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Helga
 */
@Entity
@Table(name = "binsearch_results")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BinsearchResults.findAll", query = "SELECT b FROM BinsearchResults b"),
    @NamedQuery(name = "BinsearchResults.findByIdbinsearchResults", query = "SELECT b FROM BinsearchResults b WHERE b.idbinsearchResults = :idbinsearchResults"),
    @NamedQuery(name = "BinsearchResults.findByKeyword", query = "SELECT b FROM BinsearchResults b WHERE b.keyword = :keyword"),
    @NamedQuery(name = "BinsearchResults.findByTotalResults", query = "SELECT b FROM BinsearchResults b WHERE b.totalResults = :totalResults"),
    @NamedQuery(name = "BinsearchResults.findByTotalPages", query = "SELECT b FROM BinsearchResults b WHERE b.totalPages = :totalPages"),
    @NamedQuery(name = "BinsearchResults.findByAmazonLocale", query = "SELECT b FROM BinsearchResults b WHERE b.amazonLocale = :amazonLocale"),
    @NamedQuery(name = "BinsearchResults.findBySearchParamsBrowseNodeId", query = "SELECT b FROM BinsearchResults b WHERE b.searchParamsBrowseNodeId = :searchParamsBrowseNodeId"),
    @NamedQuery(name = "BinsearchResults.findBySearchParamsItemPage", query = "SELECT b FROM BinsearchResults b WHERE b.searchParamsItemPage = :searchParamsItemPage"),
    @NamedQuery(name = "BinsearchResults.findBySearchParamsSearchIndex", query = "SELECT b FROM BinsearchResults b WHERE b.searchParamsSearchIndex = :searchParamsSearchIndex"),
    @NamedQuery(name = "BinsearchResults.findBySearchParamsPowerSearch", query = "SELECT b FROM BinsearchResults b WHERE b.searchParamsPowerSearch = :searchParamsPowerSearch"),
    @NamedQuery(name = "BinsearchResults.findBySearchParamsSort", query = "SELECT b FROM BinsearchResults b WHERE b.searchParamsSort = :searchParamsSort"),
    @NamedQuery(name = "BinsearchResults.findBySearchParamsMerchantId", query = "SELECT b FROM BinsearchResults b WHERE b.searchParamsMerchantId = :searchParamsMerchantId"),
    @NamedQuery(name = "BinsearchResults.findBySearchParamsAvailability", query = "SELECT b FROM BinsearchResults b WHERE b.searchParamsAvailability = :searchParamsAvailability"),
    @NamedQuery(name = "BinsearchResults.findBySearchParamsResponseGroup", query = "SELECT b FROM BinsearchResults b WHERE b.searchParamsResponseGroup = :searchParamsResponseGroup"),
    @NamedQuery(name = "BinsearchResults.findByRequestTimestamp", query = "SELECT b FROM BinsearchResults b WHERE b.requestTimestamp = :requestTimestamp"),
    @NamedQuery(name = "BinsearchResults.findByTimestamp", query = "SELECT b FROM BinsearchResults b WHERE b.timestamp = :timestamp"),
    @NamedQuery(name = "BinsearchResults.findByUpdatedTimestamp", query = "SELECT b FROM BinsearchResults b WHERE b.updatedTimestamp = :updatedTimestamp")})
public class BinsearchResults implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idbinsearch_results")
    private Integer idbinsearchResults;
    @Basic(optional = false)
    @Column(name = "Keyword")
    private String keyword;
    @Basic(optional = false)
    @Column(name = "TotalResults")
    private long totalResults;
    @Basic(optional = false)
    @Column(name = "TotalPages")
    private long totalPages;
    @Basic(optional = false)
    @Column(name = "AmazonLocale")
    private String amazonLocale;
    @Column(name = "SearchParams_BrowseNodeId")
    private String searchParamsBrowseNodeId;
    @Column(name = "SearchParams_ItemPage")
    private String searchParamsItemPage;
    @Column(name = "SearchParams_SearchIndex")
    private String searchParamsSearchIndex;
    @Column(name = "SearchParams_PowerSearch")
    private String searchParamsPowerSearch;
    @Column(name = "SearchParams_Sort")
    private String searchParamsSort;
    @Column(name = "SearchParams_MerchantId")
    private String searchParamsMerchantId;
    @Column(name = "SearchParams_Availability")
    private String searchParamsAvailability;
    @Column(name = "SearchParams_ResponseGroup")
    private String searchParamsResponseGroup;
    @Column(name = "RequestTimestamp")
    private String requestTimestamp;
    @Basic(optional = false)
    @Lob
    @Column(name = "OriginalResponse")
    private byte[] originalResponse;
    @Basic(optional = false)
    @Column(name = "Timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Column(name = "UpdatedTimestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTimestamp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "binsearchResultsIdbinsearchResults")
    private Collection<BrowsenodesAsin> browsenodesAsinCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idbinsearchResults")
    private Collection<Browsenodes> browsenodesCollection;

    public BinsearchResults() {
    }

    public BinsearchResults(Integer idbinsearchResults) {
        this.idbinsearchResults = idbinsearchResults;
    }

    public BinsearchResults(Integer idbinsearchResults, String keyword, long totalResults, long totalPages, String amazonLocale, byte[] originalResponse, Date timestamp) {
        this.idbinsearchResults = idbinsearchResults;
        this.keyword = keyword;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
        this.amazonLocale = amazonLocale;
        this.originalResponse = originalResponse;
        this.timestamp = timestamp;
    }

    public Integer getIdbinsearchResults() {
        return idbinsearchResults;
    }

    public void setIdbinsearchResults(Integer idbinsearchResults) {
        this.idbinsearchResults = idbinsearchResults;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public String getAmazonLocale() {
        return amazonLocale;
    }

    public void setAmazonLocale(String amazonLocale) {
        this.amazonLocale = amazonLocale;
    }

    public String getSearchParamsBrowseNodeId() {
        return searchParamsBrowseNodeId;
    }

    public void setSearchParamsBrowseNodeId(String searchParamsBrowseNodeId) {
        this.searchParamsBrowseNodeId = searchParamsBrowseNodeId;
    }

    public String getSearchParamsItemPage() {
        return searchParamsItemPage;
    }

    public void setSearchParamsItemPage(String searchParamsItemPage) {
        this.searchParamsItemPage = searchParamsItemPage;
    }

    public String getSearchParamsSearchIndex() {
        return searchParamsSearchIndex;
    }

    public void setSearchParamsSearchIndex(String searchParamsSearchIndex) {
        this.searchParamsSearchIndex = searchParamsSearchIndex;
    }

    public String getSearchParamsPowerSearch() {
        return searchParamsPowerSearch;
    }

    public void setSearchParamsPowerSearch(String searchParamsPowerSearch) {
        this.searchParamsPowerSearch = searchParamsPowerSearch;
    }

    public String getSearchParamsSort() {
        return searchParamsSort;
    }

    public void setSearchParamsSort(String searchParamsSort) {
        this.searchParamsSort = searchParamsSort;
    }

    public String getSearchParamsMerchantId() {
        return searchParamsMerchantId;
    }

    public void setSearchParamsMerchantId(String searchParamsMerchantId) {
        this.searchParamsMerchantId = searchParamsMerchantId;
    }

    public String getSearchParamsAvailability() {
        return searchParamsAvailability;
    }

    public void setSearchParamsAvailability(String searchParamsAvailability) {
        this.searchParamsAvailability = searchParamsAvailability;
    }

    public String getSearchParamsResponseGroup() {
        return searchParamsResponseGroup;
    }

    public void setSearchParamsResponseGroup(String searchParamsResponseGroup) {
        this.searchParamsResponseGroup = searchParamsResponseGroup;
    }

    public String getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(String requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
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

    @XmlTransient
    public Collection<BrowsenodesAsin> getBrowsenodesAsinCollection() {
        return browsenodesAsinCollection;
    }

    public void setBrowsenodesAsinCollection(Collection<BrowsenodesAsin> browsenodesAsinCollection) {
        this.browsenodesAsinCollection = browsenodesAsinCollection;
    }

    @XmlTransient
    public Collection<Browsenodes> getBrowsenodesCollection() {
        return browsenodesCollection;
    }

    public void setBrowsenodesCollection(Collection<Browsenodes> browsenodesCollection) {
        this.browsenodesCollection = browsenodesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idbinsearchResults != null ? idbinsearchResults.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BinsearchResults)) {
            return false;
        }
        BinsearchResults other = (BinsearchResults) object;
        if ((this.idbinsearchResults == null && other.idbinsearchResults != null) || (this.idbinsearchResults != null && !this.idbinsearchResults.equals(other.idbinsearchResults))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitites.BinsearchResults[ idbinsearchResults=" + idbinsearchResults + " ]";
    }
    
}
