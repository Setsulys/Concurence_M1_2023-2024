package fr.uge.exo3;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

import fr.uge.exo3.Banco.WireTransfer;

public class BankProcessor {

	public static void main(String[] args) {
		int transfert =3;
		int suspicious =2;
		var checkTransfert = new ArrayBlockingQueue<WireTransfer>(10);
		var display = new ArrayBlockingQueue<WireTransfer>(10);
		var bankBalance = new HashMap<Banco.Bank,Integer>();
		for(var i =0; i < transfert; i++) {
			Thread.ofPlatform().start(()->{
				try {
					for(;;) {
						var transferts = Banco.retrieveWireTransfer();
						checkTransfert.put(transferts);
						System.out.println("---> "+transferts);	
					}
				} catch (InterruptedException e) {
					throw new AssertionError(e);
				}
			});
		}
		for(var i =0; i < suspicious; i++) {
			Thread.ofPlatform().start(()->{
				try {
					for(;;) {
						var checkSuspect = checkTransfert.take();
						if(Banco.isSuspect(checkSuspect)) {
							System.out.println("Rejecting the suspicious " + checkSuspect);
						}
						if(bankBalance.computeIfPresent(checkSuspect.bank(), (k,v)-> v+ checkSuspect.amount())==null) {
							bankBalance.put(checkSuspect.bank(), checkSuspect.amount());
						}
						display.put(checkSuspect);
					}

				} catch (InterruptedException e) {
					throw new AssertionError(e);
				}
			});
		}
		for(var bank : Banco.Bank.values()) {
			Thread.ofPlatform().name(bank+"").start(()->{
				for(;;) {
					try {
						var d = display.take();
						if(!d.bank().equals(bank)) {
							display.put(d);
						}
						else {
							System.out.println(bank +" has "+bankBalance.get(bank) +" after "+ d);
						}
					} catch (InterruptedException e) {
						throw new AssertionError(e);
					}
					
					
				}
			});
		}
	}
}