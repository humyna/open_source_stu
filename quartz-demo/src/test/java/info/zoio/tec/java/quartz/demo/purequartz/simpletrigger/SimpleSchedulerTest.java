package info.zoio.tec.java.quartz.demo.purequartz.simpletrigger;

import info.zoio.tec.java.quartz.demo.purequartz.simpletrigger.SimpleScheduler;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleSchedulerTest {
	private static final Logger logger = LoggerFactory.getLogger(SimpleSchedulerTest.class);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		logger.info("start运行一次...");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		logger.info("end运行一次...");
	}

	@Before
	public void setUp() throws Exception {
		logger.info("start运行多次...");

	}

	@Test
	public void testSimpleScheduler() throws SchedulerException {
		SimpleScheduler simpleScheduler = new SimpleScheduler();
		simpleScheduler.simpleScheduler();
	}
}
