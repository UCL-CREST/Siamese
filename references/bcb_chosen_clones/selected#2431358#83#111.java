    private WikiSiteContentInfo createInfoIndexSite(Long domainId) {
        final UserInfo user = getSecurityService().getCurrentUser();
        final Locale locale = new Locale(user.getLocale());
        final String country = locale.getLanguage();
        InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("wiki_index_" + country + ".xhtml");
        if (inStream == null) {
            inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("wiki_index.xhtml");
        }
        if (inStream == null) {
            inStream = new ByteArrayInputStream(DEFAULT_WIKI_INDEX_SITE_TEXT.getBytes());
        }
        if (inStream != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                IOUtils.copyLarge(inStream, out);
                return createIndexVersion(domainId, out.toString(), user);
            } catch (IOException exception) {
                LOGGER.error("Error creating info page.", exception);
            } finally {
                try {
                    inStream.close();
                    out.close();
                } catch (IOException exception) {
                    LOGGER.error("Error reading wiki_index.xhtml", exception);
                }
            }
        }
        return null;
    }
