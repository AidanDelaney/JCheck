package org.jcheck.generator.primitive;

import java.util.Random;
import org.jcheck.generator.Gen;

/**
 *
 * @author Hampus
 */
public class BooleanGen implements Gen<Boolean> 
{
    public Boolean arbitrary(Random random, long size)
    {
        return Boolean.valueOf(random.nextBoolean());
    }
}
