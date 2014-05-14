package org.jcheck.generator;

import java.util.LinkedList;
import java.util.Random;
import org.jcheck.Arbitrary;

public class LinkedListGen implements Gen<LinkedList> 
{
    private Gen subGenerator;
    
    public LinkedListGen(Gen subGenerator)
    {
        this.subGenerator = subGenerator;
    }
    
    @SuppressWarnings("unchecked")
    public LinkedList arbitrary(Random random, long size)
    {
        int length = (int) Arbitrary.random(random, 0, 
                                      Math.min(Integer.MAX_VALUE, size));
        LinkedList list = new LinkedList();
        for (int i = 0; i < length; ++i) {
            list.add(i, subGenerator.arbitrary(random, size));
        }
        
        return list;
    }
}
