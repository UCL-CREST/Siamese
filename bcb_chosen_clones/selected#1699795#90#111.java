    public Repository createTypeSpecificRepository(String type) {
        String className = typeClassNameMap.get(type);
        if (className == null) {
            String typeList = "";
            Iterator<String> typeIter = typeClassNameMap.keySet().iterator();
            while (typeIter.hasNext()) {
                String availableType = (String) typeIter.next();
                typeList += availableType;
                if (typeIter.hasNext()) {
                    typeList += ", ";
                }
            }
            throw new ParameterInvalidException("type", type, "Invalid type [" + type + "], must be one of [" + typeList + "]");
        }
        try {
            Class clazz = Class.forName(className);
            Constructor<Repository> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (Throwable t) {
            throw new RuntimeException("Could not create registry of class [" + className + "]: " + t.getLocalizedMessage());
        }
    }
