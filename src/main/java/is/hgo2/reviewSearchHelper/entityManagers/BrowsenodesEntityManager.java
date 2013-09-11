package is.hgo2.reviewSearchHelper.entityManagers;

import is.hgo2.reviewSearchHelper.entities.Asin;
import is.hgo2.reviewSearchHelper.entities.BinsearchResults;
import is.hgo2.reviewSearchHelper.entities.Browsenodes;
import is.hgo2.reviewSearchHelper.util.Constants;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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
                                   BinsearchResults binsearchResults){
        Browsenodes browsenodes = new Browsenodes();
        browsenodes.setTimestamp(new Date());
        browsenodes.setBinItemCount(binItemCount);
        browsenodes.setBinName(binName);
        browsenodes.setBrowseNodeId(browseNodeId);
        browsenodes.setIdbinsearchResults(binsearchResults);
        return browsenodes;
    }

    /**
     * Persist a list of browsenodes objects to database
     * @param browsenodeses list of browsenodes objects to persist
     */
    public void persist(List<Browsenodes> browsenodeses){

        try{
            trx.begin();
            for(Browsenodes row: browsenodeses){
                em.persist(row);
            }
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
