    public FileInputStream execute() {
        FacesContext faces = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
        String pdfPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/pdf");
        try {
            FileOutputStream outputStream = new FileOutputStream(pdfPath + "/driveTogether.pdf");
            PdfWriter writer = PdfWriter.getInstance(doc, outputStream);
            doc.open();
            String pfad = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/pdf/template.pdf");
            logger.info("Loading PDF-Template: " + pfad);
            PdfReader reader = new PdfReader(pfad);
            PdfImportedPage page = writer.getImportedPage(reader, 1);
            PdfContentByte cb = writer.getDirectContent();
            cb.addTemplate(page, 0, 0);
            doHeader();
            doParagraph(trip, forUser);
            doc.close();
            fis = new FileInputStream(pdfPath + "/driveTogether.pdf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fis;
    }
