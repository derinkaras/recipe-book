package com.derinkaras.recipebook.exception;



// Example JSON returned to the client when this exception is thrown:
//
// {
//   "status": 404,
//   "error": "Not Found",
//   "message": "Recipe with id 5 not found",
//   "path": "/api/recipes/5",
//   "timestamp": "2025-11-19T13:24:09"
// }
//
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, Object id) {
        super(resourceName + " with id=" + id + " not found");
    }

}
