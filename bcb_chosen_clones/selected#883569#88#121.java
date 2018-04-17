    private void getInstances(Class<?> targetClass, Class<? extends TestProfile> testProfile) {
        if (testProfile == null) {
            error("You must specify a testProfile. Look at another unit test to see how to do this.");
            return;
        }
        if (targetClass == null) {
            error("You must specify a targetClass. Look at another unit test to see how to do this.");
            return;
        }
        testProfileName = testProfile.getName();
        targetClassName = targetClass.getName();
        if (!isTestProfile(testProfile)) {
            error(testProfileName + " must implement the 'TestProfile' interface.");
            return;
        }
        String clazz = null;
        try {
            clazz = testProfileName;
            Constructor<? extends TestProfile> profileConstructor = testProfile.getConstructor();
            testProfileInstance = profileConstructor.newInstance();
            clazz = targetClassName;
            Constructor<?> targetConstructor = targetClass.getConstructor();
            targetInstance = targetConstructor.newInstance();
        } catch (NoSuchMethodException exception) {
            error("Could not instantiate " + clazz + ". It must have a no-argument constructor.");
        } catch (InstantiationException exception) {
            error("Could not instantiate " + clazz + ". Exception during target object initialization.");
            Log.out(exception);
        } catch (IllegalAccessException exception) {
            error("Could not instantiate " + clazz + ". Make sure its constructor is public.");
        } catch (InvocationTargetException exception) {
            error("Could not instantiate " + clazz + ".");
        }
    }
