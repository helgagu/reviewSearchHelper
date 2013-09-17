package is.hgo2.reviewSearchHelper.util;

import static is.hgo2.reviewSearchHelper.util.Constants.*;

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
     * - full list of categories which search results for each keyword relate to was produced with method: BinSearch.getAllBrowseNodes("Productivity", Constants.ENDPOINT_US, null); <p>
     * - the csv file saved:    <p>
     * - the csv file manually analyzed and categories which were industry specific were marked to be excluded  <p>
     * - category names to be excluded added to this method  <p>
     *
     * @param BinName the name of the bin to compare to the exclusion criteria
     * @return true=the bin should be excluded, false=the bin should be included
     */
    public static String excludeBrowseNodeId(String BinName){

        if(BinName.equalsIgnoreCase(HEALTHFITNESSDIETING_BROWSENODE)){
            return INDUSTRY_SPECIFIC_EXCLUSIONREASON;
        }
        if(BinName.equalsIgnoreCase(MEDICALBOOKS_BROWSENODE)){
            return INDUSTRY_SPECIFIC_EXCLUSIONREASON;
        }
        if(BinName.equalsIgnoreCase(RELIGIONSPIRITUALITY_BROWSENODE)){
            return INDUSTRY_SPECIFIC_EXCLUSIONREASON;
        }
        if(BinName.equalsIgnoreCase(ARTPHOTOGRAPHY_BROWSENODE)){
            return INDUSTRY_SPECIFIC_EXCLUSIONREASON;
        }
        return null;
    }
}
