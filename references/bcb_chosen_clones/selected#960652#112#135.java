    private void openStream(ZipOutStreamTaskAdapter zip, String filename) {
        byte[] savedZip = getBusiness().getSystemService().getCache().getBlob(filename);
        ByteArrayOutputStream outData = new ByteArrayOutputStream();
        ZipOutputStream out = new ZipOutputStream(outData);
        zip.setOutStream(out);
        zip.setOutData(outData);
        if (savedZip != null) {
            ByteArrayInputStream inputData = new ByteArrayInputStream(savedZip);
            try {
                ZipInputStream in = new ZipInputStream(inputData);
                ZipEntry entry;
                while ((entry = in.getNextEntry()) != null) {
                    out.putNextEntry(entry);
                    if (!entry.isDirectory()) {
                        StreamUtils.readTo(in, out, false, false);
                    }
                    out.closeEntry();
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
