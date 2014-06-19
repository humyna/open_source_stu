package info.zoio.tec.java.quartz.demo.purequartz.jobexceptionscheduler;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 演示 Quartz如何处理从job中 抛出的JobExecutionException
 * 本例通过BadJob1中的自动修复和BadJob2中的从调度器中移除相应的触发器的方法处理异常
 *
 * @author humyna
 *
 */
public class JobExceptionScheduler {
	private static final Logger logger = LoggerFactory.getLogger(JobExceptionScheduler.class);

	public void jobExceptionSchedule() throws SchedulerException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		Date startTime = nextGivenSecondDate(null,15);

		//TODO Job1
		JobDetail badjob = newJob(BadJob1.class)
				.withIdentity("badjob1", "group1")
				.usingJobData("denominator", 0)
				.build();

		SimpleTrigger simpleTrigger = newTrigger()
				.startAt(startTime).withIdentity("trigger1", "group1")
				.withSchedule(
						simpleSchedule()
						.withIntervalInSeconds(10)
						.repeatForever())
				.build();

		Date sd = scheduler.scheduleJob(badjob, simpleTrigger);
		logger.info(badjob.getKey().getName() + " 将在 : "
                + sdf.format(sd) + " 执行, 重复 "
                + simpleTrigger.getRepeatCount() + " 次, 每次间隔   "
                + simpleTrigger.getRepeatInterval() / 1000 + " 秒");

		//TODO Job2
		badjob = newJob(BadJob2.class)
				.withIdentity("badjob2", "group1")
				.build();

		simpleTrigger = newTrigger()
				.startAt(startTime).withIdentity("trigger2", "group1")
				.withSchedule(
						simpleSchedule()
						.withIntervalInSeconds(10)
						.repeatForever())
				.build();

		sd = scheduler.scheduleJob(badjob, simpleTrigger);

		logger.info(badjob.getKey().getName() + " 将在 : "
                + sdf.format(sd) + " 执行, 重复 "
                + simpleTrigger.getRepeatCount() + " 次, 每次间隔   "
                + simpleTrigger.getRepeatInterval() / 1000 + " 秒");


		scheduler.start();
		logger.info("=========================启动调度(调用start()方法)=========================");
        logger.info("系统启动的时间 :" + sdf.format(new Date()));
		try {
			logger.info("等待30秒");
			Thread.sleep(30L * 1000L);
		} catch (InterruptedException e) {
		}

		scheduler.shutdown(false);
		logger.info("=========================调度已关闭=========================");

		SchedulerMetaData schedulerMetaData = scheduler.getMetaData();
	    logger.info("Summary:" + schedulerMetaData.getSummary());

		logger.info("MisfireScheduler.misfireScheduler end...");
	}
}
