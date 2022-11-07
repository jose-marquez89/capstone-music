package sample.utility;

import sample.dao.DBConnector;
import sample.dao.Query;

import javax.xml.transform.Result;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DisplayLocations {
    private static ArrayList<Country> countries = new ArrayList<Country>();
    private static ArrayList<Division> divisions = new ArrayList<Division>();

    public static void initializeLocations() throws SQLException {
        int countryId, divisionId;
        String countryName, divisionName;
        ResultSet countryResults, divisionResults;
        String countriesQuery = """
                SELECT country_id, country 
                FROM countries; 
                """;
        String divisionQuery = """
               SELECT division_id, division, country_id 
               FROM first_level_divisions;
               """;

        DBConnector.connect();

        Query.runQuery(countriesQuery);
        countryResults = Query.getResults();

        Query.runQuery(divisionQuery);
        divisionResults = Query.getResults();

        while (countryResults.next()) {
            countryId = countryResults.getInt("country_id");
            countryName = countryResults.getString("country");
            System.out.println("Adding country " + countryName);
            countries.add(new Country(countryId, countryName));
        }

        while (divisionResults.next()) {
            divisionId = divisionResults.getInt("division_id");
            countryId = divisionResults.getInt("country_id");
            divisionName = divisionResults.getString("division");

            divisions.add(new Division(divisionId, countryId, divisionName));
        }
    }

    public static ArrayList<Country> getCountries() {
        return countries;
    }

    public static ArrayList<Division> getDivisions() {
        return divisions;
    }


}
