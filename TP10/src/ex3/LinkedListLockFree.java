package ex3;

import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class LinkedListLockFree<E> {
    private record Link<E>(E value, AtomicReference<Link<E>> next) {
        private Link {
            Objects.requireNonNull(value);
        }
    }

    //private Link<E> head;
    private AtomicReference<Link<E>> head;

    /**
     * Add the non-null value at the start of the list
     *
     * @param value
     */
    public void addFirst(E value) {
        Objects.requireNonNull(value);
//        head = new Link<>(value, head);
        head = new AtomicReference<Link<E>>(new Link<E>(value,head));;
    }

    /**
     * applies the consumer the elements of the list in order
     *
     * @param consumer
     */
    public void forEach(Consumer<? super E> consumer) {
        Objects.requireNonNull(consumer);
        for (var current = head; current != null; current = current.get().next()) {
            consumer.accept(current.get().value());
        }
    }
    
    public E pollFirst() {
        if (head == null) {
            return null;
        }
        
       return head.updateAndGet(e -> {
        	var value = head.get();
        	head = head.get().next();
        	return value;
        }).value();
    } 

    public static void main(String[] args) {
        var list = new LinkedList<String>();
        list.addFirst("Noel");
        list.addFirst("papa");
        list.addFirst("petit");
        list.forEach(System.out::println);
        list.pollFirst();
        list.pollFirst();
        System.out.println("----");
        list.forEach(System.out::println);
    }
}
