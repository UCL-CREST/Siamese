    public void objectParserAssesmentItem(int file, int currentquestion, Resource resTemp) {
        NodeList nl = null;
        Node n = null;
        NamedNodeMap nnp = null;
        Node nsrc = null;
        URL url = null;
        String sFilename = "";
        String sNewPath = "";
        int indexLastSeparator;
        String sOldPath = "";
        try {
            if (file == 1) {
                nl = doc.getElementsByTagName("object");
            } else {
                nl = doc_[currentquestion].getElementsByTagName("object");
            }
            for (int i = 0; i < nl.getLength(); i++) {
                n = nl.item(i);
                nnp = n.getAttributes();
                nsrc = nnp.getNamedItem("data");
                String sTemp = nsrc.getTextContent();
                url = new URL("file", "localhost", sTemp);
                sOldPath = url.getFile();
                sOldPath = sOldPath.replace('/', File.separatorChar);
                indexLastSeparator = sOldPath.lastIndexOf(File.separatorChar);
                String sSourcePath = sOldPath;
                sFilename = sOldPath.substring(indexLastSeparator + 1);
                sNewPath = this.sTempLocation + sFilename;
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
                if (file == 1) {
                    sXml = sXml.replace(nsrc.getTextContent(), sFilename);
                } else {
                    sXml_[currentquestion] = sXml_[currentquestion].replace(nsrc.getTextContent(), sFilename);
                }
                lsImages.add(sFilename);
                resTemp.addFile(sFilename);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
