    public void createZipFile(String zipFileName, String directoryName) throws IOException {
        String directoryNameWithSlash = directoryName + "/";
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(zipFileName));
        zip.putNextEntry(new ZipEntry(directoryNameWithSlash));
        for (Map.Entry<OWLEntity, Set<ACESnippet>> entry : acetext.getEntitySnippetSetPairs()) {
            OWLEntity entity = entry.getKey();
            ACELexiconEntry lexiconEntry = lexicon.getEntry(entity);
            if (lexiconEntry == null || lexiconEntry.isPartial()) {
                continue;
            }
            zip.putNextEntry(new ZipEntry(directoryNameWithSlash + getLemmaIndex(entity)));
            String str = getAceWikiEntry(entity, lexiconEntry, entry.getValue());
            zip.write(str.getBytes(), 0, str.length());
            zip.closeEntry();
        }
        zip.closeEntry();
        zip.close();
    }
