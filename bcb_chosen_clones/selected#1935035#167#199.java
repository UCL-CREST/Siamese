    private void mergeZipJarContents(ZipOutputStream output, File f) throws IOException {
        if (!f.exists()) {
            return;
        }
        ZipFile zipf = new ZipFile(f);
        Enumeration entries = zipf.entries();
        while (entries.hasMoreElements()) {
            ZipEntry inputEntry = (ZipEntry) entries.nextElement();
            String inputEntryName = inputEntry.getName();
            int index = inputEntryName.indexOf("META-INF");
            if (index < 0) {
                try {
                    output.putNextEntry(processEntry(zipf, inputEntry));
                } catch (ZipException ex) {
                    String mess = ex.getMessage();
                    if (mess.indexOf("duplicate") >= 0) {
                        continue;
                    } else {
                        throw ex;
                    }
                }
                InputStream in = zipf.getInputStream(inputEntry);
                int len = buffer.length;
                int count = -1;
                while ((count = in.read(buffer, 0, len)) > 0) {
                    output.write(buffer, 0, count);
                }
                in.close();
                output.closeEntry();
            }
        }
        zipf.close();
    }
