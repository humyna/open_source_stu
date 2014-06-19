package info.zoio.tec.java.quartz.demo.utils;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobUtils {
	private static final Logger logger = LoggerFactory.getLogger(JobUtils.class);
	public static final void printInfo(){
		logger.info("print first job task!");
	}

	public static final void simpleJob(JobExecutionContext context){
		logger.info("run simpleJob[jobname=" + context.getJobDetail().getKey().getName()+"]");
	}
}
