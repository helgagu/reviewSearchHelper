package is.hgo2.reviewSearchHelper.entityManagers;

import is.hgo2.reviewSearchHelper.entities.Asin;
import is.hgo2.reviewSearchHelper.entities.Notaccessibleitems;
import is.hgo2.reviewSearchHelper.util.Constants;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.validation.ConstraintViolationException;
import java.util.Date;

/**
 * Class to work with the notaccessibleitems table, insert and fetch data
 */
public class NotaccessibleitemsEntitiyManager {

    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction trx;

    /**
     * Constructor that initializes the entityManagerFactory, entityManager and EntityTransaction
     */
    public NotaccessibleitemsEntitiyManager(){
        this.emf = Persistence.createEntityManagerFactory(Constants.PERSISTANCE_PROVIDER);
        this.em = emf.createEntityManager();
        this.trx = em.getTransaction();
    }

    /**
     * Creates the notaccessibleitems insert object
     * @param asin the asin object
     * @return notaccessibleitems object with asin value and timestamp
     */
    public Notaccessibleitems notaccessibleitems(Asin asin){
        Notaccessibleitems notaccessibleitems = new Notaccessibleitems();
        notaccessibleitems.setTimestamp(new Date());
        notaccessibleitems.setAsinIdasin(asin);
        return notaccessibleitems;
    }

    /**
     * Persist notaccessibleitem object to database
     * @param notaccessibleitems notaccessibleitem object to persist
     */
    public void persist(Notaccessibleitems notaccessibleitems){

        try{
            trx.begin();
            em.persist(notaccessibleitems);
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
