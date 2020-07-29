    private static Collection<String> createTopLevelFiles(Configuration configuration, Collections collections, Sets sets) throws FlickrException, SAXException, IOException, JDOMException, TransformerException {
        Collection<String> createdFiles = new HashSet<String>();
        File toplevelXmlFilename = getToplevelXmlFilename(configuration.photosBaseDirectory);
        Logger.getLogger(FlickrDownload.class).info("Creating XML file " + toplevelXmlFilename.getAbsolutePath());
        MediaIndexer indexer = new XmlMediaIndexer(configuration);
        Element toplevel = new Element("flickr").addContent(XmlUtils.createApplicationXml()).addContent(XmlUtils.createUserXml(configuration)).addContent(collections.createTopLevelXml()).addContent(sets.createTopLevelXml()).addContent(new Stats(sets).createStatsXml(indexer));
        createdFiles.addAll(indexer.writeIndex());
        XmlUtils.outputXmlFile(toplevelXmlFilename, toplevel);
        createdFiles.add(toplevelXmlFilename.getName());
        Logger.getLogger(FlickrDownload.class).info("Copying support files and performing XSLT transformations");
        IOUtils.copyToFileAndCloseStreams(XmlUtils.class.getResourceAsStream("xslt/" + PHOTOS_CSS_FILENAME), new File(configuration.photosBaseDirectory, PHOTOS_CSS_FILENAME));
        createdFiles.add(PHOTOS_CSS_FILENAME);
        IOUtils.copyToFileAndCloseStreams(XmlUtils.class.getResourceAsStream("xslt/" + PLAY_ICON_FILENAME), new File(configuration.photosBaseDirectory, PLAY_ICON_FILENAME));
        createdFiles.add(PLAY_ICON_FILENAME);
        XmlUtils.performXsltTransformation(configuration, "all_sets.xsl", toplevelXmlFilename, new File(configuration.photosBaseDirectory, ALL_SETS_HTML_FILENAME));
        createdFiles.add(ALL_SETS_HTML_FILENAME);
        XmlUtils.performXsltTransformation(configuration, "all_collections.xsl", toplevelXmlFilename, new File(configuration.photosBaseDirectory, ALL_COLLECTIONS_HTML_FILENAME));
        createdFiles.add(ALL_COLLECTIONS_HTML_FILENAME);
        createdFiles.add(Collections.COLLECTIONS_ICON_DIRECTORY);
        XmlUtils.performXsltTransformation(configuration, "stats.xsl", toplevelXmlFilename, new File(configuration.photosBaseDirectory, STATS_HTML_FILENAME));
        createdFiles.add(STATS_HTML_FILENAME);
        sets.performXsltTransformation();
        for (AbstractSet set : sets.getSets()) {
            createdFiles.add(set.getSetId());
        }
        return createdFiles;
    }
