    @SuppressWarnings("unchecked")
    public Value run(final ArgumentList argList, final SymbolTable symbols) throws JBasicException {
        String className = null;
        int argc = argList.size();
        if (argc >= 1 && argList.element(0).getType() == Value.STRING) {
            className = argList.stringElement(0);
        } else if (argc >= 1 && argList.element(0).isObject()) {
            ObjectValue v = (ObjectValue) argList.element(0);
            className = v.getObjectClass().getName();
        }
        if (className != null) {
            argList.session.checkPermission(Permissions.JAVA);
            ObjectValue newJBasicObject = null;
            int arraySize = argList.size() - 1;
            Class constructorArgClasses[] = new Class[arraySize];
            Object constructorArgs[] = new Object[arraySize];
            Boolean isObjectParm[] = new Boolean[arraySize];
            try {
                for (int ix = 0; ix < argc - 1; ix++) {
                    Value argElement = argList.element(ix + 1);
                    Class argClass = argElement.getObjectClass();
                    if (argClass == null) throw new JBasicException(Status.INVOBJOP, "use " + argElement.toString() + " with");
                    constructorArgClasses[ix] = argClass;
                    if (argElement.isObject()) {
                        constructorArgs[ix] = ((ObjectValue) argElement).getObject();
                        isObjectParm[ix] = true;
                    } else {
                        isObjectParm[ix] = false;
                        switch(argElement.getType()) {
                            case Value.INTEGER:
                                constructorArgs[ix] = new Integer(argElement.getInteger());
                                break;
                            case Value.DOUBLE:
                                constructorArgs[ix] = new Double(argElement.getDouble());
                                break;
                            case Value.BOOLEAN:
                                constructorArgs[ix] = new Boolean(argElement.getBoolean());
                                break;
                            case Value.STRING:
                                isObjectParm[ix] = true;
                                constructorArgs[ix] = argElement.getString();
                                break;
                        }
                    }
                }
                String fullClassName = ObjectValue.fullClassName(className, symbols);
                if (fullClassName == null) fullClassName = className;
                Class theClass = Class.forName(fullClassName);
                Constructor c = theClass.getConstructor(constructorArgClasses);
                if (c == null) throw new JBasicException(Status.INVCLASS, ObjectValue.methodSignature(className, constructorArgClasses));
                Object newJavaObject = c.newInstance(constructorArgs);
                newJBasicObject = new ObjectValue(newJavaObject);
                return newJBasicObject;
            } catch (JBasicException jbe) {
                throw jbe;
            } catch (Exception e) {
                throw new JBasicException(Status.INVCLASS, ObjectValue.methodSignature(className, constructorArgClasses));
            }
        }
        argList.validate(1, 2, new int[] { Value.RECORD, Value.STRING });
        Value classObject = argList.element(0);
        Value objectData = classObject.getElement("_OBJECT$DATA");
        if (objectData == null) throw new JBasicException(Status.INVCLASS, classObject.toString());
        Value classValue = objectData.getElement("CLASS");
        if (classValue == null) throw new JBasicException(Status.INVCLASS, classObject.toString());
        className = classValue.getString();
        Value isJava = objectData.getElement("ISJAVA");
        if (isJava != null && isJava.getBoolean()) {
            argList.session.checkPermission(Permissions.JAVA);
            ObjectValue v = null;
            try {
                Class theClass = Class.forName(className);
                Object theObject = theClass.newInstance();
                v = new ObjectValue(theObject);
                return v;
            } catch (Exception e) {
                throw new JBasicException(Status.INVCLASS, className);
            }
        }
        final Value newObject = classObject.copy(true, 0);
        newObject.setName(null);
        newObject.setObjectAttribute("ID", new Value(JBasic.getUniqueID()));
        newObject.setObjectAttribute("ISJAVA", new Value(false));
        newObject.setObjectAttribute("CLASS", classObject.getObjectAttribute("NAME"));
        if (argList.size() == 1) {
            newObject.setObjectAttribute("ISCLASS", new Value(false));
            newObject.removeObjectAttribute("NAME");
        } else newObject.setObjectAttribute("NAME", new Value(argList.element(1).getString().toUpperCase()));
        return newObject;
    }
