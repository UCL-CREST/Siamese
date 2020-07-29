    public void dispatch(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws ServletException {
        try {
            response.setHeader("Content-Disposition", "filename=" + filename);
            byte[] buf = new byte[1024];
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
            Iterator it = files.iterator();
            while (it.hasNext()) {
                FileMetaData file = (FileMetaData) it.next();
                if (file.isDirectory()) {
                    continue;
                }
                FileInputStream in = new FileInputStream(file.getFile());
                out.putNextEntry(new ZipEntry(file.getAbsolutePath().substring(1)));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException ioe) {
            throw new ServletException(ioe);
        }
    }
