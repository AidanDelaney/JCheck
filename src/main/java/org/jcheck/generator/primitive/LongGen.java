package org.jcheck.generator.primitive;

import java.util.Random;
import org.jcheck.Arbitrary;
import org.jcheck.generator.Gen;

public class LongGen implements Gen<Long>
{
    public Long arbitrary(Random random, long size)
    {
        long maxSize = Math.min(size, Long.MAX_VALUE);
        long minSize = Math.max(-size, Long.MIN_VALUE);
        
        return Long.valueOf(Arbitrary.random(random, minSize, maxSize));
    }
}
