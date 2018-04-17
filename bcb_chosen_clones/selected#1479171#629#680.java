    @SuppressWarnings("unchecked")
    private Collection<?> createAdaptedCollection(IModelInstanceCollection<IModelInstanceElement> modelInstanceCollection, Class<?> clazzType) {
        Collection<Object> result;
        if (Collection.class.isAssignableFrom(clazzType)) {
            try {
                Constructor<?> collectionConstructor;
                collectionConstructor = clazzType.getConstructor(new Class[0]);
                result = (Collection<Object>) collectionConstructor.newInstance(new Object[0]);
            } catch (SecurityException e) {
                result = null;
            } catch (NoSuchMethodException e) {
                result = null;
            } catch (IllegalArgumentException e) {
                result = null;
            } catch (InstantiationException e) {
                result = null;
            } catch (IllegalAccessException e) {
                result = null;
            } catch (InvocationTargetException e) {
                result = null;
            }
            if (result == null) {
                if (UniqueEList.class.isAssignableFrom(clazzType)) {
                    result = new UniqueEList<Object>();
                } else if (List.class.isAssignableFrom(clazzType)) {
                    result = new BasicEList<Object>();
                } else if (Set.class.isAssignableFrom(clazzType)) {
                    result = new HashSet<Object>();
                }
            }
            if (result != null) {
                Class<?> elementClassType;
                if (clazzType.getTypeParameters().length == 1 && clazzType.getTypeParameters()[0].getBounds().length == 1 && clazzType.getTypeParameters()[0].getBounds()[0] instanceof Class) {
                    elementClassType = (Class<?>) clazzType.getTypeParameters()[0].getBounds()[0];
                } else {
                    elementClassType = Object.class;
                }
                for (IModelInstanceElement anElement : modelInstanceCollection.getCollection()) {
                    result.add(createAdaptedElement(anElement, elementClassType));
                }
            } else {
                String msg;
                msg = EcoreModelInstanceTypeMessages.EcoreModelInstance_CannotRecreateCollection;
                throw new IllegalArgumentException(msg);
            }
        } else {
            String msg;
            msg = EcoreModelInstanceTypeMessages.EcoreModelInstance_CannotRecreateCollection;
            throw new IllegalArgumentException(msg);
        }
        return result;
    }
