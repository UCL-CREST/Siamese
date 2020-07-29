    public Result toZip(Request request, String prefix, List<Entry> entries, boolean recurse, boolean forExport) throws Exception {
        OutputStream os;
        boolean doingFile = false;
        File tmpFile = null;
        if (request.getHttpServletResponse() != null) {
            os = request.getHttpServletResponse().getOutputStream();
            request.getHttpServletResponse().setContentType(getMimeType(OUTPUT_ZIP));
        } else {
            tmpFile = getRepository().getStorageManager().getTmpFile(request, ".zip");
            os = getStorageManager().getFileOutputStream(tmpFile);
            doingFile = true;
        }
        Result result = new Result();
        result.setNeedToWrite(false);
        Element root = null;
        boolean ok = true;
        try {
            processZip(request, entries, recurse, 0, null, prefix, 0, new int[] { 0 }, forExport, null);
        } catch (IllegalArgumentException iae) {
            ok = false;
        }
        if (!ok) {
            javax.servlet.http.HttpServletResponse response = request.getHttpServletResponse();
            response.setStatus(Result.RESPONSE_UNAUTHORIZED);
            response.sendError(response.SC_INTERNAL_SERVER_ERROR, "Size of request has exceeded maximum size");
            return result;
        }
        ZipOutputStream zos = new ZipOutputStream(os);
        if (request.get(ARG_COMPRESS, true) == false) {
            zos.setLevel(0);
        }
        Hashtable seen = new Hashtable();
        try {
            if (forExport) {
                Document doc = XmlUtil.makeDocument();
                root = XmlUtil.create(doc, TAG_ENTRIES, null, new String[] {});
            }
            processZip(request, entries, recurse, 0, zos, prefix, 0, new int[] { 0 }, forExport, root);
            if (root != null) {
                String xml = XmlUtil.toString(root);
                System.err.println(xml);
                zos.putNextEntry(new ZipEntry("entries.xml"));
                byte[] bytes = xml.getBytes();
                zos.write(bytes, 0, bytes.length);
                zos.closeEntry();
            }
        } finally {
            IOUtil.close(zos);
        }
        if (doingFile) {
            os.close();
            return new Result("", getStorageManager().getFileInputStream(tmpFile), getMimeType(OUTPUT_ZIP));
        }
        getLogManager().logInfo("Zip File ended");
        return result;
    }
