    public AssessmentItemType getAssessmentItemType(String filename) {
        if (filename.contains(" ") && (System.getProperty("os.name").contains("Windows"))) {
            File source = new File(filename);
            String tempDir = System.getenv("TEMP");
            File dest = new File(tempDir + "/temp.xml");
            MQMain.logger.info("Importing from " + dest.getAbsolutePath());
            FileChannel in = null, out = null;
            try {
                in = new FileInputStream(source).getChannel();
                out = new FileOutputStream(dest).getChannel();
                long size = in.size();
                MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);
                out.write(buf);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (out != null) try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                filename = tempDir + "/temp.xml";
            }
        }
        AssessmentItemType assessmentItemType = null;
        JAXBElement<?> jaxbe = null;
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            ChangeNamespace convertfromv2p0tov2p1 = new ChangeNamespace(reader, "http://www.imsglobal.org/xsd/imsqti_v2p0", "http://www.imsglobal.org/xsd/imsqti_v2p1");
            SAXSource source = null;
            try {
                FileInputStream fis = new FileInputStream(filename);
                InputStreamReader isr = null;
                try {
                    isr = new InputStreamReader(fis, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                }
                InputSource is = new InputSource(isr);
                source = new SAXSource(convertfromv2p0tov2p1, is);
            } catch (FileNotFoundException e) {
                MQMain.logger.error("SAX/getAssessmentItemType/file not found");
            }
            jaxbe = (JAXBElement<?>) MQModel.qtiCf.unmarshal(MQModel.imsqtiUnmarshaller, source);
            assessmentItemType = (AssessmentItemType) jaxbe.getValue();
        } catch (JAXBException e) {
            MQMain.logger.error("JAX/getAssessmentItemType", e);
        } catch (SAXException e) {
            MQMain.logger.error("SAX/getAssessmentItemType", e);
        }
        return assessmentItemType;
    }
