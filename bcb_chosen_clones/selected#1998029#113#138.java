    private void saveInternal(File file, Collection<EdgeMappingDescriptor> descriptors, TaskMonitor monitor) throws IOException {
        if (!file.getName().endsWith(".ov1")) {
            file = new File(file.getAbsolutePath() + ".ov1");
        }
        File graphFile = OndexPlugin.getInstance().getOndexGraphFile();
        File mappingFile = new File(System.getProperty("java.io.tmpdir") + File.separator + "ondex_mapping.tsv");
        monitor.setStatus("saving mapping descriptors...");
        monitor.setPercentCompleted(10);
        saveMapping(mappingFile, descriptors);
        File[] inFiles = new File[] { graphFile, mappingFile };
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
        monitor.setStatus("compressing...");
        for (File inFile : inFiles) {
            FileInputStream in = new FileInputStream(inFile);
            out.putNextEntry(new ZipEntry(inFile.getName()));
            int len;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.closeEntry();
        }
        out.close();
        monitor.setPercentCompleted(95);
    }
