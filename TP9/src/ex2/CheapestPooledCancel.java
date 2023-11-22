package ex2;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import using.Answer;
import using.Request;

public class CheapestPooledCancel {

	private final String item;
	private final int nbThreads;
	private final ArrayList<Optional<Answer>> answers;
	private final ScheduledExecutorService scheduler;
	private final int timeoutMilliPerRequest;
	
	public CheapestPooledCancel(String item,int nbThreads,int timeoutMilliPerRequest) {
		this.item = item;
		this.nbThreads =nbThreads;
		this.answers = new ArrayList<>();
		this.scheduler = Executors.newScheduledThreadPool(nbThreads);
		this.timeoutMilliPerRequest = timeoutMilliPerRequest;
	}
	
	public Optional<Answer> retreive(){
		var executorService = Executors.newFixedThreadPool(nbThreads);
		var callables = new ArrayList<Callable<Optional<Answer>>>();
		
		ScheduledFuture<?> timeout = scheduler.schedule(()->{}, timeoutMilliPerRequest, TimeUnit.MICROSECONDS);
		Request.ALL_SITES.forEach(site -> callables.add(()-> new RequestWithCancel(site,item).request()));
		try {
			var futures = executorService.invokeAll(callables);
			for(var future : futures) {
				switch (future.state()) {
			      case RUNNING -> throw new AssertionError("should not be there");
			      case SUCCESS -> answers.add(future.resultNow());
			      case FAILED -> System.out.println(future.exceptionNow());
			      case CANCELLED -> System.out.println("cancelled");
				}

			}
		} catch (InterruptedException e) {	
		}
		return answers.stream().filter(e-> e.isPresent()).map(e -> e.orElseThrow()).min(Answer::compareTo);
	}
	
	public static void main(String[] args) {
		var agregator = new CheapestPooledCancel("pikachu",5,2_000);
		System.out.println(agregator.retreive());
	}
	
	
}
