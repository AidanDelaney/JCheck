package org.jcheck.generator;

import java.util.Random;

/**
 * Generator for doubles in the range [0.0-1.0).
 * 
 */
public class PercentageGen implements Gen<Double> 
{
    public Double arbitrary(Random random,long size)
    {
        return random.nextDouble();
    }
}
