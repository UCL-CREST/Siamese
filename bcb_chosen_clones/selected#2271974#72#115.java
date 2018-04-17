    public static void createReqResArchive(Request request, Response response, File zipFile) throws IOException, XMLException {
        File requestFile = File.createTempFile("req-", ".xml");
        File responseFile = File.createTempFile("res-", ".xml");
        XMLUtil.writeRequestXML(request, requestFile);
        XMLUtil.writeResponseXML(response, responseFile);
        Map<String, File> files = new HashMap<String, File>();
        files.put("request.rcq", requestFile);
        files.put("response.rcs", responseFile);
        byte[] buf = new byte[BUFF_SIZE];
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
        boolean isSuccess = false;
        try {
            for (String entryName : files.keySet()) {
                File entryFile = files.get(entryName);
                FileInputStream fis = new FileInputStream(entryFile);
                zos.putNextEntry(new ZipEntry(entryName));
                int len;
                while ((len = fis.read(buf)) > 0) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                fis.close();
            }
            isSuccess = true;
        } finally {
            IOException ioe = null;
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException ex) {
                    isSuccess = false;
                    ioe = ex;
                }
            }
            if (!isSuccess) {
                zipFile.delete();
            }
            requestFile.delete();
            responseFile.delete();
            if (ioe != null) {
                throw ioe;
            }
        }
    }
