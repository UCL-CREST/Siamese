    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final String id = request.getParameter("id");
        if (id == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        try {
            jaxrTemplate.execute(new JAXRCallback<Object>() {

                public Object execute(Connection connection) throws JAXRException {
                    RegistryObject registryObject = connection.getRegistryService().getBusinessQueryManager().getRegistryObject(id);
                    if (registryObject instanceof ExtrinsicObject) {
                        ExtrinsicObject extrinsicObject = (ExtrinsicObject) registryObject;
                        DataHandler dataHandler = extrinsicObject.getRepositoryItem();
                        if (dataHandler != null) {
                            response.setContentType("text/html");
                            try {
                                PrintWriter out = response.getWriter();
                                InputStream is = dataHandler.getInputStream();
                                try {
                                    final XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(out);
                                    xmlStreamWriter.writeStartDocument();
                                    xmlStreamWriter.writeStartElement("div");
                                    xmlStreamWriter.writeStartElement("textarea");
                                    xmlStreamWriter.writeAttribute("name", "repositoryItem");
                                    xmlStreamWriter.writeAttribute("class", "xml");
                                    xmlStreamWriter.writeAttribute("style", "display:none");
                                    IOUtils.copy(new XmlInputStreamReader(is), new XmlStreamTextWriter(xmlStreamWriter));
                                    xmlStreamWriter.writeEndElement();
                                    xmlStreamWriter.writeStartElement("script");
                                    xmlStreamWriter.writeAttribute("class", "javascript");
                                    xmlStreamWriter.writeCharacters("dp.SyntaxHighlighter.HighlightAll('repositoryItem');");
                                    xmlStreamWriter.writeEndElement();
                                    xmlStreamWriter.writeEndElement();
                                    xmlStreamWriter.writeEndDocument();
                                    xmlStreamWriter.flush();
                                } finally {
                                    is.close();
                                }
                            } catch (Throwable ex) {
                                log.error("Error while trying to format repository item " + id, ex);
                            }
                        } else {
                        }
                    } else {
                    }
                    return null;
                }
            });
        } catch (JAXRException ex) {
            throw new ServletException(ex);
        }
        return null;
    }
