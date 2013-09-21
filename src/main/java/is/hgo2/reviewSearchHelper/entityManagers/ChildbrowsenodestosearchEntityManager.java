package is.hgo2.reviewSearchHelper.entityManagers;

import is.hgo2.reviewSearchHelper.entities.BinsearchResults;
import is.hgo2.reviewSearchHelper.entities.Browsenodes;
import is.hgo2.reviewSearchHelper.entities.Childbrowsenodestosearch;
import is.hgo2.reviewSearchHelper.util.Constants;

import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * This is a class to work with the childbrowsenodestosearch database object, insert and fetch objects
 * @author Helga Gudrun Oskarsdottir
 */
public class ChildbrowsenodestosearchEntityManager {

    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction trx;

    /**
     * Constructor that initializes the entityManagerFactory, entityManager and EntityTransaction
     */
    public ChildbrowsenodestosearchEntityManager(){
        this.emf = Persistence.createEntityManagerFactory(Constants.PERSISTANCE_PROVIDER);
        this.em = emf.createEntityManager();
        this.trx = em.getTransaction();
    }


    public Childbrowsenodestosearch childbrowsenodestosearch(Long binItemCount,
                                                             String binName,
                                                             String browseNodeId,
                                                             String parentBrowseNodes,
                                                             String endpoint,
                                                             String keyword,
                                                             Browsenodes bn){

        Childbrowsenodestosearch item = new Childbrowsenodestosearch();
        item.setBinItemCount(BigInteger.valueOf(binItemCount));
        item.setBinName(binName);
        item.setBrowseNodeId(browseNodeId);
        item.setParentBrowseNode(parentBrowseNodes);
        item.setEndpoint(endpoint);
        item.setIdbrowsenodes(bn);
        item.setKeyword(keyword);
        item.setTimestamp(new Date());
        return item;
    }

    public void setResultFetched(Childbrowsenodestosearch item){
        item.setResultsFetched(Constants.RESULTS_FETCHED);
        try{
            trx.begin();
            em.merge(item);
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

    /**
     * Persist a childbrowsenodestosearch object to database
     * @param item childbrowsenodestosearch object to persist
     */
    public void persist(Childbrowsenodestosearch item){

        try{
            trx.begin();
            em.persist(item);
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

    /**
     * Get childbrowsenodestosearch object from database by the field browsenodesId
     * @param browsenodesId browsenodesId
     * @param keyword the search keyword in use
     * @param endpoint the amazon locale
     * @return Childbrowsenodestosearch object for the browsenodesId, keyword and endpoint
     */
    public Childbrowsenodestosearch getChildbrowsenodestosearch(String browsenodesId, String keyword, String endpoint){
        try{
            Childbrowsenodestosearch item = (Childbrowsenodestosearch) em.createNamedQuery("Childbrowsenodestosearch.findByBrowseNodeIdKeywordEndpoint")
                    .setParameter("browseNodeId", browsenodesId)
                    .setParameter("keyword", keyword)
                    .setParameter("endpoint", endpoint).getSingleResult();
            return item;
        } catch (NoResultException e){
            return null;
        }
    }

    /**
     * Get childbrowsetosearch object that have not been marked as result fetched.
     * @return  list of childbrowsenodestosearch object to search for results with.
     */
    public List<Childbrowsenodestosearch> getAllNotResultFetched(){
        try{
            List<Childbrowsenodestosearch> item = (List<Childbrowsenodestosearch>) em.createNamedQuery("Childbrowsenodestosearch.findByResultsFetched").setParameter("resultsFetched", 0).getResultList();
            return item;
        } catch (NoResultException e){
            return null;
        }

    }

}
