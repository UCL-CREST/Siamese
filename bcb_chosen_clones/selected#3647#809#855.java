    public String copyImages(Document doc, String sXML, String newPath, String tagName, String itemName) {
        NodeList nl = null;
        Node n = null;
        NamedNodeMap nnp = null;
        Node nsrc = null;
        URL url = null;
        String sFilename = "";
        String sNewPath = "";
        int index;
        String sOldPath = "";
        try {
            nl = doc.getElementsByTagName(tagName);
            for (int i = 0; i < nl.getLength(); i++) {
                n = nl.item(i);
                nnp = n.getAttributes();
                nsrc = nnp.getNamedItem(itemName);
                String sTemp = nsrc.getTextContent();
                url = new URL("file", "localhost", sTemp);
                sOldPath = url.getPath();
                sOldPath = sOldPath.replace('/', File.separatorChar);
                int indexFirstSlash = sOldPath.indexOf(File.separatorChar);
                String sSourcePath;
                if (itemName.equals("data")) sSourcePath = sOldPath; else sSourcePath = sOldPath.substring(indexFirstSlash + 1);
                index = sOldPath.lastIndexOf(File.separatorChar);
                sFilename = sOldPath.substring(index + 1);
                sNewPath = newPath + sFilename;
                FileChannel in = null;
                FileChannel out = null;
                try {
                    in = new FileInputStream(sSourcePath).getChannel();
                    out = new FileOutputStream(sNewPath).getChannel();
                    long size = in.size();
                    MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);
                    out.write(buf);
                } finally {
                    if (in != null) in.close();
                    if (out != null) out.close();
                }
                sXML = sXML.replace(nsrc.getTextContent(), sFilename);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sXML;
    }
