    public void run(ScriptContext sc) {
        View parent = sc.getView();
        XTreeNode node = sc.getNode();
        boolean stringArg = false;
        String arg = "";
        String cleanedCmd = command.substring(1, command.length() - 1).trim();
        int bracket = cleanedCmd.indexOf("(");
        if (bracket == -1) {
            XScripter.doError(command, "\"(\" expected");
            return;
        }
        int classEnds = cleanedCmd.lastIndexOf(".", bracket);
        String clazzName = cleanedCmd.substring(0, classEnds);
        String methodName = cleanedCmd.substring(classEnds + 1, bracket);
        if (cleanedCmd.charAt(bracket + 1) != ')') {
            stringArg = true;
            String _arg = cleanedCmd.substring(bracket + 1, cleanedCmd.length());
            if (_arg.startsWith("$")) {
                arg = XScripter.getSubstituteFor(parent, _arg.substring(1), node);
            } else {
                arg = _arg;
            }
        }
        try {
            Class clazz = Class.forName(clazzName);
            Method method = null;
            System.out.println("ClassName=" + clazzName + "/" + clazz.getName() + " methodName=" + methodName);
            Object[] args = null;
            if (stringArg) {
                method = clazz.getMethod(methodName, new Class[] { String.class });
                args = new Object[1];
                args[0] = arg;
            } else {
                method = clazz.getMethod(methodName, null);
            }
            Object obj = method.invoke(null, args);
            if (obj == null) {
                return;
            } else {
                InsertTextCommand.insertText(obj.toString(), sc);
            }
        } catch (Exception e) {
            XScripter.doError(command, e);
        }
    }
