    public static Step createStep(Resolver resolver, Element el) throws InvalidScriptException {
        String tag = el.getName();
        HashMap<String, String> attributes = createAttributeMap(el);
        String name = tag.substring(0, 1).toUpperCase() + tag.substring(1);
        if (tag.equals(TAG_WAIT)) {
            attributes.put(TAG_WAIT, "true");
            name = "Assert";
        }
        try {
            Class cls = Class.forName("abbot.script." + name, true, Thread.currentThread().getContextClassLoader());
            try {
                Class[] argTypes = new Class[] { Resolver.class, Element.class, HashMap.class };
                Constructor ctor = cls.getConstructor(argTypes);
                return (Step) ctor.newInstance(new Object[] { resolver, el, attributes });
            } catch (NoSuchMethodException nsm) {
                Class[] argTypes = new Class[] { AbstractResolver.class, HashMap.class };
                Constructor ctor = cls.getConstructor(argTypes);
                return (Step) ctor.newInstance(new Object[] { resolver, attributes });
            }
        } catch (ClassNotFoundException cnf) {
            MessageFormat mf = new MessageFormat(Strings.get("UnknownTag"));
            throw new InvalidScriptException(mf.format(new Object[] { tag }));
        } catch (Exception exc) {
            throw new InvalidScriptException(exc.getMessage());
        }
    }
