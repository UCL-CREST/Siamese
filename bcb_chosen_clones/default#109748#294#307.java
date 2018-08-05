    protected void jvmpiDumpData() {
        if (!jvmpiSupported) {
            return;
        }
        try {
            Method dataDumpMtd;
            Class[] argsTypes = new Class[0];
            dataDumpMtd = cvmJvmpiClass.getMethod("postDataDumpRequestEvent", argsTypes);
            Object[] voidArgs = new Object[0];
            dataDumpMtd.invoke(null, voidArgs);
        } catch (Exception e) {
            System.err.println("ERROR: Unexpected exception while " + "requesting JVMPI data dump: " + e);
        }
    }
