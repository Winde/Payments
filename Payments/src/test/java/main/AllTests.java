package main;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import reader.EVOReaderTest;
import sample.SampleMockTest;

@RunWith(Suite.class)
@SuiteClasses({
	EVOReaderTest.class,
	SampleMockTest.class	

})
public class AllTests {

}
