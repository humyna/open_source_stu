package info.zoio.tec.java.quartz.demo.purequartz.triggerprioritysheduler;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 
 * 演示trigger优先级的使用
 * 数值越大，优先级越高
 * 
 * @author humyna
 *
 */
public class TriggerPriorityScheduler {
	public void schedule() throws SchedulerException{
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		Date startTime = futureDate(5, IntervalUnit.SECOND);
		
		JobDetail job = newJob(TriggerEchoJob.class).withIdentity("job", "group1").build();
		
		Trigger trigger1 = newTrigger()
				.withIdentity("PriorityNeg5Trigger5SecondRepeat")
				.startAt(startTime)
				.withSchedule(simpleSchedule()
						.withRepeatCount(5)
						.withIntervalInSeconds(5))
				.withPriority(1)
				.forJob(job)
				.build();
		
		Trigger trigger2 = newTrigger()
				.withIdentity("Priority5Trigger10SecondRepeat")
				.startAt(startTime)
				.withSchedule(simpleSchedule().withRepeatCount(1).withIntervalInSeconds(10))
				.forJob(job)
				.build();
		
		Trigger trigger3 = newTrigger()
				.withIdentity("Priority10Trigger15SecondRepeat")
				.startAt(startTime)
				.withSchedule(simpleSchedule().withRepeatCount(1).withIntervalInSeconds(15))
				.withPriority(10)
				.forJob(job)
				.build();
		
		scheduler.scheduleJob(job, trigger1);
		scheduler.scheduleJob(trigger2);
		scheduler.scheduleJob(trigger3);
		
		scheduler.start();
		
		try {
			Thread.sleep(30L * 1000L);
		} catch (InterruptedException e) { }
		
		//Note: passingtrueinto theshutdownmessage tells the Quartz Scheduler to wait until all jobs have completed
		//running before returning from the method call.
		scheduler.shutdown(true);
	}
}
