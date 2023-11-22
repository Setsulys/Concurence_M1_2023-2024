package ex2;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import using.Answer;

public class RequestWithCancel {

    private final String site;
    private final String item;
    private final Object lock = new Object();
    private boolean cancelled;
    private boolean started;

    private final static String[] ARRAY_ALL_SITES = { "amazon.fr", "amazon.uk", "darty.fr", "fnac.fr", "boulanger.fr",
            "cdiscount.fr", "tombeducamion.fr", "leboncoin.fr", "ebay.fr", "ebay.com", "laredoute.fr",
            "les3suisses.fr" };
    private final static Set<String> SET_ALL_SITES = Set.of(ARRAY_ALL_SITES);
    public final static List<String> ALL_SITES = Collections.unmodifiableList(List.of(ARRAY_ALL_SITES));

    public RequestWithCancel(String site, String item) {
        if (!SET_ALL_SITES.contains(site))
            throw new IllegalStateException();
        this.site = site;
        this.item = item;
    }

    @Override
    public String toString() {
        return item + "@" + site;
    }

    public void cancel() {
        synchronized (lock) {
            cancelled = true;
            lock.notify();
        }
    }

    /**
     * Performs the request the price for the item on the site. The returned answer
     * might not be successful. This method is blocking and might not terminate. It
     * is the user's responsibility to call the method cancel() to stop the request.
     * <p>
     * This method can only be called once. All further calls will throw an
     * IllegalStateException
     *
     * @throws InterruptedException
     */
    public Optional<Answer> request() throws InterruptedException {
        synchronized (lock) {
            if (started)
                throw new IllegalStateException();
            started = true;
        }
        System.out.println("DEBUG : starting request for " + item + " on " + site);
        if (item.equals("pokeball")) {
            System.out.println("DEBUG : " + item + " is not available on " + site);
            return Optional.empty();
        }
        long hash1 = Math.abs((site + "|" + item).hashCode());
        long hash2 = Math.abs((item + "|" + site).hashCode());
        if (hash1 % 1000 < 400) { // simulating timeout
            System.out.println("DEBUG : Request " + toString() + " is not terminating");
            synchronized (lock) {
                while (!cancelled) {
                    lock.wait();
                }
            }
            System.out.println("DEBUG : Request " + toString() + " is cancelled");
            return Optional.empty();
        }
        long time = System.currentTimeMillis();
        long endTime = time + (hash1 % 1000) * 2;
        synchronized (lock) {
            while (!cancelled && time < endTime) {
                lock.wait(endTime - time);
                time = System.currentTimeMillis();
            }
            if (cancelled) {
                System.out.println("DEBUG : Request " + toString() + " is cancelled");
                return Optional.empty();
            }
        }
        if ((hash1 % 1000 < 500)) {
            System.out.println("DEBUG : " + item + " is not available on " + site);
            return Optional.empty();
        }
        int price = (int) (hash2 % 1000) + 1;
        System.out.println("DEBUG : " + item + " costs " + price + " on " + site);
        return Optional.of(new Answer(site, item, price));
    }

    public static void main(String[] args) throws InterruptedException {
        RequestWithCancel request = new RequestWithCancel("amazon.fr", "pikachu");

        new Thread(() -> {
            try {
                System.out.println(request.request());
            } catch (InterruptedException e) {
                throw new AssertionError();
            }
        }).start();
        Thread.sleep(5_000);
        request.cancel();
    }
}