package fr.uge.exo3;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Banco {

    /* All the banks supported by the API */
    public enum Bank { PICSOUBANK, DESSOUSSOUS, THUNE, OSEILLE, BLE, MASSETHUNE};

    /* Record representing the information of a wireTransfer */
    public record WireTransfer(int bankAccount, Bank bank, int amount){
        public WireTransfer {
            Objects.requireNonNull(bank);
        }
    }

    /* Method simulating the reception of wireTransfer from the internet */
    public static WireTransfer retrieveWireTransfer() throws InterruptedException {
        var rng = ThreadLocalRandom.current();
        var time = rng.nextInt(1,1_000);
        Thread.sleep(time);
        var bankAccount = rng.nextInt(1,1_000_000);
        var bank = Bank.values()[rng.nextInt(0,Bank.values().length)];
        var amount = rng.nextInt(-10_000,10_000);
        return new WireTransfer(bankAccount,bank,amount);

    }

    /* Method to determine if a wireTransfer is suspect */
    public static boolean isSuspect(WireTransfer wireTransfer) throws InterruptedException {
        Objects.requireNonNull(wireTransfer);
        var rng = ThreadLocalRandom.current();
        var time = rng.nextInt(1,1_000);
        Thread.sleep(time);
        return wireTransfer.hashCode()%10 == 0;
    }
}
