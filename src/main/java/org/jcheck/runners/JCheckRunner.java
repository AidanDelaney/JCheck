package org.jcheck.runners;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.jcheck.Configuration;
import org.jcheck.annotations.Generator;
import org.jcheck.annotations.UseGenerators;
import org.jcheck.generator.Gen;
import org.jcheck.internal.runners.statements.JCheckStatement;
import org.junit.Test;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class JCheckRunner extends BlockJUnit4ClassRunner 
{
    private Configuration configuration;
    private Map<Class<?>, Class<? extends Gen<?>>> classGenerators;
    
    public JCheckRunner(Class<?> klass) throws InitializationError
    {
        super(klass);

        long seed = getSeedForRandom();
 
        configuration = new Configuration(getInstanceOfRandom(seed), seed);

        org.jcheck.annotations.Configuration config = getClassAnnotation(org.jcheck.annotations.Configuration.class);
        if (config != null) {
            if (config.size() != 0) {
                configuration.setSize(config.size());
            }

            if (config.tests() != 0) {
                configuration.setMaxNumberOfTests(config.tests());
            }
        }

        classGenerators = new HashMap<Class<?>, Class<? extends Gen<?>>>();

        Generator generator = getClassAnnotation(Generator.class);
        if (generator != null) {
            addGenerator(generator);
        }

        UseGenerators useGenerators = getClassAnnotation(UseGenerators.class);
        if (useGenerators != null) {
           for (Generator gen : useGenerators.value()) {
               addGenerator(gen);
           }
        }
    }
    
    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test)
    {
        try {
            return new JCheckStatement((Configuration) configuration.clone(), 
                                       classGenerators,
                                       method, test);
        }
        catch (CloneNotSupportedException ex) {
            throw new RuntimeException("Unable to copy configuration.", ex);
        }
    }

    @Override
    protected void validateTestMethods(List<Throwable> errors)
    {
        List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(Test.class);

        for (FrameworkMethod eachTestMethod : methods) {
            eachTestMethod.validatePublicVoid(false, errors);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Annotation> T getClassAnnotation(Class<T> annotation)
    {
        Annotation[] classAnnotations = getTestClass().getAnnotations();
        for (Annotation classAnnotation : classAnnotations) {
            if (classAnnotation.annotationType() == annotation) {
                return (T)classAnnotation;
            }
        }
        
        return null;
    }
    
    private void addGenerator(Generator generator) throws InitializationError 
    {
        Class<?> klass = generator.klass();
        Class<? extends Gen<?>> genClass = generator.generator();

        // This might not be possible, but I'm unsure.
        if (klass == null || genClass == null) {
            throw new InitializationError("Bad @Generator found.");
        }

        if (classGenerators.containsKey(klass)) {
            throw new InitializationError("Duplicate generators found.");
        }

        classGenerators.put(klass, genClass);
    }

    private Random getInstanceOfRandom(long seed) throws InitializationError
    {
        String randomClass = System.getProperty("jcheck.random");
        if (randomClass != null) {
            try {
                Class<?> random = Class.forName(randomClass);
                Constructor<?> randConst = random.getConstructor(long.class);
                return (Random) randConst.newInstance(seed);
            }
            catch (Exception ex) {
                throw new InitializationError("Unable to create instance of random");
            }
        }
        
        return new Random(seed);
    }

    private long getSeedForRandom() throws InitializationError
    {
        String seed = System.getProperty("jcheck.seed");
        if (seed != null) {
            try {
                return Long.parseLong(seed);
            }
            catch (NumberFormatException ex) {
                throw new InitializationError("The given seed is not a number");
            }
        }
        
        return System.currentTimeMillis();
    }
}
