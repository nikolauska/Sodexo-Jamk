package fi.tanik.harjoitustyo_ruokalista;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;

/*
 * 	This class holds information of username from this phone
 */

public class UsernameHandler implements Serializable {
	private static final long serialVersionUID = 6341471678970442198L;
	/***/
	public String Username;
	
	// Get username from phone for saving purposes to database
	public UsernameHandler(Activity activity) {
		AccountManager manager = AccountManager.get(activity.getApplication());
		Account[] accounts = manager.getAccountsByType("com.google");
		List<String> possibleEmails = new LinkedList<String>();

		for (Account account : accounts) {
			possibleEmails.add(account.name);
		}

		if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
			String email = possibleEmails.get(0);
			String[] parts = email.split("@");
			    
			if (parts.length > 0 && parts[0] != null)
				Username = parts[0];
			else
			    Username = "";    
		} else
			Username = "";
	}
}
