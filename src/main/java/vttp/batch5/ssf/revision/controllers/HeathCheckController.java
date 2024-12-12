package vttp.batch5.ssf.revision.controllers;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


// Marks this class as a REST controller that will handle HTTP requests
@RestController
// Specifies the base URL for this controller, it handles requests at the root path by default
@RequestMapping
public class HeathCheckController {

   // Injects the value of the CSV file path from the application's properties file
   @Value("${csv.file.path}")
   private String csvFile;

   // This method handles GET requests at "/health"
   @GetMapping("/health")
   // The return type is a ResponseEntity that holds a String response body and an HTTP status code
   public ResponseEntity<String> getMethodName() {
      // Creates a File object using the path to the CSV file
      File f = new File(csvFile);

      // Prints whether the file exists and is a regular file (for debugging)
      System.out.printf("Exists: %b, IsFile: %b\n", f.exists(), f.isFile());

      // If the file exists and is a file (not a directory), return a 200 OK response with an empty JSON object
      if (f.exists() && f.isFile())
         return ResponseEntity.ok("{}");

      // If the file doesn't exist or is not a regular file, return a 400 Bad Request response with an empty JSON object
      return ResponseEntity.status(400).body("{}");
   }
}

// 2) Big Idea Behind the Code:
// The purpose of this controller is to serve a health check endpoint for the application. It checks whether a specific file (the CSV file) exists and is a regular file, and based on that:

// If the file exists and is a regular file, it returns an HTTP status of 200 OK with an empty JSON object as the body.
// If the file does not exist or is not a regular file (e.g., it's a directory or missing), it returns an HTTP status of 400 Bad Request with an empty JSON object as the body.
// This is useful for monitoring and ensuring that critical files are available and accessible for the application.

// 3) Can This Code Always Be the Same?
// Yes, with some assumptions: This code can remain the same in most cases where:
// The csv.file.path in the application properties is set correctly to point to a file that the application needs.
// The file being checked is expected to be a regular file, not a directory, and the check for its existence is sufficient for your use case.
// However, the code assumes:

// That the file path (csv.file.path) is always valid and points to a specific file.
// That the file will not be missing or a directory (this check is done, but how to handle different file types might need adjusting depending on future requirements).
// You always want to return the same response body ({}) regardless of success or failure.
// If any of these assumptions change (e.g., different file types or more specific error handling), you may need to modify the code.