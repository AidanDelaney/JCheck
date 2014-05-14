package org.jcheck.generator.primitive;

import java.util.Random;
import org.jcheck.Arbitrary;
import org.jcheck.generator.Gen;

/**
 *
 * @author Hampus
 */
public class StringGen implements Gen<String> 
{
    private static final Gen<Character> charGen = new CharacterGen();
    
    public String arbitrary(Random random, long size)
    {
        int length = (int)Arbitrary.random(random, 0, 
                                           Math.min(Integer.MAX_VALUE, size));
        StringBuffer buffer = new StringBuffer(length);
        for (int i = 0; i < length; ++i) {
            buffer.append(charGen.arbitrary(random, size));
        }
        
        return buffer.toString();
    }
}
