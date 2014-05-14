package org.jcheck.generator;

import java.util.Random;
import java.util.Vector;
import org.jcheck.Arbitrary;

public class VectorGen implements Gen<Vector> 
{
    private Gen subGenerator;
    
    public VectorGen(Gen subGenerator)
    {
        this.subGenerator = subGenerator;
    }
    
    @SuppressWarnings("unchecked")
    public Vector arbitrary(Random random, long size)
    {
        int length = (int) Arbitrary.random(random, 0, 
                                      Math.min(Integer.MAX_VALUE, size));
        Vector list = new Vector(length);
        for (int i = 0; i < length; ++i) {
            list.add(i, subGenerator.arbitrary(random, size));
        }
        
        return list;
    }
}
