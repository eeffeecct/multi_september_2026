package com.example.collections;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.IC_Result;
import org.openjdk.jcstress.infra.results.II_Result;
import org.openjdk.jcstress.infra.results.IS_Result;
import org.openjdk.jcstress.infra.results.I_Result;

import java.util.Map;
import java.util.TreeMap;

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
public class TestTreeMap {

    private Map<Integer, Integer> myTreeMap = new TreeMap<>();

    public TestTreeMap() {
        this.myTreeMap = new TreeMap<>();
    }

    @Actor
    public void writer1() {
        try {
            myTreeMap.put(1, 1);
        } catch (Exception e) {
        }
    }

    @Actor
    public void writer2() {
        try {
            myTreeMap.put(2, 2);
        } catch (Exception e) {
        }
    }

    @Arbiter
    public void arbiter(I_Result r) {
        r.r1 = myTreeMap.size();
    }

}
