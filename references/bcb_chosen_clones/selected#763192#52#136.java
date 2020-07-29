    public void processIntonationUnit(Element iu) {
        String originalText = iu.getText();
        Vector<Content> contentList = new Vector<Content>();
        contentList.addElement(new org.jdom.Text(originalText));
        for (Object o : mappings) {
            Element mapping = (Element) o;
            String type = mapping.getAttributeValue("type");
            String pattern = mapping.getChild("Symbol").getText();
            String number = mapping.getAttributeValue("no");
            if (type.equals("replace") || type.equals("delete")) {
                String replacement = "";
                if (type.equals("replace")) {
                    replacement = mapping.getChild("Replace").getText();
                }
                String newText = iu.getText().replaceAll("\\Q" + pattern + "\\E", replacement);
                iu.setText(newText);
                continue;
            }
            if (type.equals("single") || type.equals("combination")) {
                pattern = "\\Q" + pattern + "\\E";
            }
            Element replacingElement = (Element) (mapping.getContent(new ElementFilter()).get(1));
            Pattern p = Pattern.compile(pattern);
            int cutoffFront = 0;
            int cutoffBack = 0;
            Element cutoffChild = mapping.getChild("cutoff");
            if ((cutoffChild != null) && (cutoffChild.getAttributeValue("front") != null)) {
                cutoffFront = Integer.parseInt(cutoffChild.getAttributeValue("front"));
            }
            if ((cutoffChild != null) && (cutoffChild.getAttributeValue("back") != null)) {
                cutoffBack = Integer.parseInt(cutoffChild.getAttributeValue("back"));
            }
            for (int i = 0; i < contentList.size(); i++) {
                Content c = contentList.elementAt(i);
                if (!(c instanceof org.jdom.Text)) continue;
                Text thisText = (Text) c;
                String thisString = thisText.getText();
                Matcher m = p.matcher(thisString);
                int lastEnd = 0;
                String contentString = "";
                Vector<Content> replacements = new Vector<Content>();
                boolean hasSomethingBeenReplaced = false;
                while (m.find()) {
                    hasSomethingBeenReplaced = true;
                    int start = m.start();
                    if (lastEnd < start - cutoffFront) {
                        String s = thisString.substring(lastEnd, start - cutoffFront);
                        Text t = new Text(s);
                        replacements.add(t);
                    }
                    Element e = (Element) (replacingElement.clone());
                    List attList = e.getAttributes();
                    for (Object o2 : attList) {
                        Attribute a = (Attribute) o2;
                        if (a.getValue().equals("$match")) {
                            a.setValue(m.group());
                        }
                        if (a.getValue().equals("$match-length")) {
                            a.setValue(Integer.toString(m.group().length()));
                        }
                    }
                    replacements.add(e);
                    lastEnd = m.end() + cutoffBack;
                }
                if ((hasSomethingBeenReplaced) && (lastEnd < thisString.length())) {
                    String s = thisString.substring(lastEnd);
                    Text t = new Text(s);
                    replacements.add(t);
                }
                if (hasSomethingBeenReplaced) {
                    contentList.remove(i);
                    contentList.addAll(i, replacements);
                    c.detach();
                    i += replacements.size();
                }
            }
        }
        iu.removeContent();
        Element ot = new Element("original-text");
        ot.setText(originalText);
        iu.addContent(ot);
        Element nt = new Element("mapped");
        nt.addContent(contentList);
        iu.addContent(nt);
    }
