package com.example;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.L_Result;

// не потокобезопасно. containsKey() + put() не атомарны. два потока могут одновременно проверку пройти.

@JCStressTest
@Outcome(
        id = "first",
        expect = Expect.ACCEPTABLE,
        desc = "writer1 won the race"
)
@Outcome(
        id = "second",
        expect = Expect.ACCEPTABLE,
        desc = "writer2 won the race"
)
@Outcome(
        id = ".*",
        expect = Expect.ACCEPTABLE_INTERESTING,
        desc = "Unexpected value!"
)
@State
public class TestTrickyCache {

    ThreadSafetyPuzzles puzzles = new ThreadSafetyPuzzles();
    ThreadSafetyPuzzles.TrickyCache cache = puzzles.new TrickyCache();

    @Actor
    public void writer1() {
        cache.putIfAbsent("1", "first");
    }

    @Actor
    public void writer2() {
        cache.putIfAbsent("1", "second");
    }

    @Arbiter
    public void arbiter(L_Result r) {
        r.r1 = cache.get("1");
    }

}
