package info.zoio.tec.java.quartz.demo.purequartz;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

public class MyFirstQuartzServe {

	protected void startScheduler() throws SchedulerException {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		JobKey jobKey = new JobKey("");
		JobDetail jobDetail =  scheduler.getJobDetail(jobKey) ;
		TriggerKey triggerKey =  new TriggerKey("");
		Trigger trigger = scheduler.getTrigger(triggerKey);

		scheduler.scheduleJob(jobDetail, trigger);
		scheduler.start();
	}

}
