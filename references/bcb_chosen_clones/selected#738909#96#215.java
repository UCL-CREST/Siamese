    private void _PostParser(Document document, AnnotationManager annoMan, Document htmldoc, String baseurl) {
        xformer = annoMan.getTransformer();
        builder = annoMan.getBuilder();
        String annohash = "";
        if (document == null) return;
        NodeList ndlist = document.getElementsByTagNameNS(annoNS, "body");
        if (ndlist.getLength() != 1) {
            System.out.println("Sorry Annotation Body was found " + ndlist.getLength() + " times");
            return;
        }
        Element bodynode = (Element) ndlist.item(0);
        Node htmlNode = bodynode.getElementsByTagName("html").item(0);
        if (htmlNode == null) htmlNode = bodynode.getElementsByTagName("HTML").item(0);
        Document newdoc = builder.newDocument();
        Element rootelem = newdoc.createElementNS(rdfNS, "r:RDF");
        rootelem.setAttribute("xmlns:r", rdfNS);
        rootelem.setAttribute("xmlns:a", annoNS);
        rootelem.setAttribute("xmlns:d", dubNS);
        rootelem.setAttribute("xmlns:t", threadNS);
        newdoc.appendChild(rootelem);
        Element tmpelem;
        NodeList tmpndlist;
        Element annoElem = newdoc.createElementNS(annoNS, "a:Annotation");
        rootelem.appendChild(annoElem);
        tmpelem = (Element) document.getElementsByTagNameNS(annoNS, "context").item(0);
        String context = tmpelem.getChildNodes().item(0).getNodeValue();
        annoElem.setAttributeNS(annoNS, "a:context", context);
        NodeList elemcontl = tmpelem.getElementsByTagNameNS(alNS, "context-element");
        Node ncontext_element = null;
        if (elemcontl.getLength() > 0) {
            Node old_context_element = elemcontl.item(0);
            ncontext_element = newdoc.importNode(old_context_element, true);
        }
        tmpndlist = document.getElementsByTagNameNS(dubNS, "title");
        annoElem.setAttributeNS(dubNS, "d:title", tmpndlist.getLength() > 0 ? tmpndlist.item(0).getChildNodes().item(0).getNodeValue() : "Default");
        tmpelem = (Element) document.getElementsByTagNameNS(dubNS, "creator").item(0);
        annoElem.setAttributeNS(dubNS, "d:creator", tmpelem.getChildNodes().item(0).getNodeValue());
        tmpelem = (Element) document.getElementsByTagNameNS(annoNS, "created").item(0);
        annoElem.setAttributeNS(annoNS, "a:created", tmpelem.getChildNodes().item(0).getNodeValue());
        tmpelem = (Element) document.getElementsByTagNameNS(dubNS, "date").item(0);
        annoElem.setAttributeNS(dubNS, "d:date", tmpelem.getChildNodes().item(0).getNodeValue());
        tmpndlist = document.getElementsByTagNameNS(dubNS, "language");
        String language = (tmpndlist.getLength() > 0 ? tmpndlist.item(0).getChildNodes().item(0).getNodeValue() : "en");
        annoElem.setAttributeNS(dubNS, "d:language", language);
        Node typen = newdoc.importNode(document.getElementsByTagNameNS(rdfNS, "type").item(0), true);
        annoElem.appendChild(typen);
        Element contextn = newdoc.createElementNS(annoNS, "a:context");
        contextn.setAttributeNS(rdfNS, "r:resource", context);
        annoElem.appendChild(contextn);
        Node annotatesn = newdoc.importNode(document.getElementsByTagNameNS(annoNS, "annotates").item(0), true);
        annoElem.appendChild(annotatesn);
        Element newbodynode = newdoc.createElementNS(annoNS, "a:body");
        annoElem.appendChild(newbodynode);
        if (ncontext_element != null) {
            contextn.appendChild(ncontext_element);
        } else {
            System.out.println("No context element found, we create one...");
            try {
                XPointer xptr = new XPointer(htmldoc);
                NodeRange xprange = xptr.getRange(context, htmldoc);
                Element context_elem = newdoc.createElementNS(alNS, "al:context-element");
                context_elem.setAttributeNS(alNS, "al:text", xprange.getContentString());
                context_elem.appendChild(newdoc.createTextNode(annoMan.generateContextString(xprange)));
                contextn.appendChild(context_elem);
            } catch (XPointerRangeException e2) {
                e2.printStackTrace();
            }
        }
        WordFreq wf = new WordFreq(annoMan.extractTextFromNode(htmldoc));
        Element docident = newdoc.createElementNS(alNS, "al:document-identifier");
        annotatesn.appendChild(docident);
        docident.setAttributeNS(alNS, "al:orig-url", ((Element) annotatesn).getAttributeNS(rdfNS, "resource"));
        docident.setAttributeNS(alNS, "al:version", "1");
        Iterator it = null;
        it = wf.getSortedWordlist();
        Map.Entry ent;
        String word;
        int count;
        int i = 0;
        while (it.hasNext()) {
            ent = (Map.Entry) it.next();
            word = ((String) ent.getKey());
            count = ((Counter) ent.getValue()).count;
            if ((word.length() > 4) && (i < 10)) {
                Element wordelem = newdoc.createElementNS(alNS, "al:word");
                wordelem.setAttributeNS(alNS, "al:freq", Integer.toString(count));
                wordelem.appendChild(newdoc.createTextNode(word));
                docident.appendChild(wordelem);
                i++;
            }
        }
        try {
            StringWriter strw = new StringWriter();
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            xformer.transform(new DOMSource(newdoc), new StreamResult(strw));
            messagedigest.update(strw.toString().getBytes());
            byte[] md5bytes = messagedigest.digest();
            annohash = "";
            for (int b = 0; b < md5bytes.length; b++) {
                String s = Integer.toHexString(md5bytes[b] & 0xFF);
                annohash = annohash + ((s.length() == 1) ? "0" + s : s);
            }
            this.annohash = annohash;
            annoElem.setAttribute("xmlns:al", alNS);
            annoElem.setAttributeNS(alNS, "al:id", getAnnohash());
            Location = (baseurl + "/annotation/" + getAnnohash());
            annoElem.setAttributeNS(rdfNS, "r:about", Location);
            newbodynode.setAttributeNS(rdfNS, "r:resource", baseurl + "/annotation/body/" + getAnnohash());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        annoMan.store(newdoc.getDocumentElement());
        annoMan.createAnnoResource(newdoc.getDocumentElement(), getAnnohash());
        if (htmlNode != null) annoMan.createAnnoBody(htmlNode, getAnnohash());
        Location = (this.baseurl + "/annotation/" + getAnnohash());
        annoElem.setAttributeNS(rdfNS, "r:about", Location);
        this.responseDoc = newdoc;
    }
