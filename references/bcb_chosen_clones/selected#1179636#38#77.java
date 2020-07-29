    public Object getObject(String type, Interpreter jato, Class thisClass, Object thisObj, Element xmlIn, Element xmlOut) throws JatoException {
        Constructor ctor = null;
        Class types[] = null;
        try {
            ScriptTag params[] = getChildren(ParamScriptTag.sTypes, jato, thisClass, thisObj, xmlIn, xmlOut);
            List parmList = new ArrayList();
            List typeList = new ArrayList();
            ParamScriptTag parm;
            for (int i = 0, len = params.length; i < len; i++) {
                if (params[i] instanceof ParamScriptTag) {
                    parm = (ParamScriptTag) params[i];
                    parm.getParameters(typeList, parmList, jato, thisClass, thisObj, xmlIn, xmlOut);
                } else {
                    params[i].process(jato, thisClass, thisObj, xmlIn, xmlOut);
                }
            }
            Class ctorClass = jato.loadClass(type);
            types = (Class[]) typeList.toArray(ParamScriptTag.sClassArray);
            Object parms[] = parmList.toArray(ParamScriptTag.sObjectArray);
            ctor = ctorClass.getConstructor(types);
            return ctor.newInstance(parms);
        } catch (Exception ex) {
            StringBuffer msg = new StringBuffer("Unable to create object of type '");
            msg.append(type).append('\'');
            if (ctor != null) {
                msg.append(" using constructor ").append(ctor.toString());
            } else {
                if ((types != null) && (types.length != 0)) {
                    msg.append(" - no constructor taking argument types: ");
                    for (int i = 0; i < types.length; i++) {
                        if (i != 0) {
                            msg.append(", ");
                        }
                        msg.append(String.valueOf(types[i]));
                    }
                }
            }
            throw new JatoException(msg.toString(), ex);
        }
    }
