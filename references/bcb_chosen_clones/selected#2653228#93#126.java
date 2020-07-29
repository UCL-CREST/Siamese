    private static void compress(File root, File in, ZipOutputStream out, MessageDigest md, Set<String> fileNamesToSkipInCheckSumCalculation) throws IOException {
        if (in.isDirectory()) {
            List<File> files = Arrays.asList(in.listFiles());
            if (md != null) {
                Collections.sort(files, new Comparator<File>() {

                    public int compare(File o1, File o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
            }
            for (File f : files) {
                compress(root, f, out, md, fileNamesToSkipInCheckSumCalculation);
            }
        } else {
            String path = root.toURI().relativize(in.toURI()).toString();
            out.putNextEntry(new ZipEntry(path));
            FileInputStream fin = new FileInputStream(in);
            byte[] buffer = new byte[1024];
            int br;
            if (md == null || (fileNamesToSkipInCheckSumCalculation != null && fileNamesToSkipInCheckSumCalculation.contains(in.getName()))) {
                while ((br = fin.read(buffer)) > 0) {
                    out.write(buffer, 0, br);
                }
            } else {
                while ((br = fin.read(buffer)) > 0) {
                    out.write(buffer, 0, br);
                    md.update(buffer, 0, br);
                }
            }
            fin.close();
            out.closeEntry();
        }
    }
