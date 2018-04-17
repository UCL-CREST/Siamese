    private static ZipOutputStream fileToZipAux(File file, String relativePath, ZipOutputStream outStream, String outFileName, boolean buildDirectories) throws IOException {
        if (file.isFile()) {
            if (outFileName.compareTo(file.getAbsolutePath()) == 0) {
                return outStream;
            }
            byte[] buf = new byte[DATA_BLOCK_SIZE];
            FileInputStream in = new FileInputStream(file);
            ZipEntry entry = new ZipEntry((buildDirectories ? relativePath + "\\" : "") + file.getName());
            outStream.putNextEntry(entry);
            int len;
            while ((len = in.read(buf)) > 0) {
                outStream.write(buf, 0, len);
            }
            outStream.closeEntry();
            in.close();
            return outStream;
        } else {
            File[] children = file.listFiles();
            for (File child : children) {
                fileToZipAux(child, (relativePath == "" ? file.getName() : relativePath + "\\" + file.getName()), outStream, outFileName, buildDirectories);
            }
            return outStream;
        }
    }
