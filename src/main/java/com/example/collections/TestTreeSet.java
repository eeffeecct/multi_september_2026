package com.example.collections;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.I_Result;

import java.util.Set;
import java.util.TreeSet;

@JCStressTest
@Outcome(
        id = "2",
        expect = Expect.ACCEPTABLE,
        desc = "Expected value: 2 items"
)
@Outcome(
        id = ".*",
        expect = Expect.ACCEPTABLE_INTERESTING,
        desc = "Unexpected size!"
)
@State
public class TestTreeSet {

    private Set<Integer> myTreeSet = new TreeSet<>();

    @Actor
    public void writer1() {
        try {
            myTreeSet.add(1);
        } catch (Exception e) {
        }
    }

    @Actor
    public void writer2() {
        try {
            myTreeSet.add(2);
        } catch (Exception e) {
        }
    }

    @Arbiter
    public void arbiter(I_Result r) {
        r.r1 = myTreeSet.size();
    }

}
