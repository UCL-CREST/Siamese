    private void compress(String outputFile, ArrayList<String> inputFiles, PrintWriter log, boolean compress) throws Exception {
        String absPath = getAppConfig().getPathConfig().getAbsoluteServerPath();
        log.println("Concat files into: " + outputFile);
        OutputStream out = new FileOutputStream(absPath + outputFile);
        byte[] buffer = new byte[4096];
        int readBytes;
        for (String file : inputFiles) {
            log.println(" Read: " + file);
            InputStream in = new FileInputStream(absPath + file);
            while ((readBytes = in.read(buffer)) != -1) {
                out.write(buffer, 0, readBytes);
            }
            in.close();
        }
        out.close();
        if (compress) {
            long normalSize = new File(absPath + outputFile).length();
            ProcessBuilder builder = new ProcessBuilder("java", "-jar", "WEB-INF/yuicompressor.jar", outputFile, "-o", outputFile, "--line-break", "4000");
            builder.directory(new File(absPath));
            Process process = builder.start();
            process.waitFor();
            long minSize = new File(absPath + outputFile).length();
            long diff = normalSize - minSize;
            double percentage = Math.floor((double) diff / normalSize * 1000.0) / 10.0;
            double diffSize = (Math.floor(diff / 1024.0 * 10.0) / 10.0);
            log.println("Result: " + percentage + " % (" + diffSize + " KB)");
        }
    }
