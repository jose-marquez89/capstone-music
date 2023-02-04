package sample.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import sample.model.Customer;
import sample.utility.SimpleSearch;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SimpleSearchTest {
    private static ArrayList<Customer> searchCustomers;

    @BeforeAll
    static void setUp() {
        searchCustomers = new ArrayList<Customer>();
        searchCustomers.add(new Customer(0, "John Doe", "jodo@mail.com", "123"));
        searchCustomers.add(new Customer(1, "Jane Doe", "jado@mail.com", "123"));
        searchCustomers.add(new Customer(2, "Michael Jackson", "mj@mail.com", "123"));
        searchCustomers.add(new Customer(3, "Elton John", "ej@mail.com", "123"));
    }

    @Test
    void searchCustomer() {
        ArrayList<Customer> searchResults;
        searchResults = SimpleSearch.searchCustomer(searchCustomers, "Elton John");
        assertEquals(searchResults.get(0), searchCustomers.get(3));

        // 2nd attempt (should contain no results)
        searchResults = SimpleSearch.searchCustomer(searchCustomers, "Elvis");
        assertEquals(searchResults.size(), 0);
    }
}