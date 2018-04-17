    public static ScriptItem ParseXML(Element xml) throws ScriptException {
        try {
            String scriptobj = xml.getAttribute(SmartPresenterConstants.ATTR_SCRIPTCLASS);
            Class c = Class.forName(scriptobj);
            Class[] paramTypes = { Class.forName("org.w3c.dom.Element") };
            Constructor constructor = c.getConstructor(paramTypes);
            Object[] params = { xml };
            ScriptItem ret = (ScriptItem) constructor.newInstance(params);
            return ret;
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }
