    protected PredicateAnnotationRecord generatePredicateAnnotationRecord(PredicateAnnotationRecord par, String miDescriptor) {
        String annotClass = par.annotation.getType().substring(1, par.annotation.getType().length() - 1).replace('/', '.');
        String methodName = getMethodName(par);
        String hashKey = annotClass + CLASS_SIG_SEPARATOR_STRING + methodName;
        PredicateAnnotationRecord gr = _generatedPredicateRecords.get(hashKey);
        if (gr != null) {
            _sharedAddData.cacheInfo.incCombinePredicateCacheHit();
            return gr;
        } else {
            _sharedAddData.cacheInfo.incCombinePredicateCacheMiss();
        }
        String predicateClass = ((_predicatePackage.length() > 0) ? (_predicatePackage + ".") : "") + annotClass + "Pred";
        ClassFile predicateCF = null;
        File clonedFile = new File(_predicatePackageDir, annotClass.replace('.', '/') + "Pred.class");
        if (clonedFile.exists() && clonedFile.isFile() && clonedFile.canRead()) {
            try {
                predicateCF = new ClassFile(new FileInputStream(clonedFile));
            } catch (IOException ioe) {
                throw new ThreadCheckException("Could not open predicate class file, source=" + clonedFile, ioe);
            }
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                _templatePredicateClassFile.write(baos);
                predicateCF = new ClassFile(new ByteArrayInputStream(baos.toByteArray()));
            } catch (IOException ioe) {
                throw new ThreadCheckException("Could not open predicate template class file", ioe);
            }
        }
        clonedFile.getParentFile().mkdirs();
        final ArrayList<String> paramNames = new ArrayList<String>();
        final HashMap<String, String> paramTypes = new HashMap<String, String>();
        performCombineTreeWalk(par, new ILambda.Ternary<Object, String, String, AAnnotationsAttributeInfo.Annotation.AMemberValue>() {

            public Object apply(String param1, String param2, AAnnotationsAttributeInfo.Annotation.AMemberValue param3) {
                paramNames.add(param1);
                paramTypes.put(param1, param2);
                return null;
            }
        }, "");
        ArrayList<PredicateAnnotationRecord> memberPARs = new ArrayList<PredicateAnnotationRecord>();
        for (String key : par.combinedPredicates.keySet()) {
            for (PredicateAnnotationRecord memberPAR : par.combinedPredicates.get(key)) {
                if ((memberPAR.predicateClass != null) && (memberPAR.predicateMI != null)) {
                    memberPARs.add(memberPAR);
                } else {
                    memberPARs.add(generatePredicateAnnotationRecord(memberPAR, miDescriptor));
                }
            }
        }
        AUTFPoolInfo predicateClassNameItem = new ASCIIPoolInfo(predicateClass.replace('.', '/'), predicateCF.getConstantPool());
        int[] l = predicateCF.addConstantPoolItems(new APoolInfo[] { predicateClassNameItem });
        predicateClassNameItem = predicateCF.getConstantPoolItem(l[0]).execute(CheckUTFVisitor.singleton(), null);
        ClassPoolInfo predicateClassItem = new ClassPoolInfo(predicateClassNameItem, predicateCF.getConstantPool());
        l = predicateCF.addConstantPoolItems(new APoolInfo[] { predicateClassItem });
        predicateClassItem = predicateCF.getConstantPoolItem(l[0]).execute(CheckClassVisitor.singleton(), null);
        predicateCF.setThisClass(predicateClassItem);
        StringBuilder sb = new StringBuilder();
        sb.append("(Ljava/lang/Object;");
        if (par.passArguments) {
            sb.append("[Ljava/lang/Object;");
        }
        for (String key : paramNames) {
            sb.append(paramTypes.get(key));
        }
        sb.append(")Z");
        String methodDesc = sb.toString();
        MethodInfo templateMI = null;
        MethodInfo predicateMI = null;
        for (MethodInfo mi : predicateCF.getMethods()) {
            if ((mi.getName().toString().equals(methodName)) && (mi.getDescriptor().toString().equals(methodDesc))) {
                predicateMI = mi;
                break;
            } else if ((mi.getName().toString().equals("template")) && (mi.getDescriptor().toString().startsWith("(")) && (mi.getDescriptor().toString().endsWith(")Z"))) {
                templateMI = mi;
            }
        }
        if ((templateMI == null) && (predicateMI == null)) {
            throw new ThreadCheckException("Could not find template predicate method in class file");
        }
        if (predicateMI == null) {
            AUTFPoolInfo namecpi = new ASCIIPoolInfo(methodName, predicateCF.getConstantPool());
            l = predicateCF.addConstantPoolItems(new APoolInfo[] { namecpi });
            namecpi = predicateCF.getConstantPoolItem(l[0]).execute(CheckUTFVisitor.singleton(), null);
            AUTFPoolInfo descpi = new ASCIIPoolInfo(methodDesc, predicateCF.getConstantPool());
            l = predicateCF.addConstantPoolItems(new APoolInfo[] { descpi });
            descpi = predicateCF.getConstantPoolItem(l[0]).execute(CheckUTFVisitor.singleton(), null);
            ArrayList<AAttributeInfo> list = new ArrayList<AAttributeInfo>();
            for (AAttributeInfo a : templateMI.getAttributes()) {
                try {
                    AAttributeInfo clonedA = (AAttributeInfo) a.clone();
                    list.add(clonedA);
                } catch (CloneNotSupportedException e) {
                    throw new InstrumentorException("Could not clone method attributes");
                }
            }
            predicateMI = new MethodInfo(templateMI.getAccessFlags(), namecpi, descpi, list.toArray(new AAttributeInfo[] {}));
            predicateCF.getMethods().add(predicateMI);
            CodeAttributeInfo.CodeProperties props = predicateMI.getCodeAttributeInfo().getProperties();
            props.maxLocals += paramTypes.size() + 1 + (par.passArguments ? 1 : 0);
            InstructionList il = new InstructionList(predicateMI.getCodeAttributeInfo().getCode());
            if ((par.combineMode == Combine.Mode.OR) || (par.combineMode == Combine.Mode.XOR) || (par.combineMode == Combine.Mode.IMPLIES)) {
                il.insertInstr(new GenericInstruction(Opcode.ICONST_0), predicateMI.getCodeAttributeInfo());
            } else {
                il.insertInstr(new GenericInstruction(Opcode.ICONST_1), predicateMI.getCodeAttributeInfo());
            }
            boolean res;
            res = il.advanceIndex();
            assert res == true;
            int accumVarIndex = props.maxLocals - 1;
            AInstruction loadAccumInstr;
            AInstruction storeAccumInstr;
            if (accumVarIndex < 256) {
                loadAccumInstr = new GenericInstruction(Opcode.ILOAD, (byte) accumVarIndex);
                storeAccumInstr = new GenericInstruction(Opcode.ISTORE, (byte) accumVarIndex);
            } else {
                byte[] bytes = new byte[] { Opcode.ILOAD, 0, 0 };
                Types.bytesFromShort((short) accumVarIndex, bytes, 1);
                loadAccumInstr = new WideInstruction(bytes);
                bytes[0] = Opcode.ISTORE;
                storeAccumInstr = new WideInstruction(bytes);
            }
            il.insertInstr(storeAccumInstr, predicateMI.getCodeAttributeInfo());
            res = il.advanceIndex();
            assert res == true;
            int maxStack = 0;
            int paramIndex = 1;
            int lvIndex = 1;
            if (par.passArguments) {
                lvIndex += 1;
            }
            int memberCount = 0;
            for (PredicateAnnotationRecord memberPAR : memberPARs) {
                ++memberCount;
                il.insertInstr(new GenericInstruction(Opcode.ALOAD_0), predicateMI.getCodeAttributeInfo());
                res = il.advanceIndex();
                assert res == true;
                int curStack = 1;
                if (memberPAR.passArguments) {
                    if (par.passArguments) {
                        il.insertInstr(new GenericInstruction(Opcode.ALOAD_1), predicateMI.getCodeAttributeInfo());
                        res = il.advanceIndex();
                        assert res == true;
                        curStack += 1;
                    }
                }
                for (int paramNameIndex = 0; paramNameIndex < memberPAR.paramNames.size(); ++paramNameIndex) {
                    String t = memberPAR.paramTypes.get(memberPAR.paramNames.get(paramNameIndex));
                    if (t.length() == 0) {
                        throw new ThreadCheckException("Length of parameter type no. " + paramIndex + " string is 0 in " + predicateMI.getName() + " in class " + predicateCF.getThisClassName());
                    }
                    byte opcode;
                    int nextLVIndex = lvIndex;
                    switch(t.charAt(0)) {
                        case 'I':
                        case 'B':
                        case 'C':
                        case 'S':
                        case 'Z':
                            opcode = Opcode.ILOAD;
                            nextLVIndex += 1;
                            curStack += 1;
                            break;
                        case 'F':
                            opcode = Opcode.FLOAD;
                            nextLVIndex += 1;
                            curStack += 1;
                            break;
                        case '[':
                        case 'L':
                            opcode = Opcode.ALOAD;
                            nextLVIndex += 1;
                            curStack += 1;
                            break;
                        case 'J':
                            opcode = Opcode.LLOAD;
                            nextLVIndex += 2;
                            curStack += 2;
                            break;
                        case 'D':
                            opcode = Opcode.DLOAD;
                            nextLVIndex += 2;
                            curStack += 2;
                            break;
                        default:
                            throw new ThreadCheckException("Parameter type no. " + paramIndex + ", " + t + ", is unknown in " + predicateMI.getName() + " in class " + predicateCF.getThisClassName());
                    }
                    AInstruction load = Opcode.getShortestLoadStoreInstruction(opcode, (short) lvIndex);
                    il.insertInstr(load, predicateMI.getCodeAttributeInfo());
                    res = il.advanceIndex();
                    assert res == true;
                    ++paramIndex;
                    lvIndex = nextLVIndex;
                }
                if (curStack > maxStack) {
                    maxStack = curStack;
                }
                ReferenceInstruction predicateCallInstr = new ReferenceInstruction(Opcode.INVOKESTATIC, (short) 0);
                int predicateCallIndex = predicateCF.addMethodToConstantPool(memberPAR.predicateClass.replace('.', '/'), memberPAR.predicateMI.getName().toString(), memberPAR.predicateMI.getDescriptor().toString());
                predicateCallInstr.setReference(predicateCallIndex);
                il.insertInstr(predicateCallInstr, predicateMI.getCodeAttributeInfo());
                res = il.advanceIndex();
                assert res == true;
                if ((par.combineMode == Combine.Mode.NOT) || ((par.combineMode == Combine.Mode.IMPLIES) && (memberCount == 1))) {
                    il.insertInstr(new GenericInstruction(Opcode.ICONST_1), predicateMI.getCodeAttributeInfo());
                    res = il.advanceIndex();
                    assert res == true;
                    il.insertInstr(new GenericInstruction(Opcode.SWAP), predicateMI.getCodeAttributeInfo());
                    res = il.advanceIndex();
                    assert res == true;
                    il.insertInstr(new GenericInstruction(Opcode.ISUB), predicateMI.getCodeAttributeInfo());
                    res = il.advanceIndex();
                    assert res == true;
                }
                il.insertInstr(loadAccumInstr, predicateMI.getCodeAttributeInfo());
                res = il.advanceIndex();
                assert res == true;
                if (par.combineMode == Combine.Mode.OR) {
                    il.insertInstr(new GenericInstruction(Opcode.IOR), predicateMI.getCodeAttributeInfo());
                } else if ((par.combineMode == Combine.Mode.AND) || (par.combineMode == Combine.Mode.NOT)) {
                    il.insertInstr(new GenericInstruction(Opcode.IAND), predicateMI.getCodeAttributeInfo());
                } else if (par.combineMode == Combine.Mode.XOR) {
                    il.insertInstr(new GenericInstruction(Opcode.IADD), predicateMI.getCodeAttributeInfo());
                } else if (par.combineMode == Combine.Mode.IMPLIES) {
                    il.insertInstr(new GenericInstruction(Opcode.IOR), predicateMI.getCodeAttributeInfo());
                } else {
                    assert false;
                }
                res = il.advanceIndex();
                assert res == true;
                il.insertInstr(storeAccumInstr, predicateMI.getCodeAttributeInfo());
                res = il.advanceIndex();
                assert res == true;
            }
            if (par.combineMode == Combine.Mode.XOR) {
                il.insertInstr(loadAccumInstr, predicateMI.getCodeAttributeInfo());
                res = il.advanceIndex();
                assert res == true;
                il.insertInstr(new GenericInstruction(Opcode.ICONST_1), predicateMI.getCodeAttributeInfo());
                res = il.advanceIndex();
                assert res == true;
                il.insertInstr(new GenericInstruction(Opcode.ICONST_0), predicateMI.getCodeAttributeInfo());
                res = il.advanceIndex();
                assert res == true;
                WideBranchInstruction br2 = new WideBranchInstruction(Opcode.GOTO_W, il.getIndex() + 1);
                il.insertInstr(br2, predicateMI.getCodeAttributeInfo());
                res = il.advanceIndex();
                assert res == true;
                int jumpIndex = il.getIndex();
                il.insertInstr(new GenericInstruction(Opcode.ICONST_1), predicateMI.getCodeAttributeInfo());
                res = il.advanceIndex();
                assert res == true;
                res = il.rewindIndex(3);
                assert res == true;
                BranchInstruction br1 = new BranchInstruction(Opcode.IF_ICMPEQ, jumpIndex);
                il.insertInstr(br1, predicateMI.getCodeAttributeInfo());
                res = il.advanceIndex(4);
                assert res == true;
            } else {
                il.insertInstr(loadAccumInstr, predicateMI.getCodeAttributeInfo());
                res = il.advanceIndex();
                assert res == true;
            }
            il.deleteInstr(predicateMI.getCodeAttributeInfo());
            predicateMI.getCodeAttributeInfo().setCode(il.getCode());
            props.maxStack = Math.max(maxStack, 2);
            predicateMI.getCodeAttributeInfo().setProperties(props.maxStack, props.maxLocals);
            try {
                FileOutputStream fos = new FileOutputStream(clonedFile);
                predicateCF.write(fos);
                fos.close();
            } catch (IOException e) {
                throw new ThreadCheckException("Could not write cloned predicate class file, target=" + clonedFile);
            }
        }
        gr = new PredicateAnnotationRecord(par.annotation, predicateClass, predicateMI, paramNames, paramTypes, new ArrayList<AAnnotationsAttributeInfo.Annotation.AMemberValue>(), par.passArguments, null, new HashMap<String, ArrayList<PredicateAnnotationRecord>>());
        _generatedPredicateRecords.put(hashKey, gr);
        return gr;
    }
