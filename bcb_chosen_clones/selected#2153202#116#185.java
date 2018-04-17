    public void testWriteThreadsNoCompression() throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.loadProfiles(CommandLineProcessorFactory.PROFILE.DB, CommandLineProcessorFactory.PROFILE.REST_CLIENT, CommandLineProcessorFactory.PROFILE.COLLECTOR);
        final LocalLogFileWriter writer = (LocalLogFileWriter) bootstrap.getBean(LogFileWriter.class);
        writer.init();
        writer.setCompressionCodec(null);
        File fileInput = new File(baseDir, "testWriteOneFile/input");
        fileInput.mkdirs();
        File fileOutput = new File(baseDir, "testWriteOneFile/output");
        fileOutput.mkdirs();
        writer.setBaseDir(fileOutput);
        int fileCount = 100;
        int lineCount = 100;
        File[] inputFiles = createInput(fileInput, fileCount, lineCount);
        ExecutorService exec = Executors.newFixedThreadPool(fileCount);
        final CountDownLatch latch = new CountDownLatch(fileCount);
        for (int i = 0; i < fileCount; i++) {
            final File file = inputFiles[i];
            final int count = i;
            exec.submit(new Callable<Boolean>() {

                @Override
                public Boolean call() throws Exception {
                    FileStatus.FileTrackingStatus status = FileStatus.FileTrackingStatus.newBuilder().setFileDate(System.currentTimeMillis()).setDate(System.currentTimeMillis()).setAgentName("agent1").setFileName(file.getName()).setFileSize(file.length()).setLogType("type1").build();
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    try {
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            writer.write(status, new ByteArrayInputStream((line + "\n").getBytes()));
                        }
                    } finally {
                        IOUtils.closeQuietly(reader);
                    }
                    LOG.info("Thread[" + count + "] completed ");
                    latch.countDown();
                    return true;
                }
            });
        }
        latch.await();
        exec.shutdown();
        LOG.info("Shutdown thread service");
        writer.close();
        File[] outputFiles = fileOutput.listFiles();
        assertNotNull(outputFiles);
        File testCombinedInput = new File(baseDir, "combinedInfile.txt");
        testCombinedInput.createNewFile();
        FileOutputStream testCombinedInputOutStream = new FileOutputStream(testCombinedInput);
        try {
            for (File file : inputFiles) {
                FileInputStream f1In = new FileInputStream(file);
                IOUtils.copy(f1In, testCombinedInputOutStream);
            }
        } finally {
            testCombinedInputOutStream.close();
        }
        File testCombinedOutput = new File(baseDir, "combinedOutfile.txt");
        testCombinedOutput.createNewFile();
        FileOutputStream testCombinedOutOutStream = new FileOutputStream(testCombinedOutput);
        try {
            System.out.println("----------------- " + testCombinedOutput.getAbsolutePath());
            for (File file : outputFiles) {
                FileInputStream f1In = new FileInputStream(file);
                IOUtils.copy(f1In, testCombinedOutOutStream);
            }
        } finally {
            testCombinedOutOutStream.close();
        }
        FileUtils.contentEquals(testCombinedInput, testCombinedOutput);
    }
