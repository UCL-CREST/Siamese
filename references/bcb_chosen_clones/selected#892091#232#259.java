    void writeAllInputContents(ZipOutputStream output) throws IOException {
        Enumeration<? extends ZipEntry> jarEntries = this.jarFile.entries();
        ZipEntry curEntry;
        InputStream is;
        DataOutputStream os;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while (jarEntries.hasMoreElements()) {
            curEntry = jarEntries.nextElement();
            if (curEntry.getName().equals(MAP_INDEX_FILE) || curEntry.getName().equals(MANIFEST)) {
                continue;
            }
            is = this.jarFile.getInputStream(curEntry);
            output.putNextEntry(curEntry);
            while ((bytesRead = is.read(buffer)) > 0) {
                output.write(buffer, 0, bytesRead);
            }
            output.closeEntry();
        }
        output.putNextEntry(new ZipEntry(MANIFEST));
        os = new DataOutputStream(output);
        for (int i = 0; i < this.manifestLines.size(); ++i) {
            os.writeBytes(this.manifestLines.elementAt(i));
            os.writeByte('\n');
        }
        os.flush();
        output.closeEntry();
    }
