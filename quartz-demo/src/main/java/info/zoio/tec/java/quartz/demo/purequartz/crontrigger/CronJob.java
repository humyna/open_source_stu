package info.zoio.tec.java.quartz.demo.purequartz.crontrigger;

import info.zoio.tec.java.quartz.demo.utils.JobUtils;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CronJob implements Job{
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobUtils.cronJob(context);
	}
}
