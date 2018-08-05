    private static void addDirectoryToJar(File file, ZipOutputStream targetStream, String path) throws IOException {
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.getName().startsWith(".")) {
                continue;
            }
            if (f.isDirectory()) {
                addDirectoryToJar(f, targetStream, path + "/" + f.getName());
            } else {
                String dataFileName = path + "/" + f.getName();
                InputStream is = new FileInputStream(f);
                BufferedInputStream sourceStream = new BufferedInputStream(is);
                ZipEntry theEntry = new ZipEntry(dataFileName);
                targetStream.putNextEntry(theEntry);
                byte[] data = new byte[1024];
                int bCnt;
                while ((bCnt = sourceStream.read(data, 0, 1024)) != -1) {
                    targetStream.write(data, 0, bCnt);
                }
                targetStream.flush();
                targetStream.closeEntry();
                sourceStream.close();
            }
        }
    }
