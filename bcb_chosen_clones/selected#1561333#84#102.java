    public <T> Collection<T> instantiateAll(Class<T> makeMe) {
        LinkedList<T> out = new LinkedList<T>();
        for (Class<?> c : getClasses()) {
            if (makeMe.isAssignableFrom(c)) {
                Class<T> tc = (Class<T>) c;
                try {
                    Constructor<T> con = tc.getConstructor();
                    out.add(con.newInstance());
                } catch (IllegalArgumentException e) {
                } catch (InstantiationException e) {
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                } catch (SecurityException e) {
                } catch (NoSuchMethodException e) {
                }
            }
        }
        return out;
    }
