package org.jcheck.generator;

import java.util.Random;

/**
 * Generate <code>null</code> instead of objects with a given probability.
 */
public class NullGen<T> implements Gen<T>
{
    private Gen<T> generator;
    private double probability;
    
    /**
     * Construct a generator that will generate <code>null</code> with the
     * supplied probability instead of objects from the given generator.
     * 
     * @param generator generator for objects
     * @param probability probability that the object will be null instead of an object from the given generator
     */
    public NullGen(Gen<T> generator, double probability)
    {
        this.generator = generator;
        this.probability = probability;
    }
    
    public T arbitrary(Random random,long size)
    {
        if (random.nextDouble() < probability) {
            return null;
        }
        else {
            return generator.arbitrary(random, size);
        }
    }
}
