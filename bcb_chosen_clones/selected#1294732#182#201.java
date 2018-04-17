    public static Object createObjectFromString(String type, String value) throws Exception {
        Object result;
        if (type.equals("int")) result = new Integer(value); else if (type.equals("long")) result = new Long(value); else if (type.equals("double")) result = new Double(value); else if (type.equals("float")) result = new Float(value); else if (type.equals("short")) result = new Short(value); else if (type.equals("boolean")) result = new Boolean(value); else if (type.equals("char")) result = new Character(value.charAt(0)); else if (type.equals("byte")) result = new Byte(value); else if (type.equals("java.lang.Integer")) result = new Integer(value); else if (type.equals("java.lang.Long")) result = new Long(value); else if (type.equals("java.lang.Float")) result = new Float(value); else if (type.equals("java.lang.Double")) result = new Double(value); else if (type.equals("java.lang.Short")) result = new Short(value); else if (type.equals("java.lang.Boolean")) result = new Boolean(value); else if (Utils.getClass("java.lang.Number").isAssignableFrom(Utils.getClass(type))) {
            result = createNumberFromString(value);
        } else {
            if (value.toString().equals("null")) {
                result = null;
            } else {
                if (type.equals("javax.management.ObjectName")) {
                    result = new ObjectName(value);
                }
                Class sig[] = { value.toString().getClass() };
                Constructor c = Utils.getClass(type).getConstructor(sig);
                Object[] paramArray = new Object[1];
                paramArray[0] = value;
                result = c.newInstance(paramArray);
            }
        }
        return result;
    }
