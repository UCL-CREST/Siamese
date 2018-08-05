    public static final void export(String filename, byte[] bytes, String charset) throws IOException {
        HttpServletResponse response = findResponse();
        if (charset != null) {
            response.setCharacterEncoding(charset);
        }
        response.setContentType("application/octet-stream");
        response.setHeader("content-disposition", "attachment;filename=" + filename + ".zip");
        OutputStream os = response.getOutputStream();
        ZipOutputStream zos = new ZipOutputStream(os);
        ZipEntry zipEntry = new ZipEntry(filename + ".xml");
        zos.putNextEntry(zipEntry);
        zos.write(bytes);
        zos.close();
    }
