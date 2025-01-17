import org.junit.jupiter.api.*;

// Since My whole app contains password and Assumption : Password is Case Sensitive
// As it works in other Apps

import static org.junit.jupiter.api.Assertions.*;

class ByteMeTest {

    private static final String CUSTOMER_NAME = "Rajesh Kumar";
    private static final boolean ISVIP = true;
    private static final String CUSTOMER_ID = "acd12";


    private Customer customer;
    private ByteMe byteMe;

    @BeforeEach
    void SetVariable()
    {
        byteMe = new ByteMe();
        customer = new Customer(CUSTOMER_NAME, ISVIP);
        customer.setCustomerId(CUSTOMER_ID);
        ByteMe.customerList.put(customer.getCustomerId(),customer);
    }

    @Test
    void TestValidLoginInputAsCustomerID() {

        Customer result = byteMe.CustomerDoesExist("acd12");
        assertNotNull(result, "Customer should exist in the customerList.");
        assertEquals(customer.getCustomerId(), result.getCustomerId(), "CustomerId should match.");
    }

    @Test
    void TestInValidInputAsCustomerID()
    {
        Customer result = byteMe.CustomerDoesExist("qwr34");
        assertNull(result, "Customer should not exist in the customerList.");
        assertNotEquals(customer.getCustomerId(), result, "CustomerId should not match.");
    }

    @AfterEach
    void ClearData()
    {
        ByteMe.customerList.remove("pqr",customer);
    }
}



