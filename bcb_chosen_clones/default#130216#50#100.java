    public byte[] doTransform(boolean theme) throws Exception {
        DataStore store = DataStore.getInstance();
        String path = "";
        if (theme) {
            String themeDir = store.getProperty("path.theme");
            String userAgent = (String) headers.get("User-Agent");
            if (userAgent != null && userAgent.length() > 0) {
                String[] agents = store.getAgentMappingList();
                for (int x = 0; x < agents.length; x++) {
                    if (userAgent.indexOf(agents[x]) > -1) {
                        themeDir = store.getThemeForAgent(agents[x]);
                    }
                }
            }
            String httpDir = store.getProperty("path.httproot");
            path = httpDir + File.separator + "themes" + File.separator + themeDir + File.separator + "xsl" + File.separator + xslName;
        } else {
            path = store.getProperty("path.xsl") + File.separator + xslName;
        }
        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer xformer = null;
        if (!"1".equalsIgnoreCase(urlData.getParameter("xml"))) {
            File fileIN = new File(path);
            if (fileIN.exists() == false) throw new Exception("XSL file (" + path + ") not found");
            FileInputStream fileStream = new FileInputStream(fileIN);
            Templates template = factory.newTemplates(new StreamSource(fileStream));
            fileStream.close();
            xformer = template.newTransformer();
            buff.write("HTTP/1.0 200 OK\nContent-Type: text/html\n".getBytes());
        } else {
            buff.write("HTTP/1.0 200 OK\nContent-Type: text/xml\n".getBytes());
            xformer = factory.newTransformer();
        }
        String timeStamp = df.format(new Date());
        timeStamp += " GMT";
        buff.write(("Pragma: no-cache\n").getBytes());
        buff.write(("Cache-Control: no-cache\n").getBytes());
        buff.write(("Last-Modified: " + timeStamp + "\n").getBytes());
        buff.write(("Date: " + timeStamp + "\n").getBytes());
        buff.write(("Expires: " + timeStamp + "\n").getBytes());
        for (int x = 0; x < cookies.size(); x++) {
            String cook = (String) cookies.get(x);
            buff.write(("Set-Cookie: " + cook + "\n").getBytes());
        }
        buff.write("\n".getBytes());
        Source source = new DOMSource(document);
        Result result = new StreamResult(buff);
        xformer.transform(source, result);
        return buff.toByteArray();
    }
