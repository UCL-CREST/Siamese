    public AbstractField createField(String type, String name, Properties prop) throws GrammarFactoryException {
        AbstractField field = null;
        try {
            String className = (String) mapping.get(type.toUpperCase());
            if (className == null || "".equals(className)) {
                throw new ClassNotFoundException("datatype not found in mapping");
            }
            Constructor c = Class.forName(className).getConstructor(new Class[] { String.class, Properties.class });
            Object o = c.newInstance(new Object[] { name, prop });
            field = (AbstractField) o;
        } catch (ClassNotFoundException e) {
            throw new GrammarFactoryException("cannot create field. type: " + type + ", name: " + name + ")", e.getException());
        } catch (NoSuchMethodException e) {
            throw new GrammarFactoryException("cannot create field. type: " + type + ", name: " + name + ")", e.getCause());
        } catch (InstantiationException e) {
            throw new GrammarFactoryException("cannot create field. type: " + type + ", name: " + name + ")", e.getCause());
        } catch (IllegalAccessException e) {
            throw new GrammarFactoryException("cannot create field. type: " + type + ", name: " + name + ")", e.getCause());
        } catch (InvocationTargetException e) {
            throw new GrammarFactoryException("cannot create field. type: " + type + ", name: " + name + ")", e.getCause());
        }
        if (field == null) {
            throw new GrammarFactoryException("field type [" + type + "]  doesn't exist. (field name: " + name + ")", null);
        } else {
            return field;
        }
    }
