package org.jcheck;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Vector;
import org.jcheck.generator.Gen;
import org.jcheck.generator.ArrayListGen;
import org.jcheck.generator.BigDecimalGen;
import org.jcheck.generator.BigIntegerGen;
import org.jcheck.generator.HashMapGen;
import org.jcheck.generator.LinkedListGen;
import org.jcheck.generator.ListGen;
import org.jcheck.generator.StackGen;
import org.jcheck.generator.TreeMapGen;
import org.jcheck.generator.VectorGen;
import org.jcheck.generator.primitive.BooleanGen;
import org.jcheck.generator.primitive.ByteGen;
import org.jcheck.generator.primitive.CharacterGen;
import org.jcheck.generator.primitive.DoubleGen;
import org.jcheck.generator.primitive.FloatGen;
import org.jcheck.generator.primitive.IntegerGen;
import org.jcheck.generator.primitive.LongGen;
import org.jcheck.generator.primitive.ShortGen;
import org.jcheck.generator.primitive.StringGen;

/**
 * Utility class that helps in creating new generators.
 * 
 * @author Hampus
 */
public class Arbitrary
{
    private static final Map<Class<?>, Class<? extends Gen<?>>> STD_GENERATORS = new HashMap<Class<?>, Class<? extends Gen<?>>>()
    {{
        put(boolean.class, BooleanGen.class);
        put(Boolean.class, BooleanGen.class);
        put(char.class, CharacterGen.class);
        put(Character.class, CharacterGen.class);
        put(byte.class, ByteGen.class);
        put(Byte.class, ByteGen.class);
        put(short.class, ShortGen.class);
        put(Short.class, ShortGen.class);
        put(int.class, IntegerGen.class);
        put(Integer.class, IntegerGen.class);
        put(long.class, LongGen.class);
        put(Long.class, LongGen.class);
        put(float.class, FloatGen.class);
        put(Float.class, FloatGen.class);
        put(double.class, DoubleGen.class);
        put(Double.class, DoubleGen.class);
        put(String.class, StringGen.class);
        put(BigInteger.class, BigIntegerGen.class);
        put(BigDecimal.class, BigDecimalGen.class);
        put(ArrayList.class, ArrayListGen.class);
        put(LinkedList.class, LinkedListGen.class);
        put(Vector.class, VectorGen.class);
        put(Stack.class, StackGen.class);
        put(List.class, ListGen.class);
        put(HashMap.class, HashMapGen.class);
        put(TreeMap.class, TreeMapGen.class);
        put(SortedMap.class, TreeMapGen.class);
    }};

    /**
     * Get the standard (jcheck-supplied) generator for the given
     * Java-class.
     * 
     * @param forClass the type of object the generator should be for
     * @return the standard generator for the given class (or null if no such generator exist)
     */
    public static <T> Class<? extends Gen<T>> getStandardGenerator(Class<T> forClass)
    {
        @SuppressWarnings("unchecked")
        Class<? extends Gen<T>> genClass = (Class<? extends Gen<T>>) STD_GENERATORS.get(forClass);
        return genClass;
    }
    
    /**
     * Generate a random long in the interval [from-to].
     * 
     */
    public static long random(Random random, long from, long to)
    {
        return ((long)(random.nextDouble()*(to-from+1))) + from;
    }
}
