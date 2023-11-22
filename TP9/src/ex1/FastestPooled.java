package ex1;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import using.Answer;
import using.Request;

public class FastestPooled {
	private final String item;
	private final int timeoutMilliPerRequest;
	private final int globalTimeout;
	private final int nbThreads;
	private final ArrayList<Optional<Answer>> answers;
	
	public FastestPooled(String item, int timeoutMilliPerRequest,int globalTimeout,int nbThreads) {
		this.item = item;
		this.timeoutMilliPerRequest = timeoutMilliPerRequest;
		this.globalTimeout = globalTimeout;
		this.nbThreads =nbThreads;
		this.answers = new ArrayList<>();
	}
	
	public Optional<Answer> retreive(){
		var executorService = Executors.newFixedThreadPool(nbThreads);
		var callables = new ArrayList<Callable<Optional<Answer>>>();
		Request.ALL_SITES.forEach(site -> callables.add(()-> new Request(site,item).request(timeoutMilliPerRequest)));
		try {
			var futures = executorService.invokeAny(callables,globalTimeout,TimeUnit.MILLISECONDS);
			return futures;
		} catch (ExecutionException | TimeoutException | InterruptedException e) {
		}finally {
			executorService.shutdownNow();
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		var agregator = new FastestPooled("pikachu", 2_000,10_000,2);
		System.out.println(agregator.retreive());
	}
}
