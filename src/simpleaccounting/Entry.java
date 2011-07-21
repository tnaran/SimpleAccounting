/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simpleaccounting;

/**
 * An entry against an account.
 *
 * @author Travers
 */
public class Entry {
    private Posting posting;
    private Account account;
    private int amount;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Posting getPosting() {
        return posting;
    }

    public void setPosting(Posting posting) {
        this.posting = posting;
    }
}
