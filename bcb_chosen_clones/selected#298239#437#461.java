    private void downloadZipDocument(HttpServletResponse response) throws Exception {
        ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
        out.setMethod(ZipOutputStream.DEFLATED);
        for (int i = 0; i < this.lstZipFileName.size(); i++) {
            String documentName = (String) this.lstZipFileName.get(i);
            String documentPath = (String) this.lstZipFilePath.get(i);
            if ("NO_FILE".equals(documentPath)) {
                out.putNextEntry(new ZipEntry(documentName));
            } else {
                out.putNextEntry(new ZipEntry(documentName));
                File file = new File(documentPath);
                if (file.exists()) {
                    FileInputStream in = new FileInputStream(file);
                    byte[] buffer = new byte[8192];
                    int length = -1;
                    while ((length = in.read(buffer, 0, 8192)) != -1) {
                        out.write(buffer, 0, length);
                    }
                    in.close();
                }
            }
        }
        out.flush();
        out.close();
    }
