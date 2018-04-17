    private Property loadProperty(BasicGameCallback callback, File propertyFile) throws PropertyInstantiationException {
        String propertyName = propertyFile.getName();
        String type = null;
        try {
            LuaObjectScript script = LuaScriptConstructor.createLuaObjectScript(propertyFile);
            type = script.callStringHook("getType", 1);
            Class<Property> propertyClass = getClass(type);
            Property property = propertyClass.getConstructor(BasicGameCallback.class).newInstance(callback);
            property.setScript(script);
            property.init();
            return property;
        } catch (ClassCastException e) {
            throw new PropertyInterfaceNotImplementedException(propertyName, type);
        } catch (InstantiationException e) {
            throw new AbstractPropertyTypeException(propertyName, type);
        } catch (NoSuchMethodException e) {
            throw new UndefinedConstructorException(propertyName, type);
        } catch (ClassNotFoundException e) {
            throw new UnknownPropertyClassException(propertyName, type);
        } catch (IllegalArgumentException e) {
            throw new UndefinedConstructorException(propertyName, type);
        } catch (IllegalAccessException e) {
            throw new UndefinedConstructorException(propertyName, type);
        } catch (ScriptException e) {
            throw new PropertyScriptException(propertyName, e);
        } catch (Throwable e) {
            throw new PropertyInstantiationException(propertyName, "unexpected exception occurred during construction", e);
        }
    }
