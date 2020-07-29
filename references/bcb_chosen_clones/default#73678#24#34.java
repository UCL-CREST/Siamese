    public static Parent create(Class klass) {
        String className = klass.toString();
        System.out.println(className);
        try {
            Constructor constructor = klass.getConstructor(null);
            return (Parent) constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
