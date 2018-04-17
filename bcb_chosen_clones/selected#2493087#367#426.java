    @SuppressWarnings("unchecked")
    private static Collection<?> createAdaptedCollection(IModelInstanceCollection<IModelInstanceElement> modelInstanceCollection, Class<?> clazzType, Set<ClassLoader> classLoaders) {
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
                if (TreeSet.class.isAssignableFrom(clazzType)) {
                    result = new TreeSet<Object>();
                } else if (LinkedHashSet.class.isAssignableFrom(clazzType)) {
                    result = new LinkedHashSet<Object>();
                } else if (HashSet.class.isAssignableFrom(clazzType)) {
                    result = new HashSet<Object>();
                } else if (Stack.class.isAssignableFrom(clazzType)) {
                    result = new Stack<Object>();
                } else if (Vector.class.isAssignableFrom(clazzType)) {
                    result = new Vector<Object>();
                } else if (LinkedList.class.isAssignableFrom(clazzType)) {
                    result = new LinkedList<Object>();
                } else {
                    result = new ArrayList<Object>();
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
                    result.add(createAdaptedElement(anElement, elementClassType, classLoaders));
                }
            } else {
                String msg;
                msg = JavaModelInstanceTypeMessages.JavaModelInstance_CannotRecreateCollection;
                throw new IllegalArgumentException(msg);
            }
        } else {
            String msg;
            msg = JavaModelInstanceTypeMessages.JavaModelInstance_CannotRecreateCollection;
            throw new IllegalArgumentException(msg);
        }
        return result;
    }
