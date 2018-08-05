    }

    public final void testT4CClientWriter() throws Exception {
        InputStream is = ClassLoader.getSystemResourceAsStream(this.testFileName);
        T4CClientReader reader = new T4CClientReader(is, rc);
        File tmpFile = File.createTempFile("barde", ".log", this.tmpDir);
        System.out.println("tmp=" + tmpFile.getAbsolutePath());
        T4CClientWriter writer = new T4CClientWriter(new FileOutputStream(tmpFile), rc);
        for (Message m = reader.read(); m != null; m = reader.read()) writer.write(m);
        writer.close();
        InputStream fa = ClassLoader.getSystemResourceAsStream(this.testFileName);
        FileInputStream fb = new FileInputStream(tmpFile);
