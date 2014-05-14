package org.jcheck.generator;

import java.util.Random;

/**
 * <p>
 * Interface for all generators.
 * </p>
 * 
 * <p>
 * Implementing classes must have a zero-argument constructor if it
 * should be used directly in <code>@Generator</code> annotations.
 * </p>
 * 
 */
public interface Gen<T>
{
    /**
     * <p>
     * Returns an arbitrary object of type T. The object should
     * not have a size greater than <tt>size</tt> (or smaller than 
     * <tt>-size</tt>). What the size actually referrs to depends on the
     * implementation, but it could be the number of nodes in a tree,
     * pixels in an image etc.
     * </p>
     * 
     * <p>
     * The supplied random generator should be the only source of randomness
     * used in the generator.
     * </p>
     * 
     * @param random the random generator that should be used when randomness is needed.
     * @param size the limiting factor
     * @return a random object
     */
    public T arbitrary(Random random, long size);
}
