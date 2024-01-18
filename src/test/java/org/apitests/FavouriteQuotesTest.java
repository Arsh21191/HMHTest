package org.apitests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class FavouriteQuotesTest {

    private final String apiKey = "6690620837bdf9b16d319d0732a63e51"; // Replace with your actual API key
    private final String baseUrl = "https://favqs.com/api";

    private String userToken; // Store the user token for subsequent requests

    @BeforeClass
    public void setup() {
        // Use the provided username and password to create a user session
        String username = "arshia.bhardwaj@hotmail.com";
        String password = "9fca0c06187cb827";

        // Create a user session by authenticating with the API
        Response authResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Token token=" + apiKey)// Set content type explicitly to JSON
                .body("{ \"user\": { \"login\": \"" + username + "\", \"password\": \"" + password + "\" } }")
                .post(baseUrl + "/session");

        // Print the raw response body to the console
        System.out.println("Raw Response Body:");
        System.out.println(authResponse.getBody().asString());

        // Extract the user token from the response body using jsonPath
        userToken = authResponse.jsonPath().getString("User-Token");
    }

    @Test
    public void testMarkNonexistentQuoteAsFavorite() {

        int nonexistentQuoteId = 40;

        // Perform the request to mark the nonexistent quote as favorite using the authenticated session
        Response response = RestAssured.given()
                .header("Authorization", "Token token=" + apiKey)
                .header("User-Token", userToken) // Include the user token in the headers
                .contentType(ContentType.JSON) // Set content type explicitly to JSON
                .put(baseUrl + "/quotes/" + nonexistentQuoteId + "/fav");

        // Print the response details to the console
        System.out.println("Response Body:");
        System.out.println(response.body().asString());

        // Assert that the response contains the expected error message
        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertEquals(response.jsonPath().getString("error"), "Not Found");

        // Add more assertions based on your test criteria
    }

    @Test
    public void testMarkQuoteAsFavorite() {
        // Assuming you have the ID of a quote to mark as favorite
        int quoteIdToMarkAsFavorite = 494;

        // Perform the request to mark the quote as favorite
        Response response = RestAssured.given()
                .header("Authorization", "Token token=" + apiKey)
                .header("User-Token", userToken)
                .contentType(ContentType.JSON)
                .put(baseUrl + "/quotes/" + quoteIdToMarkAsFavorite + "/fav");

        // Assertions
        Assert.assertEquals(response.getStatusCode(), 200);

        System.out.println("Response Body:");
        System.out.println(response.prettyPrint());

        // Validate the response body
        int id = response.path("id");
        String author = response.path("author");
        String body = response.path("body");
        boolean isFavorite = response.path("user_details.favorite");

        Assert.assertEquals(id, quoteIdToMarkAsFavorite);
        Assert.assertEquals(author, "David Russell");
        Assert.assertEquals(body, "The hardest thing to learn in life is which bridge to cross and which to burn.");
        Assert.assertTrue(isFavorite, "The quote should be marked as favorite.");

    }

    @Test
    public void testUnmarkQuoteAsFavorite() {
        // Quote ID to unmark as a favorite (e.g., 494)
        int quoteIdToUnmark = 494;

        // Perform the request to unmark the quote as a favorite using the authenticated session
        Response response = RestAssured.given()
                .header("Authorization", "Token token=" + apiKey)
                .header("User-Token", userToken) // Include the user token in the headers
                .contentType(ContentType.JSON) // Set content type explicitly to JSON
                .put(baseUrl + "/quotes/" + quoteIdToUnmark + "/unfav");

        // Print the response details to the console
        System.out.println("Response Body:");
        System.out.println(response.body().asString());

        // Assert that the response contains the expected details after unmarking as favorite
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("id"), quoteIdToUnmark);
        Assert.assertFalse(response.jsonPath().getBoolean("user_details.favorite"));
    }

    @AfterClass
    public void tearDown() {
        // Perform the request to destroy the user session
        Response logoutResponse = RestAssured.given()
                .header("Authorization", "Token token=" + apiKey)
                .header("User-Token", userToken) // Include the user token in the headers
                .delete(baseUrl + "/session");

        // Print the response details to the console
        System.out.println("Logout Response Body:");
        System.out.println(logoutResponse.body().asString());

        // Assert that the user was successfully logged out
        Assert.assertEquals(logoutResponse.getStatusCode(), 200);
        Assert.assertEquals(logoutResponse.jsonPath().getString("message"), "User logged out.");
    }
}
