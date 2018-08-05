    public static Function createFunction(XQueryContext context, XQueryAST ast, FunctionDef def) throws XPathException {
        if (def == null) throw new XPathException(ast.getLine(), ast.getColumn(), "Class for function is null");
        Class<? extends Function> fclass = def.getImplementingClass();
        if (fclass == null) throw new XPathException(ast.getLine(), ast.getColumn(), "Class for function is null");
        try {
            Object initArgs[] = { context };
            Class<?> constructorArgs[] = { XQueryContext.class };
            Constructor<?> construct = null;
            try {
                construct = fclass.getConstructor(constructorArgs);
            } catch (NoSuchMethodException e) {
            }
            if (construct == null) {
                constructorArgs = new Class[2];
                constructorArgs[0] = XQueryContext.class;
                constructorArgs[1] = FunctionSignature.class;
                construct = fclass.getConstructor(constructorArgs);
                if (construct == null) throw new XPathException(ast.getLine(), ast.getColumn(), "Constructor not found");
                initArgs = new Object[2];
                initArgs[0] = context;
                initArgs[1] = def.getSignature();
            }
            Object obj = construct.newInstance(initArgs);
            if (obj instanceof Function) {
                ((Function) obj).setLocation(ast.getLine(), ast.getColumn());
                return (Function) obj;
            } else throw new XPathException(ast.getLine(), ast.getColumn(), "Function object does not implement interface function");
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
            throw new XPathException(ast.getLine(), ast.getColumn(), "Function implementation class " + fclass.getName() + " not found");
        }
    }
