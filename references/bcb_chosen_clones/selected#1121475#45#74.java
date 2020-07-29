    public final Instruction build(InstructionBuildContext _ibc) {
        if (opcodeEncoding.isModRegRmByteUsed()) {
            _ibc.setModRegRM(new ModRegRM(_ibc.getByte(), _ibc.getPrefixes()));
            _ibc.stepByte();
        }
        _ibc.setOperands(opcodeEncoding.buildOperands(_ibc));
        Instruction instruction = null;
        if (reflectiveClass == null) {
            instruction = buildInstruction(_ibc);
        } else {
            Constructor<?> constructor;
            try {
                constructor = reflectiveClass.getConstructor(new Class[] { Mnemonic.class, InstructionBuildContext.class });
                instruction = (Instruction) constructor.newInstance(new Object[] { mnemonic, _ibc });
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return (instruction);
    }
