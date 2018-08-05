    private String replaceBrackets(String input) throws JDOMException, IOException {
        Document doc = org.exmaralda.common.jdomutilities.IOUtilities.readDocumentFromString(input);
        String regex_no_bracket = "[^\\(\\)\\[\\]\\{\\}\\<\\>]+";
        String regex_round = "\\(" + regex_no_bracket + "\\)";
        String regex_square = "\\[" + regex_no_bracket + "\\]";
        String regex_curly = "\\{" + regex_no_bracket + "\\}";
        String regex_angle = "\\<" + regex_no_bracket + "\\>";
        String regex = "(" + regex_round + "|" + regex_square + "|" + regex_curly + "|" + regex_angle + ")";
        Pattern pattern = Pattern.compile(regex);
        Vector<Element> allSegments = new Vector<Element>();
        Iterator i = doc.getDescendants(new ElementFilter("seg"));
        while (i.hasNext()) {
            Element seg = (Element) (i.next());
            allSegments.add(seg);
        }
        for (Element seg : allSegments) {
            if (!(seg.getAttributeValue("type").equals("segmental"))) continue;
            Vector<Content> newContent = new Vector<Content>();
            for (Object o : seg.getContent()) {
                Content c = (Content) o;
                if (!(c instanceof Text)) {
                    newContent.add(c);
                    continue;
                }
                Text t = (Text) c;
                String originalText = t.getText();
                Matcher m = pattern.matcher(originalText);
                int lastStart = 0;
                while (m.find()) {
                    int s = m.start();
                    int e = m.end();
                    String matchedText = originalText.substring(s, e);
                    if (lastStart < s) {
                        Text text = new Text(originalText.substring(lastStart, s));
                        newContent.add(text);
                    }
                    Element nonpho = makeNonphoElement(matchedText);
                    newContent.add(nonpho);
                    lastStart = e;
                }
                if (lastStart < originalText.length() - 1) {
                    Text text = new Text(originalText.substring(lastStart));
                    newContent.add(text);
                }
            }
            seg.removeContent();
            seg.setContent(newContent);
        }
        return org.exmaralda.common.jdomutilities.IOUtilities.documentToString(doc, false);
    }
