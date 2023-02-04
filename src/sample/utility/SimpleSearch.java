package sample.utility;

import sample.model.Customer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SimpleSearch {
    private static class CustomerRank {
        private Customer customer;
        private int rank;

        public CustomerRank(Customer customer) {
            this.customer = customer;
            this.rank = 0;
        }

        public Customer getCustomer() {
            return customer;
        }

        public String getCustomerName() {
            return customer.getName();
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int adjustedRank) {
            rank = adjustedRank;
        }
    }

    private static class customerRankComparator implements Comparator<CustomerRank> {

        public int compare(CustomerRank cr1, CustomerRank cr2) {
            // reverse the comparator so that closest matches come out on top
            if (cr1.getRank() == cr2.getRank())
                return 0;
            else if (cr1.getRank() > cr2.getRank())
                return -1;
            else
                return 1;
        }
    }

    public static ArrayList<Customer> searchCustomer(ArrayList<Customer> customers, String searchTerm) {
        // this algorithm would be quite slow with larger datasets
        ArrayList<CustomerRank> customerRanks = new ArrayList<CustomerRank>();
        ArrayList<Customer> output = new ArrayList<Customer>();
        int counter;
        String termLower, custLower;
        String[] termSplit;

        termLower = searchTerm.toLowerCase();
        termSplit = termLower.split(" ");

        for (Customer c : customers) {
            customerRanks.add(new CustomerRank(c));
        }

        for (CustomerRank cr : customerRanks) {
            counter = 0;
            custLower = cr.getCustomerName().toLowerCase();
            String[] custSplit = custLower.split(" ");

            for (String term : termSplit) {
                for (String custTerm : custSplit) {
                    if (custTerm.contains(term)) {
                        cr.setRank(++counter);
                    }
                }
            }
        }

        // sort by ranking
        Collections.sort(customerRanks, new customerRankComparator());

        for (CustomerRank cr : customerRanks) {
            if (cr.rank > 0) {
                output.add(cr.getCustomer());
            }
        }

        return output;
    }
}
