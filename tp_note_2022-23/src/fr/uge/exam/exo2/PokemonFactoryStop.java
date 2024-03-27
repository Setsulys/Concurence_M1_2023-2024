package fr.uge.exam.exo2;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import fr.uge.exam.exo2.PokeAPI.Pokeball;
import fr.uge.exam.exo2.PokeAPI.Pokemon;

public class PokemonFactoryStop {
	public static void main(String[] args) throws InterruptedException {
		int captureThread =5;
		int possibleValue =11;
		int putInCrate = 2;
		var capture = new ArrayBlockingQueue<Pokemon>(10);
		var trap = new ArrayBlockingQueue<Pokeball>(10);
		var th = new ArrayList<Thread>();
		for(var i = 0;i < captureThread; i++) {
			th.add(
			Thread.ofPlatform().start(()->{
				for(;!Thread.currentThread().isInterrupted();) {
					try {
						capture.put(PokeAPI.capture());
						System.out.println(Thread.currentThread().getName() + " CAPTURE");
					} catch (InterruptedException e) {
						return;
					}
				}
			}));
		}
		th.add(
		Thread.ofPlatform().start(()->{
			for(;!Thread.currentThread().isInterrupted();){
				try {
					var captured = capture.take();
					if(captured.rarity()!=5) {
						trap.put(PokeAPI.trap(captured));
					}
					else {
						capture.put(captured);
					}
					
					System.out.println(Thread.currentThread().getName() + " TRAP");
				} catch (InterruptedException e) {
					return;
				}
			}

		}));
		th.add(
		Thread.ofPlatform().start(()->{
			for(;!Thread.currentThread().isInterrupted();){
				try {
					var captured = capture.take();
					if(captured.rarity()==5) {
						trap.put(PokeAPI.trap(captured));
					}
					else {
						capture.put(captured);
					}
					
					System.out.println(Thread.currentThread().getName() + " TRAP");
				} catch (InterruptedException e) {
					return;
				}
			}

		}));
		for(var i=0;i< possibleValue;i++) {
			for(var j = 0 ;j < putInCrate;j++) {
				var threadValue = i;
				
				var t = Thread.ofPlatform().start(()->{
					var boxing = new ArrayList<Pokeball>();
					try {
						while(boxing.size() < 6) { 
							var poke =trap.take();
							if(poke.value()==threadValue) {
								boxing.add(poke);
							}
							else {
								trap.put(poke);
							}
							System.out.println(Thread.currentThread().getName() + " BOXING");
						}
						System.out.println(PokeAPI.box(boxing));
						if(threadValue==10) {
							Thread.currentThread().interrupt();
							for(var thr:th) {
								thr.interrupt();
							}
						}
						boxing.clear();
					} catch (InterruptedException e) {
						return;
					}
				});
				th.add(t);
			}
		}
	}
}