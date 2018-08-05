    protected void addFileToJar(JarOutputStream jStream, File inputFile, String logicalFilename) throws BuildException {
        FileInputStream iStream = null;
        try {
            if (!addedfiles.contains(logicalFilename)) {
                iStream = new FileInputStream(inputFile);
                ZipEntry zipEntry = new ZipEntry(logicalFilename.replace('\\', '/'));
                jStream.putNextEntry(zipEntry);
                byte[] byteBuffer = new byte[2 * DEFAULT_BUFFER_SIZE];
                int count = 0;
                do {
                    jStream.write(byteBuffer, 0, count);
                    count = iStream.read(byteBuffer, 0, byteBuffer.length);
                } while (count != -1);
                addedfiles.add(logicalFilename);
            }
        } catch (IOException ioe) {
            log("WARNING: IOException while adding entry " + logicalFilename + " to jarfile from " + inputFile.getPath() + " " + ioe.getClass().getName() + "-" + ioe.getMessage(), Project.MSG_WARN);
        } finally {
            if (iStream != null) {
                try {
                    iStream.close();
                } catch (IOException closeException) {
                }
            }
        }
    }
