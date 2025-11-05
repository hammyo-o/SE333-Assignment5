
# Assignment 5 Unit, Mocking and Integration Testing [![SE333_CI](https://github.com/hammyo-o/SE333-Assignment5/actions/workflows/SE333_CI.yml/badge.svg?branch=main)](https://github.com/hammyo-o/SE333-Assignment5/actions/workflows/SE333_CI.yml)


## Project Overview

This project contains simplified storefronts for Amazon and BarnesAndNoble. The goal of this assignment is to practice writing Junit tests, integration tests, and automating the testing process with GitHub Actions. 

## Part 1: BarnesAndNoble Testing

1.  **Specification-Based and Black-Box Testing:** I tested the public `getPriceForCart` method. I created tests for several of the key scenarios:
    *   One book order that has stock.
    *   One order where the request was greater than the stock.
    *   An edge case that held a empty shopping cart.
    *   An edge case where the input was `null`.

2.  **Structural-Based and White-Box Testing:** I added a test that covered multiple internal code paths in a single run. The test `getPriceForMultipleBooksWithMultiStock` ensures that the `for` loop in the method runs more than once and that both the insufficient stock and the implicit `else` (sufficient stock) branches inside the loop are executed. This confirms the logic works correctly for a more complex cart.

## Part 2: Automate Testing with GitHub Actions

* I created a CI (continous integration) pipeline using a GitHub Actions workflow file located at .github/workflows/SE333_CI.yml. This workflow automates necessary quality assurance.

* Trigger: The workflow triggers automatically on every push to the main branch.

* Static Analysis: The Checkstyle plugin analyzes code quality against the default style guide. It is configured to report violations without failing the build, as required.

* Code Coverage: The JaCoCo plugin is used to measure test coverage. It runs after the tests and generates a report.

* Artifacts: After successfully running, the workflow uploads the checkstyle-result.xml and jacoco.xml artifacts.

## Part 3: Amazon Testing 
With a more complex package, I created two distinct testing suites; a suite of unit tests and a suite of integration tests. 
1. **Unit Tests (AmazonUnitTest.java)**

    Goal: Test Amazon class isolated from its dependencies (the database and pricing rules).

    Method: The Mockito framework allowed me to create mock objects for the ShoppingCart and PriceRule dependencies. This allowed me to specify their behavior and verify that the Amazon class correctly summed the results without needing a real database.

    Tests Written:

        Specification-based: A test was written to ensure the calculate() method correctly iterates through a list of mocked rules and sums their results.

        Structural-based: An edge case was tested to confirm the method returns 0.0 when the shopping cart is empty, ensuring the for loop handles this path correctly.

2. **Integration Tests (AmazonIntegrationTest.java)**

    Goal: To verify that all the components work together correctly as a system.

    Method: These tests use real instances of the objects. An SQL database is initialized @BeforeEach and @AfterEach test to ensure that each tests are independent.

    Tests Written:

        Specification-based: Front-facing scenarios were tested, such as calculating the total price for a cart containing a mix of electronic and non-electronic items. An interesting edge case was also tested where items with zero quantity still correctly incurred fees.

        Structural-based: To cover logic paths within the dependencies, I added tests for the boundaries of the DeliveryPrice rule (for 4, 10, and 11 items), confirming that the data flows correctly from the database to the pricing. 

**Workflow and Testing Summary**

The GitHub Actions workflow passes, as shown by the build status badge at the top of this README. All unit and integration tests are running successfully within the CI pipeline, and all artifacts are being generated and uploaded as expected.