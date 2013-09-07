package is.hgo2.reviewSearchHelper;

import static is.hgo2.reviewSearchHelper.Constants.*;

/**
 * Here is the logic for exclusion criteria.
 *
 * @author Helga
 */
public class ExclusionCriteria {

    /**
     * Exclusion criteria of subject categories which were manually analyzed to be out of scope and decided were not to be included in the search.
     *
     * Manual analyzation execution:
     * - full list of categories which search results for each keyword relate to was produced with method:
     * - the csv file saved:
     * - the csv file manually analyzed and categories which were industry specific were marked to be excluded
     * - category names to be excluded added to this method
     *
     * @param BinName the name of the bin to compare to the exclusion criteria
     * @return true=the bin should be excluded, false=the bin should be included
     */
    public static Boolean excludeBrowseNodeId(String BinName){

        if(BinName.equalsIgnoreCase(HEALTHFITNESSDIETING_BROWSENODE)){
            return Boolean.TRUE;
        }
        if(BinName.equalsIgnoreCase(MEDICALBOOKS_BROWSENODE)){
            return Boolean.TRUE;
        }
        if(BinName.equalsIgnoreCase(RELIGIONSPIRITUALITY_BROWSENODE)){
            return Boolean.TRUE;
        }
        if(BinName.equalsIgnoreCase(ARTPHOTOGRAPHY_BROWSENODE)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
