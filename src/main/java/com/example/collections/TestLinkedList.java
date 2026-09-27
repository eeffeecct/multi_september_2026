package com.example.collections;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.I_Result;

import java.util.LinkedList;
import java.util.List;

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
public class TestLinkedList {

    private List<Integer> myLinkedList = new LinkedList<>();

    @Actor
    public void writer1() {
        try {
            myLinkedList.add(1);
        } catch (Exception e) {
        }
    }

    @Actor
    public void writer2() {
        try {
            myLinkedList.add(2);
        } catch (Exception e) {
        }
    }

    @Arbiter
    public void arbiter(I_Result r) {
        r.r1 = myLinkedList.size();
    }

}
