package vttp.batch5.ssf.revision.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp.batch5.ssf.revision.services.TimeService;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


// Marks this class as a REST controller that handles HTTP requests
@RestController
// Specifies the base URL for this controller, it handles requests at the root path by default
@RequestMapping
public class TimeController {

   // Injects the TimeService dependency to get the current time
   @Autowired
   private TimeService timeSvc;

   // This method handles GET requests at "/time" and returns the time as JSON
   @GetMapping(path="/time", produces = MediaType.APPLICATION_JSON_VALUE)
   // The return type is a ResponseEntity that holds a String response body and an HTTP status code
   public ResponseEntity<String> getTimeAsJson() {
      // Retrieves the current time from the timeSvc service
      String time = timeSvc.getTime();

      // Creates a JSON object with a key "time" and the retrieved time as the value
      JsonObject resp = Json.createObjectBuilder()
            .add("time", time)
            .build();

      // Returns a 200 OK response with the JSON object as the body
      return ResponseEntity.ok(resp.toString());
   }

   // This method handles GET requests at "/time" and returns the time as HTML
   @GetMapping(path="/time", produces = MediaType.TEXT_HTML_VALUE)
   // The return type is a ResponseEntity that holds a String response body and an HTTP status code
   public ResponseEntity<String> getTime() {
      // Retrieves the current time from the timeSvc service
      String time = timeSvc.getTime();

      // Creates an HTML string displaying the current time
      String html = """
         <h1>The current time is %s</h1>
      """.formatted(time);

      // Returns a 200 OK response with the HTML as the body
      return ResponseEntity.ok(html);
   }
}

// 2) Big Idea Behind the Code:
// This controller defines two endpoints to provide the current time in different formats:

// JSON format: When a GET request is made to /time with the Accept header set to application/json, the controller responds with a JSON object that contains the current time as a key-value pair ({"time": "current time"}).
// HTML format: When a GET request is made to /time with the Accept header set to text/html, the controller responds with an HTML page displaying the current time in an <h1> tag.
// The TimeService is used to get the current time, which is then formatted into either JSON or HTML depending on the request's Accept header.

// 3) Can This Code Always Be the Same?
// Yes, with some assumptions: The code can remain the same as long as:
// The TimeService is correctly implemented and returns the current time as expected.
// The client sends the correct Accept header to specify whether it wants the response in JSON or HTML format.
// However, a few points to consider:

// If you need to support additional formats in the future (e.g., XML, plain text), you may need to add more methods or logic to handle these.
// If the time format or response content changes (e.g., adding a timezone or more data), you may need to modify the timeSvc.getTime() method or how the response is structured.