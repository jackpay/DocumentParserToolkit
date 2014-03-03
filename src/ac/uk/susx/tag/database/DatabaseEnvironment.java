package ac.uk.susx.tag.database;

import java.io.File;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public final class DatabaseEnvironment {

	private Environment environment;
	private static DatabaseEnvironment self;

	private DatabaseEnvironment() {
		try{
			EnvironmentConfig ec = new EnvironmentConfig();
			ec.setAllowCreate(true);
			environment = new Environment(new File("/database"),ec);
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