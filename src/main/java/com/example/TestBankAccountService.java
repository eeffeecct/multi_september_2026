package com.example;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.D_Result;

// не потокобезопасен. withdraw(), deposit() синхронизированы на разных объектах. друг друга не блокируют

@JCStressTest
@Outcome(
        id = "800.0",
        expect = Expect.ACCEPTABLE,
        desc = "Expected amount: 800.0"
)
@Outcome(
        id = ".*",
        expect = Expect.ACCEPTABLE_INTERESTING,
        desc = "Unexpected amount!"
)
@State
public class TestBankAccountService {

    ThreadSafetyPuzzles.BankAccountService account = new ThreadSafetyPuzzles.BankAccountService();

    public TestBankAccountService() {
        account.deposit(1000);
    }

    @Actor
    public void writer1() {
        try {
            account.withdraw(500);
        } catch (Exception e) {
        }
    }

    @Actor
    public void writer2() {
        try {
            account.deposit(300);
        } catch (Exception e) {
        }
    }

    @Arbiter
    public void arbiter(D_Result r) {
        r.r1 = account.getBalance();
    }

}
