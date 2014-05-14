package org.jcheck.generator;

import java.util.Random;

/**
 * Generate a random object from the list of objects in the
 * constructor. This generator should be subclassed.
 * 
 * The following example will create a generator that will
 * generate only 2, 4 and 6.
 * 
 * <code>
 * <pre>
 * public class TwoFourSixGen extends ElementGen&lt;Integer&gt;
 * {
 *     public TwoFourSixGen()
 *     {
 *         super(2,4,6);
 *     }
 * }
 * </pre>
 * </code>
 * 
 */
public class ElementGen<T> implements Gen<T>
{
    private T[] objects;

    public ElementGen(T ... objects)
    {
        this.objects = objects;
    }
    
    public T arbitrary(Random random,long size)
    {
        return objects[random.nextInt(objects.length)];
    }
}
