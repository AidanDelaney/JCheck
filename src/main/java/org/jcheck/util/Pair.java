package org.jcheck.util;

public final class Pair<T1, T2>
{
    private final T1 first;
    private final T2 second;

    public Pair(T1 first, T2 second)
    {
        this.first = first;
        this.second = second;
    }
    
    public T1 fst()
    {
        return first;
    }
    
    public T2 snd()
    {
        return second;
    }

    @Override
    public boolean equals(Object other)
    {
        if (other instanceof Pair) {
            Pair otherPair = (Pair) other;
            return isEqual(this.first, otherPair.first) &&
                   isEqual(this.second, otherPair.second);
        }
        
        return false;
    }
    
    private static boolean isEqual(Object o1, Object o2)
    {
        if (o1 == o2) {
            return true;
        }
        else if (o1 != null) {
            return o1.equals(o2);
        }
        
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash1 = first == null ? 0 : first.hashCode();
        int hash2 = second == null ? 0 : second.hashCode();
        return hash1 ^ hash2;
    }
    
    public static <T1, T2> Pair<T1, T2> make(T1 first, T2 second)
    {
        return new Pair<T1, T2>(first, second);
    }
}
