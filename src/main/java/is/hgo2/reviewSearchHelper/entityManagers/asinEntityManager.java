package is.hgo2.reviewSearchHelper.entityManagers;

import is.hgo2.reviewSearchHelper.entities.Asin;
import is.hgo2.reviewSearchHelper.util.Constants;

import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import java.util.Date;

/**
 * This is a class to work with the asin entity object. Insert into the asin table and fetch data from it.
 * @author Helga Gudrun Oskarsdottir
 */
public class AsinEntityManager {

    private  EntityManagerFactory emf;
    private  EntityManager em;
    private  EntityTransaction trx;

    /**
     * Constructor that initializes the entityManagerFactory, entityManager and EntityTransaction
     */
    public AsinEntityManager(){
        this.emf = Persistence.createEntityManagerFactory(Constants.PERSISTANCE_PROVIDER);
        this.em = emf.createEntityManager();
        this.trx = em.getTransaction();
    }

    /**
     * Creates the asin insert object
     * @param asinNumber the amazon standard item number
     * @return asin object with asin value and timestamp
     */
    public Asin asin(String asinNumber){
        Asin asin = new Asin();
        asin.setAsin(asinNumber);
        asin.setTimestamp(new Date());
        return asin;
    }

    /**
     * Persist a asin object to database
     * @param asin asin object to persist
     */
    public void persist(Asin asin){

        try{
            trx.begin();
            em.persist(asin);
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
     * Get asin object from database by the field ASIN
     * @param asinNumber amazon standard item number
     * @return asin object for the asinNumber
     */
    public Asin getAsin(String asinNumber){
        try{
            Asin asin = (Asin) em.createNamedQuery("Asin.findByAsin").setParameter("asin", asinNumber).getSingleResult();
            return asin;
        } catch (NoResultException e){
            return null;
        }
    }

}
