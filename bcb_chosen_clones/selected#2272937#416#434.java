    public static void writeDBFile(Hashtable db, File file) throws IOException {
        File tmpFile = File.createTempFile("galaxy-civilization", null, file.getParentFile());
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().putValue("Application", Main.APPLICATION_NAME);
        manifest.getMainAttributes().putValue("Application-Version", Long.toString(Main.VERSION));
        manifest.getMainAttributes().putValue("Client-Serial-Number", Main.APPLICATION_NAME);
        ZipEntry entry = new ZipEntry("db.data");
        FileOutputStream fout = new FileOutputStream(tmpFile);
        JarOutputStream jOut = new JarOutputStream(new BufferedOutputStream(fout), manifest);
        jOut.putNextEntry(entry);
        ObjectOutputStream out = new ObjectOutputStream(jOut);
        out.writeObject(db);
        out.flush();
        jOut.finish();
        jOut.close();
        fout.close();
        if (file.exists()) file.delete();
        if (!tmpFile.renameTo(file)) throw new IOException("Cannot rename tmp-file to target file: " + file.getAbsolutePath());
    }
