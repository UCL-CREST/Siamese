    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        String forw = null;
        try {
            int maxUploadSize = 50000000;
            MultipartRequest multi = new MultipartRequest(request, ".", maxUploadSize);
            String descrizione = multi.getParameter("text");
            File myFile = multi.getFile("uploadfile");
            String filePath = multi.getOriginalFileName("uploadfile");
            String path = "C:\\files\\";
            try {
                FileInputStream inStream = new FileInputStream(myFile);
                FileOutputStream outStream = new FileOutputStream(path + myFile.getName());
                while (inStream.available() > 0) {
                    outStream.write(inStream.read());
                }
                inStream.close();
                outStream.close();
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            forw = "../sendDoc.jsp";
            request.setAttribute("contentType", context.getMimeType(path + myFile.getName()));
            request.setAttribute("text", descrizione);
            request.setAttribute("path", path + myFile.getName());
            request.setAttribute("size", Long.toString(myFile.length()) + " Bytes");
            RequestDispatcher rd = request.getRequestDispatcher(forw);
            rd.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
