    public void cleanOutputDir() {
        File outputDir = new File(m_outputDirName);
        for (File file : outputDir.listFiles()) {
            if (file.isDirectory()) {
                deleteDir(file);
            } else if (file.getName().endsWith(".java")) {
                file.delete();
            }
        }
    }
