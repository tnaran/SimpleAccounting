/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simpleaccounting;

/**
 *
 * @author Travers
 */
public class NonExistantAccountException extends AccountingException {

    public NonExistantAccountException(String accountName) {
        super(
                String.format(
                    "Account '%s' is not in the ledger",
                    accountName
                ));
    }
}
