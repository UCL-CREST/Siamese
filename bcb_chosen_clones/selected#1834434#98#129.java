    private void setProperty(final Object object, final String fieldName, final Object value) {
        String field = fieldName.substring(fieldName.lastIndexOf(".") + 1);
        field = Character.toUpperCase(field.charAt(0)) + field.substring(1);
        LOG.debug("    setting " + field + " on " + object);
        Class c = object.getClass();
        LOG.debug("    getting set method for " + field);
        Method setter = null;
        try {
            PropertyDescriptor property = new PropertyDescriptor(field, c, null, "set" + field);
            setter = property.getWriteMethod();
            Class cls = setter.getParameterTypes()[0];
            if (cls.isArray()) {
                int length = Array.getLength(value);
                Object[] array = (Object[]) Array.newInstance(cls.getComponentType(), length);
                System.arraycopy(value, 0, array, 0, length);
                setter.invoke(object, new Object[] { array });
            } else {
                setter.invoke(object, new Object[] { value });
            }
            LOG.debug("  set " + field + " with " + value.getClass());
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (IllegalArgumentException e) {
            throw new StartupException(e.getMessage() + ": can't invoke " + setter.getName() + " with instance of " + value.getClass().getName());
        } catch (IllegalAccessException e) {
            throw new StartupException(e.getMessage() + ": can't access " + setter.getName());
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }
