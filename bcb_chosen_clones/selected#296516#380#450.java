    protected String createMAGEObject(String localName, Attributes atts) throws SAXException {
        Class mageClass = mageJava.getClassWithModelName(localName);
        if (mageClass == null) {
            StringOutputHelpers.writeOutput("Don't know how to create an object " + " for " + localName + " yet! Skipping.", 3);
            objectStack.push(new String("localName"));
            throw new SAXException("Unrecognized element local name " + localName + ".");
        }
        int nIdentifierIndex = atts.getIndex("", "identifier");
        String sIdentifier = null;
        if (nIdentifierIndex != -1) {
            sIdentifier = atts.getValue(nIdentifierIndex);
        }
        Constructor constructor = (Constructor) classConstructorMap.get(mageClass);
        if (constructor == null) {
            StringOutputHelpers.writeOutput("Creating a class constructor " + localName, 3);
            try {
                constructor = mageClass.getConstructor(new Class[] { Attributes.class });
            } catch (NoSuchMethodException e) {
                throw new SAXException(e);
            } catch (SecurityException e) {
                throw new SAXException(e);
            }
            classConstructorMap.put(mageClass, constructor);
        }
        StringOutputHelpers.writeOutput("Calling the class constructor for " + localName, 3);
        Object object = null;
        try {
            object = constructor.newInstance(new Object[] { atts });
            if (localName.equalsIgnoreCase("MAGE-ML")) {
                mageJava = (MAGEJava) object;
            }
        } catch (IllegalAccessException e) {
            throw new SAXException(e);
        } catch (InstantiationException e) {
            throw new SAXException(e);
        } catch (InvocationTargetException e) {
            throw new SAXException(e);
        }
        String childClassName = object.getClass().getName();
        if (aggrBiDiMap.containChild(childClassName)) {
            Object parentObject = objectStack.peek();
            Class parentClass = parentObject.getClass();
            String parentClassName = parentClass.getName();
            try {
                if (aggrBiDiMap.getParentName(childClassName).equals(parentClassName)) {
                    String methodName = aggrBiDiMap.getSetMethodName(childClassName);
                    Method setMethod = object.getClass().getMethod(methodName, new Class[] { parentClass });
                    setMethod.invoke(object, new Object[] { parentObject });
                }
            } catch (NoSuchMethodException e) {
                throw new SAXException(e);
            } catch (IllegalAccessException e) {
                throw new SAXException(e);
            } catch (IllegalArgumentException e) {
                throw new SAXException(e);
            } catch (InvocationTargetException e) {
                throw new SAXException(e);
            }
        }
        if (sIdentifier != null) {
            if (identifierMap.containsKey(sIdentifier)) {
                StringOutputHelpers.writeOutput("WARNING: The identifier \"" + sIdentifier + "\" is already associated with an object.", 2);
            } else {
                identifierMap.put(sIdentifier, object);
            }
        }
        if (!localName.equalsIgnoreCase("MAGE-ML")) {
            objectStack.push(object);
        }
        return sIdentifier;
    }
