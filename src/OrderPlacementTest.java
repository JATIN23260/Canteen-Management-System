import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderPlacementTest {
    private static final String CUSTOMER_NAME = "Jatin Kumar";
    private static final String CUSTOMER_ID = "hkH#23260*Q";

    private Customer customer;
    private ByteMe byteMe;
    private Menu menu;
    private String TestingItemAvailable = "Vanilla Ice Cream";
    private String TestingItemNotAvailable = "Naan";

    @BeforeEach
    void SetVariableInitializationAndAllItemsAreAvailable()
    {
        byteMe = new ByteMe();
        customer = new Customer(CUSTOMER_NAME,true);
        customer.setCustomerId(CUSTOMER_ID);
        menu = ByteMe.menu;
    }

    @AfterEach

    void DesolveAllItems()
    {
        customer = null;
        menu = null;
        byteMe = null;
    }


    @Test
    void Testing_All_Items_Are_Available()
    {
        assertNotNull(customer,"The Given Customer does exists ");
        customer.order.AddItems(menu,TestingItemAvailable,2,1);
    }


    @Test
    void Test_With_Some_Items_Are_Not_Available()
    {
        assertNotNull(customer,"The Given Customer does exists ");
        menu.MenuList.get("Naan").setAvailability(false);
        customer.order.AddItems(menu,TestingItemNotAvailable,2,1);
    }
}