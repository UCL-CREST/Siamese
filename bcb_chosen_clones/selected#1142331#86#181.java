    public Object applyN(Object[] args) throws Throwable {
        if (kind == 'P') throw new RuntimeException(getName() + ": invoke-special not allowed at run time");
        int nargs = args.length;
        Procedure.checkArgCount(this, nargs);
        Object arg0 = args[0];
        ObjectType dtype = (kind != 'V' && kind != '*' ? typeFrom(arg0, this) : (ObjectType) Type.make(arg0.getClass()));
        Object mname;
        if (kind == 'N') {
            mname = null;
            if (dtype instanceof TypeValue) {
                Procedure constructor = ((TypeValue) dtype).getConstructor();
                if (constructor != null) {
                    nargs--;
                    Object[] xargs = new Object[nargs];
                    System.arraycopy(args, 1, xargs, 0, nargs);
                    return constructor.applyN(xargs);
                }
            }
            if (dtype instanceof PairClassType) {
                PairClassType ptype = (PairClassType) dtype;
                dtype = ptype.instanceType;
            }
            if (dtype instanceof ArrayType) {
                Type elementType = ((ArrayType) dtype).getComponentType();
                int len;
                len = args.length - 1;
                String name;
                int length;
                int i;
                boolean lengthSpecified;
                if (len >= 2 && args[1] instanceof Keyword && ("length".equals(name = ((Keyword) args[1]).getName()) || "size".equals(name))) {
                    length = ((Number) args[2]).intValue();
                    i = 3;
                    lengthSpecified = true;
                } else {
                    length = len;
                    i = 1;
                    lengthSpecified = false;
                }
                Object arr = Array.newInstance(elementType.getReflectClass(), length);
                int index = 0;
                for (; i <= len; i++) {
                    Object arg = args[i];
                    if (lengthSpecified && arg instanceof Keyword && i < len) {
                        String kname = ((Keyword) arg).getName();
                        try {
                            index = Integer.parseInt(kname);
                        } catch (Throwable ex) {
                            throw new RuntimeException("non-integer keyword '" + kname + "' in array constructor");
                        }
                        arg = args[++i];
                    }
                    Array.set(arr, index, elementType.coerceFromObject(arg));
                    index++;
                }
                return arr;
            }
        } else {
            mname = args[1];
        }
        MethodProc proc = lookupMethods((ObjectType) dtype, mname);
        if (kind != 'N') {
            Object[] margs = new Object[nargs - (kind == 'S' || kind == 's' ? 2 : 1)];
            int i = 0;
            if (kind == 'V' || kind == '*') margs[i++] = args[0];
            System.arraycopy(args, 2, margs, i, nargs - 2);
            return proc.applyN(margs);
        } else {
            CallContext vars = CallContext.getInstance();
            int err = proc.matchN(args, vars);
            if (err == 0) return vars.runUntilValue();
            if ((nargs & 1) == 1) {
                for (int i = 1; ; i += 2) {
                    if (i == nargs) {
                        Object result;
                        result = proc.apply1(args[0]);
                        for (i = 1; i < nargs; i += 2) {
                            Keyword key = (Keyword) args[i];
                            Object arg = args[i + 1];
                            SlotSet.apply(false, result, key.getName(), arg);
                        }
                        return result;
                    }
                    if (!(args[i] instanceof Keyword)) break;
                }
            }
            MethodProc vproc = ClassMethods.apply((ClassType) dtype, "valueOf", '\0', language);
            if (vproc != null) {
                Object[] margs = new Object[nargs - 1];
                System.arraycopy(args, 1, margs, 0, nargs - 1);
                err = vproc.matchN(margs, vars);
                if (err == 0) return vars.runUntilValue();
            }
            throw MethodProc.matchFailAsException(err, proc, args);
        }
    }
