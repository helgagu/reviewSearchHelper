package is.hgo2.reviewSearchHelper.entityManagers;

import is.hgo2.reviewSearchHelper.entities.BinsearchResults;
import is.hgo2.reviewSearchHelper.entities.Browsenodes;
import is.hgo2.reviewSearchHelper.util.Constants;

import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;

/**
 * This is a class to work with the browsenodes database object, insert and fetch objects
 * @author Helga Gudrun Oskarsdottir
 */
public class BrowsenodesEntityManager {

    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction trx;

    /**
     * Constructor that initializes the entityManagerFactory, entityManager and EntityTransaction
     */
    public BrowsenodesEntityManager(){
        this.emf = Persistence.createEntityManagerFactory(Constants.PERSISTANCE_PROVIDER);
        this.em = emf.createEntityManager();
        this.trx = em.getTransaction();
    }

    /**
     * Creates a browsenodes object
     * @param binItemCount the amount of results in the bin
     * @param binName the name of the bin
     * @param browseNodeId the browseNodeId of the bin
     * @param binsearchResults the binsearchResults where the browsenodes were extracted
     * @return browsenodes object with values
     */
    public Browsenodes browsenodes(Long binItemCount,
                                   String binName,
                                   String browseNodeId,
                                   String parentBrowseNodes,
                                   String exclusionReason,
                                   String keyword,
                                   BinsearchResults binsearchResults){
        Browsenodes browsenodes = new Browsenodes();
        browsenodes.setTimestamp(new Date());
        browsenodes.setBinItemCount(binItemCount);
        browsenodes.setBinName(binName);
        browsenodes.setBrowseNodeId(browseNodeId);
        browsenodes.setParentBrowseNode(parentBrowseNodes);
        browsenodes.setExclusionReason(exclusionReason);
        browsenodes.setKeyword(keyword);
        browsenodes.setIdbinsearchResults(binsearchResults);
        return browsenodes;
    }

    /**
     * Persist a browsenodes object to database
     * @param browsenodes browsenodes object to persist
     */
    public void persist(Browsenodes browsenodes){

        try{
            trx.begin();
            em.persist(browsenodes);
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
     * Get Browsenodes object from database by the field browsenodesId and binItemCount
     * @param browsenodesId browsenodesId
     * @param keyword the keyword used in the search
     * @return Childbrowsenodestosearch object for the browsenodesId, keyword and endpoint
     */
    public Browsenodes getBrowseNode(String browsenodesId, String keyword){
        try{
            Browsenodes item = (Browsenodes) em.createNamedQuery("Browsenodes.findByBrowseNodeIdKeyword")
                    .setParameter("browseNodeId", browsenodesId)
                    .setParameter("keyword", keyword).getSingleResult();
            return item;
        } catch (NoResultException e){
            return null;
        } catch (NonUniqueResultException ue){
            List<Browsenodes> firstResult = (List<Browsenodes>) em.createNamedQuery("Browsenodes.findByBrowseNodeIdKeyword")
                    .setParameter("browseNodeId", browsenodesId)
                    .setParameter("keyword", keyword).getResultList();
            return firstResult.get(0);
        }
    }


}
