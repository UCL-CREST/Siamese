    @SuppressWarnings("static-access")
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        PrintWriter writer = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            writer = response.getWriter();
        } catch (IOException ex) {
            log(OctetStreamReader.class.getName() + "has thrown an exception: " + ex.getMessage());
        }
        String filename = request.getHeader("X-File-Name");
        try {
            filename = URLDecoder.decode(filename, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            is = request.getInputStream();
            File newFile = new File(realPath + filename);
            if (!newFile.exists()) {
                fos = new FileOutputStream(new File(realPath + filename));
                IOUtils.copy(is, fos);
                response.setStatus(response.SC_OK);
                writer.print("{success: true,detailMsg}");
            } else {
                response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
                writer.print("{success: false,detailMsg:'文件已经存在！请重命名后上传！'}");
                log(OctetStreamReader.class.getName() + "has thrown an exception: " + filename + " has existed!");
            }
        } catch (FileNotFoundException ex) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            writer.print("{success: false}");
            log(OctetStreamReader.class.getName() + "has thrown an exception: " + ex.getMessage());
        } catch (IOException ex) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            writer.print("{success: false}");
            log(OctetStreamReader.class.getName() + "has thrown an exception: " + ex.getMessage());
        } finally {
            try {
                fos.close();
                is.close();
            } catch (IOException ignored) {
            }
        }
        writer.flush();
        writer.close();
    }
