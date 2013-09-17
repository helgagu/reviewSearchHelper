package is.hgo2.reviewSearchHelper.util;

import is.hgo2.reviewSearchHelper.entities.Excludedbrowsenodes;
import is.hgo2.reviewSearchHelper.entityManagers.ExcludedbrowsenodesEntityManager;

/**
 * Here is the logic for exclusion criteria.
 *
 * @author Helga
 */
public class ExclusionCriteria {

    /**
     * Exclusion criteria of subject categories which were manually analyzed to be out of scope and decided were not to be included in the search. <p><p>
     *
     * Manual analyzation execution:   <p>
     * - full list of categories which search results for each keyword relate to was produced with method: BinSearch.getAllBrowseNodes(%keyword, Constants.ENDPOINT_US, null); <p>
     * - data extracted and the csv file saved    <p>
     * - the csv file manually analyzed and categories which were industry specific, history, tool specific, religion specific were marked to be excluded  <p>
     * - excluded browsenodes added to table excludedbrowsenodes  <p>
     *
     * @param browseNodesId the browseNodesId to compare to the exclusion criteria
     * @return true=the bin should be excluded, false=the bin should be included
     */
    public static String excludeBrowseNodeId(String browseNodesId){

        ExcludedbrowsenodesEntityManager ebnEm = new ExcludedbrowsenodesEntityManager();
        Excludedbrowsenodes ebn = ebnEm.getExcludedBrowseNode(browseNodesId);

        if(ebn != null){
            return ebn.getExclusionReason();
        }
        return null;
    }
}
