    public static void testdownloading() throws Exception {
        String url = "http://en.wikipedia.org/wiki/Special:Export/Wales";
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);
        ((HttpMethodBase) method).getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        System.out.println("starting...");
        int statusCode = client.executeMethod(method);
        InputStream incomingstream;
        try {
            incomingstream = method.getResponseBodyAsStream();
            DOMParser parser = new DOMParser();
            parser.parse(incomingstream);
            Document retrievedDocument = parser.getDocument();
            if (retrievedDocument.hasChildNodes()) {
                NodeList nl = retrievedDocument.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) {
                    System.out.println(nl.item(0).getNodeName() + "\n");
                }
            } else {
                System.out.println("No child nodes !!!!!!!!!");
            }
            NodeList result = retrievedDocument.getElementsByTagName("text");
            System.out.println("Wiki Content of the page:\n");
            System.out.println("How many results:" + result.getLength());
            String finalContent = "";
            WikiMediaToCreoleConverter wmc = new WikiMediaToCreoleConverter();
            for (int i = 0; i < result.getLength(); i++) {
                Node eachelement = result.item(i);
                String wikitext = eachelement.getTextContent();
                Pattern linkInALink = Pattern.compile("\\[\\[[^\\]]*(\\[\\[.*\\]\\]).*\\]\\]");
                Pattern innerlink = Pattern.compile("\\[\\[[^\\[]*?\\]\\]");
                Matcher m = linkInALink.matcher(wikitext);
                System.out.println("*** Links inside links preprocessing ***");
                String processed = new String("");
                int mindex = 0;
                while (m.find()) {
                    System.out.println(m.group() + "\n Inner Links:");
                    String toreplace = m.group();
                    Matcher m2 = innerlink.matcher(toreplace);
                    processed = processed + wikitext.substring(mindex, m.start());
                    int currentindex = 0;
                    String replaced = new String("");
                    while (m2.find()) {
                        String linktext = m2.group().substring(2, m2.group().length() - 2);
                        String[] sp = linktext.split("\\|");
                        if (sp.length == 2) {
                            linktext = sp[1];
                        }
                        replaced = replaced + toreplace.substring(currentindex, m2.start()) + linktext;
                        currentindex = m2.end();
                    }
                    replaced = replaced + toreplace.substring(currentindex);
                    processed = processed + replaced;
                    mindex = m.end();
                }
                processed = processed + wikitext.substring(mindex);
                Pattern linksequence = Pattern.compile("(\\[\\[[^\\[\\]]*\\]\\][\r\n]*)+");
                Matcher fm = linksequence.matcher(wikitext.substring(mindex));
                int tail = 0;
                while (fm.find()) {
                    if (fm.hitEnd()) {
                        tail = fm.group().length();
                    }
                }
                processed = processed.substring(0, processed.length() - tail);
                finalContent = wmc.convert(new StringReader(processed));
            }
            result = retrievedDocument.getElementsByTagName("timestamp");
            String timestamp = new String(result.item(0).getTextContent());
            System.out.println("Timestamp : " + timestamp);
            result = retrievedDocument.getElementsByTagName("title");
            String pageTitle = new String(result.item(0).getTextContent());
            System.out.println("Title : " + pageTitle);
            List<String> atlist = new ArrayList<String>();
            atlist.addAll(wmc.getImageLinks());
            Document doc = TransformerHelper.newDocument();
            Element root = doc.createElement("article");
            doc.appendChild(root);
            Element att = doc.createElement("attachments");
            root.appendChild(att);
            for (String a : atlist) {
                Element e = doc.createElement("filename");
                att.appendChild(e);
                Text t = doc.createTextNode(a);
                e.appendChild(t);
            }
            Element titleelement = doc.createElement("title");
            root.appendChild(titleelement);
            Text titletext = doc.createTextNode(pageTitle);
            titleelement.appendChild(titletext);
            Element timestampelement = doc.createElement("timestamp");
            root.appendChild(timestampelement);
            Text timestamptext = doc.createTextNode(timestamp);
            timestampelement.appendChild(timestamptext);
            Element content = doc.createElement("content");
            root.appendChild(content);
            Text text = doc.createTextNode(finalContent);
            content.appendChild(text);
            DOMTransformer.prettyPrint(doc);
            String urlraw = "http://en.wikipedia.org/wiki/Apple";
            GetMethod method2 = new GetMethod(urlraw);
            ((HttpMethodBase) method2).getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
            statusCode = client.executeMethod(method2);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
            }
            String responseHTML = method2.getResponseBodyAsString();
            LinkGetter lg = new LinkGetter();
            List<String> actualURLs = lg.getLinks(responseHTML);
            System.out.println("Image links:");
            for (String name : atlist) {
                System.out.println("finding link for:" + name);
                for (String link : actualURLs) {
                    if (link.contains(name.replace(' ', '_'))) {
                        System.out.println("found: " + link);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XindiceException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
