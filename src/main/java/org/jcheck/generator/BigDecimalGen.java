package org.jcheck.generator;

import java.math.BigDecimal;
import java.util.Random;
import org.jcheck.generator.Gen;

public class BigDecimalGen implements Gen<BigDecimal>
{
    private static final BigIntegerGen intGen = new BigIntegerGen();
    
    public BigDecimal arbitrary(Random random, long size)
    {
        return new BigDecimal(intGen.arbitrary(random, size));
    }
}
