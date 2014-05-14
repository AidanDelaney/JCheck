package org.jcheck.generator;

import java.util.HashMap;
import java.util.Random;
import org.jcheck.Arbitrary;

public class HashMapGen implements Gen<HashMap> 
{
    private Gen keyGenerator;
    private Gen valueGenerator;
    
    public HashMapGen(Gen keyGenerator, Gen valueGenerator)
    {
        this.keyGenerator = keyGenerator;
        this.valueGenerator = valueGenerator;
    }
    
    @SuppressWarnings("unchecked")
    public HashMap arbitrary(Random random, long size)
    {
        int length = (int) Arbitrary.random(random, 0, 
                                      Math.min(Integer.MAX_VALUE, size));
        HashMap map = new HashMap();
        for (int i = 0; i < length; ++i) {
            Object key = keyGenerator.arbitrary(random, size);
            Object value = valueGenerator.arbitrary(random, size);
            map.put(key, value);
        }
        
        return map;
    }
}
