    public static void replaceEntryInZipFile(String zipFilePath, String replacementEntryName, InputStream replacementEntryStream, String outputFilePath) throws IOException {
        ZipOutputStream tempZipStream = null;
        File tempZipFile = null;
        File zipFile = null;
        ZipFile zip = null;
        FileInputStream file = null;
        InputStream entryStream = null;
        try {
            zipFile = new File(zipFilePath);
            zip = new ZipFile(zipFile);
            tempZipFile = new File(outputFilePath);
            tempZipStream = new ZipOutputStream(new FileOutputStream(tempZipFile));
            byte[] buffer = new byte[1024];
            int bytesRead;
            Enumeration entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String entryName = entry.getName();
                if (entryName.equals(replacementEntryName)) {
                    entry = new ZipEntry(entryName);
                    entryStream = replacementEntryStream;
                } else {
                    entryStream = zip.getInputStream(entry);
                    entry.setCompressedSize(-1);
                }
                tempZipStream.putNextEntry(entry);
                while ((bytesRead = entryStream.read(buffer)) != -1) {
                    tempZipStream.write(buffer, 0, bytesRead);
                }
                entryStream.close();
            }
        } catch (IOException e) {
            throw e;
        } finally {
            IOException ex = null;
            if (tempZipStream != null) {
                try {
                    tempZipStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    ex = e;
                }
            }
            if (zip != null) {
                try {
                    zip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    ex = e;
                }
            }
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    ex = e;
                }
            }
            if (entryStream != null) {
                try {
                    entryStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    ex = e;
                }
            }
            if (ex != null) {
                throw new IOException("error(s) caught closing files: " + ex.getMessage());
            }
        }
    }
