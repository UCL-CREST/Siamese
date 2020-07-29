    private IModelInstanceObject copyForAtPreWithReflections() throws CopyForAtPreException {
        IModelInstanceObject result;
        Class<?> adapteeClass;
        adapteeClass = this.myEObject.getClass();
        try {
            EObject copiedAdaptedObject;
            Constructor<?> emptyConstructor;
            emptyConstructor = adapteeClass.getConstructor(new Class[0]);
            copiedAdaptedObject = (EObject) emptyConstructor.newInstance(new Object[0]);
            while (adapteeClass != null) {
                for (Field field : adapteeClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    if (!(Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers()))) {
                        field.set(copiedAdaptedObject, field.get(this.myEObject));
                    }
                }
                adapteeClass = adapteeClass.getSuperclass();
            }
            result = new EcoreModelInstanceObject(copiedAdaptedObject, this.myAdaptedType, this.myType, this.getOriginalType(), this.myFactory);
        } catch (SecurityException e) {
            String msg;
            msg = EcoreModelInstanceTypeMessages.EcoreModelInstanceObject_CannotCopyForAtPre;
            msg = NLS.bind(msg, this.getName(), e.getMessage());
            throw new CopyForAtPreException(msg, e);
        } catch (NoSuchMethodException e) {
            String msg;
            msg = EcoreModelInstanceTypeMessages.EcoreModelInstanceObject_CannotCopyForAtPre;
            msg = NLS.bind(msg, this.getName(), e.getMessage());
            throw new CopyForAtPreException(msg, e);
        } catch (IllegalArgumentException e) {
            String msg;
            msg = EcoreModelInstanceTypeMessages.EcoreModelInstanceObject_CannotCopyForAtPre;
            msg = NLS.bind(msg, this.getName(), e.getMessage());
            throw new CopyForAtPreException(msg, e);
        } catch (InstantiationException e) {
            String msg;
            msg = EcoreModelInstanceTypeMessages.EcoreModelInstanceObject_CannotCopyForAtPre;
            msg = NLS.bind(msg, this.getName(), e.getMessage());
            throw new CopyForAtPreException(msg, e);
        } catch (IllegalAccessException e) {
            String msg;
            msg = EcoreModelInstanceTypeMessages.EcoreModelInstanceObject_CannotCopyForAtPre;
            msg = NLS.bind(msg, this.getName(), e.getMessage());
            throw new CopyForAtPreException(msg, e);
        } catch (InvocationTargetException e) {
            String msg;
            msg = EcoreModelInstanceTypeMessages.EcoreModelInstanceObject_CannotCopyForAtPre;
            msg = NLS.bind(msg, this.getName(), e.getMessage());
            throw new CopyForAtPreException(msg, e);
        }
        return result;
    }
