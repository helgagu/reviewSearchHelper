package is.hgo2.reviewSearchHelper.entityManagers;

import is.hgo2.reviewSearchHelper.entities.Asin;
import is.hgo2.reviewSearchHelper.entities.Excludedbrowsenodes;
import is.hgo2.reviewSearchHelper.util.Constants;

import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import java.util.Date;

/**
 * This is a class to work with the excludedBrowseNodes entity object. Fetch data from it.
 * @author Helga Gudrun Oskarsdottir
 */
public class ExcludedbrowsenodesEntityManager {

    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction trx;

    /**
     * Constructor that initializes the entityManagerFactory, entityManager and EntityTransaction
     */
    public ExcludedbrowsenodesEntityManager(){
        this.emf = Persistence.createEntityManagerFactory(Constants.PERSISTANCE_PROVIDER);
        this.em = emf.createEntityManager();
        this.trx = em.getTransaction();
    }

    /**
     * Get asin object from database by the field ASIN
     * @param browseNodeId amazon standard item number
     * @return Excludedbrowsenodes object for the browseNodeId
     */
    public Excludedbrowsenodes getExcludedBrowseNode(String browseNodeId){
        try{
            Excludedbrowsenodes ebn = (Excludedbrowsenodes) em.createNamedQuery("Excludedbrowsenodes.findByBrowseNodeId").setParameter("browseNodeId", browseNodeId).getSingleResult();
            return ebn;
        } catch (NoResultException e){
            return null;
        }
    }

}
