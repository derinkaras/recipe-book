| Scenario                       | Use             | Why                            |
| ------------------------------ | --------------- | ------------------------------ |
| Get/update/delete **one item** | `@PathVariable` | Identifies a resource          |
| Sub-resource                   | `@PathVariable` | Hierarchy (users/{id}/recipes) |
| Action on resource             | `@PathVariable` | Target specific resource       |
| Filtering                      | `@RequestParam` | Optional & modifies query      |
| Searching                      | `@RequestParam` | Optional                       |
| Sorting                        | `@RequestParam` | Optional                       |
| Pagination                     | `@RequestParam` | Optional                       |
| Small flags/toggles            | `@RequestParam` | Simple primitive values        |
| Create/Update complex object   | `@RequestBody`  | JSON data                      |
