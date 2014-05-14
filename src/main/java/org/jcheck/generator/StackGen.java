package org.jcheck.generator;

import java.util.Random;
import java.util.Stack;
import org.jcheck.Arbitrary;

public class StackGen implements Gen<Stack> 
{
    private Gen subGenerator;
    
    public StackGen(Gen subGenerator)
    {
        this.subGenerator = subGenerator;
    }
    
    @SuppressWarnings("unchecked")
    public Stack arbitrary(Random random, long size)
    {
        int length = (int) Arbitrary.random(random, 0, 
                                      Math.min(Integer.MAX_VALUE, size));
        Stack list = new Stack();
        for (int i = 0; i < length; ++i) {
            list.add(subGenerator.arbitrary(random, size));
        }
        
        return list;
    }
}
