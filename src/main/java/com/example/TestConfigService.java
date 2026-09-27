package com.example;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.I_Result;

import java.util.concurrent.atomic.AtomicInteger;

// не потокобезопасен

@JCStressTest
@Outcome(
        id = "2",
        expect = Expect.ACCEPTABLE,
        desc = "Both keys stored correctly"
)
@Outcome(
        id = ".*",
        expect = Expect.ACCEPTABLE_INTERESTING,
        desc = "Lost a write"
)
@State
public class TestConfigService {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    private final String key1;
    private final String key2;

    public TestConfigService() {
        int id = COUNTER.incrementAndGet() % 50_000;
        key1 = "key1_" + id;
        key2 = "key2_" + id;
    }

    @Actor
    public void writer1() {
        try {
            ThreadSafetyPuzzles.ConfigService.getInstance().set(key1, "A");
        } catch (Exception e) {
        }
    }

    @Actor
    public void writer2() {
        try {
            ThreadSafetyPuzzles.ConfigService.getInstance().set(key2, "B");
        } catch (Exception e) {
        }
    }

    @Arbiter
    public void arbiter(I_Result r) {
        try {
            ThreadSafetyPuzzles.ConfigService cfg = ThreadSafetyPuzzles.ConfigService.getInstance();
            int count = 0;
            if ("A".equals(cfg.get(key1))) {
                count++;
            }
            if ("B".equals(cfg.get(key2))) {
                count++;
            }
            r.r1 = count;
        } catch (Exception e) {
            r.r1 = -1;
        }
    }
}
