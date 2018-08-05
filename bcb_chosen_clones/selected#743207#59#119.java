    private void createWar() throws IOException, XMLStreamException {
        String appName = this.fileout.getName();
        int i = appName.indexOf(".");
        if (i != -1) appName = appName.substring(0, i);
        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(this.fileout));
        {
            ZipEntry entry = new ZipEntry("WEB-INF/web.xml");
            zout.putNextEntry(entry);
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLStreamWriter w = factory.createXMLStreamWriter(zout, "ASCII");
            w.writeStartDocument("ASCII", "1.0");
            w.writeStartElement("web-app");
            w.writeAttribute("xsi", XSI, "schemaLocation", "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml /ns/javaee/web-app_2_5.xsd");
            w.writeAttribute("version", "2.5");
            w.writeAttribute("xmlns", J2EE);
            w.writeAttribute("xmlns:xsi", XSI);
            w.writeStartElement("description");
            w.writeCharacters("Site maintenance for " + appName);
            w.writeEndElement();
            w.writeStartElement("display-name");
            w.writeCharacters(appName);
            w.writeEndElement();
            w.writeStartElement("servlet");
            w.writeStartElement("servlet-name");
            w.writeCharacters("down");
            w.writeEndElement();
            w.writeStartElement("jsp-file");
            w.writeCharacters("/WEB-INF/jsp/down.jsp");
            w.writeEndElement();
            w.writeEndElement();
            w.writeStartElement("servlet-mapping");
            w.writeStartElement("servlet-name");
            w.writeCharacters("down");
            w.writeEndElement();
            w.writeStartElement("url-pattern");
            w.writeCharacters("/*");
            w.writeEndElement();
            w.writeEndElement();
            w.writeEndElement();
            w.writeEndDocument();
            w.flush();
            zout.closeEntry();
        }
        {
            ZipEntry entry = new ZipEntry("WEB-INF/jsp/down.jsp");
            zout.putNextEntry(entry);
            PrintWriter w = new PrintWriter(zout);
            if (this.messageFile != null) {
                IOUtils.copyTo(new FileReader(this.messageFile), w);
            } else if (this.messageString != null) {
                w.print("<html><body>" + this.messageString + "</body></html>");
            } else {
                w.print("<html><body><div style='text-align:center;font-size:500%;'>Oh No !<br/><b>" + appName + "</b><br/>is down for maintenance!</div></body></html>");
            }
            w.flush();
            zout.closeEntry();
        }
        zout.finish();
        zout.flush();
        zout.close();
    }
