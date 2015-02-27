package model.dataobjects.reader;

import java.io.InputStream;

import model.dataobjects.User;
import model.statistics.Movements;

public interface StatementReader {

	
	public  Movements getMovements(User account, InputStream data);
}
