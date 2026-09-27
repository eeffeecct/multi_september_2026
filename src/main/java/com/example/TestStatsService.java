package com.example;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.I_Result;

// Thread safety

@JCStressTest
@Outcome(
        id = "2",
        expect = Expect.ACCEPTABLE,
        desc = "Expected number: 2"
)
@Outcome(
        id = ".*",
        expect = Expect.ACCEPTABLE_INTERESTING,
        desc = "Unexpected number!"
)
@State
public class TestStatsService {

    ThreadSafetyPuzzles.StatsService statsService = new ThreadSafetyPuzzles.StatsService();

    @Actor
    public void writer1() {
        try {
            statsService.add(1);
        } catch (Exception e) {
        }
    }

    @Actor
    public void writer2() {
        try {
            statsService.add(1);
        } catch (Exception e) {
        }
    }

    @Arbiter
    public void arbiter(I_Result r) {
        r.r1 = statsService.getTotal();
    }
}
