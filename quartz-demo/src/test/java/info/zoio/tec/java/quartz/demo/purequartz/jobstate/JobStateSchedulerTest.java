package info.zoio.tec.java.quartz.demo.purequartz.jobstate;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobStateSchedulerTest {
private static final Logger logger = LoggerFactory.getLogger(JobStateSchedulerTest.class);

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
	public void testJobStateScheduler() throws SchedulerException {
		JobStateScheduler jobStateScheduler = new JobStateScheduler();
		jobStateScheduler.jobStateScheduler();
	}

}
