    public static String getFile(String serviceName, String wsdlLocation, String endpoint) throws AxisFault {
        mLog.debug("Downloading WSDL file from: " + wsdlLocation);
        mLog.debug("Received endpoint: " + endpoint);
        String fileLocation = null;
        try {
            String tempDir = System.getProperty("java.io.tmpdir");
            URL url = new URL(wsdlLocation);
            String WSDLFile = tempDir + File.separator + serviceName + ".wsdl";
            String tmpWSDLFile = WSDLFile + ".tmp";
            File inputFile = new File(WSDLFile);
            File tmpFile = new File(tmpWSDLFile);
            if (!inputFile.exists() || inputFile.length() == 0) {
                mLog.debug("Downloading the WSDL");
                inputFile.createNewFile();
                InputStream in = url.openStream();
                FileOutputStream out = new FileOutputStream(inputFile);
                URLConnection con = url.openConnection();
                int fileLength = con.getContentLength();
                ReadableByteChannel channelIn = Channels.newChannel(in);
                FileChannel channelOut = out.getChannel();
                channelOut.transferFrom(channelIn, 0, fileLength);
                channelIn.close();
                channelOut.close();
                out.flush();
                out.close();
                in.close();
                Document tmpDocument = XMLUtils.newDocument(new FileInputStream(inputFile));
                NodeList nl1 = tmpDocument.getElementsByTagName("wsdlsoap:address");
                for (int i = 0; i < nl1.getLength(); i++) {
                    Node node1 = nl1.item(i);
                    if (node1.getNodeName().equals("wsdlsoap:address")) {
                        ((Element) node1).setAttribute("location", endpoint);
                    }
                }
                FileOutputStream tmpOut = new FileOutputStream(tmpFile);
                XMLUtils.DocumentToStream(tmpDocument, tmpOut);
                tmpOut.flush();
                tmpOut.close();
                boolean retVal = inputFile.delete();
                if (retVal) {
                    retVal = tmpFile.renameTo(new File(WSDLFile));
                }
                mLog.debug("Return Value: " + retVal);
            } else {
                mLog.debug("The WSDL is already at the ServiceProvider");
            }
            fileLocation = WSDLFile;
        } catch (MalformedURLException mx) {
            mLog.error("MalformedURLException: " + mx.getMessage() + ", cause: " + mx.getCause().getMessage());
            throw new AxisFault(mx.getMessage(), mx.getCause());
        } catch (IOException ix) {
            mLog.error("IOException: " + ix.getMessage() + ", cause: " + ix.getCause().getMessage());
            throw new AxisFault(ix.getMessage(), ix.getCause());
        } catch (ParserConfigurationException px) {
            mLog.error("ParserConfigurationException: " + px.getMessage() + ", cause: " + px.getCause().getMessage());
            throw new AxisFault(px.getMessage(), px.getCause());
        } catch (SAXException sx) {
            mLog.error("SAXException: " + sx.getMessage() + ", cause: " + sx.getCause().getMessage());
            throw new AxisFault(sx.getMessage(), sx.getCause());
        }
        return fileLocation;
    }
