    private static Iterable<Pair<Path, String>> create(URL configLoc, Class<? extends Iterable<Pair<Path, String>>> clazz) throws IOException, ParseException {
        try {
            Constructor<? extends Iterable<Pair<Path, String>>> constructor = clazz.getConstructor(URL.class);
            return constructor.newInstance(configLoc);
        } catch (SecurityException ex) {
            throw new Error(ex);
        } catch (NoSuchMethodException ex) {
            throw new Error(ex);
        } catch (IllegalArgumentException ex) {
            throw new Error(ex);
        } catch (InstantiationException ex) {
            throw new Error(ex);
        } catch (IllegalAccessException ex) {
            throw new Error(ex);
        } catch (InvocationTargetException ex) {
            IOUtils.checkCause(IOException.class, ex);
            IOUtils.checkCause(ParseException.class, ex);
            throw new RuntimeException(ex.getCause());
        }
    }
