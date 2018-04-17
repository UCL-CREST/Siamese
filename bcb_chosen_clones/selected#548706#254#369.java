    public static <T> T createConstantsInstance(Class<T> constantsClass, Locale locale) {
        ImplKey key = new ImplKey(constantsClass.getName(), locale);
        T result = (T) cache.get(key);
        if (result != null) {
            return result;
        }
        String ifaceName = constantsClass.getName();
        final ClassPool classPool = getClassPool();
        try {
            CtClass iface = classPool.get(constantsClass.getName());
            CtClass baseConstantsImpl = classPool.get(BaseConstantsImpl.class.getName());
            CtClass stringClass = classPool.get(String.class.getName());
            CtClass impl = classPool.makeClass(ifaceName + "Impl_" + locale.toString(), baseConstantsImpl);
            impl.addInterface(iface);
            final CtClass resourceBundle = classPool.get(ResourceBundle.class.getName());
            CtField resField = new CtField(resourceBundle, "res", impl);
            impl.addField(resField);
            KeyGenerator keyGenerator = new MethodNameKeyGenerator();
            for (Object ann : iface.getAnnotations()) {
                if (ann instanceof GenerateKeys) {
                    String keyGeneratorClassName = ((GenerateKeys) ann).value();
                    Class<KeyGenerator> keyGeneratorClass;
                    try {
                        keyGeneratorClass = (Class<KeyGenerator>) Class.forName(keyGeneratorClassName);
                    } catch (ClassNotFoundException cnfe) {
                        keyGeneratorClass = (Class<KeyGenerator>) Class.forName(KeyGenerator.class.getName() + "." + keyGeneratorClassName);
                    }
                    keyGenerator = keyGeneratorClass.newInstance();
                }
            }
            ResourceBundle resBundle = ResourceBundle.getBundle(ifaceName, locale);
            for (CtMethod method : iface.getMethods()) {
                if ((method.getModifiers() & Modifier.ABSTRACT) == 0) continue;
                String methodKey = null;
                String meaning = null;
                Object defaultValue = null;
                for (Object ann : method.getAnnotations()) {
                    if (ann instanceof Key) {
                        methodKey = ((Key) ann).value();
                    } else if (ann instanceof Meaning) {
                        meaning = ((Meaning) ann).value();
                    } else if (ann instanceof DefaultStringValue) {
                        defaultValue = ((DefaultStringValue) ann).value();
                    } else if (ann instanceof DefaultBooleanValue) {
                        defaultValue = ((DefaultBooleanValue) ann).value();
                    } else if (ann instanceof DefaultDoubleValue) {
                        defaultValue = ((DefaultDoubleValue) ann).value();
                    } else if (ann instanceof DefaultIntValue) {
                        defaultValue = ((DefaultIntValue) ann).value();
                    } else if (ann instanceof DefaultFloatValue) {
                        defaultValue = ((DefaultFloatValue) ann).value();
                    } else if (ann instanceof DefaultMessage) {
                        defaultValue = ((DefaultMessage) ann).value();
                    }
                }
                if (methodKey == null) {
                    methodKey = keyGenerator.generateKey(ifaceName, method.getName(), String.valueOf(defaultValue), meaning);
                }
                CtMethod methodImpl = new CtMethod(method, impl, null);
                CtClass returnType = methodImpl.getReturnType();
                Initializer initializer;
                String valueString;
                try {
                    valueString = resBundle.getString(methodKey);
                } catch (MissingResourceException mre) {
                    if (defaultValue != null) valueString = String.valueOf(defaultValue); else throw mre;
                }
                if (returnType.isPrimitive()) {
                    if (returnType.getSimpleName().equals("int")) initializer = CtField.Initializer.constant(Integer.parseInt(valueString)); else if (returnType.getSimpleName().equals("float")) initializer = CtField.Initializer.byExpr(Float.parseFloat(valueString) + "f"); else if (returnType.getSimpleName().equals("double")) initializer = CtField.Initializer.constant(Double.parseDouble(valueString)); else if (returnType.getSimpleName().equals("boolean")) initializer = CtField.Initializer.byExpr(String.valueOf(Boolean.parseBoolean(valueString))); else throw new IllegalStateException(returnType + " is not a supported primitive return type of a constant in " + iface.getName());
                } else if (returnType.equals(stringClass)) {
                    initializer = CtField.Initializer.constant(valueString);
                } else if (returnType.getSimpleName().equals("Map")) {
                    CtField keyField = new CtField(stringClass, method.getName().toUpperCase(), impl);
                    keyField.setModifiers(Modifier.FINAL | Modifier.STATIC);
                    impl.addField(keyField, CtField.Initializer.constant(methodKey));
                    initializer = CtField.Initializer.byExpr("getMap(" + keyField.getName() + ")");
                } else if (returnType.getSimpleName().equals("String[]")) {
                    CtField keyField = new CtField(stringClass, method.getName().toUpperCase(), impl);
                    keyField.setModifiers(Modifier.FINAL | Modifier.STATIC);
                    impl.addField(keyField, CtField.Initializer.constant(methodKey));
                    initializer = CtField.Initializer.byExpr("getStringArray(" + keyField.getName() + ")");
                    returnType = classPool.get(String[].class.getName());
                } else throw new IllegalStateException(returnType + " is not a supported return type of a constant in " + iface.getName());
                CtField valueField = new CtField(returnType, method.getName(), impl);
                impl.addField(valueField, initializer);
                methodImpl.setModifiers(Modifier.PUBLIC);
                methodImpl.setBody("return this." + valueField.getName() + ";");
                impl.addMethod(methodImpl);
            }
            CtConstructor ctor = new CtConstructor(new CtClass[] { resourceBundle }, impl);
            ctor.setModifiers(Modifier.PUBLIC);
            ctor.setExceptionTypes(new CtClass[] { classPool.get(MissingResourceException.class.getName()) });
            impl.addConstructor(ctor);
            ctor.setBody("super($1);");
            Class<T> implClass = impl.toClass();
            T instance = implClass.getConstructor(ResourceBundle.class).newInstance(resBundle);
            cache.put(key, instance);
            return instance;
        } catch (NotFoundException e) {
            throw new Error(e);
        } catch (RuntimeException e) {
            throw new Error(e);
        } catch (CannotCompileException e) {
            throw new Error(e);
        } catch (InvocationTargetException e) {
            throw new Error(e);
        } catch (NoSuchMethodException e) {
            throw new Error(e);
        } catch (ClassNotFoundException e) {
            throw new Error(e);
        } catch (InstantiationException e) {
            throw new Error(e);
        } catch (IllegalAccessException e) {
            throw new Error(e);
        }
    }
