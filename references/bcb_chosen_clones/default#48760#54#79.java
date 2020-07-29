    public HTMLDocument handleURL(String suburl, HTTPSession session, HTTPRequestHeader header) throws WebMailException {
        UserData user = ((WebMailSession) session).getUser();
        Document helpdoc = (Document) cache.get(user.getPreferredLocale().getLanguage() + "/" + user.getTheme());
        if (helpdoc == null) {
            String helpdocpath = "file://" + store.getBasePath(user.getPreferredLocale(), user.getTheme()) + "help.xml";
            try {
                DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                helpdoc = parser.parse(helpdocpath);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new WebMailException("Could not parse " + helpdocpath);
            }
            cache.put(user.getPreferredLocale().getLanguage() + "/" + user.getTheme(), helpdoc);
        }
        Node n = session.getModel().importNode(helpdoc.getDocumentElement(), true);
        session.getModel().getDocumentElement().appendChild(n);
        if (header.isContentSet("helptopic") && session instanceof WebMailSession) {
            ((WebMailSession) session).getUserModel().setStateVar("helptopic", header.getContent("helptopic"));
        }
        HTMLDocument retdoc = new XHTMLDocument(session.getModel(), store.getStylesheet("help.xsl", user.getPreferredLocale(), user.getTheme()));
        session.getModel().getDocumentElement().removeChild(n);
        if (header.isContentSet("helptopic") && session instanceof WebMailSession) {
            ((WebMailSession) session).getUserModel().removeAllStateVars("helptopic");
        }
        return retdoc;
    }
