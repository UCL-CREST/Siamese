    private DeepClonerSPI createDeepCloner(ClassLoader classLoader) {
        final Class<DeepClonerSPI> deepClonerClass = ClassLoaderUtil.loadClass("org.powermock.classloading.DeepCloner");
        final Constructor<DeepClonerSPI> constructor = Whitebox.getConstructor(deepClonerClass, ClassLoader.class);
        try {
            return constructor.newInstance(classLoader);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate DeepCloner. The DeepCloner implementation must have a one-arg constructor taking a Classloader as parameter.", e);
        }
    }
