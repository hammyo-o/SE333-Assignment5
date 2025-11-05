package org.example.Barnes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


// This line tells JUnit 5 to enable Mockito's features
@ExtendWith(MockitoExtension.class)
class BarnesAndNobleTests {

    @Mock
    private BuyBookProcess buyBookProcess;
    @Mock
    private BookDatabase bookDatabase;
    @InjectMocks
    private BarnesAndNoble barnesAndNoble;

    @Test
    @DisplayName("specification-based - in stock")
    void getPriceForBookInstock() {
        // Arrange
        String isbn = "1234567890";
        int price = 50;
        int quantityInStock = 10;
        int quantityToBuy = 5;

        Book book = new Book(isbn, price, quantityInStock);
        when(bookDatabase.findByISBN(isbn)).thenReturn(book);

        Map<String, Integer> order = Map.of(isbn, quantityToBuy);

        // Act
        PurchaseSummary summary = barnesAndNoble.getPriceForCart(order);

        // Assert
        assertNotNull(summary);
        assertEquals(price * quantityToBuy, summary.getTotalPrice());
        assertTrue(summary.getUnavailable().isEmpty());
    }
    @Test
    @DisplayName("specification-based - not enough in stock")
    void getPriceForBookNotInStock() {
        Book book = new Book("0987654321", 30, 2);
        when(bookDatabase.findByISBN("0987654321")).thenReturn(book);
        Map<String, Integer> order = Map.of("0987654321", 5);
        //act
        PurchaseSummary summary = barnesAndNoble.getPriceForCart(order);
        //assert
        assertNotNull(summary);
        assertEquals(30 * 2, summary.getTotalPrice());
        assertFalse(summary.getUnavailable().isEmpty());
        assertEquals(3, summary.getUnavailable().get(book));
    }

    @Test
    @DisplayName("specification-based - null order")
    void getPriceForNullOrder() {
        // Act
        PurchaseSummary summary = barnesAndNoble.getPriceForCart(null);
        // Assert
        assertNull(summary);
    }
    @Test
    @DisplayName("specification-based - empty order")
    void getPriceForEmptyOrder() {
        // Act
        PurchaseSummary summary = barnesAndNoble.getPriceForCart(Map.of());
        // Assert
        assertNotNull(summary);
        assertEquals(0, summary.getTotalPrice());
        assertTrue(summary.getUnavailable().isEmpty());
    }

    @Test
    @DisplayName("structural-based")
    void getPriceForMultipleBooksWithMultiStock() {
        // Arrange
        String isbn1 = "1234567890";
        String isbn2 = "0987654321";
        int price1 = 50;
        int price2 = 30;
        int quantityInStock1 = 10;
        int quantityInStock2 = 2;
        int quantityToBuy1 = 5;
        int quantityToBuy2 = 5;

        Book book1 = new Book(isbn1, price1, quantityInStock1);
        Book book2 = new Book(isbn2, price2, quantityInStock2);
        when(bookDatabase.findByISBN(isbn1)).thenReturn(book1);
        when(bookDatabase.findByISBN(isbn2)).thenReturn(book2);

        Map<String, Integer> order = Map.of(
                isbn1, quantityToBuy1,
                isbn2, quantityToBuy2
        );

        // Act
        PurchaseSummary summary = barnesAndNoble.getPriceForCart(order);

        // Assert
        assertNotNull(summary);
        assertEquals((price1 * quantityToBuy1) + (price2 * quantityInStock2), summary.getTotalPrice());
        assertFalse(summary.getUnavailable().isEmpty());
        assertEquals(3, summary.getUnavailable().get(book2));
    }
}