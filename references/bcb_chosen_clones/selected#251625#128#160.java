    public NakedValue createAdapterForValue(final Object value) {
        Assert.assertFalse("can't create an adapter for a NOF adapter", value instanceof Naked);
        Assert.assertFalse("can't create an adapter for a NO Specification", value instanceof NakedObjectSpecification);
        NakedValue adapter = null;
        NakedObjectReflector reflector = NakedObjectsContext.getReflector();
        NakedObjectSpecification specification = reflector.loadSpecification(value.getClass());
        Class adapterClass = (Class) adapterClasses.get(specification);
        if (adapterClass != null) {
            try {
                Constructor[] constructors = adapterClass.getConstructors();
                for (int i = 0; i < constructors.length; i++) {
                    if (constructors[i].getParameterTypes().length == 1) {
                        adapter = (NakedValue) constructors[i].newInstance(new Object[] { value });
                        break;
                    }
                }
                if (adapter == null) {
                    throw new ObjectLoaderException("Failed to find suitable constructor in " + adapterClass);
                }
            } catch (InstantiationException e) {
                throw new ObjectLoaderException("Failed to create value adapter of type " + adapterClass, e);
            } catch (IllegalAccessException e) {
                throw new ObjectLoaderException("Failed to create value adapter of type " + adapterClass + "; could not access constructor", e);
            } catch (IllegalArgumentException e) {
                throw new ObjectLoaderException("Failed to create value adapter of type " + adapterClass, e);
            } catch (InvocationTargetException e) {
                throw new ObjectLoaderException("Failed to create value adapter of type " + adapterClass, e);
            }
        } else {
            adapter = reflector.createValueAdapter(value);
        }
        return adapter;
    }
