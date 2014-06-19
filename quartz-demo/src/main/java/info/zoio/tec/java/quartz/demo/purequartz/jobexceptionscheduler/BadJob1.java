package info.zoio.tec.java.quartz.demo.purequartz.jobexceptionscheduler;

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

/**
 * 演示抛出异常时的自动修复
 * 从Map中取出 denominator
 * 如果 是0抛出异常,然后将denominator设置成1,也就是说只有第一次会有异常抛出,以后都 正常
 *
 * @author humyna
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BadJob1 implements Job {
	private static final Logger logger = LoggerFactory.getLogger(BadJob1.class);

	public void execute(JobExecutionContext context) throws JobExecutionException {
		//日期格式化
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		logger.info( context.getJobDetail().getKey().getName() + " 在:["
                + sdf.format(new Date()) + "] 开始执行!");

		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		int denominator = jobDataMap.getInt("denominator");
		if(denominator == 0){
			jobDataMap.put("denominator", 1);
			JobExecutionException e1 = new JobExecutionException(new Exception());
			logger.info("在BadJob1 中发生 错误, 将停止运行!! ");
			e1.setRefireImmediately(true);//设置了 job 类抛出异常后的处理方式 ,此处意为 发生异常后 立即重新执行
			// 抛出异常
			throw e1;
		}
	}
}
