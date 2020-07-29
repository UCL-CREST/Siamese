    private ComponentDownload createElementsFile(List elements, IRequestCycle cycle, String name) throws IOException {
        byte[] buffer = new byte[1024];
        String basePath = cycle.getRequestContext().getServlet().getServletContext().getRealPath("/");
        if (elements.size() == 1) {
            ComponentElement ce = (ComponentElement) elements.get(0);
            ce.setBasePath(basePath);
            File retFile = null;
            try {
                retFile = ce.getElementFile();
            } catch (Exception e) {
                throw new ApplicationRuntimeException("Baaad. Bad. ce.getElementFile threw an exception =( " + e.toString());
            }
            return new ComponentDownload(retFile, ce.getFileName(), ce.getContentType());
        }
        File f = File.createTempFile(name, "zip", null);
        f.deleteOnExit();
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
        Iterator i = elements.iterator();
        while (i.hasNext()) {
            ComponentElement ce = (ComponentElement) i.next();
            FileInputStream in = new FileInputStream(basePath + ce.getPath());
            out.putNextEntry(new ZipEntry(ce.getFileName()));
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.closeEntry();
            in.close();
        }
        out.close();
        return new ComponentDownload(f, name + ".zip", "application/zip");
    }
