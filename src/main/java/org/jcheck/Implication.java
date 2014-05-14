package org.jcheck;

import org.jcheck.exceptions.ImplicationFalseError;

public class Implication 
{
    /**
     * <p>
     * Make an implication that must hold for the test to run.
     * </p>
     * 
     * <p>
     * Example:
     * <code>
     * <pre>
     * &#064;Test
     * public void testDivision(int i, int j)
     * {
     *     imply(j != 0);
     *     assertEquals(i/j, someDivision(i, j));
     * }
     * </pre>
     * </code>
     * </p>
     * 
     * 
     * @param implication
     */
    public static final void imply(boolean implication)
    {
        if (!implication) {
            throw new ImplicationFalseError();
        }
    }
    
    /**
     * <p>
     * Make an implication that must hold for the test to run.
     * </p>
     * 
     * <p>
     * Example:
     * <code>
     * <pre>
     * &#064;Test
     * public void testDivision(int i, int j)
     * {
     *     imply("Can't divide by zero", j != 0);
     *     assertEquals(i/j, someDivision(i, j));
     * }
     * </pre>
     * </code>
     * </p>
     * 
     * 
     * @param implication
     */
    public static final void imply(String message, boolean implication)
    {
        if (!implication) {
            throw new ImplicationFalseError(message);
        }
    }
}
