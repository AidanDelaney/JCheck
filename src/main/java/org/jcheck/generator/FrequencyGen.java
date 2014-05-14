package org.jcheck.generator;

import java.util.Random;
import org.jcheck.util.Pair;

public class FrequencyGen<T> implements Gen<T>
{
    private Pair<Integer, Gen<T>>[] generatorPairs;
    private int size;
    
    public FrequencyGen(Pair<Integer, Gen<T>> ... generatorPairs)
    {
        this.generatorPairs = generatorPairs;
        this.size = 0;
        for (int i = 0; i < generatorPairs.length; ++i)
        {
            this.size += generatorPairs[i].fst();
        }
    }
    
    public T arbitrary(Random random, long size)
    {
        int generatorNumber = random.nextInt(this.size);
        Gen<T> generator = pick(0, generatorNumber);
        return generator.arbitrary(random, size);
    }
    
    private Gen<T> pick(int pos, int sought)
    {
        int interval = generatorPairs[pos].fst();
        if (interval <= sought) {
            return generatorPairs[pos].snd();
        }
        else {
            return pick(pos+1, sought-interval);
        }
    }
}
