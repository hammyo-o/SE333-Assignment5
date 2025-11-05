package org.example.Amazon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.example.Amazon.Cost.PriceRule;

@ExtendWith(MockitoExtension.class)
class AmazonUnitTest {

    @Mock
    private ShoppingCart shoppingCart;

    @Mock
    private PriceRule priceRule1;

    @Mock
    private PriceRule priceRule2;

    @DisplayName("specification-based ")
    @Test
    void testCalculateSumming() {
        // Arrange
        Item item1 = new Item(org.example.Amazon.Cost.ItemType.OTHER, "Book A", 2, 15.0);
        Item item2 = new Item(org.example.Amazon.Cost.ItemType.ELECTRONIC, "Gadget B", 1, 50.0);

        when(shoppingCart.getItems()).thenReturn(java.util.List.of(item1, item2));
        when(priceRule1.priceToAggregate(shoppingCart.getItems())).thenReturn(30.0);
        when(priceRule2.priceToAggregate(shoppingCart.getItems())).thenReturn(50.0);

        Amazon amazon = new Amazon(shoppingCart, java.util.List.of(priceRule1, priceRule2));

        // Act
        double totalPrice = amazon.calculate();

        // Assert
        assertEquals(80.0, totalPrice);
        verify(priceRule1, times(1)).priceToAggregate(shoppingCart.getItems());
        verify(priceRule2, times(1)).priceToAggregate(shoppingCart.getItems());
}
    @DisplayName("Structural-based testing")
    @Test
    void calculateWithNoItems() {
        // Arrange
        when(shoppingCart.getItems()).thenReturn(java.util.List.of());
        when(priceRule1.priceToAggregate(shoppingCart.getItems())).thenReturn(0.0);
        when(priceRule2.priceToAggregate(shoppingCart.getItems())).thenReturn(0.0);

        Amazon amazon = new Amazon(shoppingCart, java.util.List.of(priceRule1, priceRule2));

        // Act
        double totalPrice = amazon.calculate();

        // Assert
        assertEquals(0.0, totalPrice);
        verify(priceRule1, times(1)).priceToAggregate(shoppingCart.getItems());
        verify(priceRule2, times(1)).priceToAggregate(shoppingCart.getItems());
    }

}