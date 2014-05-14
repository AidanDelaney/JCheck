package org.jcheck;

import java.util.Random;

public final class Configuration implements Cloneable
{
    private final Random random;
    private final long seed;
    private int maxNumberOfTests;
    private int maxNumberOfFailedParams;
    private long size;

    public Configuration(Random random, long seed)
    {
        this(random, seed, 100, 50, 100);
    }
    
    public Configuration(Random random, long seed, int maxNumberOfTests,
                         int maxNumberOfFailedParams, long size)
    {
        this.random = random;
        this.seed = seed;
        this.maxNumberOfTests = maxNumberOfTests;
        this.maxNumberOfFailedParams = maxNumberOfFailedParams;
        this.size = size;
    }
    
    public void setSize(long size)
    {
        this.size = size;
    }
    
    public void setMaxNumberOfTests(int tests)
    {
        maxNumberOfTests = tests;
    }
      
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        Configuration config = (Configuration) super.clone();
        return config;
    }

    public Random getRandom()
    {
        return random;
    }

    public int getMaxNumberOfTests()
    {
        return maxNumberOfTests;
    }

    public int getMaxNumberOfFailedParams()
    {
        return maxNumberOfFailedParams;
    }

    public long getSize()
    {
        return size;
    }

    public long getSeed()
    {
        return seed;
    }
}
