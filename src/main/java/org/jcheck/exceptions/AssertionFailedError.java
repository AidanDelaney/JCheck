package org.jcheck.exceptions;

import java.util.Arrays;

/**
 *
 * @author Hampus
 */
public class AssertionFailedError extends junit.framework.AssertionFailedError
{
    private String parameters;
    private int numtests;
    private Throwable error;
    private long seed;

    public AssertionFailedError(Object[] params, int numtests, Throwable error, long seed)
    {
        this.parameters = Arrays.toString(params);
        this.numtests = numtests;
        this.error = error;
        this.seed = seed;
    }
    
    @Override
    public String getMessage()
    {
        return String.format("%s after %d tests with input parameters %s (rng seed = %d).",
                             error.getMessage(), numtests, parameters, seed);
    }
}
