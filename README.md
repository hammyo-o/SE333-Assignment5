# Assignment 5 Unit, Mocking and Integration Testing 


## Project Overview

This project contains simplified storefronts for Amazon and BarnesAndNoble. The goal of this assignment is to practice writing Junit tests, integration tests, and automating the testing process with GitHub Actions. 

## Part 1: BarnesAndNoble Testing

1.  **Specification-Based and Black-Box Testing:** I tested the public `getPriceForCart` method. I created tests for several of the key scenarios:
    *   One book order that has stock.
    *   One order where the request was greater than the stock.
    *   An edge case that held a empty shopping cart.
    *   An edge case where the input was `null`.

2.  **Structural-Based and White-Box Testing:** I added a test that covered multiple internal code paths in a single run. The test `getPriceForMultipleBooksWithMultiStock` ensures that the `for` loop in the method runs more than once and that both the insufficient stock and the implicit `else` (sufficient stock) branches inside the loop are executed. This confirms the logic works correctly for a complex cart.