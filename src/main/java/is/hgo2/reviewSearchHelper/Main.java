package is.hgo2.reviewSearchHelper;

import is.hgo2.reviewSearchHelper.entities.Asin;
import is.hgo2.reviewSearchHelper.entityManagers.AsinEntityManager;
import is.hgo2.reviewSearchHelper.util.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: Helga
 * Date: 17.9.2013
 * Time: 08:30
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String [] args) throws Exception{

        BinSearch binSearch = new BinSearch();
        binSearch.getAllBrowseNodes("Productivity", Constants.ENDPOINT_US, null);
        binSearch.getAllBrowseNodes("Personal Productivity", Constants.ENDPOINT_US, null);
        binSearch.getAllBrowseNodes("Knowledge Worker Productivity", Constants.ENDPOINT_US, null);
        binSearch.getAllBrowseNodes("Efficient", Constants.ENDPOINT_US, null);
        binSearch.getAllBrowseNodes("Effective*", Constants.ENDPOINT_US, null);

    }
}
