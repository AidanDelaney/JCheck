package org.jcheck.generator.primitive;

import java.util.Random;
import org.jcheck.Arbitrary;
import org.jcheck.generator.Gen;

/**
 *
 * @author Hampus
 */
public class CharacterGen implements Gen<Character> 
{
    public Character arbitrary(Random random, long size)
    {
        return Character.valueOf((char)Arbitrary.random(random, 32, 255));
    }
}
