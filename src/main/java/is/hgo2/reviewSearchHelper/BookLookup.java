package is.hgo2.reviewSearchHelper;

import is.hgo2.reviewSearchHelper.amazonMessages.EditorialReview;
import is.hgo2.reviewSearchHelper.amazonMessages.Item;
import is.hgo2.reviewSearchHelper.amazonMessages.ItemLookupResponse;
import is.hgo2.reviewSearchHelper.amazonMessages.Items;
import is.hgo2.reviewSearchHelper.entities.Asin;
import is.hgo2.reviewSearchHelper.entities.Books;
import is.hgo2.reviewSearchHelper.entityManagers.AsinEntityManager;
import is.hgo2.reviewSearchHelper.entityManagers.BooksEntityManager;
import is.hgo2.reviewSearchHelper.entityManagers.EditorialreviewsEntityManager;
import is.hgo2.reviewSearchHelper.util.Util;

import java.math.BigInteger;
import java.util.List;

/**
 * This class is for the lookup methods of the ASIN to get detail information about the books
 * @author Helga Gudrun Oskarsdottir
 */
public class BookLookup {

    Util util;
    AmazonClient client;

    /**
     * Constructor
     * @throws Exception
     */
    public BookLookup() throws Exception{
         util = new Util();
         client = new AmazonClient(util);
    }

    /**
     * Insert into the books tables the details about each book found in the binSearch
     * @param endpoint the amazon locale e.g. com, co.uk
     * @throws Exception
     */
    public void setAllBooks(String endpoint) throws Exception{
        AsinEntityManager aem = new AsinEntityManager();
        List<Asin> asins = aem.getAll();

        for(Asin asin: asins){
            ItemLookupResponse response = client.sendItemLookupRequest(asin.getAsin(), endpoint);

            Books book = setBook(response, asin, endpoint);
            setEditorialReviews(book, response);

        }

    }

    /**
     * Insert into editorialReviews the reviews for the book
     * @param book the book object which the reviews are for
     * @param response the response object
     * @throws Exception
     */
    private void setEditorialReviews(Books book, ItemLookupResponse response) throws Exception{
        EditorialreviewsEntityManager erem = new EditorialreviewsEntityManager();

        for(Items items : response.getItems()){
            for(Item item : items.getItem()){
                for(EditorialReview editorialItem : item.getEditorialReviews().getEditorialReview()){
                      erem.persist(erem.editorialreviews(editorialItem.getContent().getBytes(), book, util.unmarshalResponse(response), editorialItem.getSource()));
                }
            }
        }
    }

    /**
     * Insert into books the detail for a specific book
     * @param response the lookup response
     * @param asin the asin object for the book
     * @return  books object with values
     * @throws Exception
     */
    private Books setBook(ItemLookupResponse response, Asin asin, String endpoint) throws Exception{
        BooksEntityManager bem = new BooksEntityManager();

        String title = "";
        String binding = "";
        String detailUrl = "";
        String edition = "";
        String eisbn = "";
        String isbn = "";
        String manufacturer = "";
        BigInteger pages = BigInteger.ZERO;
        String publicationDate = "";
        String publisher = "";
        BigInteger salesrank = BigInteger.ZERO;
        String authors = "";


        for(Items items : response.getItems()){
            for(Item item : items.getItem()){
                detailUrl = item.getDetailPageURL();
                title = item.getItemAttributes().getTitle();
                binding = item.getItemAttributes().getBinding();
                edition = item.getItemAttributes().getEdition();

                StringBuilder sbEisbn = new StringBuilder();
                for(String eisbnItem : item.getItemAttributes().getEISBN()){
                    sbEisbn.append(eisbnItem);
                    sbEisbn.append(":");
                }
                eisbn = sbEisbn.toString();
                isbn = item.getItemAttributes().getISBN();
                manufacturer = item.getItemAttributes().getManufacturer();
                pages = item.getItemAttributes().getNumberOfPages();
                publicationDate = item.getItemAttributes().getPublicationDate();
                publisher = item.getItemAttributes().getPublisher();
                if(item.getSalesRank() == null){
                    salesrank = BigInteger.valueOf(Long.valueOf(item.getSalesRank()));
                }

                StringBuilder sbAuthors = new StringBuilder();
                for(String author : item.getItemAttributes().getAuthor()){
                      sbAuthors.append(author);
                      sbAuthors.append(":");
                }
                authors = sbAuthors.toString();
            }
        }

        Books book = bem.books(title, binding, detailUrl, edition, eisbn, asin, isbn, manufacturer, pages, publicationDate, publisher, salesrank, util.unmarshalResponse(response), authors, endpoint);
        bem.persist(book);
        return book;
    }
}

