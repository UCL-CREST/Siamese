    static void show(final Class clazz, final Display display) {
        assert clazz != null;
        assert display != null;
        try {
            final Constructor<Dialog> c = clazz.getConstructor(new Class[] { Display.class });
            final Dialog d = c.newInstance(new Object[] { display });
            d.runModal();
        } catch (Exception e) {
            logger.error(e);
        }
    }
