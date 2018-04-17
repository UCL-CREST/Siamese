    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        InputStream is = null;
        InputStream page = null;
        OutputStream os = null;
        String rootUrl = null;
        try {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (!isMultipart) {
                request.setAttribute("error", "Form isn't a multipart form");
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/error.jsp");
                rd.forward(request, response);
            }
            ServletFileUpload upload = new ServletFileUpload();
            String webUrl = null;
            FileItemIterator iter = upload.getItemIterator(request);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getFieldName();
                if (name.equals("webpage")) {
                    is = item.openStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    IOUtils.copy(is, baos);
                    page = new ByteArrayInputStream(baos.toByteArray());
                } else if (name.equals("weburl")) {
                    InputStream wpIs = null;
                    try {
                        webUrl = Streams.asString(item.openStream());
                        URL u = new URL(webUrl);
                        wpIs = new BufferedInputStream(u.openStream());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        IOUtils.copy(wpIs, baos);
                        page = new ByteArrayInputStream(baos.toByteArray());
                    } finally {
                        IOUtils.closeQuietly(wpIs);
                    }
                } else if (name.equals("rooturl")) {
                    rootUrl = Streams.asString(item.openStream());
                }
            }
            if (page == null) {
                request.setAttribute("error", "Form doesn't have an html file");
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/error.jsp");
                rd.forward(request, response);
            }
            ToMailerDelegate delegate = new ToMailerDelegate(page, rootUrl);
            os = new BufferedOutputStream(response.getOutputStream());
            os.write(delegate.getMailer());
            os.flush();
        } catch (Exception e) {
            streamException(request, response, e);
        } finally {
            IOUtils.closeQuietly(page);
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }
