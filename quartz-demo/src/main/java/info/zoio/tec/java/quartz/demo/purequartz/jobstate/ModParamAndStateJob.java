package info.zoio.tec.java.quartz.demo.purequartz.jobstate;

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
 * 接收参数和维护状态
 * 
 * @PersistJobDataAfterExecution 保存在JobDataMap传递的参数  没有此注解，执行次数统计(from job jobDataMap)始终不会变化
 * @DisallowConcurrentExecution   保证多个任务间不会同时执行.在多任务执行时最好加上
 * 
 * @author humyna
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ModParamAndStateJob implements Job {
	private static final Logger logger = LoggerFactory.getLogger(ModParamAndStateJob.class);
	
	// 静态变量  
    public static final String FAVORITE_COLOR = "favorite color";  
    public static final String EXECUTION_COUNT = "count";  
    
    // Quartz将每次将会重新实例化对象 ，非静态的成员变量不能用来保持状态  
    private int _counter = 1;  
    
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		//日期格式化  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  

		//获取参数
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		String favoriteColor = jobDataMap.getString(FAVORITE_COLOR);  
        int count = jobDataMap.getInt(EXECUTION_COUNT);  
        logger.info("ModParamAndStateJob: " + context.getJobDetail().getKey().getName() + " 在 " + sdf.format(new Date()) + " 执行了 ...  \n"  
                        + "      喜欢的颜色是：  " + favoriteColor + "\n"  
                        + "      执行次数统计(from job jobDataMap)： " + count + "\n"  
                        + "      执行次数统计(from job 类的成员变 量 ): "  
                        + _counter + " \n ");
        // 每次+1并放回Map中  
        count++;  
        jobDataMap.put(EXECUTION_COUNT, count);  
        // 成员变量的增加没有意义，每次实例化对象的时候会同时初始化该变量  
        _counter++;  
	}

}
