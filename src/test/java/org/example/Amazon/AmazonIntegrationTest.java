package org.example.Amazon;

import org.example.Amazon.Cost.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AmazonIntegrationTest {
    private Amazon amazon;
    private Database database;

    

    //create a new database and shopping cart before each test

    @BeforeEach
    void setUp() {
        database = new Database();
        ShoppingCart shoppingCart = new ShoppingCartAdaptor(database);
        List<PriceRule> rules = List.of(new RegularCost(), new DeliveryPrice(), new ExtraCostForElectronics());
        amazon = new Amazon(shoppingCart, rules);
    }

    //close the database after each test
    @AfterEach
    void tearDown() {
        database.resetDatabase();
        database.close();
    }


    @DisplayName("Integration Test: Calculate total price with multiple items")
    @Test
    void testCalculateTotalPriceWithMultipleItems() {
        // Arrange
        Item item1 = new Item(ItemType.OTHER, "Book A", 2, 15.0);
        Item item2 = new Item(ItemType.ELECTRONIC, "Gadget B", 1, 50.0);
        Item item3 = new Item(ItemType.OTHER, "Book C", 3, 10.0);

        amazon.addToCart(item1);
        amazon.addToCart(item2);
        amazon.addToCart(item3);

        // Act
        double totalPrice = amazon.calculate();

        // Assert
        // RegularCost: (2*15) + (1*50) + (3*10) = 30 + 50 + 30 = 110
        // DeliveryPrice: 5 (for 3 items)
        // ExtraCostForElectronics: 7.5 
        // Total: 110 + 5 + 7.5 = 122.5
        assertEquals(122.5, totalPrice);
    }
    
    @DisplayName("Integration Test: Calculate total price with multiple electronics")
    @Test
    void testCalculateTotalPriceWithMultipleElectronics() {
        // Arrange
        Item item1 = new Item(ItemType.ELECTRONIC, "Gadget A", 1, 100.0);
        Item item2 = new Item(ItemType.ELECTRONIC, "Gadget B", 2, 150.0);

        amazon.addToCart(item1);
        amazon.addToCart(item2);

        // Act
        double totalPrice = amazon.calculate();

        // Assert
        // RegularCost: (1*100) + (2*150) = 100 + 300 = 400
        // Flat DeliveryPrice: 5 (for 2 items)
        // Flat ExtraCostForElectronics: 7.5 
        // Total: 400 + 5 + 7.5 = 412.5
        assertEquals(412.5, totalPrice);
    }

    @DisplayName("Integration Test: Calculate total price with multiple other items")
    @Test
    void testCalculateTotalPriceWithMultipleOtherItems() {
        // Arrange
        Item item1 = new Item(ItemType.OTHER, "Gadget A", 1, 100.0);
        Item item2 = new Item(ItemType.OTHER, "Gadget B", 2, 150.0);

        amazon.addToCart(item1);
        amazon.addToCart(item2);

        // Act
        double totalPrice = amazon.calculate();

        // Assert
        // RegularCost: (1*100) + (2*150) = 100 + 300 = 400
        // Flat DeliveryPrice: 5 (for 2 items)
        // Total: 400 + 5 = 405.0
        assertEquals(405.0, totalPrice);
    }
    
    @DisplayName("Integration Test: Calculate total price with no quantity items")
    @Test
    void testCalculateTotalPriceWithNoItems() {
        // Arrange
        // Zero quantity item added to cart
        Item item1 = new Item(ItemType.OTHER, "Book A", 0, 15.0);
        Item item2 = new Item(ItemType.ELECTRONIC, "Gadget B", 0, 50.0);

        amazon.addToCart(item1);
        amazon.addToCart(item2);
        // Act
        double totalPrice = amazon.calculate();

        // Assert
        // The price should only be the sum of the fees, as item costs are zero.
        // RegularCost: 0.0 | DeliveryPrice: 5.0 | ExtraCostForElectronics: 7.5
        assertEquals(12.5, totalPrice);
    }

    /**
     * Helper method to test the full price for a given number of items.
     * This avoids duplication in multiple tests.
     * @param numberOfItems The number of standard items to add to the cart.
     * @param expectedTotalPrice The expected final price, including all costs.
     */
    private void assertPriceForNumberOfItems(int numberOfItems, double expectedTotalPrice) {
        // ARRANGE:
        Item standardItem = new Item(ItemType.OTHER, "Standard Item", 1, 10.0);

        //add the specified number of items to the database via the cart.
        for (int i = 0; i < numberOfItems; i++) {
            amazon.addToCart(standardItem);
        }

        // ACT:
        double finalPrice = amazon.calculate();

        // ASSERT:
        // Verify the total price matches our calculation.
        assertEquals(expectedTotalPrice, finalPrice);
    }

    @Test
    @DisplayName("structural-based: Delivery Price for 4 items")
    void calculate_checksDeliveryPriceBoundary_for4items() {
        // Regular: 40.0, Delivery: 12.5, Electronics: 0 -> Total: 52.5
        assertPriceForNumberOfItems(4, 52.5);
    }

    @Test
    @DisplayName("structural-based: Delivery Price for 10 items")
    void calculate_checksDeliveryPriceBoundary_for10items() {
        // Regular: 100.0, Delivery: 12.5, Electronics: 0 -> Total: 112.5
        assertPriceForNumberOfItems(10, 112.5);
    }

    @Test
    @DisplayName("structural-based: Delivery Price for 11 items")
    void calculate_checksDeliveryPriceBoundary_for11items() {
        // Regular: 110.0, Delivery: 20.0, Electronics: 0 -> Total: 130.0
        assertPriceForNumberOfItems(11, 130.0);
    }

}
