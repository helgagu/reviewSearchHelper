package is.hgo2.reviewSearchHelper.entityManagers;

import is.hgo2.reviewSearchHelper.entities.Excludedbrowsenodes;
import is.hgo2.reviewSearchHelper.util.Constants;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

/**
 * This is a class to work with the excludedBrowseNodes entity object. Fetch data from it.
 * @author Helga Gudrun Oskarsdottir
 */
public class ExcludedbrowsenodesEntityManager {

    private EntityManager em;

    /**
     * Constructor that initializes the entityManagerFactory, entityManager and EntityTransaction
     */
    public ExcludedbrowsenodesEntityManager(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(Constants.PERSISTANCE_PROVIDER);
        this.em = emf.createEntityManager();
    }

    /**
     * Get asin object from database by the field ASIN
     * @param browseNodeId amazon standard item number
     * @return Excludedbrowsenodes object for the browseNodeId
     */
    public Excludedbrowsenodes getExcludedBrowseNode(String browseNodeId){
        try{
            return (Excludedbrowsenodes) em.createNamedQuery("Excludedbrowsenodes.findByBrowseNodeId").setParameter("browseNodeId", browseNodeId).getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

}
