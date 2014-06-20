package info.zoio.tec.java.quartz.demo.purequartz.triggerprioritysheduler;

import info.zoio.tec.java.quartz.demo.utils.JobUtils;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerEchoJob implements Job {
	private static final Logger logger = LoggerFactory.getLogger(TriggerEchoJob.class);
	
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobUtils.triggerEchoHob(context);
	}

}
