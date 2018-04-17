    private void scanWords(QDomNode node) {
        String value = node.nodeValue();
        QDomDocumentFragment fragment = doc.createDocumentFragment();
        boolean matchFound = false;
        int previousPosition = 0;
        String valueEnd = "";
        String regex = buildRegex();
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            matchFound = true;
            String valueStart = "";
            int start = matcher.start();
            int end = matcher.end();
            if (value.substring(start).startsWith(" ")) start++;
            if (value.substring(start, end).endsWith(" ")) end--;
            if (matcher.start() > 0) {
                valueStart = value.substring(previousPosition, start);
            }
            String valueMiddle = value.substring(start, end);
            valueEnd = "";
            if (matcher.end() < value.length()) {
                valueEnd = value.substring(end);
            }
            previousPosition = end;
            if (!valueStart.equals("")) {
                QDomText startText = doc.createTextNode(valueStart);
                fragment.appendChild(startText);
            }
            QDomElement hilight = doc.createElement("en-hilight");
            hilight.appendChild(doc.createTextNode(valueMiddle));
            fragment.appendChild(hilight);
        }
        if (matchFound) {
            if (previousPosition != value.length()) {
                QDomText endText = doc.createTextNode(valueEnd);
                fragment.appendChild(endText);
            }
            newNodes.add(fragment);
            oldNodes.add(node);
        }
    }
