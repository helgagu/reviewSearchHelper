package is.hgo2.reviewSearchHelper.entityManagers;

import is.hgo2.reviewSearchHelper.entities.Asin;
import is.hgo2.reviewSearchHelper.util.Constants;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Helga
 * Date: 10.9.2013
 * Time: 16:41
 * To change this template use File | Settings | File Templates.
 */
public class AsinEntityManager {

    private  EntityManagerFactory emf;
    private  EntityManager em;
    private  EntityTransaction trx;

    public AsinEntityManager(){
        this.emf = Persistence.createEntityManagerFactory(Constants.PERSISTANCE_PROVIDER);
        this.em = emf.createEntityManager();
        this.trx = em.getTransaction();
    }

    public Asin asin(String asinNumber){
        Asin asin = new Asin();
        asin.setAsin(asinNumber);
        return asin;
    }

    public void persist(List<Asin> asin){

        try{
            trx.begin();
            for(Asin row: asin){
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

    public Asin getAsin(String asinNumber){
        Asin asin = (Asin) em.createNamedQuery("Asin.findByAsin").setParameter("asin", asinNumber).getSingleResult();
        return asin;
    }
}
