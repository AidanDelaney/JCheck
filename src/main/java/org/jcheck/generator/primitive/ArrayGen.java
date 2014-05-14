package org.jcheck.generator.primitive;

import java.lang.reflect.Array;
import java.util.Random;
import org.jcheck.Arbitrary;
import org.jcheck.generator.Gen;

public class ArrayGen implements Gen
{
    private final int dimension;
    private final Gen generator;
    private final Class componentClass;
    

    public ArrayGen(int dimension, Class componentClass, Gen generator)
    {
        this.generator = generator;
        this.dimension = dimension;
        this.componentClass = componentClass;
    }
    
    public Object arbitrary(Random random, long size)
    {
        int arraySize = (int) Arbitrary.random(random, 0, 
                                               Math.min(Integer.MAX_VALUE, size));
        int[] dimensions = new int[dimension];
        for (int i = 0; i < dimension; ++i) {
            dimensions[i] = arraySize;
        }

        Object array = Array.newInstance(componentClass, dimensions);
        fillArray(array, random, (int)size);
        
        return array;
    }
    
    private void fillArray(Object array, Random random, int size)
    {
        int arraySize = Array.getLength(array);
        for (int i = 0; i < arraySize; ++i) {
            Class comp = array.getClass().getComponentType();
            if (comp.isArray()) {
                fillArray(Array.get(array, i), random, size);
            }
            else {
                Array.set(array, i, generator.arbitrary(random, size));
            }
        }
    }
}
