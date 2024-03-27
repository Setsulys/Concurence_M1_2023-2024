package fr.uge.exam.exo2;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import fr.uge.exam.exo2.PokeAPI.Pokeball;
import fr.uge.exam.exo2.PokeAPI.Pokemon;

public class PokemonFactory {
	public static void main(String[] args) {
		int captureThread =5;
		int possibleValue =11;
		int putInCrate = 2;
		var capture = new ArrayBlockingQueue<Pokemon>(10);
		var trap = new ArrayBlockingQueue<Pokeball>(10);
		for(var i = 0;i < captureThread; i++) {
			Thread.ofPlatform().start(()->{
				for(;;) {
					try {
						capture.put(PokeAPI.capture());
						System.out.println(Thread.currentThread().getName() + " CAPTURE");
					} catch (InterruptedException e) {
						throw new AssertionError(e);
					}
				}
			});
		}
		Thread.ofPlatform().start(()->{
			for(;;){
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
					throw new AssertionError(e);
				}
			}

		});
		Thread.ofPlatform().start(()->{
			for(;;){
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
					throw new AssertionError(e);
				}
			}

		});
		for(var i=0;i< possibleValue;i++) {
			for(var j = 0 ;j < putInCrate;j++) {
				var threadValue = i;
				Thread.ofPlatform().start(()->{
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
						boxing.clear();
					} catch (InterruptedException e) {
						throw new AssertionError(e);
					}
				});
			}
		}
	}
}