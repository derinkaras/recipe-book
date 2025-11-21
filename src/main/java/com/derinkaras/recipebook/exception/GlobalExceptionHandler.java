package com.derinkaras.recipebook.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


/**
 * GlobalExceptionHandler
 *
 * This class centralizes all exception handling for the application.
 *
 * WHY THIS IS IMPORTANT:
 * ----------------------
 * 1. Separation of Concerns:
 *    - Service classes throw plain Java exceptions (e.g., ResourceNotFoundException)
 *      without knowing anything about HTTP status codes or JSON responses.
 *    - Controllers stay clean and only delegate to services.
 *    - This handler converts exceptions → proper HTTP responses.
 *
 * 2. Consistent Error Responses:
 *    - Instead of Spring returning HTML error pages, stack traces, or inconsistent
 *      formats, all errors are transformed into a unified JSON response structure.
 *    - Every 404, 400, etc. looks the same across all endpoints.
 *
 * 3. Centralized Logic:
 *    - Error formatting, message creation, and HTTP status mapping live in ONE place.
 *    - You avoid repeating try/catch or ResponseEntity logic in every controller.
 *
 * 4. API Stability and Security:
 *    - Exception details and stack traces are never leaked to clients.
 *    - The API contract remains stable even if internal code changes.
 *
 * HOW IT WORKS:
 * -------------
 * - Services throw custom exceptions like ResourceNotFoundException.
 * - This class catches them via @ExceptionHandler methods.
 * - Each method builds a meaningful JSON error body and the correct HTTP status.
 *
 * Result: A clean, professional, maintainable error-handling pipeline.
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        // Example JSON returned to the client when a ResourceNotFoundException is thrown:
        //
        // HTTP 404 Not Found
        // {
        //   "error": "NOT_FOUND",
        //   "message": "Recipe with id 5 not found"
        // }
        //
        // - "error" is always NOT_FOUND
        // - "message" comes from ex.getMessage()
        // - The HTTP status code is set using HttpStatus.NOT_FOUND (built-in 404)
        //

        Map<String, Object> body = new HashMap<>();
        body.put("error", "NOT_FOUND");
        body.put("message", ex.getMessage());
        // HttpStatus.NOT_FOUND is a builtin http status code error code 404
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }


    // Handles validation errors for controller methods that use @Valid @RequestBody.
    //
    // FLOW:
    //
    // 1. A controller endpoint is defined like this:
    //        @PostMapping("/recipes")
    //        public RecipeDto create(@Valid @RequestBody CreateRecipeRequest req) { ... }
    //
    //    - @RequestBody tells Spring to bind the JSON body to the CreateRecipeRequest object.
    //    - @Valid tells Spring to run Jakarta Bean Validation on that object
    //      (e.g. @NotNull, @NotBlank, @Size on its fields).
    //
    // 2. If the JSON is invalid according to those annotations
    //      (for example: missing a @NotNull field like title,
    //       or sending an empty string for a @NotBlank field),
    //    Spring does NOT call the controller method at all.
    //    Instead, it throws MethodArgumentNotValidException.
    //
    // 3. Because this class is annotated with @RestControllerAdvice,
    //    this method is automatically invoked whenever a MethodArgumentNotValidException occurs.
    //
    // 4. From the exception, we extract the first field error:
    //        ex.getBindingResult().getFieldError().getDefaultMessage()
    //          ex.getBindingResult()
    //          → contains all validation problems
    //
    //          getFieldError()
    //          → gets the first field that failed validation
    //
    //          getDefaultMessage()
    //          → gets the message from your DTO annotation:
    //            This gives us the custom validation message from the DTO
    //          (e.g. "title is required").
    //
    // 5. We build a small JSON body:
    //        {
    //          "error": "VALIDATION_ERROR",
    //          "message": "<validation message from the failed field>"
    //        }
    //
    // 6. We return that body with HTTP 400 (BAD_REQUEST), so the client
    //    clearly knows the request payload was invalid.
    //
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "VALIDATION_ERROR");
        body.put("message", ex.getBindingResult().getFieldError().getDefaultMessage());
        // Error code 400
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handlerOther(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "INTERNAL_ERROR");
        body.put("message", ex.getMessage());
        // Error code 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(DuplicateResourceException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "DUPLICATE");
        body.put("message", ex.getMessage());
        // This will be a 409 conflict status code
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

}
