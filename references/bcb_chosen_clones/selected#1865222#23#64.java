    public FileIndex(File file) throws IOException {
        this.file = file;
        if (file.isFile() && !file.getName().endsWith(".zip")) {
            System.out.println("Converting " + file + " to zip format");
            InputStream in;
            String newFile = file.getAbsolutePath();
            if (file.getName().endsWith("tar.gz")) {
                in = new GZIPInputStream(new FileInputStream(file));
                newFile = newFile.substring(0, newFile.length() - ".tar.gz".length()) + ".zip";
            } else if (file.getName().endsWith(".tar")) {
                in = new FileInputStream(file);
                newFile = newFile.substring(0, newFile.length() - ".tar".length()) + ".zip";
            } else {
                throw new IOException("file is not in a compatible tar or tar.gz format");
            }
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(newFile));
            out.setMethod(ZipOutputStream.DEFLATED);
            out.setLevel(Deflater.BEST_COMPRESSION);
            TarInputStream tarStream = new TarInputStream(in);
            tarStream.setBufferDebug(false);
            tarStream.setDebug(false);
            TarEntryEnumerator tarEnum = new TarEntryEnumerator(tarStream);
            byte[] buf = new byte[512];
            while (tarEnum.hasMoreElements()) {
                TarEntry entry = (TarEntry) tarEnum.nextElement();
                if (!entry.getName().endsWith(".gif") && !entry.getName().endsWith(".png") && !entry.getName().endsWith(".html")) {
                    ZipEntry zipEntry = new ZipEntry(entry.getName());
                    int len;
                    out.putNextEntry(zipEntry);
                    while ((len = tarStream.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                }
            }
            in.close();
            out.flush();
            out.close();
            System.out.println("Done Converting " + file + " to zip format new file is at " + newFile);
            this.file = new File(newFile);
        }
    }
