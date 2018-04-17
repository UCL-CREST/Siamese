    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        imageName = "cap.png";
        rb = ResourceBundle.getBundle("LocalStrings");
        OSAbsoluteDir = rb.getString("servletAlbum.start");
        WebRelativeDir = rb.getString("servletAlbum.imgmain");
        imageOSAbsolute = OSAbsoluteDir + File.separator + imageName;
        imageWebRelative = WebRelativeDir + "/" + imageName;
        out.println("<html>");
        out.println("<body>");
        out.println("<head>");
        out.println("<title>Screen Capture Servlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<TABLE>");
        out.println("<TR><TD>imageName</TD><TD>imageOSAbsolute</TD><TD>imageWebRelative</TD></TR>");
        out.println("<TR><TD>" + imageName + "</TD><TD>" + imageOSAbsolute + "</TD><TD>" + imageWebRelative + "</TD></TR>");
        out.println("</TABLE>");
        try {
            Robot rob = new Robot();
            BufferedImage bi = rob.createScreenCapture(new Rectangle(0, 0, 1024, 768));
            File imgFile = new File(imageOSAbsolute);
            ImageIO.write(bi, "png", imgFile);
            out.println("<IMG SRC='" + imageWebRelative + "'>");
        } catch (IOException ioe) {
            out.println("IOException: " + ioe.toString());
        } catch (AWTException awte) {
            out.println("AWTException: " + awte.toString());
        } catch (Exception e) {
            out.println("Exception: " + e.toString());
        }
        out.println("</body>");
        out.println("</html>");
    }
