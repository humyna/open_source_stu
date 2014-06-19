package info.zoio.tec.java.quartz.demo.purequartz.misfire;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

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
 * 演示Quartz对于Misfire的作业采取不同策略的执行情况
 * 可通过单独执行调度作业(执行job1时，注释掉job2)观察执行结果中作业的处理时间间隔来理解不同策略的含义
 *
 * Quartz的 Misfire处理规则:
 * 调度(scheduleJob)或恢复调度(resumeTrigger,resumeJob)后不同的misfire对应的处理规则
 *
 * CronTrigger
 *
 * 			withMisfireHandlingInstructionDoNothing
 * 			——不触发立即执行
 * 			——等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
 *
 * 			withMisfireHandlingInstructionIgnoreMisfires
 * 			——以错过的第一个频率时间立刻开始执行
 * 			——重做错过的所有频率周期后
 * 			——当下一次触发频率发生时间大于当前时间后，再按照正常的Cron频率依次执行
 *
 * 			withMisfireHandlingInstructionFireAndProceed
 * 			——以当前时间为触发频率立刻触发一次执行
 * 			——然后按照Cron频率依次执行
 *
 *
 * 	SimpleTrigger
 *
 * 			withMisfireHandlingInstructionFireNow
 * 			——以当前时间为触发频率立即触发执行
 * 			——执行至FinalTIme的剩余周期次数
 * 			——以调度或恢复调度的时刻为基准的周期频率，FinalTime根据剩余次数和当前时间计算得到
 * 			——调整后的FinalTime会略大于根据starttime计算的到的FinalTime值
 *
 * 			withMisfireHandlingInstructionIgnoreMisfires
 * 			——以错过的第一个频率时间立刻开始执行
 * 			——重做错过的所有频率周期
 * 			——当下一次触发频率发生时间大于当前时间以后，按照Interval的依次执行剩下的频率
 * 			——共执行RepeatCount+1次
 *
 * 			withMisfireHandlingInstructionNextWithExistingCount
 * 			——不触发立即执行
 * 			——等待下次触发频率周期时刻，执行至FinalTime的剩余周期次数
 * 			——以startTime为基准计算周期频率，并得到FinalTime
 * 			——即使中间出现pause，resume以后保持FinalTime时间不变
 *
 *
 * 			withMisfireHandlingInstructionNowWithExistingCount
 * 			——以当前时间为触发频率立即触发执行
 * 			——执行至FinalTIme的剩余周期次数
 * 			——以调度或恢复调度的时刻为基准的周期频率，FinalTime根据剩余次数和当前时间计算得到
 * 			——调整后的FinalTime会略大于根据starttime计算的到的FinalTime值
 *
 * 			withMisfireHandlingInstructionNextWithRemainingCount
 * 			——不触发立即执行
 * 			——等待下次触发频率周期时刻，执行至FinalTime的剩余周期次数
 * 			——以startTime为基准计算周期频率，并得到FinalTime
 * 			——即使中间出现pause，resume以后保持FinalTime时间不变
 *
 * 			withMisfireHandlingInstructionNowWithRemainingCount
 * 			——以当前时间为触发频率立即触发执行
 * 			——执行至FinalTIme的剩余周期次数
 * 			——以调度或恢复调度的时刻为基准的周期频率，FinalTime根据剩余次数和当前时间计算得到
 * 			——调整后的FinalTime会略大于根据starttime计算的到的FinalTime值
 * 			MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT
 * 			——此指令导致trigger忘记原始设置的starttime和repeat-count
 * 			——触发器的repeat-count将被设置为剩余的次数
 * 			——这样会导致后面无法获得原始设定的starttime和repeat-count值
 *
 * @author humyna
 *
 */
public class MisfireScheduler {
	private static final Logger logger = LoggerFactory.getLogger(MisfireScheduler.class);

	public void misfireScheduler() throws SchedulerException{
		logger.info("MisfireScheduler.misfireScheduler start...");
		//日期格式化
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		//获得Scheduler对象
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		//下一个15秒
		Date startTime = nextGivenSecondDate(null, 15);

		//TODO job1  根据策略作业会每隔12s执行一次
		JobDetail jobDetail1 = newJob(StatefulDumbJob.class)
				.withIdentity("job1", "group1")
				.usingJobData(StatefulDumbJob.EXECUTION_DELAY,10000l)	 // 设置参数:休眠时间 10s
				.build();
		SimpleTrigger simpleTrigger1 = newTrigger()
				.withIdentity("trigger1", "group1")
				.startAt(startTime)
				.withSchedule(
						simpleSchedule().withIntervalInSeconds(3)
						.repeatForever())
				.build();
		Date sd = scheduler.scheduleJob(jobDetail1, simpleTrigger1);
		logger.info(jobDetail1.getKey().getName() + " 将在 : "
                + sdf.format(sd) + " 执行, 重复 "
                + simpleTrigger1.getRepeatCount() + " 次, 每次间隔   "
                + simpleTrigger1.getRepeatInterval() / 1000 + " 秒");

		//TODO job2 根据策略作业会每隔10s执行一次
		jobDetail1 = newJob(StatefulDumbJob.class)
				.withIdentity("job2", "group1")
				.usingJobData(StatefulDumbJob.EXECUTION_DELAY,10000l)	 // 设置参数:休眠时间 10s
				.build();
		simpleTrigger1 = newTrigger()
				.withIdentity("trigger2", "group1")
				.startAt(startTime)
				.withSchedule(
						simpleSchedule().withIntervalInSeconds(3)
						.repeatForever()
						// 设置错失触发后的调度策略
						.withMisfireHandlingInstructionNowWithRemainingCount())
				.build();
		sd = scheduler.scheduleJob(jobDetail1, simpleTrigger1);
		logger.info(jobDetail1.getKey().getName() + " 将在 : "
                + sdf.format(sd) + " 执行, 重复 "
                + simpleTrigger1.getRepeatCount() + " 次, 每次间隔   "
                + simpleTrigger1.getRepeatInterval() / 1000 + " 秒");


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

		logger.info("MisfireScheduler.misfireScheduler end...");
	}
}
