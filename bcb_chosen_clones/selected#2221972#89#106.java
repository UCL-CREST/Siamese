    private void createWikiPages(WikiContext context) throws PluginException {
        OntologyWikiPageName owpn = new OntologyWikiPageName(omemo.getFormDataAlias().toUpperCase(), omemo.getFormDataVersionDate());
        String wikiPageFullFileName = WikiPageName2FullFileName(context, owpn.toString());
        String rdfFileNameWithPath = getWorkDir(context) + File.separator + owpn.toFileName();
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            fos = new FileOutputStream(wikiPageFullFileName);
            fis = new FileInputStream(rdfFileNameWithPath);
            InfoExtractor infoe = new InfoExtractor(fis, omemo.getFormDataNS(), omemo.getFormDataOntLang());
            infoe.writePage(getWorkDir(context), owpn, Omemo.checksWikiPageName);
            fis.close();
            fos.close();
        } catch (Exception e) {
            log.error("Can not read local rdf file or can not write wiki page");
            throw new PluginException("Error creating wiki pages. See logs");
        }
    }
