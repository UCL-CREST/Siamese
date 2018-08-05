    public void setVariables(Properties varDef) throws Exception {
        sendMsg("Setting  the variables ...");
        outJar.putNextEntry(new ZipEntry("vars"));
        ObjectOutputStream objOut = new ObjectOutputStream(outJar);
        objOut.writeObject(varDef);
        objOut.flush();
        outJar.closeEntry();
    }
