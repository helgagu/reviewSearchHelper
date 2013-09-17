package is.hgo2.reviewSearchHelper.entityManagers;

import is.hgo2.reviewSearchHelper.entities.Asin;
import is.hgo2.reviewSearchHelper.entities.Books;
import is.hgo2.reviewSearchHelper.entities.Editorialreviews;
import is.hgo2.reviewSearchHelper.util.Constants;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;

/**
 * Class to work with the editorialreviews database objects, insert and fetch data
 */
public class EditorialreviewsEntityManager {

    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction trx;

    /**
     * Constructor that initializes the entityManagerFactory, entityManager and EntityTransaction
     */
    public EditorialreviewsEntityManager(){
        this.emf = Persistence.createEntityManagerFactory(Constants.PERSISTANCE_PROVIDER);
        this.em = emf.createEntityManager();
        this.trx = em.getTransaction();
    }

    /**
     * create editorialreview object
     * @param editorialreviewsMsg the value of the editorialreviews
     * @param books the book that the editorialreview is for
     * @param originalResponse the amazon xml response
     * @param source the source of the editorialreview
     * @return editorialreview object with values
     */
    public Editorialreviews editorialreviews(byte[] editorialreviewsMsg,
                                             Books books,
                                             byte[] originalResponse,
                                             String source){
        Editorialreviews editorialreviews = new Editorialreviews();
        editorialreviews.setTimestamp(new Date());
        editorialreviews.setEditorialReview(editorialreviewsMsg);
        editorialreviews.setIdbooks(books);
        editorialreviews.setOriginalResponse(originalResponse);
        editorialreviews.setSource(source);
        return editorialreviews;
    }

    /**
     * Persist a list of editorialreview objects to database
     * @param editorialreviewses list of editorialreview objects to persist
     */
    public void persist(List<Editorialreviews> editorialreviewses){

        try{
            trx.begin();
            for(Editorialreviews row: editorialreviewses){
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
