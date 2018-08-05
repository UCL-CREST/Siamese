    private void exec(String namespace, String func, Signature sign) throws Exception {
        execedInst = 0;
        System.out.println("---EXEC [" + namespace + ":" + func + ":" + sign + "]---");
        pushFunction(namespace, func, sign);
        do {
            exec: for (int i = opP; i < ops.length; i++, opP++) {
                byte opcode = ops[i].getOp();
                byte[] payload = ops[i].getPayload();
                Value sym1, sym2;
                int lhs, rhs, val;
                execedInst++;
                if (execedInst >= instrMax) {
                    throw new IOException("TMI!");
                }
                System.out.print("Executing [" + currentScript.getName() + "." + currentFunction.getName() + ":" + currentFunction.getSignature() + "] " + i + " (" + InstructionCodes.instrNames[opcode] + ") - stack: ");
                if (valStack.isEmpty()) {
                    System.out.println("empty.");
                } else {
                    Value top = valStack.peek();
                    System.out.println("[" + valStack.getStackSize() + "] type: " + symbolNames[top.getType().baseType] + ", val: " + top.getVal() + ".");
                }
                if (instrStackReq[opcode] > 0 && valStack.getStackSize() < instrStackReq[opcode]) {
                    throw new IOException("Stack underflow - need: " + instrStackReq[opcode] + ", have: " + valStack.getStackSize() + ".");
                }
                switch(opcode) {
                    case OP_NOP:
                        break;
                    case OP_CLONE:
                        valStack.push(valStack.peek());
                        break;
                    case OP_VRISTORE:
                    case OP_VRSSTORE:
                    case OP_VRNREFSTORE:
                        sym1 = valStack.pop();
                        sym2 = valStack.pop();
                        if (sym2.getType().baseType != SYM_VAR) {
                            throw new IOException("ERROR: Expected " + symbolNames[sym2.getType().baseType] + " as second value.");
                        }
                        if ((opcode == OP_VRISTORE && sym1.getType().baseType == SYM_INT) || (opcode == OP_VRSSTORE && sym1.getType().baseType == SYM_STRING) || (opcode == OP_VRNREFSTORE && sym1.getType().baseType == SYM_NREF)) {
                            ((VariableReference) sym2.getVal()).setValue(sym1);
                        } else {
                            throw new IOException("ERROR: Wrong ref-type (" + symbolNames[sym2.getType().baseType] + ") on the stack for " + instrNames[opcode]);
                        }
                        break;
                    case OP_VRILOAD:
                    case OP_VRSLOAD:
                    case OP_VRNREFLOAD:
                        sym1 = valStack.pop();
                        if (sym1.getType().baseType != SYM_VAR) {
                            throw new IOException("ERROR: Expected " + symbolNames[sym1.getType().baseType] + " for " + instrNames[opcode] + ".");
                        }
                        sym2 = ((VariableReference) sym1.getVal()).getValue();
                        if ((opcode == OP_VRILOAD && sym2.getType().baseType == SYM_INT) || (opcode == OP_VRSLOAD && sym2.getType().baseType == SYM_STRING) || (opcode == OP_VRNREFLOAD && sym2.getType().baseType == SYM_NREF)) {
                            valStack.push(sym2);
                        } else {
                            throw new IOException("ERROR: Wrong ref-type (" + symbolNames[sym2.getType().baseType] + ") on the stack for " + instrNames[opcode]);
                        }
                        break;
                    case OP_NREFSTORE:
                    case OP_SSTORE:
                    case OP_ISTORE:
                    case OP_INKSTORE:
                        sym1 = valStack.pop();
                        if ((opcode == OP_ISTORE && sym1.getType().baseType != SYM_INT) || (opcode == OP_SSTORE && sym1.getType().baseType != SYM_STRING) || (opcode == OP_NREFSTORE && sym1.getType().baseType != SYM_NREF) || (opcode == OP_INKSTORE && sym1.getType().baseType != SYM_INVOKABLE)) {
                            throw new IOException("Wrong type on the stack.");
                        }
                        locVars[payload[0]] = sym1;
                        break;
                    case OP_NREFLOAD:
                    case OP_SLOAD:
                    case OP_ILOAD:
                    case OP_INKLOAD:
                        sym1 = locVars[payload[0]];
                        if ((opcode == OP_ILOAD && sym1.getType().baseType != SYM_INT) || (opcode == OP_SLOAD && sym1.getType().baseType != SYM_STRING) || (opcode == OP_NREFLOAD && sym1.getType().baseType != SYM_NREF) || (opcode == OP_INKLOAD && sym1.getType().baseType != SYM_INVOKABLE)) {
                            throw new IOException("Local variable #" + payload[0] + " has wrong type for op: " + instrNames[opcode] + ", got type: " + sym1.getType());
                        }
                        valStack.push(sym1);
                        break;
                    case OP_IINC:
                        sym1 = locVars[payload[0]];
                        if (sym1.getType().baseType != SYM_INT) {
                            throw new IOException("ERROR: Variable #" + payload[0] + " was not integer type.");
                        }
                        locVars[payload[0]] = new Value(payload[1] + (Integer) sym1.getVal(), new Type(SYM_INT));
                        break;
                    case OP_SPUSH:
                        valStack.push(new Value(curStrTable[payload[0]], new Type(SYM_STRING)));
                        break;
                    case OP_PUSH:
                        valStack.push(new Value((int) payload[0], new Type(SYM_INT)));
                        break;
                    case OP_VRPUSH:
                        valStack.push(new Value(new VariableReference(locVars, payload[0]), new Type(SYM_VAR)));
                        break;
                    case OP_DISCARD:
                        valStack.pop();
                        break;
                    case OP_CONCAT:
                        sym1 = valStack.pop();
                        sym2 = valStack.pop();
                        valStack.push(new Value(String.valueOf(sym2.getVal()) + (String.valueOf(sym1.getVal())), new Type(SYM_STRING)));
                        break;
                    case OP_SWAP:
                        sym1 = valStack.pop();
                        sym2 = valStack.pop();
                        valStack.push(sym1);
                        valStack.push(sym2);
                        break;
                    case OP_PRINT:
                        sym1 = valStack.pop();
                        if (sym1.getType().baseType != SYM_STRING) {
                            throw new IOException("Print requires string value on stack");
                        }
                        System.out.println(sym1.getVal());
                        break;
                    case OP_INKPUSH:
                    case OP_CALL:
                        Script script;
                        Function toCall = null;
                        int index = payload[0];
                        String[] dep = null;
                        Signature sig = null;
                        if (index < 0) {
                            script = currentScript;
                            toCall = script.getFunction(Math.abs(payload[0]) - 1);
                        } else {
                            dep = currentScript.getDependency(index).split("#");
                            if (opcode == OP_CALL) {
                                script = loadScript(dep[0]);
                            } else {
                                script = scriptMap.get(dep[0]);
                            }
                            try {
                                sig = Utils.readMethodSignature(dep[2], this);
                            } catch (ClassNotFoundException e) {
                                throw new IOException("ERROR: Cannot generate signature for call: " + e);
                            }
                            if (script != null) {
                                toCall = script.getFunction(dep[1], sig);
                                if (toCall == null) {
                                    throw new IOException("ERROR: " + dep[0] + " does not contain " + dep[1] + " with signature: " + dep[2]);
                                }
                            }
                        }
                        if (opcode == OP_CALL) {
                            opP = i + 1;
                            pushFunction(script, toCall);
                            i = -1;
                        } else {
                            if (script != null) {
                                valStack.push(new Value(new LoadedInvokable(script, toCall), new Type(SYM_INVOKABLE)));
                            } else {
                                valStack.push(new Value(new LazyInvokable(this, dep[0], dep[1], sig), new Type(SYM_INVOKABLE)));
                            }
                        }
                        break;
                    case OP_SCALL:
                        Invokable inv;
                        sym1 = valStack.pop();
                        if (sym1.getType().baseType != SYM_INVOKABLE) {
                            throw new IOException("ERROR: scall expects invokable value on the stack.");
                        }
                        inv = (Invokable) sym1.getVal();
                        opP = i + 1;
                        pushFunction(inv.getScript(), inv.getFunction());
                        i = -1;
                        break;
                    case OP_NINVOKE:
                    case OP_SNINVOKE:
                        String[] cmd = currentScript.getDependency(payload[0]).split("#");
                        Method met;
                        Object args[];
                        int argc;
                        Class<?> retType;
                        Object r;
                        try {
                            sig = Utils.readMethodSignature(cmd[2], this);
                        } catch (ClassNotFoundException e) {
                            throw new IOException("Cannot regenerate signature: " + e.toString());
                        } catch (Exception e) {
                            throw new IOException(e.toString());
                        }
                        argc = sig.getArgumentCount();
                        if (valStack.getStackSize() < argc) {
                            throw new IOException(cmd[0] + "." + cmd[1] + " requires " + (argc) + " symbols on the stack.");
                        }
                        args = new Object[argc];
                        for (int s = args.length - 1; s >= 0; s--) {
                            args[s] = valStack.pop().getVal();
                        }
                        if (!sig.checkArguments(args)) {
                            throw new IOException("Incompatible arguments for " + cmd[1] + ".");
                        }
                        retType = sig.getReturnType();
                        try {
                            met = sig.findMethod(cmd[0], cmd[1], this);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            throw new IOException("Missing class: " + cmd[0] + ".");
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                            throw new IOException("Incompatible ABI for " + cmd[0] + "." + cmd[1] + " with signature: " + cmd[2] + ".");
                        } catch (Exception e) {
                            throw new IOException(e.toString());
                        }
                        if (!Modifier.isPublic(met.getModifiers())) {
                            throw new IOException(cmd[0] + "." + cmd[1] + " is not either not public!");
                        }
                        if (opcode == OP_SNINVOKE) {
                            if (!Modifier.isStatic(met.getModifiers())) {
                                throw new IOException(cmd[0] + "." + cmd[1] + " is not static!");
                            }
                            r = null;
                        } else {
                            Value v;
                            if (Modifier.isStatic(met.getModifiers())) {
                                throw new IOException(cmd[0] + "." + cmd[1] + " is static!");
                            }
                            v = valStack.pop();
                            if (v.getType().baseType != SYM_NREF) {
                                throw new IOException("ERROR: Expected native ref on stack, got: " + symbolNames[v.getType().baseType]);
                            }
                            r = v.getVal();
                        }
                        try {
                            Object ret = met.invoke(r, args);
                            if (retType != void.class) {
                                if (Utils.isCompatibleType(retType, int.class)) {
                                    valStack.push(new Value(ret, new Type(SYM_INT)));
                                } else if (retType == String.class || ret.getClass() == String.class) {
                                    valStack.push(new Value(ret, new Type(SYM_STRING)));
                                } else {
                                    valStack.push(new Value(ret, new Type(SYM_NREF)));
                                }
                            }
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            throw new IOException(e.toString());
                        } catch (IllegalAccessException e) {
                            throw new AssertionError(e);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                            throw new IOException("Unhandled exception: " + e);
                        }
                        break;
                    case OP_NEWNREF:
                        Class<?> refclass;
                        Class<?>[] conargs;
                        Constructor<?> con;
                        String cargs;
                        cmd = currentScript.getDependency(payload[0]).split("#");
                        cargs = cmd.length > 1 ? cmd[1] : "";
                        try {
                            conargs = Utils.readClassList(cargs, this);
                            refclass = this.loadClass(cmd[0]);
                            con = refclass.getConstructor(conargs);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            throw new IOException("ERROR: Faild to load constructor for " + cmd[0] + " with sig (" + cargs + ").");
                        } catch (SecurityException e) {
                            throw new IOException(e.toString());
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                            throw new IOException("ERROR: No constructor for " + cmd[0] + " with sig (" + cargs + ").");
                        }
                        if (!Modifier.isPublic(con.getModifiers())) {
                            throw new IOException("ERROR: Constructor for " + cmd[0] + " with sig (" + cargs + ") is not public.");
                        }
                        argc = conargs.length;
                        if (valStack.getStackSize() < argc) {
                            throw new IOException(cmd[0] + (cargs.equals("") ? "" : "#" + cargs) + " requires " + (argc) + " symbols on the stack.");
                        }
                        args = new Object[argc];
                        for (int s = args.length - 1; s >= 0; s--) {
                            args[s] = valStack.pop().getVal();
                        }
                        try {
                            r = con.newInstance(args);
                        } catch (InvocationTargetException e) {
                            e.getCause().printStackTrace();
                            throw new IOException("ERROR: Constructor threw exception.");
                        } catch (IllegalAccessException e) {
                            throw new AssertionError(e);
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new IOException("ERROR: Failed to initialize instance of " + cmd[0]);
                        }
                        valStack.push(new Value(r, new Type(SYM_NREF)));
                        break;
                    case OP_SREFPUSH:
                        String[] ref = currentScript.getDependency(payload[0]).split("#");
                        try {
                            Class<?> cl = this.loadClass(ref[0]);
                            Field f = cl.getField(ref[1]);
                            if (!Modifier.isStatic(f.getModifiers()) || !Modifier.isPublic(f.getModifiers())) {
                                throw new IOException(ref[0] + "#" + ref[1] + " is not public static.");
                            }
                            valStack.push(new Value(f.get(null), new Type(SYM_NREF)));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            throw new IOException(e.toString());
                        } catch (SecurityException e) {
                            throw new AssertionError(e);
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                            throw new IOException(e.toString());
                        } catch (IllegalArgumentException e) {
                            throw new AssertionError(e);
                        } catch (IllegalAccessException e) {
                            throw new AssertionError(e);
                        }
                        break;
                    case OP_JMPNZ:
                    case OP_JMPZ:
                        sym1 = valStack.pop();
                        if (sym1.getType().baseType != SYM_INT) {
                            throw new IOException("Wrong type on the stack.");
                        }
                        if ((opcode == OP_JMPNZ) ^ ((Integer) sym1.getVal() != 0)) {
                            break;
                        }
                    case OP_JMP:
                        System.out.print("Jumping from: " + i);
                        opP = i = payload[0];
                        System.out.println(" to " + (i + 1));
                        break;
                    case OP_EQ:
                    case OP_NEQ:
                        Object olhs, orhs;
                        boolean equal;
                        sym1 = valStack.pop();
                        sym2 = valStack.pop();
                        if (sym1.getType() != sym2.getType()) {
                            throw new IOException("Different type of symbols on the stack.");
                        }
                        olhs = sym2.getVal();
                        orhs = sym1.getVal();
                        equal = olhs == null ? orhs == null : olhs.equals(orhs);
                        valStack.push(!equal ^ (opcode == OP_EQ) ? VALUE_TRUE : VALUE_FALSE);
                        break;
                    case OP_LT:
                    case OP_GT:
                    case OP_GT_EQ:
                    case OP_LT_EQ:
                    case OP_ICMPEQ:
                    case OP_ICMPNE:
                    case OP_ICMPLT:
                    case OP_ICMPGE:
                    case OP_ICMPGT:
                    case OP_ICMPLE:
                        boolean isT, cmp = false;
                        sym1 = valStack.pop();
                        sym2 = valStack.pop();
                        if (sym1.getType().baseType != SYM_INT || sym2.getType().baseType != SYM_INT) {
                            throw new IOException("The symbols are not ints.");
                        }
                        lhs = (Integer) sym2.getVal();
                        rhs = (Integer) sym1.getVal();
                        switch(opcode) {
                            case OP_ICMPEQ:
                                isT = lhs == rhs;
                                break;
                            case OP_ICMPNE:
                                isT = lhs != rhs;
                                break;
                            case OP_LT:
                                cmp = true;
                            case OP_ICMPLT:
                                isT = lhs < rhs;
                                break;
                            case OP_GT:
                                cmp = true;
                            case OP_ICMPGT:
                                isT = lhs > rhs;
                                break;
                            case OP_LT_EQ:
                                cmp = true;
                            case OP_ICMPLE:
                                isT = lhs <= rhs;
                                break;
                            case OP_GT_EQ:
                                cmp = true;
                            case OP_ICMPGE:
                                isT = lhs >= rhs;
                                break;
                            default:
                                throw new AssertionError("Unhandled op-code");
                        }
                        if (cmp) {
                            valStack.push(isT ? VALUE_TRUE : VALUE_FALSE);
                        } else if (isT) {
                            System.out.print("Jumping from: " + i);
                            opP = i = payload[0];
                            System.out.println(" to " + (i + 1));
                        }
                        break;
                    case OP_ADD:
                    case OP_SUBSTRACT:
                    case OP_MULTIPLY:
                    case OP_DIVIDE:
                    case OP_MODULUS:
                    case OP_B_OR:
                    case OP_B_AND:
                    case OP_B_XOR:
                        sym1 = valStack.pop();
                        sym2 = valStack.pop();
                        if (sym1.getType().baseType != SYM_INT || sym2.getType().baseType != SYM_INT) {
                            throw new IOException("The symbols are not ints.");
                        }
                        lhs = (Integer) sym2.getVal();
                        rhs = (Integer) sym1.getVal();
                        switch(opcode) {
                            case OP_SUBSTRACT:
                                rhs = -rhs;
                            case OP_ADD:
                                val = lhs + rhs;
                                break;
                            case OP_MULTIPLY:
                                val = lhs * rhs;
                                break;
                            case OP_DIVIDE:
                                if (rhs == 0) {
                                    throw new IOException("Divide by zero.");
                                }
                                val = lhs / rhs;
                                break;
                            case OP_MODULUS:
                                if (rhs == 0) {
                                    throw new IOException("Divide by zero.");
                                }
                                val = lhs % rhs;
                                break;
                            case OP_B_OR:
                                val = lhs | rhs;
                                break;
                            case OP_B_AND:
                                val = lhs & rhs;
                                break;
                            case OP_B_XOR:
                                val = lhs ^ rhs;
                                break;
                            default:
                                throw new AssertionError("Invalid opcode");
                        }
                        valStack.push(new Value(val, new Type(SYM_INT)));
                        break;
                    case OP_NOT:
                    case OP_INZ:
                    case OP_BIT_COM:
                    case OP_SIGN_INV:
                        sym1 = valStack.pop();
                        if (sym1.getType().baseType != SYM_INT) {
                            throw new IOException("The symbol is not an int.");
                        }
                        lhs = (Integer) sym1.getVal();
                        switch(opcode) {
                            case OP_NOT:
                                val = lhs == 0 ? 1 : 0;
                                break;
                            case OP_INZ:
                                val = lhs != 0 ? 1 : 0;
                                break;
                            case OP_BIT_COM:
                                val = ~lhs;
                                break;
                            case OP_SIGN_INV:
                                val = -lhs;
                                break;
                            default:
                                throw new AssertionError("Unhandled op code");
                        }
                        valStack.push(new Value(val, new Type(SYM_INT)));
                        break;
                    case OP_PST:
                        StackFrame[] trace = generateStackTrace(i);
                        System.err.println("Trace: " + trace[0]);
                        for (int j = 1; j < trace.length; j++) {
                            System.out.println("        at: " + trace[j]);
                        }
                        break;
                    case OP_RET:
                        break exec;
                    default:
                        throw new IOException("Unhandled OP code: " + InstructionCodes.instrNames[opcode]);
                }
            }
            if (opStack.size() == 0) {
                break;
            }
            popFunction();
        } while (true);
        System.out.println("Executed " + execedInst + " instructions.");
    }
