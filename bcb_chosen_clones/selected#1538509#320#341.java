    private Comic loadNative(URL path, String name, ContentProvider cp) throws ComicException {
        URLClassLoader loader = new URLClassLoader(new URL[] { path });
        try {
            Class<?> cClass = loader.loadClass(name);
            if (!Comic.class.isAssignableFrom(cClass)) throw new ComicException("Load error: Bad class file");
            Constructor cons = cClass.getConstructor(new Class[] { ContentProvider.class });
            if (cons == null) throw new ComicException("Unable to find correct constructor");
            return (Comic) cons.newInstance(new Object[] { cp });
        } catch (NoSuchMethodException e) {
            throw new ComicException("Unable to find correct constructor");
        } catch (InstantiationException e) {
            throw new ComicException("Unable to load comic " + name + " (init exception)");
        } catch (InvocationTargetException e) {
            throw new ComicException("Unable to invocate constructor " + name + " " + e.getCause());
        } catch (IllegalAccessException e) {
            throw new ComicException("Unable to access comic " + name);
        } catch (ClassNotFoundException e) {
            throw new ComicException("Unable to locate comic " + name);
        } catch (NoClassDefFoundError e) {
            throw new ComicException("Unable to load comic " + name + " (no class: " + e.getMessage() + ")");
        }
    }
