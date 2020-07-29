    public void setInfo(Info info) throws Exception {
        sendMsg("Setting the installer informations ...");
        outJar.putNextEntry(new ZipEntry("info"));
        ObjectOutputStream objOut = new ObjectOutputStream(outJar);
        objOut.writeObject(info);
        objOut.flush();
        outJar.closeEntry();
    }
