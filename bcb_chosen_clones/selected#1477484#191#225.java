    public void apply(CallContext ctx) throws Throwable {
        int arg_count = argTypes.length;
        boolean is_constructor = isConstructor();
        boolean slink = is_constructor && method.getDeclaringClass().hasOuterLink();
        try {
            if (member == null) {
                Class clas = method.getDeclaringClass().getReflectClass();
                Class[] paramTypes = new Class[arg_count + (slink ? 1 : 0)];
                for (int i = arg_count; --i >= 0; ) paramTypes[i + (slink ? 1 : 0)] = argTypes[i].getReflectClass();
                if (slink) paramTypes[0] = method.getDeclaringClass().getOuterLinkType().getReflectClass();
                if (is_constructor) member = clas.getConstructor(paramTypes); else if (method != Type.clone_method) member = clas.getMethod(method.getName(), paramTypes);
            }
            Object result;
            if (is_constructor) {
                Object[] args = ctx.values;
                if (slink) {
                    int nargs = args.length + 1;
                    Object[] xargs = new Object[nargs];
                    System.arraycopy(args, 0, xargs, 1, nargs - 1);
                    xargs[0] = ((PairClassType) ctx.value1).staticLink;
                    args = xargs;
                }
                result = (((java.lang.reflect.Constructor) member).newInstance(args));
            } else if (method == Type.clone_method) {
                Object arr = ctx.value1;
                Class elClass = arr.getClass().getComponentType();
                int n = java.lang.reflect.Array.getLength(arr);
                result = java.lang.reflect.Array.newInstance(elClass, n);
                System.arraycopy(arr, 0, result, 0, n);
            } else result = retType.coerceToObject(((java.lang.reflect.Method) member).invoke(ctx.value1, ctx.values));
            if (!takesContext()) ctx.consumer.writeObject(result);
        } catch (java.lang.reflect.InvocationTargetException ex) {
            throw ex.getTargetException();
        }
    }
