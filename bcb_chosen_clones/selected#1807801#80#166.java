    private void buildImpl() throws RemoteException {
        if (interfaceClazz == null) throw new RemoteException("Interface class can not be null");
        if (!interfaceClazz.isInterface()) throw new RemoteException("Interface class must be an interface");
        Method[] methods = interfaceClazz.getMethods();
        Method method;
        for (int i = 0; i < methods.length; i++) {
            method = methods[i];
            checkRemoteExceptionDeclared(method);
        }
        String implName = interfaceClazz.getName() + "RIFImpl";
        ClassGen cg = new ClassGen(implName, "java.lang.Object", "<generated>", ACC_PUBLIC | ACC_SUPER, new String[] { interfaceClazz.getName() });
        ConstantPoolGen cp = cg.getConstantPool();
        InstructionList il;
        InstructionFactory instructionFactory = new InstructionFactory(cg, cp);
        FieldGen field;
        field = new FieldGen(ACC_PRIVATE, new ObjectType(RIFInvoker.class.getName()), "invoker", cp);
        cg.addField(field.getField());
        il = new InstructionList();
        MethodGen methodGen = new MethodGen(ACC_PUBLIC, Type.VOID, new Type[] { new ObjectType(RIFInvoker.class.getName()) }, new String[] { "invoker" }, "<init>", implName, il, cp);
        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(instructionFactory.createInvoke("java.lang.Object", "<init>", Type.VOID, Type.NO_ARGS, Constants.INVOKESPECIAL));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 1));
        il.append(instructionFactory.createFieldAccess(implName, "invoker", new ObjectType(RIFInvoker.class.getName()), Constants.PUTFIELD));
        il.append(InstructionFactory.createReturn(Type.VOID));
        methodGen.setMaxStack();
        methodGen.setMaxLocals();
        cg.addMethod(methodGen.getMethod());
        il.dispose();
        for (int i = 0; i < methods.length; i++) {
            method = methods[i];
            Class[] paramClassTypes = method.getParameterTypes();
            String[] paramNames = new String[paramClassTypes.length];
            for (int j = 0; j < paramClassTypes.length; j++) {
                paramNames[j] = "arg" + j;
            }
            Type[] paramTypes = new Type[paramClassTypes.length];
            int paramCount = paramTypes.length;
            for (int j = 0; j < paramTypes.length; j++) {
                Class paramClassType = paramClassTypes[j];
                paramTypes[j] = Type.getType(paramClassType);
            }
            Class returnTypeClazz = method.getReturnType();
            Type returnType = (method.getReturnType() == null) ? Type.VOID : Type.getType(returnTypeClazz);
            il = new InstructionList();
            methodGen = new MethodGen(ACC_PUBLIC, returnType, paramTypes, paramNames, method.getName(), implName, il, cp);
            il.append(new org.apache.bcel.generic.PUSH(cp, paramCount));
            il.append(instructionFactory.createNewArray(Type.OBJECT, (short) 1));
            for (int j = 0; j < paramCount; j++) {
                il.append(InstructionConstants.DUP);
                il.append(new org.apache.bcel.generic.PUSH(cp, j));
                il.append(InstructionFactory.createLoad(Type.OBJECT, j + 1));
                il.append(InstructionConstants.AASTORE);
            }
            il.append(InstructionFactory.createStore(Type.OBJECT, paramCount + 1));
            il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
            il.append(instructionFactory.createFieldAccess(implName, "invoker", new ObjectType(RIFInvoker.class.getName()), Constants.GETFIELD));
            il.append(new org.apache.bcel.generic.PUSH(cp, method.getName()));
            il.append(InstructionFactory.createLoad(Type.OBJECT, paramCount + 1));
            il.append(instructionFactory.createInvoke(RIFInvoker.class.getName(), "invoke", Type.OBJECT, new Type[] { Type.STRING, new ArrayType(Type.OBJECT, 1) }, Constants.INVOKEVIRTUAL));
            if (!Type.VOID.equals(returnType)) {
                if (returnTypeClazz != null && returnTypeClazz.isPrimitive()) {
                    il.append(instructionFactory.createInvoke("java.lang.Object", "toString", Type.STRING, Type.NO_ARGS, Constants.INVOKEVIRTUAL));
                    if (Boolean.TYPE.equals(returnTypeClazz)) {
                        il.append(instructionFactory.createInvoke("java.lang.Boolean", "valueOf", new ObjectType("java.lang.Boolean"), new Type[] { Type.STRING }, Constants.INVOKESTATIC));
                        il.append(instructionFactory.createInvoke("java.lang.Boolean", "booleanValue", Type.BOOLEAN, Type.NO_ARGS, Constants.INVOKEVIRTUAL));
                    } else if (Long.TYPE.equals(returnTypeClazz)) il.append(instructionFactory.createInvoke("java.lang.Long", "parseLong", Type.LONG, new Type[] { Type.STRING }, Constants.INVOKESTATIC)); else if (Integer.TYPE.equals(returnTypeClazz)) il.append(instructionFactory.createInvoke("java.lang.Integer", "parseInt", Type.INT, new Type[] { Type.STRING }, Constants.INVOKESTATIC)); else if (Short.TYPE.equals(returnTypeClazz)) il.append(instructionFactory.createInvoke("java.lang.Short", "parseShort", Type.SHORT, new Type[] { Type.STRING }, Constants.INVOKESTATIC)); else if (Float.TYPE.equals(returnTypeClazz)) il.append(instructionFactory.createInvoke("java.lang.Float", "parseFloat", Type.FLOAT, new Type[] { Type.STRING }, Constants.INVOKESTATIC)); else if (Double.TYPE.equals(returnTypeClazz)) il.append(instructionFactory.createInvoke("java.lang.Double", "parseDouble", Type.DOUBLE, new Type[] { Type.STRING }, Constants.INVOKESTATIC)); else if (Byte.TYPE.equals(returnTypeClazz)) il.append(instructionFactory.createInvoke("java.lang.Byte", "parseByte", Type.INT, new Type[] { Type.STRING }, Constants.INVOKESTATIC)); else if (Character.TYPE.equals(returnTypeClazz)) throw new IllegalArgumentException("Return type char is not currently supported");
                } else {
                    il.append(instructionFactory.createCast(Type.OBJECT, returnType));
                }
            }
            il.append(InstructionFactory.createReturn(returnType));
            methodGen.setMaxStack();
            methodGen.setMaxLocals();
            cg.addMethod(methodGen.getMethod());
            il.dispose();
        }
        manager.getClassLoader().registerClass(implName, cg.getJavaClass().getBytes());
        try {
            Class implClass = Class.forName(implName, true, manager.getClassLoader());
            Constructor constructor = implClass.getConstructor(new Class[] { RIFInvoker.class });
            impl = (I) constructor.newInstance(new Object[] { this });
        } catch (Exception e) {
            log.error("Error while instantiating " + implName + " for " + interfaceClazz.getName(), e);
            throw new RemoteException("Error while instantiating " + implName + " for " + interfaceClazz.getName(), e);
        }
    }
