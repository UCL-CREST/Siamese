    private static JumbleListener getListener(String className) {
        try {
            final Class clazz = Class.forName(className);
            try {
                final Constructor c = clazz.getConstructor(new Class[0]);
                return (JumbleListener) c.newInstance(new Object[0]);
            } catch (IllegalAccessException e) {
                System.err.println("Invalid output class. Exception: ");
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                System.err.println("Invalid output class. Exception: ");
                e.printStackTrace();
            } catch (InstantiationException e) {
                System.err.println("Invalid output class. Exception: ");
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                System.err.println("Invalid output class. Exception: ");
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            ;
        }
        throw new IllegalArgumentException("Couldn't create JumbleListener: " + className);
    }
