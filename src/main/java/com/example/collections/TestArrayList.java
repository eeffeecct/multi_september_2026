package com.example.collections;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.IC_Result;
import org.openjdk.jcstress.infra.results.II_Result;
import org.openjdk.jcstress.infra.results.IS_Result;
import org.openjdk.jcstress.infra.results.I_Result;

import java.util.ArrayList;
import java.util.List;
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
public class TestArrayList {

    private List<Integer> myArrayList = new ArrayList<>();

    public TestArrayList() {
        this.myArrayList = new ArrayList<>();
    }

    @Actor
    public void writer1() {
        try {
            myArrayList.add(1);
        } catch (Exception e) {
        }
    }

    @Actor
    public void writer2() {
        try {
            myArrayList.add(2);
        } catch (Exception e) {
        }
    }

    @Arbiter
    public void arbiter(I_Result r) {
        r.r1 = myArrayList.size();
    }

}
