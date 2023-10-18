package ex3;

import java.util.stream.IntStream;

public class ExampleExchanger2 {
    public static void main(String[] args) throws InterruptedException {
        var exchanger = new ExchangerReuse<String>();
        IntStream.range(0, 10).forEach(i -> {
            Thread.ofPlatform().start(() -> {
                try {
                    System.out.println("thread " + i + " received from " + exchanger.exchange("thread " + i));
                } catch (InterruptedException e) {
                    throw new AssertionError(e);
                }
            });
        });
    }
}
