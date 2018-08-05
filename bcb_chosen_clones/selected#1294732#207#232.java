    public static Object[] getParameters(XTextField[] inputs, String[] params) throws Exception {
        Object result[] = new Object[inputs.length];
        Object userInput;
        for (int i = 0; i < inputs.length; i++) {
            userInput = inputs[i].getValue();
            if (userInput instanceof XObject) {
                result[i] = ((XObject) userInput).getObject();
            } else {
                result[i] = null;
                if (params[i].toString().equals("int")) result[i] = new Integer((String) userInput); else if (params[i].toString().equals("long")) result[i] = new Long((String) userInput); else if (params[i].toString().equals("float")) result[i] = new Float((String) userInput); else if (params[i].toString().equals("double")) result[i] = new Double((String) userInput); else if (params[i].toString().equals("short")) result[i] = new Short((String) userInput); else if (params[i].toString().equals("char")) result[i] = new Character(((String) userInput).charAt(0)); else if (params[i].toString().equals("boolean")) result[i] = new Boolean((String) userInput); else if (Utils.getClass("java.lang.Number").isAssignableFrom(Utils.getClass(params[i]))) {
                    result[i] = createNumberFromString((String) userInput);
                } else {
                    if (userInput.toString().equals("null")) {
                        result[i] = null;
                    } else {
                        Class sig[] = { userInput.toString().getClass() };
                        Constructor c = (Utils.getClass(params[i]).getConstructor(sig));
                        Object[] paramArray = new Object[1];
                        paramArray[0] = userInput;
                        result[i] = c.newInstance(paramArray);
                    }
                }
            }
        }
        return result;
    }
