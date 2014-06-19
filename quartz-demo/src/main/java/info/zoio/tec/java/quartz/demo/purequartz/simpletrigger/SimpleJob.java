package info.zoio.tec.java.quartz.demo.purequartz.simpletrigger;

import info.zoio.tec.java.quartz.demo.utils.JobUtils;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SimpleJob implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobUtils.simpleJob(context);
	}

}
