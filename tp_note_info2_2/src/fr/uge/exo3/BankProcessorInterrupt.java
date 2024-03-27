package fr.uge.exo3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

import fr.uge.exo3.Banco.WireTransfer;

public class BankProcessorInterrupt {



	public static void main(String[] args) {
		int transfert =3;
		int suspicious =2;
		var checkTransfert = new ArrayBlockingQueue<WireTransfer>(10);
		var display = new ArrayBlockingQueue<WireTransfer>(10);
		var bankBalance = new HashMap<Banco.Bank,Integer>();
		var interruptList = new ArrayList<WireTransfer>();
		var interrupting = 10;
		var threads = new ArrayList<Thread>();
		for(var i =0; i < transfert; i++) {
			var t = Thread.ofPlatform().start(()->{
				try {
					for(;!Thread.currentThread().isInterrupted();) {
						var transferts = Banco.retrieveWireTransfer();
						checkTransfert.put(transferts);
						System.out.println("---> "+transferts);	
					}

				} catch (InterruptedException e) {
					return;
				}
			});
			threads.add(t);
		}
		for(var i =0; i < suspicious; i++) {
			var t=Thread.ofPlatform().start(()->{
				try {
					for(;!Thread.currentThread().isInterrupted();) {
						var checkSuspect = checkTransfert.take();
						if(Banco.isSuspect(checkSuspect)) {
							System.out.println("Rejecting the suspicious " + checkSuspect);
							interruptList.add(checkSuspect);
							if(interrupting==interruptList.size()) {
								for(var tre  : threads) {
									tre.interrupt();
								}
							}
						}
						if(bankBalance.computeIfPresent(checkSuspect.bank(), (k,v)-> v+ checkSuspect.amount())==null) {
							bankBalance.put(checkSuspect.bank(), checkSuspect.amount());
						}
						display.put(checkSuspect);

					}


				} catch (InterruptedException e) {
					return;
				}
			});
			threads.add(t);
		}
		for(var bank : Banco.Bank.values()) {
			var t =Thread.ofPlatform().name(bank+"").start(()->{
				for(;!Thread.currentThread().isInterrupted();) {
					try {
						var d = display.take();
						if(!d.bank().equals(bank)) {
							display.put(d);
						}
						else {
							System.out.println(bank +" has "+bankBalance.get(bank) +" after "+ d);
						}
					} catch (InterruptedException e) {
						return;
					}	
				}
			});
			threads.add(t);
		}

		
	}
}
