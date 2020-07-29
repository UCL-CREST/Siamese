    protected static void addFileToJar(JarOutputStream jStream, File inputFile, String logicalFilename, ArrayList addedfiles) throws Exception {
        FileInputStream iStream = null;
        try {
            if (!addedfiles.contains(logicalFilename)) {
                iStream = new FileInputStream(inputFile);
                ZipEntry zipEntry = new ZipEntry(logicalFilename.replace('\\', '/'));
                jStream.putNextEntry(zipEntry);
                byte[] byteBuffer = new byte[2 * 1024];
                int count = 0;
                do {
                    jStream.write(byteBuffer, 0, count);
                    count = iStream.read(byteBuffer, 0, byteBuffer.length);
                } while (count != -1);
                addedfiles.add(logicalFilename);
            }
        } catch (IOException ioe) {
            tools.util.LogMgr.err("Filetil.addFileToJar " + ioe);
            ioe.printStackTrace();
        } finally {
            if (iStream != null) {
                try {
                    iStream.close();
                } catch (IOException closeException) {
                }
            }
        }
    }
