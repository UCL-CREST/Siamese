    private static Object createObject(String className, EntityManager manager) {
        Constructor entityManagerConstructor = null;
        Object object = null;
        try {
            Class[] entityManagerArgaClass = new Class[] { EntityManager.class };
            Object[] inArgs = new Object[] { manager };
            Class classDefinition = Class.forName(className);
            entityManagerConstructor = classDefinition.getConstructor(entityManagerArgaClass);
            log.trace("Constructor: " + entityManagerConstructor.toString());
            object = entityManagerConstructor.newInstance(inArgs);
            log.trace("Object: " + object.toString());
        } catch (Exception e) {
            log.error(e);
        }
        return object;
    }
