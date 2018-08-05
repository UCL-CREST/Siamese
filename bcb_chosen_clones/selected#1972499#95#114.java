    public static boolean hasValidConfiguration(final Settings settings) {
        final File batchfile = new File(settings.getPath());
        if (!batchfile.exists() || !batchfile.isFile()) {
            return false;
        }
        try {
            final StringBuilder batchOutput = new StringBuilder();
            String line;
            final Process batchProcess = Runtime.getRuntime().exec(settings.getPath() + " --version", null, batchfile.getParentFile());
            final BufferedReader input = new BufferedReader(new InputStreamReader(batchProcess.getInputStream()));
            while ((line = input.readLine()) != null) {
                batchOutput.append(line);
            }
            input.close();
            return batchOutput.toString().trim().startsWith("PHPMD ");
        } catch (Exception err) {
            err.printStackTrace();
            return false;
        }
    }
