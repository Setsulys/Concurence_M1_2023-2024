	package ApiRequest;

import java.util.ArrayList;
import java.util.Optional;

public class CheapestSequential {

    private final String item;
    private final int timeoutMilliPerRequest;

    public CheapestSequential(String item, int timeoutMilliPerRequest) {
        this.item = item;
        this.timeoutMilliPerRequest = timeoutMilliPerRequest;
    }

    /**
     * @return the cheapest price for item if it is sold
     */
    public Optional<Answer> retrieve() throws InterruptedException {
        var answers = new ArrayList<Answer>();
        for(var site : Request.ALL_SITES) {
        	var request = new Request(site,item);
        	var answer = request.request(timeoutMilliPerRequest);
        	if(answer.isPresent()) {
        		answers.add(answer.get());
        	}
        }
        var price = answers.stream().map(e-> e);
        return null;
    }

    public static void main(String[] args) throws InterruptedException {
        var agregator = new CheapestSequential("pikachu", 2_000);
        var answer = agregator.retrieve();
        System.out.println(answer); // Optional[pikachu@darty.fr : 214]
    }
}
