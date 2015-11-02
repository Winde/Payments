package reader;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import main.ApplicationTestModule;
import model.dataobjects.User;
import model.dataobjects.reader.EVOReader;
import model.statistics.Movements;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTestModule.class)
//@ContextConfiguration
//@WebAppConfiguration
public class EVOReaderTest {
	
	public static final String EMPTY_FILE = "";
	public static final String INCOMPLETE_COLUMNS_CSV = "example;example";

	@Autowired
	private EVOReader reader;
	
	
	
//	private MockMvc mvc;

	@Before
	public void setUp() throws Exception {	
	}

	@Test
	public void testFileEmpty() throws Exception {
		InputStream emptyFileStream = new ByteArrayInputStream(EMPTY_FILE.getBytes(StandardCharsets.UTF_8));
		User anyUser = new User();
		
		Movements result = reader.getMovements(anyUser, emptyFileStream);
		
		assertTrue(result.getPayments().isEmpty());
		assertTrue(result.getIncomeEntries().isEmpty());
	}
	
	@Test
	public void testIncompleteColumns() throws Exception {
		InputStream emptyFileStream = new ByteArrayInputStream(INCOMPLETE_COLUMNS_CSV.getBytes(StandardCharsets.UTF_8));
		User anyUser = new User();
		
		Movements result = reader.getMovements(anyUser, emptyFileStream);
		
		assertTrue(result.getPayments().isEmpty());
		assertTrue(result.getIncomeEntries().isEmpty());
	}
	
}
