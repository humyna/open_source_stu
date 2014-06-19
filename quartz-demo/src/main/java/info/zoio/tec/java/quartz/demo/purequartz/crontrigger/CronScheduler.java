package info.zoio.tec.java.quartz.demo.purequartz.crontrigger;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 演示CronTrigger的使用
 * 
 * 通配符说明:
 * *表示所有值. 例如:在分的字段上设置 "*",表示每一分钟都会触发。
 * ? 表示不指定值。使用的场景为不需要关心当前设置这个字段的值。例如:要在每月的10号触发一个操作，但不关心是周几，所以需要周位置的那个字段设置为"?" 具体设置为 0 0 0 10* ?
 * - 表示区间。例如 在小时上设置 "10-12",表示 10,11,12点都会触发。
 * , 表示指定多个值，例如在周字段上设置 "MON,WED,FRI" 表示周一，周三和周五触发
 * /用于递增触发。如在秒上面设置"5/15" 表示从5秒开始，每增15秒触发(5,20,35,50)。 在月字段上设置'1/3'所示每月1号开始，每隔三天触发一次。
 * L 表示最后的意思。在日字段设置上，表示当月的最后一天(依据当前月份，如果是二月还会依据是否是润年[leap]), 在周字段上表示星期六，相当于"7"或"SAT"。如果在"L"前加上数字，则表示该数据的最后一个。例如在周字段上设置"6L"这样的格式,则表示“本月最后一个星期五"
 * W 表示离指定日期的最近那个工作日(周一至周五). 例如在日字段上设置"15W"，表示离每月15号最近的那个工作日触发。如果15号正好是周六，则找最近的周五(14号)触发, 如果15号是周未，则找最近的下周一(16号)触发.如果15号正好在工作日(周一至周五)，则就在该天触发。如果指定格式为 "1W",它则表示每月1号往后最近的工作日触发。如果1号正是周六，则将在3号下周一触发。(注，"W"前只能设置具体的数字,不允许区间"-").
 * 
 * 小提示       'L'和 'W'可以一组合使用。如果在日字段上设置"LW",则表示在本月的最后一个工作日触发
 * # 序号(表示每月的第几个周几)，例如在周字段上设置"6#3"表示在每月的第三个周六.注意如果指定"#5",正好第五周没有周六，则不会触发该配置(用在母亲节和父亲节再合适不过了)
 * 小提示 :周字段的设置，若使用英文字母是不区分大小写的 MON 与mon相同.
 * 
 * @author humyna
 *
 */
public class CronScheduler {
	private static final Logger logger = LoggerFactory.getLogger(CronScheduler.class);
	
	public void cronScheduler() throws SchedulerException{
		logger.info("SimpleScheduler.simpleScheduler start...");
		//日期格式化  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
		//获得 Scheduler对象
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		
		//TODO job1 每20s运行一次  
		JobDetail job = newJob(CronJob.class)
				.withIdentity("job1", "group1")
				.build();
		
		//cronTrigger
		CronTrigger cronTrigger = newTrigger()
				.withIdentity("trigger1", "group1")
				.withSchedule(cronSchedule("0/20 * * * * ?"))
				.build();
		
		Date sd = scheduler.scheduleJob(job, cronTrigger);
		logger.info(job.getKey().getName() + " 将在: "  
                + sdf.format(sd) + " 运行. \r基于Cron表达式 : "  
                + cronTrigger.getCronExpression() + "(含义:每20s运行一次)");
		
		//TODO job2偶数分钟每隔15秒
		job = newJob(CronJob.class)
				.withIdentity("job2", "group1")
				.build();
		
		//cronTrigger
		cronTrigger = newTrigger()
				.withIdentity("trigger2", "group1")
				.withSchedule(cronSchedule("15 0/2 * * * ?"))
				.build();
		sd = scheduler.scheduleJob(job, cronTrigger);
		logger.info(job.getKey().getName() + " 将在: "  
                + sdf.format(sd) + " 运行. \r基于Cron表达式 : "  
                + cronTrigger.getCronExpression() + "(含义:偶数分钟每隔15秒运行一次)");
		
		//TODO job3 9-18时，每偶数分钟
		job = newJob(CronJob.class)
				.withIdentity("job3", "group1")
				.build();
		
		//cronTrigger
		cronTrigger = newTrigger()
				.withIdentity("trigger3", "group1")
				.withSchedule(cronSchedule("0 0/2 9-1 * * ?"))
				.build();
		sd = scheduler.scheduleJob(job, cronTrigger);
		logger.info(job.getKey().getName() + " 将在: "  
                + sdf.format(sd) + " 运行. \r基于Cron表达式 : "  
                + cronTrigger.getCronExpression() + "(含义:9-18时，每偶数分钟运行一次)");
			
		//TODO job4 9-12时，每3分钟
		job = newJob(CronJob.class)
				.withIdentity("job4", "group1")
				.build();
		
		//cronTrigger
		cronTrigger = newTrigger()
				.withIdentity("trigger4", "group1")
				.withSchedule(cronSchedule("0 0/3 9-12 * * ?"))
				.build();
		sd = scheduler.scheduleJob(job, cronTrigger);
		logger.info(job.getKey().getName() + " 将在: "  
                + sdf.format(sd) + " 运行. \r基于Cron表达式 : "  
                + cronTrigger.getCronExpression() + "(含义:9-12时，每3分钟运行一次)");
		
		//TODO job5 每个月的1号，15号上午10点运行
		job = newJob(CronJob.class)
				.withIdentity("job5", "group1")
				.build();
		
		//cronTrigger
		cronTrigger = newTrigger()
				.withIdentity("trigger5", "group1")
				.withSchedule(cronSchedule("0 0 10am 1,15 * ?"))
				.build();
		sd = scheduler.scheduleJob(job, cronTrigger);
		logger.info(job.getKey().getName() + " 将在: "  
                + sdf.format(sd) + " 运行. \r基于Cron表达式 : "  
                + cronTrigger.getCronExpression() + "(含义:每个月的1号，15号上午10点各运行一次)");
		
		//TODO job6 周一到周五，每隔30秒运行一次
		job = newJob(CronJob.class)
				.withIdentity("job6", "group1")
				.build();
		
		//cronTrigger
		cronTrigger = newTrigger()
				.withIdentity("trigger6", "group1")
				.withSchedule(cronSchedule("0,30 * * ? * MON-FRI"))
				.build();
		sd = scheduler.scheduleJob(job, cronTrigger);
		logger.info(job.getKey().getName() + " 将在: "  
                + sdf.format(sd) + " 运行. \r基于Cron表达式 : "  
                + cronTrigger.getCronExpression() + "(含义:周一到周五，每隔30秒运行一次)");
	
		//TODO job7周六周日，每隔30秒运行一次
		job = newJob(CronJob.class)
				.withIdentity("job7", "group1")
				.build();
		
		//cronTrigger
		cronTrigger = newTrigger()
				.withIdentity("trigger7", "group1")
				.withSchedule(cronSchedule("0,30 * * ? * SAT,SUN"))
				.build();
		sd = scheduler.scheduleJob(job, cronTrigger);
		logger.info(job.getKey().getName() + " 将在: "  
                + sdf.format(sd) + " 运行. \r基于Cron表达式 : "  
                + cronTrigger.getCronExpression() + "(含义:周六,周日  每30秒运行 )");
		
		// 所有的任务都被加入到了 scheduler中 ,但只有 schedulers.start(); 时才开始执行  
		scheduler.start();  
		logger.info("=========================启动调度(调用start()方法)=========================");
		logger.info("系统启动的时间 :" + sdf.format(new Date()));
		
		try {  
	    	   logger.info("等待5分钟...");  
	           Thread.sleep(300L * 1000L);  
        } catch (Exception e) { } 
       
        scheduler.shutdown(true);
        logger.info("=========================调度已关闭=========================");
       
        SchedulerMetaData schedulerMetaData = scheduler.getMetaData();
        logger.info("Summary:" + schedulerMetaData.getSummary());
	}
}
