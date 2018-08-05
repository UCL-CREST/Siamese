    public static void entering(String[] args) throws IOException, CodeCheckException {
        ClassWriter writer = new ClassWriter();
        writer.readClass(new BufferedInputStream(new FileInputStream(args[0])));
        int constantIndex = writer.getStringConstantIndex("Entering ");
        int fieldRefIndex = writer.getReferenceIndex(ClassWriter.CONSTANT_Fieldref, "java/lang/System", "out", "Ljava/io/PrintStream;");
        int printlnRefIndex = writer.getReferenceIndex(ClassWriter.CONSTANT_Methodref, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
        int printRefIndex = writer.getReferenceIndex(ClassWriter.CONSTANT_Methodref, "java/io/PrintStream", "print", "(Ljava/lang/String;)V");
        for (Iterator i = writer.getMethods().iterator(); i.hasNext(); ) {
            MethodInfo method = (MethodInfo) i.next();
            if (method.getName().equals("readConstant")) continue;
            CodeAttribute attribute = method.getCodeAttribute();
            ArrayList instructions = new ArrayList(10);
            byte[] operands;
            operands = new byte[2];
            NetByte.intToPair(fieldRefIndex, operands, 0);
            instructions.add(new Instruction(OpCode.getOpCodeByMnemonic("getstatic"), 0, operands, false));
            instructions.add(new Instruction(OpCode.getOpCodeByMnemonic("dup"), 0, null, false));
            instructions.add(Instruction.appropriateLdc(constantIndex, false));
            operands = new byte[2];
            NetByte.intToPair(printRefIndex, operands, 0);
            instructions.add(new Instruction(OpCode.getOpCodeByMnemonic("invokevirtual"), 0, operands, false));
            instructions.add(Instruction.appropriateLdc(writer.getStringConstantIndex(method.getName()), false));
            operands = new byte[2];
            NetByte.intToPair(printlnRefIndex, operands, 0);
            instructions.add(new Instruction(OpCode.getOpCodeByMnemonic("invokevirtual"), 0, operands, false));
            attribute.insertInstructions(0, 0, instructions);
            attribute.codeCheck();
        }
        BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(args[1]));
        writer.writeClass(outStream);
        outStream.close();
    }
