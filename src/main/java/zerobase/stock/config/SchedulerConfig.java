package zerobase.stock.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

	// 스케줄러는 한개의 스레드로 동작함. 따라서 여러개의 스케줄러가 존재할 경우에는
	// 하나의 스레드로 나눠가면서 번갈아서 실행됨.
	// 그러므로 스케줄 주기가 다르더라도 한번씩 실행되므로 원하는 주기대로 돌릴 수가 없음
	// 스레드 풀로 스케줄을 여러개 만들어서 이 부분을 해결할 수 있음 -> SchedulerConfig 파일에 있음
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		ThreadPoolTaskScheduler threadPool = new ThreadPoolTaskScheduler();
		int n = Runtime.getRuntime().availableProcessors(); // CPU 코어 개수
		threadPool.setPoolSize(n + 1); // 스케줄링을 위한 스레드 개수는 CPU 코어 개수 + 1개가 적당함
		threadPool.initialize();

		taskRegistrar.setTaskScheduler(threadPool); // 스케줄러에서 사용하는 스레드 설정
	}
}
