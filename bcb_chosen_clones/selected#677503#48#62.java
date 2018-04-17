    @Test
    @SuppressWarnings("unchecked")
    public void test() throws Exception {
        assertTrue("Compilation error", compileClass(_classNames));
        Class wrapperClass = loadClass(_wrapperClassName);
        Class expectedWrapperClass = loadClass(_expectedWrapperClassName);
        Class wrappableClass = loadClass(_wrappableClassName);
        assertSimilar(expectedWrapperClass, wrapperClass);
        if ((wrapperClass.getModifiers() & Modifier.ABSTRACT) == 0) {
            Constructor constructor = wrapperClass.getConstructor(wrappableClass);
            Object wrappableObject = loadClass(_baseImplClassName).newInstance();
            Object instance = constructor.newInstance(wrappableObject);
            assertNotNull(instance);
        }
    }
