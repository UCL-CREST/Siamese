    public int matchN(Object[] args, CallContext ctx) {
        int num = numArgs();
        int nargs = args.length;
        int min = num & 0xFFF;
        if (nargs < min) return MethodProc.NO_MATCH_TOO_FEW_ARGS | min;
        int max = num >> 12;
        if (nargs > max && max >= 0) return MethodProc.NO_MATCH_TOO_MANY_ARGS | max;
        Object[] evalFrame = new Object[lambda.frameSize];
        int key_args = lambda.keywords == null ? 0 : lambda.keywords.length;
        int opt_args = lambda.defaultArgs == null ? 0 : lambda.defaultArgs.length - key_args;
        int i = 0;
        int opt_i = 0;
        int key_i = 0;
        int min_args = lambda.min_args;
        for (Declaration decl = lambda.firstDecl(); decl != null; decl = decl.nextDecl()) {
            Object value;
            if (i < min_args) value = args[i++]; else if (i < min_args + opt_args) {
                if (i < nargs) value = args[i++]; else value = lambda.evalDefaultArg(opt_i, ctx);
                opt_i++;
            } else if (lambda.max_args < 0 && i == min_args + opt_args) {
                if (decl.type instanceof ArrayType) {
                    int rem = nargs - i;
                    Type elementType = ((ArrayType) decl.type).getComponentType();
                    if (elementType == Type.objectType) {
                        Object[] rest = new Object[rem];
                        System.arraycopy(args, i, rest, 0, rem);
                        value = rest;
                    } else {
                        Class elementClass = elementType.getReflectClass();
                        value = java.lang.reflect.Array.newInstance(elementClass, rem);
                        for (int j = 0; j < rem; j++) {
                            Object el;
                            try {
                                el = elementType.coerceFromObject(args[i + j]);
                            } catch (ClassCastException ex) {
                                return NO_MATCH_BAD_TYPE | (i + j);
                            }
                            java.lang.reflect.Array.set(value, j, el);
                        }
                    }
                } else value = LList.makeList(args, i);
            } else {
                Keyword keyword = lambda.keywords[key_i++];
                int key_offset = min_args + opt_args;
                value = Keyword.searchForKeyword(args, key_offset, keyword);
                if (value == Special.dfault) value = lambda.evalDefaultArg(opt_i, ctx);
                opt_i++;
            }
            if (decl.type != null) {
                try {
                    value = decl.type.coerceFromObject(value);
                } catch (ClassCastException ex) {
                    return NO_MATCH_BAD_TYPE | i;
                }
            }
            if (decl.isIndirectBinding()) {
                gnu.mapping.Location loc = decl.makeIndirectLocationFor();
                loc.set(value);
                value = loc;
            }
            evalFrame[decl.evalIndex] = value;
        }
        ctx.values = evalFrame;
        ctx.where = 0;
        ctx.next = 0;
        ctx.proc = this;
        return 0;
    }
