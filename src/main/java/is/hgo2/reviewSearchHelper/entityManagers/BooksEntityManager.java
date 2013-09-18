package is.hgo2.reviewSearchHelper.entityManagers;

import is.hgo2.reviewSearchHelper.entities.Asin;
import is.hgo2.reviewSearchHelper.entities.Books;
import is.hgo2.reviewSearchHelper.util.Constants;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.validation.ConstraintViolationException;
import java.math.BigInteger;
import java.util.Date;

/**
 * This is a class to work with the books entity object. Insert into the books table and fetch data from it.
 * @author Helga Gudrun Oskarsdottir
 */
public class BooksEntityManager {

    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction trx;

    /**
     * Constructor that initializes the entityManagerFactory, entityManager and EntityTransaction
     */
    public BooksEntityManager(){
        this.emf = Persistence.createEntityManagerFactory(Constants.PERSISTANCE_PROVIDER);
        this.em = emf.createEntityManager();
        this.trx = em.getTransaction();
    }

    /**
     * Creates books object
     * @param title title of book
     * @param binding binding of book
     * @param detailUrl url to amazon detail page
     * @param edition book edition
     * @param eisbn ecommerce isbn
     * @param asin amazon standard item number
     * @param isbn isbn
     * @param manufacturer book manufacturer
     * @param pages number of pages in book
     * @param publicationDate date of publication
     * @param publisher name of publisher
     * @param salesrank place in amazons best selling books list
     * @param originalResponse the original xml response
     * @param authors name of authors
     * @return books object with values
     */
    public Books books(String title,
                       String binding,
                       String detailUrl,
                       String edition,
                       String eisbn,
                       Asin asin,
                       String isbn,
                       String manufacturer,
                       BigInteger pages,
                       String publicationDate,
                       String publisher,
                       BigInteger salesrank,
                       byte[] originalResponse,
                       String authors,
                       String endpoint){
        Books books = new Books();
        books.setTimestamp(new Date());
        books.setOriginalResponse(originalResponse);
        books.setAuthors(authors);
        books.setBinding(binding);
        books.setDetailPageUrl(detailUrl);
        books.setEdition(edition);
        books.setEisbn(eisbn);
        books.setIdasin(asin);
        books.setIsbn(isbn);
        books.setManufacturer(manufacturer);
        books.setPages(pages);
        books.setPublicationDate(publicationDate);
        books.setPublisher(publisher);
        books.setSalesrank(salesrank);
        books.setTitle(title);
        books.setAmazonLocale(endpoint);
        return books;
    }

    /**
     * Persist book object to database
     * @param book book object to persist
     */
    public void persist(Books book){

        try{
            trx.begin();
            em.persist(book);
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
