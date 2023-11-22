package ex1;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import using.Answer;
import using.Request;

public class CheapestPooled {
	
	private final String item;
	private final int timeoutMilliPerRequest;
	private final int nbThreads;
	private final ArrayList<Optional<Answer>> answers;
	
	public CheapestPooled(String item, int timeoutMilliPerRequest,int nbThreads) {
		this.item = item;
		this.timeoutMilliPerRequest = timeoutMilliPerRequest;
		this.nbThreads =nbThreads;
		this.answers = new ArrayList<>();
	}
	
	public Optional<Answer> retreive(){
		var executorService = Executors.newFixedThreadPool(nbThreads);
		var callables = new ArrayList<Callable<Optional<Answer>>>();
		Request.ALL_SITES.forEach(site -> callables.add(()-> new Request(site,item).request(timeoutMilliPerRequest)));
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
		var agregator = new CheapestPooled("pikachu", 2_000,5);
		System.out.println(agregator.retreive());
	}
	
	
}
