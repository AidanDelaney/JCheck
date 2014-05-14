package org.jcheck.generator;

import java.util.Random;
import java.util.TreeMap;
import org.jcheck.Arbitrary;

public class TreeMapGen implements Gen<TreeMap> 
{
    private Gen keyGenerator;
    private Gen valueGenerator;
    
    public TreeMapGen(Gen keyGenerator, Gen valueGenerator)
    {
        this.keyGenerator = keyGenerator;
        this.valueGenerator = valueGenerator;
    }
    
    @SuppressWarnings("unchecked")
    public TreeMap arbitrary(Random random, long size)
    {
        int length = (int) Arbitrary.random(random, 0, 
                                      Math.min(Integer.MAX_VALUE, size));
        TreeMap map = new TreeMap();
        for (int i = 0; i < length; ++i) {
            Object key = keyGenerator.arbitrary(random, size);
            Object value = valueGenerator.arbitrary(random, size);
            map.put(key, value);
        }
        
        return map;
    }
}
