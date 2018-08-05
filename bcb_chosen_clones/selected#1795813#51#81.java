    private void createSoundbank(String testSoundbankFileName) throws Exception {
        System.out.println("Create soundbank");
        File packageDir = new File("testsoundbank");
        if (packageDir.exists()) {
            for (File file : packageDir.listFiles()) assertTrue(file.delete());
            assertTrue(packageDir.delete());
        }
        packageDir.mkdir();
        String sourceFileName = "testsoundbank/TestSoundBank.java";
        File sourceFile = new File(sourceFileName);
        FileWriter writer = new FileWriter(sourceFile);
        writer.write("package testsoundbank;\n" + "public class TestSoundBank extends com.sun.media.sound.ModelAbstractOscillator { \n" + "    @Override public int read(float[][] buffers, int offset, int len) throws java.io.IOException { \n" + "   return 0;\n" + " }\n" + "    @Override public String getVersion() {\n" + "   return \"" + (soundbankRevision++) + "\";\n" + "    }\n" + "}\n");
        writer.close();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(new File(".")));
        compiler.getTask(null, fileManager, null, null, null, fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile))).call();
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(testSoundbankFileName));
        ZipEntry ze = new ZipEntry("META-INF/services/javax.sound.midi.Soundbank");
        zos.putNextEntry(ze);
        zos.write("testsoundbank.TestSoundBank".getBytes());
        ze = new ZipEntry("testsoundbank/TestSoundBank.class");
        zos.putNextEntry(ze);
        FileInputStream fis = new FileInputStream("testsoundbank/TestSoundBank.class");
        int b = fis.read();
        while (b != -1) {
            zos.write(b);
            b = fis.read();
        }
        zos.close();
    }
