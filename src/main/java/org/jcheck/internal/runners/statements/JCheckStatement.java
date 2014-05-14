package org.jcheck.internal.runners.statements;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import junit.framework.AssertionFailedError;
import org.jcheck.Arbitrary;
import org.jcheck.Configuration;
import org.jcheck.annotations.Generator;
import org.jcheck.annotations.UseGenerators;
import org.jcheck.exceptions.ImplicationFalseError;
import org.jcheck.generator.Gen;
import org.jcheck.generator.primitive.ArrayGen;
import org.jcheck.util.Pair;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class JCheckStatement extends Statement
{
    private Configuration configuration;
    private FrameworkMethod method;
    private Object test;
 
    private HashMap<Pair<Type, Integer>, Gen<?>> genCache = new HashMap<Pair<Type, Integer>, Gen<?>>();
    private Map<Integer, Class<? extends Gen<?>>> fromPositionGenerators = new HashMap<Integer, Class<? extends Gen<?>>>();
    private Map<Class<?>, Class<? extends Gen<?>>> fromClassGenerators = new HashMap<Class<?>, Class<? extends Gen<?>>>();
    private Map<Class<?>, Class<? extends Gen<?>>> classGenerators = new HashMap<Class<?>, Class<? extends Gen<?>>>();
    
    public JCheckStatement(Configuration configuration,
                           Map<Class<?>, Class<? extends Gen<?>>> classGenerators, 
                           FrameworkMethod method, 
                           Object test)
    {
        this.configuration = configuration;
        this.classGenerators = classGenerators;
        this.method = method;
        this.test = test;
        
        collectGenerators();
    }
    
    @Override
    public void evaluate() throws Throwable
    {
        int maxNumberOfTests = configuration.getMaxNumberOfTests();
        long size = configuration.getSize();

        org.jcheck.annotations.Configuration configAnnotation = method.getAnnotation(org.jcheck.annotations.Configuration.class);
        if (configAnnotation != null) {
            if (configAnnotation.size() != 0) {
                size = configAnnotation.size();
            }
            
            if (configAnnotation.tests() != 0) {
                maxNumberOfTests = configAnnotation.tests();
            }
        }

        Type[] paramTypes = method.getMethod().getGenericParameterTypes();
        Object[] paramList = new Object[paramTypes.length];

        // If this is a test that has no parameters, only
        // run it once as JUnit normally does
        if (paramTypes.length == 0) {
            method.invokeExplosively(test, paramList);
            return;
        }
        
        for (int tests = 0; tests < maxNumberOfTests; ++tests) {
            for (int failed = 1; ; ++failed) {
                for (int pNo = 0; pNo < paramTypes.length; ++pNo) {
                    paramList[pNo] = arbitrary(paramTypes[pNo], pNo, 
                                               configuration.getRandom(), size);
                }

                try {
                    method.invokeExplosively(test, paramList);
                    break;
                }
                catch (ImplicationFalseError ex) {
                    if (failed >= configuration.getMaxNumberOfFailedParams()) {
                        throw new AssertionFailedError(String.format("Arguments exhausted after %d test (%d tries). %s", 
                                                                     tests,
                                                                     failed,
                                                                     ex.getMessage()));
                    }
                }
                catch(AssertionError ex) {
                    if (paramList.length > 0) {
                        throw new org.jcheck.exceptions.AssertionFailedError(paramList, tests, ex, configuration.getSeed());
                    }
                    else {
                        throw ex;
                    }
                }
            }
        }
    }
    
    private void collectGenerators()
    {
        UseGenerators useGenerators = method.getAnnotation(UseGenerators.class);
        if (useGenerators != null) {
            for (Generator generator : useGenerators.value()) {
                collectGenerator(generator);
            }
        }

        collectGenerator(method.getAnnotation(Generator.class));        
        collectParameterGenerators();
    }
    
    private void collectParameterGenerators()
    {
        Generator[] generators = getParameterAnnotations(Generator.class);
        for (int i = 0; i < generators.length; ++i) {
            if (generators[i] != null) {
                Class<? extends Gen<?>> genClass = generators[i].generator();
                fromPositionGenerators.put(i, genClass);
            }
        }
    }
    
    private void collectGenerator(Generator generator) {
        if (generator != null) {
            Class<?> klass = generator.klass();

            // I'm unsure if this can happen
            if (klass == null) {
                throw new RuntimeException("Bad @Generator found.");
            }

            Class<? extends Gen<?>> genClass = generator.generator();
            fromClassGenerators.put(klass, genClass);
        }
    }
    
    @SuppressWarnings("unchecked")
    private <T extends Annotation> T[] getParameterAnnotations(Class<T> annotationClass)
    {
        Annotation[][] paramAnnotations = method.getMethod().getParameterAnnotations();
        T[] annotations = (T[]) Array.newInstance(annotationClass, paramAnnotations.length);
        
        for (int i = 0; i < paramAnnotations.length; ++i) {
            for (int j = 0; j < paramAnnotations[i].length; ++j) {
                Annotation paramAnnotation = paramAnnotations[i][j];
                if (paramAnnotation.annotationType().isAssignableFrom(annotationClass)) {
                    annotations[i] = (T) paramAnnotation;
                }
            }
        }
        
        return annotations;
    }
    
    private Object arbitrary(Type paramType, int position, 
                             Random random, long size)
    {
        Gen<?> generator = genCache.get(Pair.make(paramType, position));
        if (generator == null) {
            generator = generator(paramType, position);
            genCache.put(Pair.make(paramType, position), generator);
        }

        return generator.arbitrary(random, size);
    }
    
    @SuppressWarnings("unchecked")
    private Gen<?> generator(Type t, int position)
    {
        if (t instanceof Class) {
            Class c = (Class) t;
            return generator(c, null, position);
        }
        else if (t instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) t;
            Type type = paramType.getRawType();
            if (!(type instanceof Class)) {
                throw new RuntimeException("Can't handle the given type " + t.toString());
            }
            
            Type[] types = paramType.getActualTypeArguments();
            Gen[] subGenerators = new Gen[types.length];
            for (int i = 0; i < types.length; ++i) {
                subGenerators[i] = generator(types[i], position);
            }
            
            return generator((Class) type, subGenerators, position);
        }
        else if (t instanceof GenericArrayType) {
            GenericArrayType arrt = (GenericArrayType) t;
            return generatorForArray(arrt, position);
        }
        else if (t instanceof WildcardType) {
            WildcardType wt = (WildcardType) t;
            Type[] upperBounds = wt.getUpperBounds();
            if (upperBounds.length > 1) {
                throw new RuntimeException(String.format("Can't handle wildcard types with multiple upper bounds (%s).",
                                                         wt.toString()));
            }
            
            return generator(upperBounds[0], position);
        }
        else if (t instanceof TypeVariable) {
            TypeVariable tv = (TypeVariable) t;
            Type[] bounds = tv.getBounds();
            if (bounds.length > 1) {
                throw new RuntimeException(String.format("Can't handle type variable with multiple bounds (%s).",
                                           tv.toString()));
            }

            return generator(bounds[0], position);
        }
        
        throw new RuntimeException(String.format("Unable to find a matching generator for type %s.",
                                                 t.toString()));
    }
    
    private Gen<?> generatorForArray(GenericArrayType t, int position) {
        int dimension = 1;
        Type subType;
        for (subType = t.getGenericComponentType(); subType instanceof GenericArrayType; 
             subType = ((GenericArrayType) subType).getGenericComponentType()) {
            ++dimension;
        }

        Gen subGen = generator(subType, position);   
        Class componentClass = findUnderlyingClass(subType);
        if (componentClass == null) {
            throw new RuntimeException(String.format("Unable to find a matching generator for type %s.", t.toString()));
        }
        
        return new ArrayGen(dimension, componentClass, subGen);
    }
    
    private static Class findUnderlyingClass(Type t)
    {
        if (t instanceof Class) {
            return (Class) t;
        }
        else if (t instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) t;
            return findUnderlyingClass(paramType.getRawType());
        }
        else if (t instanceof WildcardType) {
            WildcardType wt = (WildcardType) t;
            Type[] upperBounds = wt.getUpperBounds();
            // TODO: Handle null and length > 1
            return findUnderlyingClass(upperBounds[0]);
        }
        else if (t instanceof TypeVariable) {
            TypeVariable tv = (TypeVariable) t;
            // TODO: Handle null and length > 1
            return findUnderlyingClass(tv.getBounds()[0]);
        }
        
        return null;
    }
    
    private Gen<?> generatorForArray(Class<?> paramClass, int position)
    {
        int dimension = 0;
        Class<?> component = paramClass;
        while (true) {
            Class<?> tmp = component.getComponentType();
            if (tmp != null) {
                dimension++;
                component = tmp;
            }
            else {
                break;
            }
        }

        return new ArrayGen(dimension, component, 
                            generator(component, position));
    }
    
    @SuppressWarnings("unchecked")
    private <T, E extends Gen<T>> E generator(Class<T> paramClass, Gen[] subGenerators, int position)
    {
        Class<E> generatorClass = (Class<E>) fromPositionGenerators.get(position);
        if (generatorClass != null) {
            return instantiateGenerator(generatorClass, subGenerators);
        }

        generatorClass = (Class<E>) fromClassGenerators.get(paramClass);
        if (generatorClass != null) {
            return instantiateGenerator(generatorClass, subGenerators);
        }

        generatorClass = (Class<E>) classGenerators.get(paramClass);
        if (generatorClass != null) {
            return instantiateGenerator(generatorClass, subGenerators);
        }
        
        if (paramClass.isArray()) {
            return (E) generatorForArray(paramClass, position);
        }
        
        generatorClass = (Class<E>) Arbitrary.getStandardGenerator(paramClass);
        if (generatorClass != null) {
            return instantiateGenerator(generatorClass, subGenerators);
        }

        throw new RuntimeException(String.format("Unable to find a matching generator for class %s.",
                                                 paramClass.getName()));
    }

    @SuppressWarnings("unchecked")
    private <E extends Gen<?>> E instantiateGenerator(Class<E> genClass, Gen[] subGenerators)
    {
        Class[] subGenClasses = new Class[subGenerators == null ? 0 : subGenerators.length];
        Arrays.fill(subGenClasses, Gen.class);
        
        try {
            if (subGenClasses.length == 0) {
                return genClass.newInstance();
            }
            else {
                Constructor constr = genClass.getConstructor(subGenClasses);
                return (E) constr.newInstance((Object[])subGenerators);
            }
        }
        catch (InstantiationException ex) {
            unableToInstantiateGenerator(genClass, ex);
        }
        catch (IllegalAccessException ex) {
            unableToInstantiateGenerator(genClass, ex);
        }
        catch (IllegalArgumentException ex) {
            unableToInstantiateGenerator(genClass, ex);
        }
        catch (InvocationTargetException ex) {
            unableToInstantiateGenerator(genClass, ex);
        }
        catch (NoSuchMethodException ex) {
            unableToInstantiateGenerator(genClass, ex);
        }
        catch (SecurityException ex) {
            unableToInstantiateGenerator(genClass, ex);
        }
        
        // Should never happen
        return null;
    }
    
    private static void unableToInstantiateGenerator(Class genClass, Exception ex)
    {
        throw new RuntimeException(String.format("Unable to instantiate generator %s.", genClass.getName()), ex);
    }
}
