    private static Policy instantiatePolicy(String policyClassName) {
        try {
            Class<?> clazz;
            try {
                clazz = Class.forName("org.chargecar.prize.policies." + policyClassName);
                policyClassName = "org.chargecar.prize.policies." + policyClassName;
            } catch (ClassNotFoundException e) {
            }
            clazz = Class.forName(policyClassName);
            final Constructor<?> constructor = clazz.getConstructor();
            if (constructor != null) {
                final Policy policy = (Policy) constructor.newInstance();
                if (policy == null) {
                    System.err.println("Instantiation of Policy implementation [\" + policyClassName + \"] returned null.  Weird.");
                } else {
                    return policy;
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException while trying to find Policy implementation [" + policyClassName + "]: " + e);
        } catch (NoSuchMethodException e) {
            System.err.println("NoSuchMethodException while trying to find no-arg constructor for Policy implementation [" + policyClassName + "]: " + e);
        } catch (IllegalAccessException e) {
            System.err.println("IllegalAccessException while trying to instantiate Policy implementation [" + policyClassName + "]: " + e);
        } catch (InvocationTargetException e) {
            System.err.println("InvocationTargetException while trying to instantiate Policy implementation [" + policyClassName + "]: " + e);
        } catch (InstantiationException e) {
            System.err.println("InstantiationException while trying to instantiate Policy implementation [" + policyClassName + "]: " + e);
        }
        return null;
    }
