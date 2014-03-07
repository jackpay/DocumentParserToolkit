package ac.uk.susx.tag.database;

import java.io.File;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.Transaction;
import com.sleepycat.je.TransactionConfig;

public final class DatabaseEnvironment {

	private Environment environment;
	private static DatabaseEnvironment self;
	private String databaseLoc = "database";

	private DatabaseEnvironment(String databaseLoc) {
		this.databaseLoc = databaseLoc;
	}
	
	private DatabaseEnvironment() {
		try{
			EnvironmentConfig ec = new EnvironmentConfig();
			ec.setAllowCreate(true);
			ec.setTransactional(true);
			environment = new Environment(new File(databaseLoc),ec);
			TransactionConfig txnConfig = new TransactionConfig();
			txnConfig.setReadCommitted(true);
			Transaction txn = environment.beginTransaction(null, txnConfig);
			
		}
		catch (DatabaseException dbe){
			dbe.printStackTrace();
		}
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void close() {
		if (environment != null) {
			try {
				environment.close();
			}
			catch (DatabaseException dbe) {
				dbe.printStackTrace();
			}
		}
	}

	public static DatabaseEnvironment getInstance() {
		if(self == null) {
			self = new DatabaseEnvironment();
		}
		return self;
	}
}