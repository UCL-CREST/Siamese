    private static File[] getWsdls(File dirfile) throws Exception {
        File[] allfiles = dirfile.listFiles();
        List<File> files = new ArrayList<File>();
        if (allfiles != null) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String outputDir = argMap.get(OUTPUT_DIR);
            for (File file : allfiles) {
                if (file.getName().endsWith(WSDL_SUFFIX)) {
                    files.add(file);
                }
                if (file.getName().endsWith(WSDL_SUFFIX) || file.getName().endsWith(XSD_SUFFIX)) {
                    md.update(FileUtil.getBytes(file));
                }
            }
            computedHash = md.digest();
            hashFile = new File(outputDir + File.separator + argMap.get(BASE_PACKAGE).replace('.', File.separatorChar) + File.separator + "hash.md5");
            if (hashFile.exists()) {
                byte[] readHash = FileUtil.getBytes(hashFile);
                if (Arrays.equals(readHash, computedHash)) {
                    System.out.println("Skipping generation, files not changed.");
                    files.clear();
                }
            }
        }
        File[] filesarr = new File[files.size()];
        files.toArray(filesarr);
        return filesarr;
    }
