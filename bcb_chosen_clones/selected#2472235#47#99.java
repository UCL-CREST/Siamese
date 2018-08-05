    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        String session_id = session.getId();
        File session_fileDir = new File(destinationDir + java.io.File.separator + session_id);
        session_fileDir.mkdir();
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
        fileItemFactory.setSizeThreshold(1 * 1024 * 1024);
        fileItemFactory.setRepository(tmpDir);
        ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
        String pathToFile = new String();
        try {
            List items = uploadHandler.parseRequest(request);
            Iterator itr = items.iterator();
            while (itr.hasNext()) {
                FileItem item = (FileItem) itr.next();
                if (item.isFormField()) {
                    ;
                } else {
                    pathToFile = getServletContext().getRealPath("/") + "files" + java.io.File.separator + session_id;
                    File file = new File(pathToFile + java.io.File.separator + item.getName());
                    item.write(file);
                    getContents(file, pathToFile);
                    ComtorStandAlone.setMode(Mode.CLOUD);
                    Comtor.start(pathToFile);
                }
            }
            try {
                File reportFile = new File(pathToFile + java.io.File.separator + "comtorReport.txt");
                String reportURLString = AWSServices.storeReportS3(reportFile, session_id).toString();
                if (reportURLString.startsWith("https")) reportURLString = reportURLString.replaceFirst("https", "http");
                String requestURL = request.getRequestURL().toString();
                String url = requestURL.substring(0, requestURL.lastIndexOf("/"));
                out.println("<html><head/><body>");
                out.println("<a href=\"" + url + "\">Return to home</a>&nbsp;&nbsp;");
                out.println("<a href=\"" + reportURLString + "\">Report URL</a><br/><hr/>");
                Scanner scan = new Scanner(reportFile);
                out.println("<pre>");
                while (scan.hasNextLine()) out.println(scan.nextLine());
                out.println("</pre><hr/>");
                out.println("<a href=\"" + url + "\">Return to home</a>&nbsp;&nbsp;");
                out.println("<a href=\"" + reportURLString + "\">Report URL</a><br/>");
                out.println("</body></html>");
            } catch (Exception ex) {
                System.err.println(ex);
            }
        } catch (FileUploadException ex) {
            System.err.println("Error encountered while parsing the request" + ex);
        } catch (Exception ex) {
            System.err.println("Error encountered while uploading file" + ex);
        }
    }
