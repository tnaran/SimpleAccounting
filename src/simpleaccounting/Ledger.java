/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simpleaccounting;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a set of accounts and their transactions.
 *
 * @author Travers
 */
public class Ledger {
    private Map<String, Account> accounts;
    private List<Posting> postings;

    public Ledger() {
        accounts = new HashMap<String, Account>();
        postings = new ArrayList<Posting>();
    }

    public Account newAccount(String name, Account.Type type) {
        // Split name at ':'
        String names[] = name.split(":");
        
        // Build tree of accounts
        Account lastAccount = null;
        for (int i = 0; i < names.length; i++) {
            String accountName = names[i];

            // Fetch the existing account with names[i]
            Account a = accounts.get(accountName);

            // If account doesn't exist then create it with <names[i], type>
            if( a == null ) {
                a = new Account(accountName, type);
            }

            // Add this account to the ledger or the last account found
            if( i == 0 ) {
                accounts.put(accountName, a);
            } else {
                // Add new account to the previous account
                lastAccount.addSubAccount(a);
            }

            lastAccount = a;
        }

        return lastAccount;
    }

    public Account getAccount(String name) {
        String[] names = name.split(":");

        Account acct = null;
        for (int i = 0; i < names.length; i++) {
            String accountName = names[i];

            if (acct == null) {
                acct = accounts.get(accountName);
            } else {
                acct = acct.getSubAccount(accountName);
            }
        }

        return acct;
    }

    public List<Posting> getPostings() {
        return postings;
    }

    Posting newPosting(Date date, String memo) {
        Posting p = new Posting(this);
        p.setPostingDate(date);
        p.setMemo(memo);
        return p;
    }
}
