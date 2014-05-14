package org.jcheck.generator;

import java.util.Random;

/**
 * Generate a value from one of the given set of generators.
 * 
 */
public class OneOfGen<T> implements Gen<T>
{
    private Gen<T>[] generators;

    public OneOfGen(Gen<T> ... generators)
    {
        this.generators = generators;
    }

    @Override
    public T arbitrary(Random random,long size)
    {
        Gen<T> generator = generators[random.nextInt(generators.length)];
        return generator.arbitrary(random, size);
    }
}
