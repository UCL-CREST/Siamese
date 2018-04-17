    private static IAtom dublicateAtom(final IAtom a, final Collection<ITerm> t) {
        if (a == null) {
            throw new NullPointerException("The atom must not be null");
        }
        final ITerm[] terms = (t == null) ? a.getTuple().toArray(new ITerm[a.getTuple().size()]) : t.toArray(new ITerm[t.size()]);
        try {
            return a.getClass().getConstructor(ITerm[].class).newInstance(new Object[] { terms });
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Couldn't find the consturctor for " + a.getClass().getName(), e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Couldn't create the instance " + "(the class is abstract) for " + a.getClass().getName(), e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Couldn't access the constructor for " + a.getClass().getName(), e);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw new IllegalArgumentException("The constructor of " + a.getClass().getName() + " threw an exception", e);
        }
    }
