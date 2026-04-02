## Book review API
Spring Boot | MySQL | Redis | Docker | OpenLibrary API

A high-performance RESTful backend service designed to manage book metadata and user reviews. This project demonstrates multi-level caching strategies, external API integration, and containerized deployment using modern DevOps practices.

## Features
1. External API Integration: Consumes the OpenLibrary API to fetch real-time book metadata.
2. Multi-Level Caching: Implements Redis to reduce latency and minimize redundant external API calls.
3. Custom TTL Logic: Tailored caching durations for metadata (short-term) and reviews (long-term).
4. Global Exception Handling: Centralized error management using @RestControllerAdvice for standardized JSON error responses.
5. Containerized Architecture: Fully orchestrated environment with Docker Compose for seamless deployment.
6. Relational Persistence: Robust data mapping and persistence using Spring Data JPA and MySQL.

## Run with Docker
1. Run: docker compose up --build
2. Available at: http://localhost:8000/swagger-ui/index.html

## How to use
Once the containers are running (via docker-compose up), follow these steps to test the full lifecycle of the application:

### Step 1: Starting the application
* Access the Interactive UI
* Open your browser and navigate to: http://localhost:8000/swagger-ui/index.html

### Step 2: Saving books and reviews
1. search for a book: use the GET /api/books/search endpoint
* input: title = "The hobbit"
* what happens: the app checks redis(miss) -> calls external api -> saves the result as cache(ttl 5min).
* copy the bookId you want to review.

2. Add User review: use the POST /api/reviews (to add both book and review) use POST /api/books (to add just the book).
*payload for /api/reviews:
```
{
  "bookId": "string",
  "stars": 10,
  "comment": "string"
}
```
* once you sent the post request, if the cache has not expired, no external api calls will be made. book data will come from the cache.

4. Get Review List: Use GET /api/reviews/book to see your saved review. This response is also cached for high-speed retrieval.
   
### Strp 3: Cleaning & Management
* Use PATCH /api/reviews to edit reviews(payload is same as POST /api/reviews).
* Use DELETE /api/reviews to delete just the review, book stays in the db for you to review later.
* use DELETE /api/reviews to delete book and its review from the db.


