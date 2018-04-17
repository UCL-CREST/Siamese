    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String presentationtype = request.getParameter("presentationtype");
        Document document = new Document();
        try {
            if ("pdf".equals(presentationtype)) {
                response.setContentType("application/pdf");
                PdfWriter.getInstance(document, response.getOutputStream());
            } else if ("html".equals(presentationtype)) {
                response.setContentType("text/html");
                HtmlWriter.getInstance(document, response.getOutputStream());
            } else {
                response.sendRedirect("http://www.lowagie.com/iText/tutorial/ch01.html#step2");
            }
            document.open();
            document.add(new Paragraph(new Date().toString()));
        } catch (DocumentException de) {
            de.printStackTrace();
            System.err.println("document: " + de.getMessage());
        }
        document.close();
    }
