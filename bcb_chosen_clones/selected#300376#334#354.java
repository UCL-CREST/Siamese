    protected Object createProxyInstance(ObjectCreator creator, String serviceId, Class serviceInterface, String description) {
        ProxyServiceProxyToken token = ProxySerializationSupport.createToken(serviceId);
        ClassFab classFab = registry.newClass(serviceInterface);
        classFab.addField("creator", Modifier.PRIVATE | Modifier.FINAL, ObjectCreator.class);
        classFab.addField("token", Modifier.PRIVATE | Modifier.FINAL, ProxyServiceProxyToken.class);
        classFab.addConstructor(new Class[] { ObjectCreator.class, ProxyServiceProxyToken.class }, null, "{ creator = $1; token = $2; }");
        classFab.addInterface(Serializable.class);
        classFab.addInterface(serviceInterface);
        MethodSignature writeReplaceSig = new MethodSignature(Object.class, "writeReplace", null, new Class[] { ObjectStreamException.class });
        classFab.addMethod(Modifier.PRIVATE, writeReplaceSig, "return token;");
        String body = format("return (%s) creator.createObject();", serviceInterface.getName());
        MethodSignature sig = new MethodSignature(serviceInterface, "delegate", null, null);
        classFab.addMethod(Modifier.PRIVATE, sig, body);
        classFab.proxyMethodsToDelegate(serviceInterface, "delegate()", description);
        Class proxyClass = classFab.createClass();
        try {
            return proxyClass.getConstructors()[0].newInstance(creator, token);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
