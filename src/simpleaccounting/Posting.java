/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simpleaccounting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A group of related account entries
 *
 * @author Travers
 */
public class Posting {
    private Ledger ledger;
    private Date postingDate;
    private String memo;
    private List<Entry> entries;

    public Posting(Ledger l) {
        ledger = l;
        entries = new ArrayList<Entry>();
    }
    
    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Date postingDate) {
        this.postingDate = postingDate;
    }

    public Entry addEntry(Account a, int amount) {
        Entry e = new Entry();
        e.setPosting(this);
        e.setAccount(a);
        e.setAmount(amount);
        return e;
    }

    /**
     * Returns true if the posting is balanced.  The sum of the credits equals
     * the sum of the debits.
     *
     * @return True or false
     */
    public boolean isBalanced() {
        int balance = 0;

        for( Entry e : entries ) {
            balance += e.getAmount();
        }

        return balance == 0;
    }

    protected Posting addEntry(String account, int amount) throws NonExistantAccountException {
        Entry e = new Entry();
        Account a = ledger.getAccount(account);
        if (a != null) {
            e.setAccount(a);
            e.setAmount(amount);
        } else {
            throw new NonExistantAccountException(account);
        }
        entries.add(e);
        
        return this;
    }

    public Posting credit(String account, int amount) throws NonExistantAccountException {
        return addEntry(account, amount);
    }

    public Posting debit(String account, int amount) throws NonExistantAccountException {
        return addEntry(account, -amount);
    }

    void post() throws PostingUnbalancedException {
        if (isBalanced()) {
            for (Entry entry : entries) {
                entry.getAccount().addEntry(entry);
            }
        } else {
            throw new PostingUnbalancedException(this);
        }
    }
}
