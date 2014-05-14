package org.jcheck.generator;

import java.util.ArrayList;
import java.util.Random;
import org.jcheck.Arbitrary;

public class ArrayListGen implements Gen<ArrayList> 
{
    private Gen subGenerator;
    
    public ArrayListGen(Gen subGenerator)
    {
        this.subGenerator = subGenerator;
    }
    
    @SuppressWarnings("unchecked")
    public ArrayList arbitrary(Random random, long size)
    {
        int length = (int) Arbitrary.random(random, 0, 
                                      Math.min(Integer.MAX_VALUE, size));
        ArrayList list = new ArrayList(length);
        for (int i = 0; i < length; ++i) {
            list.add(i, subGenerator.arbitrary(random, size));
        }
        
        return list;
    }
}
