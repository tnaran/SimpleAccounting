/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simpleaccounting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Travers
 */
public class Account {

    public enum Type {
        ASSET(-1),
        LIABILITY(1),
        INCOME(1),
        EXPENSE(-1);

        private int normalBalanceSign;

        private Type(int normalBalanceSign) {
            this.normalBalanceSign = normalBalanceSign;
        }

        /**
         * Returns the usual sign for this type of account.  Normally
         * debit accounts (like Assets and Expenses) are negative (-1),
         * normally credit accounts are positive (1).
         *
         * @return -1 or 1
         */
        public int getNormalBalanceSign() {
            return normalBalanceSign;
        }
    }

    protected static class AccountName {
        public String firstName;
        public String restOfName;
    }

    
    private String name;
    private Type type;
    protected List<Entry> entries;
    private Map<String, Account> subAccounts;

    static private AccountName toAccountName(String name) {
        AccountName acctName = new AccountName();
        acctName.restOfName = null;
        acctName.firstName = name;
        int separator = name.indexOf(':');
        if (separator > 0) {
            acctName.firstName = name.substring(0, separator);
            acctName.restOfName = name.substring(separator + 1);
        } else if (separator == 0) {
            acctName.firstName = name.substring(1);
        }
        return acctName;
    }

    public Account(String name, Type type) {
        AccountName acctName = toAccountName(name);
        
        this.name = acctName.firstName;
        this.type = type;
        this.entries = new ArrayList<Entry>();
        this.subAccounts = new HashMap<String, Account>();

        if (acctName.restOfName != null) {
            Account sub = new Account(acctName.restOfName, type);
            subAccounts.put(sub.getName(), sub);
        }
    }

    public List<Entry> getEntries() {
        List<Entry> allEntries = new ArrayList<Entry>(entries);

        for(Account a : subAccounts.values()) {
            allEntries.addAll(a.getEntries());
        }

        return allEntries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    protected void addEntry(Entry e) {
        this.entries.add(e);
    }

    void addSubAccount(Account a) {
        subAccounts.put(a.getName(), a);
    }

    public Account getSubAccount(String accountName) {
        return subAccounts.get(accountName);
    }

    public int getTrialBalance() {
        int balance = 0;

        for( Entry e : getEntries() ) {
            balance += e.getAmount();
        }

        return balance;
    }
}
