Introduction
This project demonstrates the validation of the FAV QUOTE and LIST QUOTES endpoints provided by the FavQuotes API. The tests are designed to cover the main functionality of these endpoints.

Prerequisites
Before running the tests, you need to create an account on https://favqs.com and generate an API key. Replace YOUR_API_KEY in the code with the actual API key you obtained.

Technologies Used
Testing Framework: RestAssured with TestNG

Test Scenarios Covered
FavouriteQuotesTest.java: Validates the functionality of the FAV QUOTE endpoint by marking the quotes as favourite and unmarking the quote.
List Quotes by Author: Tests the LIST QUOTES endpoint by retrieving quotes based on a specific author. Ensures the response contains quotes from the specified author and validates the LIST QUOTES endpoint by searching for quotes containing a specific keyword. Ensures the response includes quotes relevant to the keyword.

Instructions to Compile and Run
To run all the testcases run the testng.xml file which is under resources folder.
