    public void buildSiteForEdit(String sBasePath, String sOutputPath, String sCtrlPath, String sMenuPath, String sIntegradorPath, String sSelPageOptions, Properties oEnvironmentProps, Properties oUserProps) throws IOException, DOMException, TransformerException, TransformerConfigurationException, MalformedURLException {
        Transformer oTransformer;
        StreamResult oStreamResult;
        StreamSource oStreamSrcXML;
        StringWriter oStrWritter;
        InputStream oXMLStream = null;
        String sTransformed;
        StringBuffer oPostTransform;
        String sKey;
        String sMedia;
        Object sVal;
        Page oCurrentPage;
        int iCloseHead, iOpenBody, iCloseBody;
        int iReaded;
        char CharBuffer[] = new char[8192];
        String sCharBuffer;
        long lElapsed = 0;
        final String sSep = System.getProperty("file.separator");
        if (DebugFile.trace) {
            lElapsed = System.currentTimeMillis();
            DebugFile.writeln("Begin Pageset.buildSiteForEdit(" + sBasePath + "," + sOutputPath + "," + sCtrlPath + "," + sMenuPath + ")");
            DebugFile.incIdent();
        }
        FileSystem oFS = new FileSystem();
        Vector vPages = pages();
        if (!sBasePath.endsWith(sSep)) sBasePath += sSep;
        String sWebServer = oEnvironmentProps.getProperty("webserver", "");
        if (DebugFile.trace && sWebServer.length() == 0) DebugFile.writeln("WARNING: webserver property not set at EnvironmentProperties");
        if (!sWebServer.endsWith("/")) sWebServer += "/";
        Node oContainers = oMSite.seekChildByName(oMSite.getRootNode().getFirstChild(), "containers");
        if (oContainers == null) {
            if (DebugFile.trace) DebugFile.writeln("ERROR: <containers> node not found.");
            throw new DOMException(DOMException.NOT_FOUND_ERR, "<containers> node not found");
        }
        if (DebugFile.trace) DebugFile.writeln("new FileInputStream(" + (sURI.startsWith("file://") ? sURI.substring(7) : sURI) + ")");
        for (int c = 0; c < vPages.size(); c++) {
            oCurrentPage = (Page) vPages.get(c);
            oXMLStream = new FileInputStream(sURI.startsWith("file://") ? sURI.substring(7) : sURI);
            oStreamSrcXML = new StreamSource(oXMLStream);
            oStrWritter = new StringWriter();
            oStreamResult = new StreamResult(oStrWritter);
            try {
                oTransformer = StylesheetCache.newTransformer(sBasePath + "xslt" + sSep + "templates" + sSep + oMSite.name() + sSep + oCurrentPage.template());
                sMedia = oTransformer.getOutputProperty(OutputKeys.MEDIA_TYPE);
                if (DebugFile.trace) DebugFile.writeln(OutputKeys.MEDIA_TYPE + "=" + sMedia);
                if (null == sMedia) sMedia = "html"; else sMedia = sMedia.substring(sMedia.indexOf('/') + 1);
                if (null == oCurrentPage.getTitle()) throw new NullPointerException("Page " + String.valueOf(c) + " title is null");
                if (DebugFile.trace) DebugFile.writeln("Page.filePath(" + sOutputPath + oCurrentPage.getTitle().replace(' ', '_') + "." + sMedia + ")");
                oCurrentPage.filePath(sOutputPath + oCurrentPage.getTitle().replace(' ', '_') + "." + sMedia);
                StylesheetCache.setParameters(oTransformer, oEnvironmentProps);
                StylesheetCache.setParameters(oTransformer, oUserProps);
                oTransformer.setParameter("param_page", ((Page) (vPages.get(c))).getTitle());
                oTransformer.transform(oStreamSrcXML, oStreamResult);
            } catch (TransformerConfigurationException e) {
                oLastXcpt = e;
                sMedia = null;
                SourceLocator sl = e.getLocator();
                if (DebugFile.trace) {
                    if (sl == null) {
                        DebugFile.writeln("ERROR TransformerConfigurationException " + e.getMessage());
                    } else {
                        DebugFile.writeln("ERROR TransformerConfigurationException " + e.getMessage() + " line=" + String.valueOf(sl.getLineNumber()) + " column=" + String.valueOf(sl.getColumnNumber()));
                    }
                }
            } catch (TransformerException e) {
                oLastXcpt = e;
                sMedia = null;
                if (DebugFile.trace) DebugFile.writeln("ERROR TransformerException " + e.getMessageAndLocation());
            }
            oTransformer = null;
            oStreamResult = null;
            sTransformed = oStrWritter.toString();
            if (DebugFile.trace) DebugFile.writeln("transformation length=" + String.valueOf(sTransformed.length()));
            if (sTransformed.length() > 0) {
                iCloseHead = sTransformed.indexOf("</head");
                if (iCloseHead < 0) iCloseHead = sTransformed.indexOf("</HEAD");
                iOpenBody = sTransformed.indexOf("<body", iCloseHead);
                if (iOpenBody < 0) iOpenBody = sTransformed.indexOf("<BODY", iCloseHead);
                iCloseBody = sTransformed.indexOf(">", iOpenBody + 5);
                for (char s = sTransformed.charAt(iCloseBody + 1); s == '\r' || s == '\n' || s == ' ' || s == '\t'; s = sTransformed.charAt(++iCloseBody)) ;
                oPostTransform = new StringBuffer(sTransformed.length() + 4096);
                oPostTransform.append(sTransformed.substring(0, iCloseHead));
                oPostTransform.append("\n<script language=\"JavaScript\" src=\"" + sMenuPath + "\"></script>");
                oPostTransform.append("\n<script language=\"JavaScript\" src=\"" + sIntegradorPath + "\"></script>\n");
                oPostTransform.append(sTransformed.substring(iCloseHead, iCloseHead + 7));
                oPostTransform.append(sTransformed.substring(iOpenBody, iCloseBody));
                try {
                    sCharBuffer = oFS.readfilestr(sCtrlPath, "UTF-8");
                    if (DebugFile.trace) DebugFile.writeln(String.valueOf(sCharBuffer.length()) + " characters readed");
                } catch (com.enterprisedt.net.ftp.FTPException ftpe) {
                    throw new IOException(ftpe.getMessage());
                }
                try {
                    if (DebugFile.trace) DebugFile.writeln("Gadgets.replace(" + sCtrlPath + ",http://demo.hipergate.com/," + sWebServer + ")");
                    Gadgets.replace(sCharBuffer, "http://demo.hipergate.com/", sWebServer);
                } catch (org.apache.oro.text.regex.MalformedPatternException e) {
                }
                oPostTransform.append("<!--Begin " + sCtrlPath + "-->\n");
                oPostTransform.append(sCharBuffer);
                sCharBuffer = null;
                oPostTransform.append("\n<!--End " + sCtrlPath + "-->\n");
                oPostTransform.append(sTransformed.substring(iCloseBody));
            } else {
                oPostTransform = new StringBuffer("Page " + ((Page) vPages.get(c)).getTitle() + " could not be rendered.");
                if (oLastXcpt != null) oPostTransform.append("<BR>" + oLastXcpt.getMessageAndLocation());
            }
            if (DebugFile.trace) DebugFile.writeln("new FileWriter(" + sOutputPath + oCurrentPage.getTitle().replace(' ', '_') + "_." + sMedia + ")");
            if (sSelPageOptions.length() == 0) oFS.writefilestr(sOutputPath + oCurrentPage.getTitle().replace(' ', '_') + "_." + sMedia, oPostTransform.toString(), "UTF-8"); else try {
                oFS.writefilestr(sOutputPath + oCurrentPage.getTitle().replace(' ', '_') + "_." + sMedia, Gadgets.replace(oPostTransform.toString(), ":selPageOptions", sSelPageOptions), "UTF-8");
            } catch (Exception e) {
            }
            oPostTransform = null;
            sTransformed = null;
        }
        oXMLStream.close();
        if (DebugFile.trace) {
            DebugFile.writeln("done in " + String.valueOf(System.currentTimeMillis() - lElapsed) + " miliseconds");
            DebugFile.decIdent();
            DebugFile.writeln("End Pageset.buildSiteForEdit()");
        }
    }
