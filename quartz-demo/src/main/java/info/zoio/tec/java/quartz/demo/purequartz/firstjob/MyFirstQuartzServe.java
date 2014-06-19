package info.zoio.tec.java.quartz.demo.purequartz.firstjob;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 【技能get】
 * 1.Eclipse快捷键导入静态类中的方法
 * 先设置：window->Preferences->Java->Editor->Content Assist->Favorites,右上方有个new type按钮，把静态方法所在的类名称填写进去就可以了
 * 静态类有：org.quartz.DateBuilder、org.quartz.JobBuilder、org.quartz.SimpleScheduleBuilder、org.quartz.TriggerBuilder
 *
 * @author humyna
 *
 */
public class MyFirstQuartzServe {
	private static final Logger logger = LoggerFactory.getLogger(MyFirstQuartzServe.class);

	public void startScheduler() throws SchedulerException {
		logger.info("初始化 获得 Scheduler对象");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		// 获得 Scheduler 对象
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();


		//demo1
//		JobKey jobKey = new JobKey("");
//		JobDetail jobDetail =  scheduler.getJobDetail(jobKey) ;
//		TriggerKey triggerKey =  new TriggerKey("");
//		Trigger trigger = scheduler.getTrigger(triggerKey);

		//demo2
		//
		Date runTime =  evenMinuteDate(new Date());
		//定义一个Job对象并MyFirstQuartzJob上
		JobDetail jobDetail = newJob(MyFirstQuartzJob.class)
				.withIdentity("job1", "group1")
				.build();
		//定义一个Trigger，startAt方法定义了任务开始的时间
		Trigger trigger = newTrigger()
				.withIdentity("trigger1", "group1")
				.startAt(runTime)
				.build();



		scheduler.scheduleJob(jobDetail, trigger);
		logger.info(jobDetail.getKey() + " 将在: " + sdf.format(runTime) +" 运行");

		//Start
		scheduler.start();
		logger.info("作业调度已经启动");
		logger.info("等待 70秒,保证下一个整数分钟出现");
		 try {
            //等待70秒，保证下一个整数分钟出现，
			//注意:如果主线程停止，任务是不会执行的
            Thread.sleep(70L * 1000L);
        } catch (Exception e) {
        	//TODO
        }

		//stutdown
		scheduler.shutdown();
		logger.info("作业调度已经关闭");
	}

}
