package org.jcheck.generator.primitive;

import java.util.Random;
import org.jcheck.generator.Gen;

public class FloatGen implements Gen<Float>
{
    private final static Gen<Integer> intGen = new IntegerGen();
    
    public Float arbitrary(Random random, long size)
    {
        float a = (float) intGen.arbitrary(random, size);
        float b = (float) intGen.arbitrary(random, size);
        float c = (float) Math.abs(intGen.arbitrary(random, size));

        return a + (b / (c+1.0f));
    }
}
