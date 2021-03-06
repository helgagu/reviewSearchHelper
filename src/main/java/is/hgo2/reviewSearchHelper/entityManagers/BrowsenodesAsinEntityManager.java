package is.hgo2.reviewSearchHelper.entityManagers;

import is.hgo2.reviewSearchHelper.entities.*;
import is.hgo2.reviewSearchHelper.util.Constants;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.validation.ConstraintViolationException;
import java.util.Date;

/**
 * Class to work with the browsenodesAsin database object, insert and fetch objects
 */
public class BrowsenodesAsinEntityManager {

    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction trx;

    /**
     * Constructor that initializes the entityManagerFactory, entityManager and EntityTransaction
     */
    public BrowsenodesAsinEntityManager(){
        this.emf = Persistence.createEntityManagerFactory(Constants.PERSISTANCE_PROVIDER);
        this.em = emf.createEntityManager();
        this.trx = em.getTransaction();
    }

    /**
     * Creates the  insert object
     * @param asinNumber the amazon standard item number
     * @return BrowsenodesAsin object with browsenodesAsin value and timestamp
     */
    public BrowsenodesAsin asin(String asinNumber, Asin asin, Childbrowsenodestosearch browsenodes, BinsearchResults bin){
        BrowsenodesAsin browsenodesAsin = new BrowsenodesAsin();
        browsenodesAsin.setAsin(asinNumber);
        browsenodesAsin.setTimestamp(new Date());
        browsenodesAsin.setIdasin(asin);
        browsenodesAsin.setBinsearchResultsIdbinsearchResults(bin);
        browsenodesAsin.setIdchildBrowseNodesToSearch(browsenodes);
        return browsenodesAsin;
    }

    /**
     * Persist a browsenodesAsin object to database
     * @param browsenodesAsin browsenodesAsin objects to persist
     */
    public void persist(BrowsenodesAsin browsenodesAsin){

        try{
            trx.begin();
            em.persist(browsenodesAsin);
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
