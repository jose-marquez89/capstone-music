package sample.utility;

/**
 * An extension of the Location class used to model
 * first level divisions from the database.
 *
 * Allows the display of division names while
 * translating to integer ids during selection.
 *
 * @author Jose Marquez
 */
public class Division extends Location {
    private int countryId;

    /**
     * Constructor for the <code>Division</code> class.
     *
     * @param id the integer id for the intended first level division
     * @param countryId the integer id for the intended division country
     * @param name the string name for the intended first level division
     */
    public Division(int id, int countryId, String name) {
        super(id, name);
        this.countryId = countryId;
    }

    public int getCountryId() {
        return countryId;
    }
}
