    public void operate() throws ApplicationException {
        InputStream inputStream = null;
        try {
            String zipfilename = filename;
            if (zipfilename.indexOf(".") >= 0) {
                zipfilename = zipfilename.substring(0, zipfilename.lastIndexOf("."));
            }
            zipfilename = TextUtility.replaceString(zipfilename, "+", "%20");
            response.addHeader("Content-Transfer-Encoding", "base64");
            response.addHeader("Content-Disposition", "attachment; filename=" + zipfilename + ".zip");
            response.setContentType("application/x-zip-compressed");
            org.apache.tools.zip.ZipOutputStream sos = new org.apache.tools.zip.ZipOutputStream(response.getOutputStream());
            sos.setEncoding(this.encoding);
            sos.setMethod(ZipOutputStream.DEFLATED);
            org.apache.tools.zip.ZipEntry theEntry = new org.apache.tools.zip.ZipEntry(this.innerFilename);
            sos.putNextEntry(theEntry);
            byte[] buffer = new byte[DATA_BLOCK_SIZE];
            int length = -1;
            inputStream = this.getInputStream();
            while ((length = inputStream.read(buffer, 0, DATA_BLOCK_SIZE)) != -1) {
                sos.write(buffer, 0, length);
            }
            sos.flush();
            sos.close();
        } catch (Exception e) {
            log.error(e, e);
            throw new ApplicationException(com.dcivision.framework.ErrorConstant.COMMON_FATAL_ERROR, e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ignore) {
            }
        }
    }
