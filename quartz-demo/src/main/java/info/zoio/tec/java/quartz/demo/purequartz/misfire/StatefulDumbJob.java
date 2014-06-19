package info.zoio.tec.java.quartz.demo.purequartz.misfire;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class StatefulDumbJob implements Job{
	private static final Logger logger = LoggerFactory.getLogger(StatefulDumbJob.class);

	// 静态常量，作为任务在调用间，保持数据的键(key)
    // NUM_EXECUTIONS，保存的计数每次递增1
    // EXECUTION_DELAY，任务在执行时，中间睡眠的时间。本例中睡眠时间过长导致了错失触发
    public static final String NUM_EXECUTIONS = "NumExecutions";
    public static final String EXECUTION_DELAY = "ExecutionDelay";

    public void execute(JobExecutionContext context) throws JobExecutionException {
    	//日期格式化
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		logger.info( context.getJobDetail().getKey().getName() + " 在:["
                + sdf.format(new Date()) + "] 执行了!");
		//获取参数,任务执行计数
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		int executeCount = 0;
		if(jobDataMap.containsKey(NUM_EXECUTIONS)){
			executeCount = jobDataMap.getInt(NUM_EXECUTIONS);
		}
		executeCount++;
		jobDataMap.put(NUM_EXECUTIONS, executeCount);

		// 睡眠时间:由调度类重新设置值 ,本例为 睡眠10s
		long delay = 5000l;
		if(jobDataMap.containsKey(EXECUTION_DELAY)){
			delay = jobDataMap.getLong(EXECUTION_DELAY);
		}

		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) { }

		logger.info(context.getJobDetail().getKey().getName()
                + " 完成次数  : " + executeCount);
    }
}
