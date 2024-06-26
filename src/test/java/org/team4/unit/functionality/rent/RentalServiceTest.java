package org.team4.unit.functionality.rent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.team4.functionality.rent.RentalService;
import org.team4.functionality.rent.ReturnService;
import org.team4.maintaindb.MaintainBooks;
import org.team4.maintaindb.MaintainRent;
import org.team4.model.items.Book;
import org.team4.model.items.Item;
import org.team4.model.items.RentedItem;
import org.team4.model.user.User;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import static org.junit.Assert.*;

public class RentalServiceTest {
    private RentalService rentalService;

    private ReturnService returnService;

    private MaintainRent rentMaintain;

    private MaintainBooks maintainBooks;

    User user ;
    Item item1, item2, item3, item4, item5, item6,  overdueItem, approachingOverdueItem;

    Book EffectiveJava;


    @Before
    public void setUp() throws Exception {




        rentalService = new RentalService();
        user = new User("user@example.com", "password", "John Doe", "MEMBER");
        item1= new Item("New Java", 2020, 10, 15.99, "123456789", true, true);
        item2= new Item("Harry Potter", 2020, 10, 20.99, "123316789", true, true);
        item3 = new Item("Unavailable Book", 2011, 0, 100, "987654321", true, true);
        item4 = new Item("Test 11th Book", 2020, 20, 125.99, "983134143", true, true);
        item5=  new Item("NoQuantity", 2001, 0, 10.0, "943426789", false, true);
        item6=  new Item("Decrease Book", 2021, 5, 29.99, "987654301", true, true);
        overdueItem=  new Item("Overdue Book", 2010, 10, 25.99, "414231221233", true, true);
        approachingOverdueItem= new Item("Approaching DueDate Book", 2000, 20, 225.99, "9231313414141", true, true);

        returnService = new ReturnService();
        rentMaintain = MaintainRent.getInstance();
        maintainBooks= MaintainBooks.getInstance();
        EffectiveJava= maintainBooks.searchExactBookByISBN("9780134685991");

    }

    @After
    public void returnItem() throws Exception {
        Book effectiveJava = maintainBooks.searchExactBookByISBN("9780134685991");

        returnService.returnItem(user,item1);
        returnService.returnItem(user,item2);
        returnService.returnItem(user,item3);
        returnService.returnItem(user,item6);
        rentMaintain.update();
        for (int i = 0; i < 10; i++) {
            returnService.returnItem(user, new Item("Book " + i, 2010, 1, 15.99, "98231341" + i, true, true));
        }
        rentMaintain.returnRentedItem(user.getEmail(), overdueItem.getISBN());

        rentMaintain.returnRentedItem(user.getEmail(), approachingOverdueItem.getISBN());

        for (int i = 0; i < 5; i++) {
            rentMaintain.returnRentedItem(user.getEmail(), "91345" + i);

        }
        rentMaintain.returnRentedItem(user.getEmail(), effectiveJava.getISBN());
        rentMaintain.update();
    }


    @Test
    public void testItemRentedSuccessfully() throws Exception {
        boolean result = rentalService.rentItem(user, item1);
        assertTrue(result);

    }
    @Test
    public void testRentItemNotAvailable() {
        Exception exception = assertThrows(Exception.class, () -> rentalService.rentItem(user, item3));
        assertEquals("Item is not available for rent.", exception.getMessage());
    }

    @Test
    public void testCannotRentItem() throws Exception {

        Exception exception = assertThrows(Exception.class, () -> rentalService.rentItem(user, item5));
        assertEquals("Item is not available for rent.", exception.getMessage());
    }

    @Test
    public void testOverdueItemsPenaltyCalculation() {
        double penalty = rentalService.calculatePenaltyForItem(user.getEmail(), item1.getISBN());

        double expectedPenalty =  0.0;
        assertEquals(expectedPenalty, penalty, 0.01);
    }
    @Test
    public void testRentItemAlreadyRentedByUserThrowsException() throws Exception {
        boolean firstResult = rentalService.rentItem(user, item2);

        Exception exception = assertThrows(Exception.class, () -> rentalService.rentItem(user, item2));
        assertEquals("You have already rented this item.", exception.getMessage());
    }
    @Test
    public void testRentItemWhenMaxRentalsReached() throws Exception {
        for (int i = 0; i < 10; i++) {
            rentalService.rentItem(user, new Item("Book " + i, 2010, 1, 15.99, "98231341" + i, true, true));
        }


        Exception exception = assertThrows(Exception.class, () -> rentalService.rentItem(user, item4));
        assertEquals("You cannot rent more than 10 items.", exception.getMessage());
    }


    @Test
    public void testCheckOverdueItems() {

        Calendar pastDueDate = Calendar.getInstance();
        pastDueDate.add(Calendar.DAY_OF_MONTH, -10);

        rentMaintain.addNewRentedItem(user.getEmail(), "Overdue Book", "414231221233",
                new java.sql.Date(System.currentTimeMillis()),
                new java.sql.Date(pastDueDate.getTimeInMillis()));

        List<RentedItem> overdueItems = rentalService.checkOverdue(user.getEmail());
        assertEquals(1, overdueItems.size());
    }

    @Test
    public void testCalculatePenaltyForItem() {
        Calendar pastDueDate = Calendar.getInstance();
        pastDueDate.add(Calendar.DAY_OF_MONTH, -10);
        rentMaintain.addNewRentedItem(user.getEmail(), "Overdue Book", "414231221233",
                new java.sql.Date(System.currentTimeMillis()),
                new java.sql.Date(pastDueDate.getTimeInMillis()));
        double penalty = rentalService.calculatePenaltyForItem(user.getEmail(),overdueItem.getISBN());
        double expectedPenalty = 10 * 0.5;
        assertEquals( expectedPenalty, penalty, 0.01);
    }

    @Test
    public void testGetApproachingOrOverdueItems() {
        String userEmail = user.getEmail();
        Calendar dueDate = Calendar.getInstance();


        dueDate.add(Calendar.HOUR, 23);
        rentMaintain.addNewRentedItem(userEmail, "Approaching DueDate Book", "9231313414141",
                new java.sql.Date(System.currentTimeMillis()),
                new java.sql.Date(dueDate.getTimeInMillis()));


        List<RentedItem> approachingOrOverdueItems = rentalService.getApproachingOrOverdueItems(userEmail);

        assertEquals( 1, approachingOrOverdueItems.size());
    }
    @Test
    public void testGetRentalCountForUser() {
        for (int i = 0; i < 5; i++) {
            rentMaintain.addNewRentedItem(user.getEmail(), "Book " + i, "91345" + i,
                    new java.sql.Date(new Date().getTime()),
                    new java.sql.Date(new Date().getTime()));
        }

        int count = rentalService.getRentalCountForUser(user.getEmail());

        assertEquals( 5, count);
    }

    @Test
    public void testPrintOverduePenalties() {

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        rentalService.printOverduePenalties(user.getEmail(), item1.getISBN());

        assertTrue( outContent.toString().contains(user.getEmail()));
        assertTrue( outContent.toString().contains("overdue items"));
        assertTrue( outContent.toString().contains("Penalty $"));


        System.setOut(System.out);
    }
    @Test
    public void testRentedItemDecreasesQuantity() throws Exception {

        int originalQuantity = EffectiveJava.getQuantity();
        rentalService.rentItem(user, EffectiveJava);
        Book updatedEffectiveJava = maintainBooks.searchExactBookByISBN(EffectiveJava.getISBN());
        assertEquals(originalQuantity - 1, updatedEffectiveJava.getQuantity());
    }








}
