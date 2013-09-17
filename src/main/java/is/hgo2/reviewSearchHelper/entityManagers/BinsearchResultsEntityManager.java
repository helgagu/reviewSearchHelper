package is.hgo2.reviewSearchHelper.entityManagers;

import is.hgo2.reviewSearchHelper.entities.BinsearchResults;
import is.hgo2.reviewSearchHelper.util.Constants;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;

/**
 * This is a class to work with the binsearchResults entity object. Insert into the binsearchResults table and fetch data from it.
 * @author Helga Gudrun Oskarsdottir
 */
public class BinsearchResultsEntityManager {

    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction trx;

    /**
     * Constructor that initializes the entityManagerFactory, entityManager and EntitiyTransaction
     */
    public BinsearchResultsEntityManager(){
        this.emf = Persistence.createEntityManagerFactory(Constants.PERSISTANCE_PROVIDER);
        this.em = emf.createEntityManager();
        this.trx = em.getTransaction();
    }

    /**
     * Creates a binsearchResult object
     * @param amazonLocale the endpoint of the amazon url e.g. com, co.uk, (not null)
     * @param keyword the search keyword (not null)
     * @param originalResponse the original response xml from amazon
     * @param availability search parameter availability, has the value available if restricting to only available books otherwise null
     * @param browseNodeId search parameter browseNodeId if used
     * @param merchantId search parameter merchantId, value = amazon , books only sold by amazon, not third party sellers
     * @param powerSearch search parameter power search string, in the format; keyword:%keyword and language:english
     * @param responseGroup search parameter responseGroup, refers to what response we expect from amazon
     * @param searchIndex search parameter searchIndex = books, narrows the search by product type
     * @param sort search parameter sort, sort = relevancerank : by how many times mentioned in description, title etc...
     * @param totalResults number of books in the search results for the search parameters (not null)
     * @param totalPages number of pages of 10 results which are available, it is only possible to access pages 1-10  (not null)
     * @param requestTimestamp
     * @return
     */
    public BinsearchResults binsearchResults(String amazonLocale,
                                             String keyword,
                                             byte[] originalResponse,
                                             String availability,
                                             String browseNodeId,
                                             String merchantId,
                                             String powerSearch,
                                             String responseGroup,
                                             String searchIndex,
                                             String sort,
                                             Long totalResults,
                                             Long totalPages,
                                             String itemPage,
                                             String requestTimestamp){
        BinsearchResults bin = new BinsearchResults();
        bin.setTimestamp(new Date());
        bin.setAmazonLocale(amazonLocale);
        bin.setKeyword(keyword);
        bin.setOriginalResponse(originalResponse);
        bin.setRequestTimestamp(requestTimestamp);
        bin.setSearchParamsAvailability(availability);
        bin.setSearchParamsItemPage(itemPage);
        bin.setSearchParamsBrowseNodeId(browseNodeId);
        bin.setSearchParamsMerchantId(merchantId);
        bin.setSearchParamsPowerSearch(powerSearch);
        bin.setSearchParamsResponseGroup(responseGroup);
        bin.setSearchParamsSearchIndex(searchIndex);
        bin.setSearchParamsSort(sort);
        bin.setTotalPages(totalPages);
        bin.setTotalResults(totalResults);
        return bin;
    }

    /**
     * Persist a binsearchResult objects to database
     * @param binsearchResults binsearchResult object to persist
     */
    public void persist(BinsearchResults binsearchResults){

        try{
            trx.begin();
            em.persist(binsearchResults);
            trx.commit();
            em.close();
            emf.close();
        } catch (ConstraintViolationException c) {
            System.out.println("******************************************* Database error");
            System.out.println(c.getConstraintViolations());
            System.out.println("******************************************* Database error");
            throw c;
        }

    }

}
