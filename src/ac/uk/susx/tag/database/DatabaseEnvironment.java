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
	private Transaction tnx;

	private DatabaseEnvironment(String databaseLoc) {
		this();
		this.databaseLoc = databaseLoc;
	}
	
	private DatabaseEnvironment() {
		tnx = null;
		try{
			EnvironmentConfig ec = new EnvironmentConfig();
			ec.setTxnTimeout(300000L);
			ec.setLockTimeout(300000L); // Set to 5mins max timeout.
			ec.setCachePercent(25);
			ec.setAllowCreate(true);
			ec.setTransactional(true);
			environment = new Environment(new File(databaseLoc),ec);
			TransactionConfig tc = new TransactionConfig();
			tc.setReadCommitted(true);
			tnx = environment.beginTransaction(null, tc);
			
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
				tnx.commit();
				environment.cleanLog();
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
	
	/**
	 * Returns a database instance at a given location adhering to the singleton pattern.
	 * If one already exists at another location it is closed and a new one opened at the new location.
	 * @param databaseLoc
	 * @return DatabaseEnvironment singleton currently running.
	 */
	public static DatabaseEnvironment getInstance(String databaseLoc) {
		if(self == null) {
			self = new DatabaseEnvironment(databaseLoc);
		}
		else {
			self.close();
			self = new DatabaseEnvironment(databaseLoc); 
		}
		return self;
	}
}