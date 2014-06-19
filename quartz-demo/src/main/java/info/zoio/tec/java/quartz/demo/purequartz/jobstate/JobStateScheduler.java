package info.zoio.tec.java.quartz.demo.purequartz.jobstate;

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
 * 演示Job接收参数和维护状态
 * 说明：
 * 	 对于单个任务来说：
 *		普通私有成员变量的操作不会影响到下次执行结果，_counter每次执行都是初始值1
 *		JobDataMap容器中保存的favorite color 、count  可以保持状态和参数传递
 * 参数传递和状态处理方式：
 *		1.参数传递：使用job.getJobDataMap().put()方式向Job当中传递参数，JobDataMap类实际上最终继承了实现Map接口的"DirtyFlagMap"类，
 *			而DirtyFlagMap内部又保存了一个HashMap的引用，操作都是针对这个HashMap进行的。
 *		2.JobDataMap的持久化 即PersistJobDataAfterExecution这个注解的使用[在ModParamAndStateJob.java]加上注解之后，每次执行完，JobDataMap都会被序列化，上次任务执行放入的值都会保存下来。
 * 
 * @author humyna
 *
 */
public class JobStateScheduler {
	private static final Logger logger = LoggerFactory.getLogger(JobStateScheduler.class);
	
	public void jobStateScheduler() throws SchedulerException{
		logger.info("JobStateScheduler.jobStateScheduler start...");
		//日期格式化  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		//获得Scheduler对象
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		
		//下一个10秒
		Date startTime = nextGivenSecondDate(null, 10);
		
		//TODO job1
		JobDetail jobDetail1 = newJob(ModParamAndStateJob.class)
				.withIdentity("job1", "group1")
				.build();
		SimpleTrigger simpleTrigger1 = newTrigger()
				.withIdentity("trigger1", "group1")
				.startAt(startTime)
				.withSchedule(
						simpleSchedule().withIntervalInSeconds(5)
						.withRepeatCount(5))
				.build();
		
		jobDetail1.getJobDataMap().put(ModParamAndStateJob.FAVORITE_COLOR, "=======  Green  ========");
		jobDetail1.getJobDataMap().put(ModParamAndStateJob.EXECUTION_COUNT, 1);
		Date sd = scheduler.scheduleJob(jobDetail1, simpleTrigger1);
		logger.info(jobDetail1.getKey().getName() + " 将在 : "  
                + sdf.format(sd) + " 执行, 重复 "  
                + simpleTrigger1.getRepeatCount() + " 次, 每次间隔   "  
                + simpleTrigger1.getRepeatInterval() / 1000 + " 秒");
		
		//TODO job2
		JobDetail jobDetail2 = newJob(ModParamAndStateJob.class)
				.withIdentity("job2", "group1")
				.build();
		SimpleTrigger simpleTrigger2 = newTrigger()
				.withIdentity("trigger2", "group1")
				.startAt(startTime)
				.withSchedule(
						simpleSchedule().withIntervalInSeconds(5)
						.withRepeatCount(6))
				.build();
		
		jobDetail2.getJobDataMap().put(ModParamAndStateJob.FAVORITE_COLOR, "=======  Red  ========");
		jobDetail2.getJobDataMap().put(ModParamAndStateJob.EXECUTION_COUNT, 1);
		Date sd2 = scheduler.scheduleJob(jobDetail2, simpleTrigger2);
		logger.info(jobDetail2.getKey().getName() + " 将在 : "  
                + sdf.format(sd2) + " 执行, 重复 "  
                + simpleTrigger2.getRepeatCount() + " 次, 每次间隔   "  
                + simpleTrigger2.getRepeatInterval() / 1000 + " 秒");
		
		
		scheduler.start();
		logger.info("=========================启动调度(调用start()方法)=========================");
        logger.info("系统启动的时间 :" + sdf.format(new Date()));
		try {
			logger.info("等待2分钟");
			Thread.sleep(120L * 1000L);
		} catch (InterruptedException e) {
		}
		
		scheduler.shutdown(true);
		logger.info("=========================调度已关闭=========================");
		
		SchedulerMetaData schedulerMetaData = scheduler.getMetaData();
	    logger.info("Summary:" + schedulerMetaData.getSummary());
		logger.info("JobStateScheduler.jobStateScheduler end...");
	}

}
