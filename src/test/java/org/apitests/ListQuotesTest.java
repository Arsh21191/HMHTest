package org.apitests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class ListQuotesTest {

    private final String apiKey = "6690620837bdf9b16d319d0732a63e51"; // Replace with your actual API key

    @Test
    public void testListQuotesEndpoint() {
        // Updated request with filter and type parameters
        Response response = RestAssured.given()
                .header("Authorization", "Token token=" + apiKey)
                .param("filter", "Mark Twain") // Updated filter parameter
                .param("type", "author") // Added type parameter
                .get("https://favqs.com/api/quotes/");

        System.out.println("Response Body:");
        System.out.println(response.prettyPrint());

        Assert.assertEquals(response.getStatusCode(), 200);

       // Extract the authors from all quotes and check that they are all "Mark Twain"
        List<String> authorNames = response.jsonPath().getList("quotes.author");

        // Check if the list is not null before iterating
        if (authorNames != null) {
            for (String authorName : authorNames) {
                Assert.assertTrue("Mark Twain".equals(authorName), "Author is not Mark Twain");
            }
        } else {
            Assert.fail("Author names list is null");
        }
    }
}
