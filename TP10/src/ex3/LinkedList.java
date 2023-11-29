package ex3;

import java.util.Objects;
import java.util.function.Consumer;

public class LinkedList<E> {

    private record Link<E>(E value, Link<E> next) {
        private Link {
            Objects.requireNonNull(value);
        }
    }

    private Link<E> head;

    /**
     * Add the non-null value at the start of the list
     *
     * @param value
     */
    public void addFirst(E value) {
        Objects.requireNonNull(value);
        head = new Link<>(value, head);
    }

    /**
     * applies the consumer the elements of the list in order
     *
     * @param consumer
     */
    public void forEach(Consumer<? super E> consumer) {
        Objects.requireNonNull(consumer);
        for (var current = head; current != null; current = current.next) {
            consumer.accept(current.value);
        }
    }

    public static void main(String[] args) {
        var list = new LinkedList<String>();
        list.addFirst("Noel");
        list.addFirst("papa");
        list.addFirst("petit");
        list.forEach(System.out::println);
    }

}
