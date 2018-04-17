    public static <T> T createMessagesInstance(Class<T> messagesClass, Locale locale) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ImplKey cacheKey = new ImplKey(messagesClass.getName(), locale);
        T result = (T) cache.get(cacheKey);
        if (result != null) {
            return result;
        }
        String ifaceName = messagesClass.getName();
        final ClassPool classPool = getClassPool();
        try {
            CtClass iface = classPool.get(messagesClass.getName());
            CtClass messageFormat = classPool.get(MessageFormat.class.getName());
            CtClass stringClass = classPool.get(String.class.getName());
            CtClass localeClass = classPool.get(Locale.class.getName());
            CtClass impl = classPool.makeClass(ifaceName + "Impl_" + locale.toString());
            impl.addInterface(iface);
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
            CtConstructor ctorMethod = new CtConstructor(new CtClass[] { localeClass }, impl);
            impl.addConstructor(ctorMethod);
            ctorMethod.setModifiers(Modifier.PUBLIC);
            ctorMethod.setBody("super();");
            CtField localeField = new CtField(localeClass, "_locale", impl);
            impl.addField(localeField, CtField.Initializer.byParameter(0));
            TreeMap<String, String> keys = new TreeMap<String, String>();
            for (CtMethod method : iface.getMethods()) {
                if ((method.getModifiers() & Modifier.ABSTRACT) == 0) continue;
                CtMethod methodImpl = new CtMethod(method, impl, null);
                String methodKey = null;
                String meaning = null;
                String defaultValue = null;
                for (Object ann : method.getAnnotations()) {
                    if (ann instanceof Key) {
                        methodKey = ((Key) ann).value();
                    } else if (ann instanceof Meaning) {
                        meaning = ((Meaning) ann).value();
                    } else if (ann instanceof DefaultStringValue) {
                        defaultValue = ((DefaultStringValue) ann).value();
                    } else if (ann instanceof DefaultBooleanValue) {
                        defaultValue = String.valueOf(((DefaultBooleanValue) ann).value());
                    } else if (ann instanceof DefaultDoubleValue) {
                        defaultValue = String.valueOf(((DefaultDoubleValue) ann).value());
                    } else if (ann instanceof DefaultMessage) {
                        defaultValue = ((DefaultMessage) ann).value();
                    }
                }
                if (methodKey == null) {
                    methodKey = keyGenerator.generateKey(ifaceName, method.getName(), defaultValue, meaning);
                }
                String value;
                try {
                    value = resBundle.getString(methodKey);
                } catch (java.util.MissingResourceException mre) {
                    if (defaultValue != null) {
                        value = defaultValue;
                    } else throw mre;
                }
                CtField patternField = new CtField(stringClass, method.getName() + "Pattern", impl);
                impl.addField(patternField, CtField.Initializer.constant(value));
                CtField formatField = new CtField(messageFormat, method.getName() + "MessageFormat", impl);
                impl.addField(formatField, CtField.Initializer.byExpr("new java.text.MessageFormat(" + patternField.getName() + ", _locale)"));
                methodImpl.setModifiers(Modifier.PUBLIC);
                methodImpl.setBody("return " + formatField.getName() + ".format($args, new StringBuffer(), null).toString();");
                impl.addMethod(methodImpl);
                keys.put(methodKey, defaultValue);
            }
            final Class implClazz = impl.toClass();
            final Constructor ctor = implClazz.getConstructor(Locale.class);
            Object instance = ctor.newInstance(locale);
            cache.put(cacheKey, instance);
            return messagesClass.cast(instance);
        } catch (NotFoundException e) {
            throw new Error(e);
        } catch (RuntimeException e) {
            throw new Error(e);
        } catch (CannotCompileException e) {
            throw new Error(e);
        } catch (NoSuchMethodException e) {
            throw new Error(e);
        } catch (InvocationTargetException e) {
            throw new Error(e);
        }
    }
