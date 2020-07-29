    @Override
    public EvaluationResult evaluate(EvaluationEnvironment env, boolean asType, boolean withValues) throws EvaluationException {
        EvaluationResult ctxType;
        EvaluationResult[] argRes = new EvaluationResult[theArguments.length];
        for (int i = 0; i < argRes.length; i++) {
            argRes[i] = theArguments[i].evaluate(env, false, withValues);
            if (argRes[i].getPackageName() != null || argRes[i].isType()) throw new EvaluationException(argRes[i].getFirstVar() + " cannot be resolved to a variable", this, theArguments[i].getMatch().index);
        }
        if (theContext == null) {
            Class<?> c = env.getImportMethodType(theName);
            if (c != null) ctxType = new EvaluationResult(new Type(c)); else {
                ParsedFunctionDeclaration[] funcs = env.getDeclaredFunctions();
                ParsedFunctionDeclaration goodTarget = null;
                ParsedFunctionDeclaration badTarget = null;
                for (ParsedFunctionDeclaration func : funcs) {
                    if (!func.getName().equals(theName)) continue;
                    ParsedDeclaration[] _paramTypes = func.getParameters();
                    Type[] paramTypes = new Type[_paramTypes.length];
                    for (int p = 0; p < paramTypes.length; p++) paramTypes[p] = _paramTypes[p].evaluateType(env);
                    if (!func.isVarArgs() && paramTypes.length != argRes.length) continue;
                    if (paramTypes.length > argRes.length + 1) continue;
                    boolean bad = false;
                    int p;
                    for (p = 0; !bad && p < paramTypes.length - 1; p++) {
                        if (!paramTypes[p].isAssignable(argRes[p].getType())) bad = true;
                    }
                    if (bad) {
                        if (badTarget == null) badTarget = func;
                        continue;
                    }
                    ParsedFunctionDeclaration target = null;
                    if (paramTypes.length == argRes.length && (paramTypes.length == 0 || paramTypes[p].isAssignable(argRes[p].getType()))) target = func; else if (func.isVarArgs()) {
                        Type varArgType = paramTypes[p].getComponentType();
                        for (; !bad && p < argRes.length; p++) if (!varArgType.isAssignable(argRes[p].getType())) bad = true;
                        if (!bad) {
                            target = func;
                            Type arrType;
                            if (argRes.length < paramTypes.length) arrType = varArgType; else {
                                arrType = argRes[paramTypes.length - 1].getType();
                                for (int i = paramTypes.length; i < argRes.length; i++) arrType = arrType.getCommonType(argRes[i].getType());
                            }
                            Object newArg = java.lang.reflect.Array.newInstance(arrType.toClass(), argRes.length - paramTypes.length + 1);
                            for (int i = paramTypes.length - 1; i < argRes.length; i++) java.lang.reflect.Array.set(newArg, i - paramTypes.length + 1, argRes[i].getValue());
                            EvaluationResult[] newArgRes = new prisms.lang.EvaluationResult[paramTypes.length];
                            System.arraycopy(argRes, 0, newArgRes, 0, newArgRes.length - 1);
                            newArgRes[newArgRes.length - 1] = new EvaluationResult(arrType.getArrayType(), newArg);
                            argRes = newArgRes;
                        }
                    }
                    if (target == null) {
                        if (badTarget == null) badTarget = func;
                        continue;
                    }
                    goodTarget = target;
                    break;
                }
                if (goodTarget != null) return goodTarget.execute(env, argRes, withValues); else if (badTarget != null) {
                    StringBuilder msg = new StringBuilder();
                    msg.append(theName).append('(');
                    ParsedDeclaration[] paramTypes = badTarget.getParameters();
                    int p;
                    for (p = 0; p < paramTypes.length - 1; p++) {
                        msg.append(paramTypes[p].getType().evaluate(env, true, withValues).getType());
                        msg.append(", ");
                    }
                    if (badTarget.isVarArgs()) msg.append(paramTypes[p].getType().evaluate(env, true, withValues).getType().getComponentType()).append("..."); else msg.append(paramTypes[p].getType().evaluate(env, true, withValues).getType());
                    msg.append(')');
                    StringBuilder types = new StringBuilder();
                    for (p = 0; p < argRes.length; p++) {
                        if (p > 0) types.append(", ");
                        types.append(argRes[p].getType());
                    }
                    throw new EvaluationException("The function " + msg + " is undefined for parameter types " + types, this, getStored("name").index);
                } else throw new EvaluationException((isMethod ? "Method " : "Field ") + theName + " unrecognized", this, getStored("name").index);
            }
        } else ctxType = theContext.evaluate(env, false, withValues);
        if (ctxType == null) throw new EvaluationException("No value for context to " + (isMethod ? "method " : "field ") + theName, this, theContext.getMatch().index);
        boolean isStatic = ctxType.isType();
        if (!isMethod) {
            if (ctxType.getPackageName() != null || ctxType.isType()) {
                String name = ctxType.getPackageName() + "." + theName;
                java.lang.Class<?> clazz;
                try {
                    clazz = Class.forName(name);
                } catch (ClassNotFoundException e) {
                    clazz = null;
                }
                if (clazz != null) return new EvaluationResult(new Type(clazz));
                if (env.getClassGetter().isPackage(name)) return new EvaluationResult(name);
                if (!ctxType.isType()) throw new EvaluationException(ctxType.getFirstVar() + " cannot be resolved to a variable", this, theContext.getMatch().index);
            }
            if (!ctxType.isType() && ctxType.getType().isPrimitive()) throw new EvaluationException("The primitive type " + ctxType.getType().getBaseType().getName() + " does not have a field " + theName, this, theContext.getMatch().index + theContext.getMatch().text.length());
            if (theName.equals("length") && ctxType.getType().isArray()) return new EvaluationResult(new Type(Integer.TYPE), withValues ? Integer.valueOf(java.lang.reflect.Array.getLength(ctxType.getValue())) : null); else if (theName.equals("class") && ctxType.isType()) return new EvaluationResult(new Type(Class.class, ctxType.getType()), ctxType.getType().getBaseType());
            java.lang.reflect.Field field;
            try {
                field = ctxType.getType().getBaseType().getField(theName);
            } catch (Exception e) {
                throw new EvaluationException("Could not access field " + theName + " on type " + ctxType.typeString(), e, this, getStored("name").index);
            }
            if (field == null) throw new EvaluationException(ctxType.typeString() + "." + theName + " cannot be resolved or is not a field", this, getStored("name").index);
            if (env.usePublicOnly() && (field.getModifiers() & Modifier.PUBLIC) == 0) throw new EvaluationException(ctxType.typeString() + "." + theName + " is not visible", this, getStored("name").index);
            if (isStatic && (field.getModifiers() & Modifier.STATIC) == 0) throw new EvaluationException("Cannot make a static reference to non-static field " + theName + " from the type " + ctxType.typeString() + "." + theName + " is not static", this, getStored("name").index);
            try {
                return new EvaluationResult(ctxType.getType().resolve(field.getGenericType(), field.getDeclaringClass(), null, new java.lang.reflect.Type[0], new Type[0]), withValues ? field.get(ctxType.getValue()) : null);
            } catch (Exception e) {
                throw new EvaluationException("Retrieval of field " + field.getName() + " of type " + field.getDeclaringClass().getName() + " failed", e, this, getStored("name").index);
            }
        } else {
            if (ctxType.getPackageName() != null) throw new EvaluationException(ctxType.getFirstVar() + " cannot be resolved to a variable", this, theContext.getMatch().index);
            if (!ctxType.isType() && ctxType.getType().isPrimitive()) {
                StringBuilder msg = new StringBuilder();
                msg.append(theName).append('(');
                int p;
                for (p = 0; p < argRes.length; p++) {
                    if (p > 0) msg.append(", ");
                    msg.append(argRes[p].typeString());
                }
                msg.append(')');
                throw new EvaluationException("Cannot invoke " + msg + " on primitive type " + ctxType.getType(), this, theContext.getMatch().index + theContext.getMatch().text.length());
            }
            if ("getClass".equals(theName)) {
                if (isStatic) throw new EvaluationException("Cannot access the non-static getClass() method from a static context", this, getStored("name").index);
                try {
                    return new EvaluationResult(new Type(Class.class, ctxType.getType()), withValues ? ctxType.getValue().getClass() : null);
                } catch (NullPointerException e) {
                    throw new EvaluationException("Argument to getClass() is null", e, this, getStored("dot").index);
                }
            }
            java.lang.reflect.Method[] methods;
            if (ctxType.getType().getBaseType() == Type.NULL.getClass()) methods = Object.class.getMethods(); else methods = ctxType.getType().getBaseType().getMethods();
            if (!env.usePublicOnly()) methods = prisms.util.ArrayUtils.mergeInclusive(java.lang.reflect.Method.class, methods, ctxType.getType().getBaseType().getDeclaredMethods());
            java.lang.reflect.Method goodTarget = null;
            java.lang.reflect.Method badTarget = null;
            java.util.Map<String, Type> inferred = new java.util.HashMap<String, Type>();
            Type[] argTypes = new Type[argRes.length];
            for (int i = 0; i < argTypes.length; i++) argTypes[i] = argRes[i].getType();
            for (java.lang.reflect.Method m : methods) {
                if (!m.getName().equals(theName)) continue;
                java.lang.reflect.Type[] _paramTypes = m.getGenericParameterTypes();
                Type[] paramTypes = new Type[_paramTypes.length];
                for (int p = 0; p < paramTypes.length; p++) paramTypes[p] = ctxType.getType().resolve(_paramTypes[p], m.getDeclaringClass(), null, m.getGenericParameterTypes(), argTypes);
                if (paramTypes.length > argRes.length + 1) continue;
                inferred.clear();
                inferMethodTypes(inferred, m, argTypes);
                boolean bad = false;
                int p;
                for (p = 0; !bad && p < paramTypes.length - 1; p++) {
                    if (!paramTypes[p].isAssignable(argRes[p].getType())) bad = true;
                }
                if (bad) {
                    if (badTarget == null) badTarget = m;
                    continue;
                }
                java.lang.reflect.Method target = null;
                if (paramTypes.length == argRes.length && (paramTypes.length == 0 || paramTypes[p].isAssignable(argRes[p].getType()))) target = m; else if (m.isVarArgs()) {
                    Type varArgType = paramTypes[p].getComponentType();
                    for (; !bad && p < argRes.length; p++) if (!varArgType.isAssignable(argRes[p].getType())) bad = true;
                    if (!bad) {
                        target = m;
                        Type arrType;
                        if (argRes.length < paramTypes.length) arrType = varArgType; else {
                            arrType = argRes[paramTypes.length - 1].getType();
                            for (int i = paramTypes.length; i < argRes.length; i++) arrType = arrType.getCommonType(argRes[i].getType());
                        }
                        Object newArg = java.lang.reflect.Array.newInstance(arrType.toClass(), argRes.length - paramTypes.length + 1);
                        for (int i = paramTypes.length - 1; i < argRes.length; i++) java.lang.reflect.Array.set(newArg, i - paramTypes.length + 1, argRes[i].getValue());
                        EvaluationResult[] newArgRes = new prisms.lang.EvaluationResult[paramTypes.length];
                        System.arraycopy(argRes, 0, newArgRes, 0, newArgRes.length - 1);
                        newArgRes[newArgRes.length - 1] = new EvaluationResult(arrType.getArrayType(), newArg);
                        argRes = newArgRes;
                    }
                }
                if (target == null) {
                    if (badTarget == null) badTarget = m;
                    continue;
                }
                if (env.usePublicOnly() && (target.getModifiers() & Modifier.PUBLIC) == 0) badTarget = target; else if (isStatic && (target.getModifiers() & Modifier.STATIC) == 0) badTarget = target; else {
                    goodTarget = target;
                    break;
                }
            }
            if (goodTarget != null) {
                for (java.lang.reflect.Type c : goodTarget.getGenericExceptionTypes()) {
                    Type ct = ctxType.getType().resolve(c, goodTarget.getDeclaringClass(), inferred, goodTarget.getGenericParameterTypes(), argTypes);
                    if (!env.canHandle(ct)) throw new prisms.lang.EvaluationException("Unhandled exception type " + ct, this, getStored("name").index);
                }
                Class<?>[] paramTypes = goodTarget.getParameterTypes();
                Object[] args = new Object[paramTypes.length];
                for (int i = 0; i < args.length - 1; i++) args[i] = argRes[i].getValue();
                if (!goodTarget.isVarArgs()) {
                    if (args.length > 0) args[args.length - 1] = argRes[args.length - 1].getValue();
                } else args[args.length - 1] = argRes[argRes.length - 1].getValue();
                if (withValues && !isStatic && !Modifier.isStatic(goodTarget.getModifiers()) && ctxType.getValue() == null) throw new ExecutionException(new Type(NullPointerException.class), new NullPointerException(), theContext, theContext.getMatch().index);
                try {
                    Type retType = ctxType.getType().resolve(goodTarget.getGenericReturnType(), goodTarget.getDeclaringClass(), inferred, goodTarget.getGenericParameterTypes(), argTypes);
                    return new EvaluationResult(retType, withValues ? goodTarget.invoke(ctxType.getValue(), args) : null);
                } catch (java.lang.reflect.InvocationTargetException e) {
                    throw new ExecutionException(new Type(e.getCause().getClass()), e.getCause(), this, getStored("name").index);
                } catch (Exception e) {
                    throw new EvaluationException("Could not invoke method " + theName + " of class " + ctxType.typeString(), e, this, getStored("name").index);
                }
            } else if (badTarget != null) {
                StringBuilder msg = new StringBuilder();
                msg.append(theName).append('(');
                java.lang.reflect.Type[] paramTypes = badTarget.getGenericParameterTypes();
                int p;
                for (p = 0; p < paramTypes.length - 1; p++) {
                    msg.append(new Type(paramTypes[p]));
                    msg.append(", ");
                }
                if (badTarget.isVarArgs()) msg.append(new Type(paramTypes[p]).getComponentType()).append("..."); else msg.append(new Type(paramTypes[p]));
                msg.append(')');
                if (env.usePublicOnly() && (badTarget.getModifiers() & Modifier.PUBLIC) == 0) throw new EvaluationException("The method " + msg + " from the type " + ctxType.typeString() + "." + theName + " is not visible", this, getStored("name").index);
                if (isStatic && (badTarget.getModifiers() & Modifier.STATIC) == 0) throw new EvaluationException("Cannot make a static reference to the non-static method " + msg + " from the type " + ctxType.typeString(), this, getStored("name").index); else {
                    StringBuilder types = new StringBuilder();
                    for (p = 0; p < argRes.length; p++) {
                        if (p > 0) types.append(", ");
                        types.append(argRes[p].getType());
                    }
                    throw new EvaluationException("The method " + ctxType.getType() + "." + msg + " is undefined for parameter types " + types, this, getStored("name").index);
                }
            } else {
                StringBuilder msg = new StringBuilder();
                msg.append(theName).append('(');
                int a;
                for (a = 0; a < argRes.length; a++) {
                    msg.append(argRes[a].typeString());
                    if (a < argRes.length - 1) msg.append(", ");
                }
                msg.append(')');
                throw new EvaluationException("The method " + msg + " is undefined for the type " + ctxType.typeString(), this, getStored("name").index);
            }
        }
    }
