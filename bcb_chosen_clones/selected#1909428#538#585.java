    public DynamicObject2D createObject(Class<?> elementClass, Class<?>[] paramClasses, Object[] param) {
        Constructor<?> cons;
        DynamicObject2D newObject;
        Class<?>[] buildClasses = new Class[paramClasses.length];
        for (int i = 0; i < paramClasses.length; i++) {
            buildClasses[i] = paramClasses[i];
            if (paramClasses[i].isArray()) {
                Class<?> arrayClass = paramClasses[i].getComponentType();
                if (Shape2D.class.isAssignableFrom(arrayClass)) buildClasses[i] = Array.newInstance(DynamicShape2D.class, 0).getClass();
                if (Transform2D.class.isAssignableFrom(arrayClass)) buildClasses[i] = Array.newInstance(DynamicTransform2D.class, 0).getClass();
                if (Measure2D.class.isAssignableFrom(arrayClass)) buildClasses[i] = Array.newInstance(DynamicMeasure2D.class, 0).getClass();
                if (Vector2D.class.isAssignableFrom(arrayClass)) buildClasses[i] = Array.newInstance(DynamicVector2D.class, 0).getClass();
                if (DynamicPredicate2D.class.isAssignableFrom(arrayClass)) buildClasses[i] = Array.newInstance(DynamicPredicate2D.class, 0).getClass();
            } else {
                if (DynamicShape2D.class.isAssignableFrom(paramClasses[i])) buildClasses[i] = DynamicShape2D.class;
                if (DynamicTransform2D.class.isAssignableFrom(paramClasses[i])) buildClasses[i] = DynamicTransform2D.class;
                if (DynamicMeasure2D.class.isAssignableFrom(paramClasses[i])) buildClasses[i] = DynamicMeasure2D.class;
                if (DynamicVector2D.class.isAssignableFrom(paramClasses[i])) buildClasses[i] = DynamicVector2D.class;
                if (DynamicPredicate2D.class.isAssignableFrom(paramClasses[i])) buildClasses[i] = DynamicPredicate2D.class;
                if (Shape2D.class.isAssignableFrom(paramClasses[i])) buildClasses[i] = DynamicShape2D.class;
                if (Transform2D.class.isAssignableFrom(paramClasses[i])) buildClasses[i] = DynamicTransform2D.class;
                if (Measure2D.class.isAssignableFrom(paramClasses[i])) buildClasses[i] = DynamicMeasure2D.class;
                if (Vector2D.class.isAssignableFrom(paramClasses[i])) buildClasses[i] = DynamicVector2D.class;
                if (DynamicPredicate2D.class.isAssignableFrom(paramClasses[i])) buildClasses[i] = DynamicPredicate2D.class;
            }
        }
        try {
            cons = elementClass.getConstructor(buildClasses);
        } catch (NoSuchMethodException ex) {
            System.out.println("couldn't create constructor for " + elementClass.getName());
            return null;
        }
        try {
            newObject = (DynamicObject2D) cons.newInstance(param);
        } catch (InvocationTargetException ex) {
            System.out.println(param);
            System.out.println(param[0]);
            System.out.println(cons);
            System.out.println(ex);
            System.out.println(ex.getTargetException());
            return null;
        } catch (Exception ex) {
            System.out.println("Couldn't instanciate new class : " + elementClass.getName());
            System.out.println(ex);
            return null;
        }
        return newObject;
    }
