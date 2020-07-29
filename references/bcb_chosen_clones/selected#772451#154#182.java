    private Documentor getDocumentorFromAnnotation(final Class<?> testClass, NakedObjectConfiguration configuration) throws InitializationError {
        final DocumentUsing documentUsingAnnotation = testClass.getAnnotation(DocumentUsing.class);
        if (documentUsingAnnotation == null) {
            return null;
        }
        final Class<? extends Documentor> documentorClass = documentUsingAnnotation.value();
        try {
            Constructor<? extends Documentor> constructor = documentorClass.getConstructor(new Class[] { NakedObjectConfiguration.class, String.class });
            return constructor.newInstance(configuration, testClass.getCanonicalName());
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
        }
        try {
            return documentorClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
