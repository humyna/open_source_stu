package info.zoio.tec.java.quartz.demo.purequartz.simplejob;


import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.JobKey.jobKey;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 演示SimpleTrigger的使用
 * 
 * @author humyna
 */
public class SimpleScheduler {
	private static final Logger logger =  LoggerFactory.getLogger(SimpleScheduler.class);
	public void simpleScheduler() throws SchedulerException{
		logger.info("SimpleScheduler.simpleScheduler start...");
		//日期格式化  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
		//获得 Scheduler对象
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		
		//下一个第15秒 例:  
        //   当前 10秒,则 执行时间为15秒  
        //   当前 16秒,则 执行时间为30秒  
        //   当前 31秒,则 执行时间为45秒  
        //   当前 46秒,则 执行时间为00秒  
        Date startTime = DateBuilder.nextGivenSecondDate(null, 15); 
        
        //TODO job1 执行一次
        JobDetail simpleJob = newJob(SimpleJob.class)
        		.withIdentity("job1", "group1")
        		.build();
        SimpleTrigger simpleTrigger = (SimpleTrigger)newTrigger()
        		.withIdentity("trigger1","group1")
        		.startAt(startTime)
        		.build();
        
        //把job1和 trigger加入计划 
        //sd:此作业开始执行的时间 
        Date sd = scheduler.scheduleJob(simpleJob, simpleTrigger);
        logger.info(simpleJob.getKey().getName() + " 将在 : " + sdf.format(sd) + " 时运行.重复"  
                + simpleTrigger.getRepeatCount() + " 次, 每次间隔 "  
                + simpleTrigger.getRepeatInterval() / 1000 + " 秒");
        
        //TODO job2 和job1相同，只执行一次
        simpleJob = newJob(SimpleJob.class)
        		.withIdentity("job2", "group1")
        		.build();
        simpleTrigger = (SimpleTrigger)newTrigger()
        		.withIdentity("trigger2","group1")
        		.startAt(startTime)
        		.build();
        
        //把job2和 trigger加入计划 
        sd = scheduler.scheduleJob(simpleJob, simpleTrigger);
        logger.info(simpleJob.getKey().getName() + " 将在 : " + sdf.format(sd) + " 时运行.重复"  
                + simpleTrigger.getRepeatCount() + " 次, 每次间隔 "  
                + simpleTrigger.getRepeatInterval() / 1000 + " 秒");
        
        //TODO job3 执行11次(执行1次重复10次)，每10秒重复一次
        simpleJob = newJob(SimpleJob.class)
        		.withIdentity("job3", "group1")
        		.build();
        simpleTrigger = (SimpleTrigger)newTrigger()
        		.withIdentity("trigger3", "group3")
        		.startAt(startTime)
        		.withSchedule(
        				simpleSchedule()
        				.withIntervalInSeconds(10)
        				.withRepeatCount(10)
        				)
        		.build();
        //把job3和 trigger加入计划 
        sd = scheduler.scheduleJob(simpleJob, simpleTrigger);
        logger.info(simpleJob.getKey().getName() + " 将在 : " + sdf.format(sd) + " 时运行.重复 "  
                + simpleTrigger.getRepeatCount() + " 次, 每次间隔 "  
                + simpleTrigger.getRepeatInterval() / 1000 + " 秒");
        
        
        //TODO trigger3 每隔10s重复.共重复2次  
        //说明：上面job3已经 设定了 trigger3重复10次,每次10s  
        //    在这里又改变了 trigger3的设置,不会对前面的设置构成影响,而是当做一个新的来处理  
        simpleJob = newJob(SimpleJob.class)
        		.withIdentity("job4", "group1")
        		.build();
        simpleTrigger = newTrigger()
        		.withIdentity("trigger3", "group2")
        		.startAt(startTime)
        		.withSchedule(simpleSchedule()
        				.withIntervalInSeconds(10)
        				.withRepeatCount(2)
        				)
        		.build();
//       sd = scheduler.scheduleJob(simpleTrigger);//org.quartz.SchedulerException: Trigger's related Job's name cannot be null
       sd = scheduler.scheduleJob(simpleJob,simpleTrigger);
       logger.info("改变过trigger3属性:["+ simpleJob.getKey().getName() + "]在" + sdf.format(sd) + " 时运行;重复  "  
                + simpleTrigger.getRepeatCount() + " 次, 每次间隔 "  
                + simpleTrigger.getRepeatInterval() / 1000 + " 秒");
       
       //TODO job5 将在2分钟后运行一次
       simpleJob = newJob(SimpleJob.class)
       		.withIdentity("job5", "group1")
       		.build();
       simpleTrigger = (SimpleTrigger)newTrigger()
       		.withIdentity("trigger5","group1")
       		.startAt(futureDate(2, IntervalUnit.MINUTE))
       		.build();
       
       sd = scheduler.scheduleJob(simpleJob, simpleTrigger);
       logger.info(simpleJob.getKey().getName() + " 将在 : " + sdf.format(sd) + " 时运行.重复 "  
               + simpleTrigger.getRepeatCount() + " 次, 每次间隔 "  
               + simpleTrigger.getRepeatInterval() / 1000 + " 秒");
       
       
       //TODO job6
       simpleJob = newJob(SimpleJob.class)
          		.withIdentity("job6", "group1")
          		.build();
       simpleTrigger = (SimpleTrigger)newTrigger()  
    		   .withIdentity("trigger6", "group1")  
    		   .startAt(startTime)  
    		   .withSchedule(simpleSchedule()
    				   .withIntervalInSeconds(40)  
                       .repeatForever())
               .build();  
       sd = scheduler.scheduleJob(simpleJob, simpleTrigger);
       logger.info(simpleJob.getKey().getName() + " 将在 : " + sdf.format(sd) + " 时运行.重复 "  
               + simpleTrigger.getRepeatCount() + " 次, 每次间隔 "  
               + simpleTrigger.getRepeatInterval() / 1000 + " 秒");
       
       // 所有的任务都被加入到了 scheduler中 ,但只有 schedulers.start(); 时才开始执行  
       scheduler.start();  
       logger.info("=========================启动调度(调用start()方法)=========================");
       logger.info("系统启动的时间 :" + sdf.format(new Date()));
       
       
       //TODO  在 scheduler.start()之后,还可以将 jobs添加到执行计划中
       // job7 将重复3次 ,每1分钟重复一次  
       simpleJob = newJob(SimpleJob.class)
    		   .withIdentity("job7", "group1")
    		   .build();  
       simpleTrigger = newTrigger()  
               .withIdentity("trigger7", "group1")  
               .startAt(startTime)  
               .withSchedule(  
                       simpleSchedule()  
                       .withIntervalInMinutes(1) // 1分钟   
                       .withRepeatCount(3))     // 重复3次  
               .build(); 
       sd = scheduler.scheduleJob(simpleJob, simpleTrigger);
       logger.info(simpleJob.getKey().getName() + " 将在 : " + sdf.format(sd) + " 时运行.重复 "  
               + simpleTrigger.getRepeatCount() + " 次, 每次间隔 "  
               + simpleTrigger.getRepeatInterval() / 1000 + " 秒");
       
       //TODO  job8可以立即执行.无trigger注册  
       simpleJob = newJob(SimpleJob.class)
    		   .withIdentity("job8", "group1")  
               .storeDurably()
               .build();  
       scheduler.addJob(simpleJob, true);  
       logger.info("手动触发  job8...(立即执行)");  
       scheduler.triggerJob(jobKey("job8", "group1"));  
       
       try {  
    	   logger.info("等待30 秒...");  
           Thread.sleep(30L * 1000L);  
       } catch (Exception e) { } 
       
       //TODO
       // job7 将马上执行,重复10次,每秒5次  
       logger.info("------------------重新安排 ...------------------");  
       simpleTrigger = newTrigger()  
               .withIdentity("trigger7", "group1")  
               .startAt(startTime)  
               .withSchedule(  
                       simpleSchedule().withIntervalInSeconds(5)  
                               .withRepeatCount(10)).build();  
       scheduler.rescheduleJob(simpleTrigger.getKey(), simpleTrigger);
       logger.info("job7被重新安排 在 : " + sdf.format(sd) +"  执行. \r"
       		+ "当前时间 :" + sdf.format(new Date())+"预定执行时间已过,任务立即执行");
       
       try {  
    	   logger.info("等待10分钟...");  
           Thread.sleep(600L * 1000L);  
       } catch (Exception e) { } 
       
       scheduler.shutdown(true);
       logger.info("=========================调度已关闭=========================");
       
       SchedulerMetaData schedulerMetaData = scheduler.getMetaData();
       logger.info("Summary:" + schedulerMetaData.getSummary());
	}
}
