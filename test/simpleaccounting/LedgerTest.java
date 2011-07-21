/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleaccounting;

import simpleaccounting.Account.Type;
import java.util.Date;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Travers
 */
public class LedgerTest {

    Ledger ledger;
    Account cash;
    Account anne;

    @Before
    public void setUp() {
        ledger = new Ledger();
        cash = ledger.newAccount("Cash", Type.ASSET);
        anne = ledger.newAccount("Cash:Anne", Type.ASSET);
        ledger.newAccount("Dues Received", Type.INCOME);
        ledger.newAccount("Tickets Purchased", Type.EXPENSE);
    }

    @Test
    public void testNestedAccounts() {
        Account a;

        a = ledger.getAccount("Cash");
        assertEquals(cash, a);

        a = ledger.getAccount("Cash:Anne");
        assertEquals(anne, a);
    }

    @Test
    public void makePosting() throws AccountingException {
        ledger.newPosting(new Date(), "Received $10 from Anne")
                .debit("Cash:Anne", 1000)
                .credit("Dues Received", 1000)
                .post();

        int cashBalance = ledger.getAccount("Cash").getTrialBalance();
        assertEquals(-1000, cashBalance);

        int anneBalance = ledger.getAccount("Cash:Anne").getTrialBalance();
        assertEquals(-1000, anneBalance);

        int duesBalance = ledger.getAccount("Dues Received").getTrialBalance();
        assertEquals(1000, duesBalance);
    }

    @Test(expected = NonExistantAccountException.class)
    public void makePostWithBadAccount() throws NonExistantAccountException, PostingUnbalancedException {
        ledger.newPosting(new Date(), "Received $10 from Bob")
                .debit("Cash:Bob", 1000)
                .credit("Dues Received", 1000)
                .post();
    }

    @Test(expected = PostingUnbalancedException.class)
    public void makeUnbalancedPost() throws NonExistantAccountException, PostingUnbalancedException {
        ledger.newPosting(new Date(), "Received $10 from Anne")
                .debit("Cash:Anne", 900)
                .credit("Dues Received", 1000)
                .post();
    }

    @Test
    public void forgetToPost() throws AccountingException {
        ledger.newPosting(new Date(), "Received $10 from Anne")
                .debit("Cash:Anne", 1000)
                .credit("Dues Received", 1000);

        int cashBalance = ledger.getAccount("Cash").getTrialBalance();
        assertEquals(0, cashBalance);

        int anneBalance = ledger.getAccount("Cash:Anne").getTrialBalance();
        assertEquals(0, anneBalance);

        int duesBalance = ledger.getAccount("Dues Received").getTrialBalance();
        assertEquals(0, duesBalance);
    }

    @Test
    public void makeSureExceptionDoesntPost() {
        try {
            makeUnbalancedPost();
        } catch (AccountingException e) {
            // Exactly
        }

        int cashBalance = ledger.getAccount("Cash").getTrialBalance();
        assertEquals(0, cashBalance);

        int anneBalance = ledger.getAccount("Cash:Anne").getTrialBalance();
        assertEquals(0, anneBalance);

        int duesBalance = ledger.getAccount("Dues Received").getTrialBalance();
        assertEquals(0, duesBalance);

        try {
            makePostWithBadAccount();
        } catch (AccountingException ex) {
            // Exactly
        }

        cashBalance = ledger.getAccount("Cash").getTrialBalance();
        assertEquals(0, cashBalance);

        anneBalance = ledger.getAccount("Cash:Anne").getTrialBalance();
        assertEquals(0, anneBalance);

        duesBalance = ledger.getAccount("Dues Received").getTrialBalance();
        assertEquals(0, duesBalance);
    }
}
