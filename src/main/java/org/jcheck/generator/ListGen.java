package org.jcheck.generator;

import java.util.List;
import java.util.Random;

public class ListGen implements Gen<List> 
{
    private Gen[] listGenerators;
    
    public ListGen(Gen subGenerator)
    {
        listGenerators = new Gen[] { new ArrayListGen(subGenerator),
                                     new LinkedListGen(subGenerator),
                                     new StackGen(subGenerator),
                                     new VectorGen(subGenerator)
                                   };
    }
    
    @SuppressWarnings("unchecked")
    public List arbitrary(Random random, long size)
    {
        Gen gen = listGenerators[random.nextInt(listGenerators.length)];
        return (List) gen.arbitrary(random, size);
    }
}
