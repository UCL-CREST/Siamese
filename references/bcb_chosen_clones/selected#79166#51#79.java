    public void finish() throws Exception {
        DataOutputStream datOut;
        ObjectOutputStream objOut;
        int size;
        int i;
        sendMsg("Finishing the enpacking ...");
        outJar.putNextEntry(new ZipEntry("kind"));
        datOut = new DataOutputStream(outJar);
        datOut.writeUTF("standard-kunststoff");
        datOut.flush();
        outJar.closeEntry();
        outJar.putNextEntry(new ZipEntry("packs.info"));
        objOut = new ObjectOutputStream(outJar);
        size = packs.size();
        objOut.writeInt(size);
        for (i = 0; i < size; i++) objOut.writeObject(packs.get(i));
        objOut.flush();
        outJar.closeEntry();
        outJar.putNextEntry(new ZipEntry("langpacks.info"));
        datOut = new DataOutputStream(outJar);
        size = langpacks.size();
        datOut.writeInt(size);
        for (i = 0; i < size; i++) datOut.writeUTF((String) langpacks.get(i));
        datOut.flush();
        outJar.closeEntry();
        outJar.flush();
        outJar.close();
        sendStop();
    }
