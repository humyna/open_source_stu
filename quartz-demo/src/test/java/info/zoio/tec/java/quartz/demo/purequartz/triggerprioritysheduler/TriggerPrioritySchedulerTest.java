package info.zoio.tec.java.quartz.demo.purequartz.triggerprioritysheduler;

import info.zoio.tec.java.quartz.demo.purequartz.simpletrigger.SimpleSchedulerTest;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerPrioritySchedulerTest {

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
	public void testSchedule() throws SchedulerException {
		TriggerPriorityScheduler trs = new TriggerPriorityScheduler();
		trs.schedule();
	}

}
