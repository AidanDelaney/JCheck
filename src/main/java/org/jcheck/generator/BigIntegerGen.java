package org.jcheck.generator;

import java.math.BigInteger;
import java.util.Random;
import org.jcheck.generator.Gen;

public class BigIntegerGen implements Gen<BigInteger>
{
    public BigInteger arbitrary(Random random, long size)
    {
        return new BigInteger((int)Math.min(Integer.MAX_VALUE, size), random);
    }
}
