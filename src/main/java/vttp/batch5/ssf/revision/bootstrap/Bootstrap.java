// Package declaration for this class
package vttp.batch5.ssf.revision.bootstrap;

// Importing necessary classes
import java.io.BufferedReader;   // To read text from a file
import java.io.FileReader;        // To read a file
import java.util.LinkedList;      // To store customers in a list
import java.util.List;            // General-purpose list interface

// Spring annotations to mark this class as a Spring Bean and a command-line runner
import org.springframework.beans.factory.annotation.Value; // To inject property values
import org.springframework.boot.CommandLineRunner;       // To run code at application startup
import org.springframework.stereotype.Component;           // Marks this as a Spring component

// Importing custom model class for Customer
import vttp.batch5.ssf.revision.models.Customer;

@Component  // Marks this class as a Spring Bean, making it eligible for component scanning
public class Bootstrap implements CommandLineRunner {  // Implements CommandLineRunner to run at application startup

   // Injecting values from application.properties
   @Value("${my.api.pass-key}")  // Inject the API key from the properties file
   private String apiKey;

   @Value("${csv.file.path}")    // Inject the file path for the CSV file from the properties file
   private String csvFile;

   // The main method that gets executed when the application starts
   @Override
   public void run(String... args) {

      // Prints the CSV file path to the console for debugging
      System.out.printf(">>>> CSV file path: %s\n", csvFile);

      List<Customer> customers;  // Declare a list to hold customer objects

      try {
         // Try to parse the customers from the CSV file using the withStream method
         customers = withStream(csvFile);

         // Print the list of customers after processing
         System.out.printf(">>> customers: %s\n", customers);
      } catch (Exception ex) {
         // If there's an error, print the stack trace
         ex.printStackTrace();
      }

      // Print the API Key to the console (for debugging purposes)
      System.out.printf(">>> API KEY: %s\n", apiKey);
   }

   // Method to process the CSV file using streams
   public List<Customer> withStream(String file) throws Exception {

      // Using try-with-resources to automatically close the file reader and buffered reader
      try (FileReader fr = new FileReader(file)) {
         BufferedReader br = new BufferedReader(fr);  // Wrap the file reader in a buffered reader

         // Process the file line by line using streams
         return br.lines()  // Convert the file into a stream of lines
            .skip(1)  // Skip the header row
            .limit(10)  // Limit the stream to 10 lines (for testing purposes)
            .map(line -> {
               return line.split(",");  // Split each line by commas to create an array of fields
            }) // Convert string to string array
            .filter(fields -> "chile".equals(fields[6].trim().toLowerCase()))  // Filter for customers from Chile
            .map(fields -> {
               // Map the fields to a Customer object
               Customer customer = new Customer();
               customer.setCustomerId(fields[1]);  // Set customer ID
               customer.setFirstName(fields[2]);   // Set first name
               customer.setLastName(fields[3]);    // Set last name
               customer.setCompany(fields[4]);    // Set company name
               customer.setCity(fields[5]);       // Set city
               customer.setCountry(fields[6]);    // Set country
               return customer;  // Return the created Customer object
            }) // Convert string array to Customer object
            .toList();  // Collect the results into a list and return it
      }
   }

   // Method to process the CSV file using a loop (alternative to the stream method)
   public List<Customer> withLoop(String file) throws Exception {

      // Declare a list to store the customers
      List<Customer> customers = new LinkedList<>();

      // Using try-with-resources to automatically close the file reader and buffered reader
      try (FileReader fr = new FileReader(file)) {
         BufferedReader br = new BufferedReader(fr);  // Wrap the file reader in a buffered reader

         br.readLine();  // Skip the header line
         String line;

         // Loop through the file, reading each line
         while (null != (line = br.readLine())) {

            // Split the line by commas to create an array of fields
            String[] fields = line.split(",");

            // Print the country for debugging
            System.out.printf(">> %s\n", fields[6]);

            // Filter out customers who are not from Hungary
            if (!"hungary".equals(fields[6].trim().toLowerCase()))  // Skip if country is not Hungary
               continue;

            // Create a new Customer object and set its fields
            Customer customer = new Customer();
            customer.setCustomerId(fields[1]);  // Set customer ID
            customer.setFirstName(fields[2]);   // Set first name
            customer.setLastName(fields[3]);    // Set last name
            customer.setCompany(fields[4]);    // Set company name
            customer.setCity(fields[5]);       // Set city
            customer.setCountry(fields[6]);    // Set country

            // Add the customer to the list
            customers.add(customer);
         }

         // Return the list of customers
         return customers;
      }

   }
   
}

// 2) Big Idea Behind the Code:
// This class reads customer data from a CSV file, processes it, and filters out certain entries based on the country field. It provides two methods for doing this:

// withStream: Uses Java Streams to read, process, filter, and map CSV data into Customer objects.
// withLoop: Uses traditional loops and conditionals to achieve the same goal. It reads the file line by line and filters the data as it goes.
// In both methods:

// The CSV file is processed, skipping the header.
// Each customer entry is mapped to a Customer object and added to a list.
// The country field is used for filtering customers (e.g., Chile or Hungary).
// The data is then printed for debugging purposes and the API key is also printed to verify the injected value.
// 3) Can This Code Always Be the Same?
// WithStream: This method is based on the assumption that the CSV file structure is consistent, with the same number of fields per row, and that the country field is always the 7th column. If the CSV format changes (e.g., additional columns, different order), the code may need adjustment.
// WithLoop: Similar to withStream, this method assumes a specific file structure. The filtering logic is hardcoded for "Hungary". If this needs to be dynamic or change, you would need to modify it.
// The number of records processed is limited to 10 in withStream, which might not always be the case depending on the use case. The limit could be adjusted or removed depending on requirements.
// Thus, while the structure can work in typical scenarios, it might need tweaking based on different CSV formats, filtering requirements, or changes to how the data is processed.