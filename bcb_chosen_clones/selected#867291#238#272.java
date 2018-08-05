    public static void toPDF() throws DocumentException, MalformedURLException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("test.pdf"));
        document.open();
        Paragraph p = new Paragraph();
        p.add(new Chunk("LightAttachment Report\n", new Font(Font.HELVETICA, 22, Font.BOLD)));
        Paragraph p2 = new Paragraph();
        p2.add(new Chunk("Session ", new Font(Font.HELVETICA, 18)));
        p2.add(new Chunk("#454654", new Font(Font.COURIER, 18)));
        p2.add(new Chunk(" on ", new Font(Font.HELVETICA, 18)));
        String host = "";
        Enumeration<NetworkInterface> net = NetworkInterface.getNetworkInterfaces();
        while (net.hasMoreElements()) {
            NetworkInterface ni = net.nextElement();
            if (ni.getDisplayName().equals("localhost")) {
                Enumeration<InetAddress> addr = ni.getInetAddresses();
                host = addr.nextElement().getHostAddress();
            }
        }
        p2.add(new Chunk(host, new Font(Font.COURIER, 18)));
        p.setAlignment(Paragraph.ALIGN_CENTER);
        p2.setAlignment(Paragraph.ALIGN_CENTER);
        com.lowagie.text.Image image = com.lowagie.text.Image.getInstance("session.png");
        image.scalePercent(60);
        com.lowagie.text.Image image2 = com.lowagie.text.Image.getInstance("message.png");
        image2.scalePercent(60);
        com.lowagie.text.Image image3 = com.lowagie.text.Image.getInstance("messages-size.png");
        image3.scalePercent(60);
        document.add(p);
        document.add(p2);
        document.add(image);
        document.add(image2);
        document.add(image3);
        document.close();
    }
