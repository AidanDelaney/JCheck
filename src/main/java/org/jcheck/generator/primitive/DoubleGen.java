package org.jcheck.generator.primitive;

import java.util.Random;
import org.jcheck.generator.Gen;

public class DoubleGen implements Gen<Double>
{
    private final static Gen<Integer> intGen = new IntegerGen();
    
    public Double arbitrary(Random random, long size)
    {
        double a = (double) intGen.arbitrary(random, size);
        double b = (double) intGen.arbitrary(random, size);
        double c = (double) Math.abs(intGen.arbitrary(random, size));

        return a + (b / (c+1.0));
    }
}
