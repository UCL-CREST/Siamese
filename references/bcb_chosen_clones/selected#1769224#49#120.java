    public OperatorDescription deserialize(File file) throws SAXException, ParserConfigurationException, IOException, XMLParserException {
        Charset charset = Charset.forName("ISO-8859-15");
        CharsetDecoder decoder = charset.newDecoder();
        Pattern linePattern = Pattern.compile(".*\r?\n");
        Pattern propertiesOpenTag = Pattern.compile("<ohua:operator-properties>");
        Pattern propertiesEndTag = Pattern.compile("</ohua:operator-properties>");
        FileInputStream opDescriptorFile = new FileInputStream(file);
        FileChannel opDescriptorChannel = opDescriptorFile.getChannel();
        MappedByteBuffer mappedByteBuffer = opDescriptorChannel.map(FileChannel.MapMode.READ_ONLY, 0, (int) opDescriptorChannel.size());
        CharBuffer cb = decoder.decode(mappedByteBuffer);
        Matcher lm = linePattern.matcher(cb);
        Matcher pm = null;
        int startIndexCastorContent = 0;
        int endIndexCastorContent = 0;
        int dataBytesSeen = 0;
        int lines = 0;
        boolean castorContent = false;
        while (lm.find()) {
            lines++;
            CharSequence cs = lm.group();
            if (pm == null) {
                pm = propertiesOpenTag.matcher(cs);
            } else {
                pm.reset(cs);
            }
            if (pm.find()) {
                if (!castorContent) {
                    castorContent = true;
                    dataBytesSeen += pm.end();
                    startIndexCastorContent = dataBytesSeen;
                    dataBytesSeen = 0;
                    pm = propertiesEndTag.matcher(cs);
                } else {
                    dataBytesSeen += pm.start() - 1;
                    endIndexCastorContent = startIndexCastorContent + dataBytesSeen;
                    break;
                }
            } else {
                dataBytesSeen += cs.length();
            }
            if (lm.end() == cb.limit()) {
                _logger.fine("No properties found in operator descriptor " + file.getName());
                break;
            }
        }
        _logger.fine("part 1: " + cb.subSequence(0, startIndexCastorContent));
        _logger.fine("part 2: " + cb.subSequence(startIndexCastorContent, endIndexCastorContent));
        _logger.fine("part 3: " + cb.subSequence(endIndexCastorContent, cb.length()));
        ByteBuffer xmlReaderBuffer = ByteBuffer.allocate(cb.length() - (endIndexCastorContent - startIndexCastorContent));
        ByteBuffer castorBuffer = ByteBuffer.allocate(endIndexCastorContent - startIndexCastorContent);
        mappedByteBuffer.position(0);
        mappedByteBuffer.get(xmlReaderBuffer.array(), 0, startIndexCastorContent);
        mappedByteBuffer.position(startIndexCastorContent);
        mappedByteBuffer.get(castorBuffer.array(), 0, endIndexCastorContent - startIndexCastorContent);
        mappedByteBuffer.position(endIndexCastorContent);
        mappedByteBuffer.get(xmlReaderBuffer.array(), startIndexCastorContent, cb.length() - endIndexCastorContent);
        opDescriptorChannel.close();
        ByteArrayInputStream xmlReaderInputStream = new ByteArrayInputStream(xmlReaderBuffer.array());
        ByteArrayInputStream castorInputStream = new ByteArrayInputStream(castorBuffer.array());
        XMLReader xmlReader = OhuaXMLParserFactory.getInstance().createXMLReader();
        xmlReader.setContentHandler(_opDescParser);
        xmlReader.parse(new InputSource(xmlReaderInputStream));
        OperatorDescription description = _opDescParser.getParsedOperatorDescription();
        if (castorInputStream.available() > 0) {
            Mapping propertiesMapping = new Mapping();
            description.setPropertiesMapping(propertiesMapping);
            propertiesMapping.loadMapping(new InputSource(castorInputStream));
            castorInputStream.close();
        }
        xmlReaderInputStream.close();
        return description;
    }
