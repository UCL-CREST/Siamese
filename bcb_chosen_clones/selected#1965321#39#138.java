    public void applyHighlighting() {
        try {
            String line = textArea.getText().replaceAll("\n", " ");
            int caretPosition = textArea.getCaretPosition();
            Element root = textArea.getDocument().getDefaultRootElement();
            int line2 = root.getElementIndex(caretPosition);
            lineEnd = root.getElement(line2).getStartOffset();
            java.util.List styles = new java.util.ArrayList();
            System.out.println("SUB TEST " + line.substring(lineEnd));
            line = line.substring(lineEnd);
            Pattern pattern;
            Matcher matcher;
            if (line.trim().length() > 0) if (line.trim().charAt(0) != ';') {
                for (int i = 0; i < reservedWords.length; i++) {
                    pattern = Pattern.compile("\\s" + reservedWords[i] + "(?![^;\\s]+)");
                    matcher = pattern.matcher("\n" + line.toUpperCase().split(";")[0] + "\n");
                    while (matcher.find()) {
                        System.out.println("reservedWords find");
                        StyledDocument doc = (StyledDocument) textArea.getDocument();
                        MutableAttributeSet attr = new SimpleAttributeSet();
                        StyleConstants.setForeground(attr, new Color(0, 0, 204));
                        textArea.setCharacterAttributes(attr, false);
                        StyleConstants.setBold(attr, true);
                        doc.setCharacterAttributes(lineEnd + matcher.start(), reservedWords[i].length(), attr, true);
                        System.out.println("RESERVED WORDS :" + (lineEnd + matcher.start()) + " " + reservedWords[i].length());
                    }
                }
                for (int i = 0; i < registers.length; i++) {
                    pattern = Pattern.compile("(\\s|,)" + registers[i] + "(?![^,;\\s]+)");
                    matcher = pattern.matcher("\n" + line.toUpperCase().split(";")[0] + "\n");
                    while (matcher.find()) {
                        System.out.println("registers find");
                        StyledDocument doc = (StyledDocument) textArea.getDocument();
                        MutableAttributeSet attr = new SimpleAttributeSet();
                        StyleConstants.setForeground(attr, new Color(102, 0, 51));
                        StyleConstants.setBold(attr, true);
                        textArea.setCharacterAttributes(attr, false);
                        doc.setCharacterAttributes(lineEnd + matcher.start(), registers[i].length(), attr, true);
                        System.out.println("REGISTERS: " + (lineEnd + matcher.start()) + " " + reservedWords[i].length());
                    }
                }
                for (int i = 0; i < declarations.length; i++) {
                    pattern = Pattern.compile("\\s" + declarations[i] + "(?![^;\\s]+)");
                    matcher = pattern.matcher("\n" + line.toUpperCase().split(";")[0] + "\n");
                    while (matcher.find()) {
                        System.out.println("declarations find");
                        StyledDocument doc = (StyledDocument) textArea.getDocument();
                        MutableAttributeSet attr = new SimpleAttributeSet();
                        StyleConstants.setForeground(attr, new Color(00, 100, 100));
                        StyleConstants.setBold(attr, true);
                        textArea.setCharacterAttributes(attr, false);
                        doc.setCharacterAttributes(matcher.start(), declarations[i].length(), attr, true);
                    }
                }
                pattern = Pattern.compile("^\\s\\w+:");
                matcher = pattern.matcher("\n" + line.toUpperCase().split(";")[0] + "\n");
                while (matcher.find()) {
                    System.out.println("labels find");
                    StyledDocument doc = (StyledDocument) textArea.getDocument();
                    MutableAttributeSet attr = new SimpleAttributeSet();
                    StyleConstants.setForeground(attr, new Color(00, 100, 100));
                    StyleConstants.setBold(attr, true);
                    textArea.setCharacterAttributes(attr, false);
                    doc.setCharacterAttributes(lineEnd + matcher.start() - 1, matcher.end() - (matcher.start()), attr, true);
                }
                pattern = Pattern.compile("(\\s|,)((0B([0-1]+))|(0X([0-9A-F]+))|([0-9]+))(?![^;\\s]+)");
                matcher = pattern.matcher("\n" + line.toUpperCase().split(";")[0] + "\n");
                while (matcher.find()) {
                    System.out.println("(bin/hex/dec numbers) find");
                    StyledDocument doc = (StyledDocument) textArea.getDocument();
                    MutableAttributeSet attr = new SimpleAttributeSet();
                    StyleConstants.setForeground(attr, new Color(240, 51, 0));
                    StyleConstants.setBold(attr, true);
                    textArea.setCharacterAttributes(attr, false);
                    doc.setCharacterAttributes(lineEnd + matcher.start(), matcher.end() - matcher.start(), attr, true);
                }
                pattern = Pattern.compile("(\"[^\"]*\"|'[^']*')");
                matcher = pattern.matcher("\n" + line.toUpperCase().split(";")[0] + "\n");
                while (matcher.find()) System.out.println("strings find");
                StyledDocument doc = (StyledDocument) textArea.getDocument();
                MutableAttributeSet attr = new SimpleAttributeSet();
                StyleConstants.setForeground(attr, new Color(204, 0, 0));
                StyleConstants.setBold(attr, true);
                textArea.setCharacterAttributes(attr, false);
                doc.setCharacterAttributes(lineEnd + matcher.start() - 1, matcher.end() - matcher.start(), attr, true);
            }
            pattern = Pattern.compile("\\Q;\\E");
            matcher = pattern.matcher(line);
            if (matcher.find()) System.out.println("comments find");
            StyledDocument doc = (StyledDocument) textArea.getDocument();
            MutableAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setForeground(attr, new Color(63, 127, 95));
            textArea.setCharacterAttributes(attr, false);
            StyleConstants.setBold(attr, false);
            doc.setCharacterAttributes(lineEnd + matcher.start(), (line.length() - matcher.start()), attr, true);
            System.out.println(lineEnd + matcher.start() + " " + (line.length() - matcher.start()));
            System.out.println("ddd" + textArea.getText(lineEnd + matcher.start(), (line.length() - matcher.start())));
        } catch (Exception ex) {
        }
    }
