    public final Machine newInstance(final MachineContext machineContext) throws Exception {
        final Class machineContextClass = machineContext.getClass();
        final String machineContextClassName = machineContext.getClass().getName();
        final String machineClassName = machineContextClassName.substring(0, machineContextClassName.lastIndexOf("$"));
        final Class machineClass = Class.forName(machineClassName);
        final Constructor machineConstructor = machineClass.getConstructor(new Class[] { machineContextClass });
        machineConstructor.setAccessible(true);
        return (Machine) machineConstructor.newInstance(new Object[] { machineContext });
    }
