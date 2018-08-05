    public HTMLDocument handleURL(String suburl, HTTPSession session, HTTPRequestHeader header) throws WebMailException {
        if (session == null) {
            String empty = "<USERMODEL><USERDATA><FULL_NAME>User</FULL_NAME></USERDATA><STATEDATA><VAR name=\"img base uri\" value=\"/webmail/lib/templates/en/bibop/\"/><VAR name=\"base uri\" value=\"/webmail/WebMail/\"/></STATEDATA></USERMODEL>";
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document emptyUser = null;
            try {
                emptyUser = factory.newDocumentBuilder().parse(new InputSource(new StringReader(empty)));
            } catch (ParserConfigurationException pce) {
            } catch (SAXException saxe) {
            } catch (IOException ioe) {
            }
            HTMLDocument content = new XHTMLDocument(emptyUser, store.getStylesheet("logout.xsl", parent.getDefaultLocale(), parent.getDefaultTheme()));
            return content;
        }
        UserData user = ((WebMailSession) session).getUser();
        HTMLDocument content = new XHTMLDocument(session.getModel(), store.getStylesheet("logout.xsl", user.getPreferredLocale(), user.getTheme()));
        if (!session.isLoggedOut()) {
            session.logout();
        }
        return content;
    }
