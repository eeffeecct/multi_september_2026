package main.java.com.example;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.IC_Result;
import org.openjdk.jcstress.infra.results.II_Result;
import org.openjdk.jcstress.infra.results.IS_Result;
import org.openjdk.jcstress.infra.results.I_Result;

import java.util.ArrayList;
import java.util.List;

@JCStressTest
@Outcome(
        id = "12, 23",
        expect = Expect.ACCEPTABLE,
        desc = "Expected value: 2 items"
)
@Outcome(
        id = ".*",
        expect = Expect.ACCEPTABLE_INTERESTING,
        desc = "Unexpected size!"
)
@State
public class TestSum {

    private List<Integer> myArray;

    public TestSum() {
        this.myArray = new ArrayList<>();
    }

    @Actor
    public void writer1() {
        try {
            myArray.add(1);
        } catch (Exception e) {
        }
    }

    @Actor
    public void writer2() {
        try {
            myArray.add(1);
        } catch (Exception e) {
        }
    }

    @Arbiter
    public void arbiter(II_Result r) {
        r.r1 = myArray.size();
    }

}
