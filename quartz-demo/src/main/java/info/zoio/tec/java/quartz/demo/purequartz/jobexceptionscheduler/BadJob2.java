package info.zoio.tec.java.quartz.demo.purequartz.jobexceptionscheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 演示从调度器中移除相应的触发器
 *
 * @author humyna
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BadJob2 implements Job {
	private static final Logger logger = LoggerFactory.getLogger(BadJob2.class);

	public void execute(JobExecutionContext context) throws JobExecutionException {
		//日期格式化
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		logger.info( context.getJobDetail().getKey().getName() + " 在:["
                + sdf.format(new Date()) + "] 开始执行!");

		logger.info("在BadJob2 中发生 错误, 将停止运行!! ");
        JobExecutionException e2 = new JobExecutionException(new Exception());
        //设置将自动 去除 这个任务的触发器,所以这个任务不会再执行
        e2.setUnscheduleAllTriggers(true);

        // 抛出异常
        throw e2;
	}

}
