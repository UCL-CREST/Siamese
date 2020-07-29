    protected void changeCase(String typeOfCase) {
        if (m_editor.getSelectedText() == null) {
            m_editor.selectAll();
            if (m_editor.getSelectedText() == null) {
                return;
            }
        }
        String result = m_editor.getSelectedText();
        if (typeOfCase.equals("UPPERCASE")) {
            result = result.toUpperCase();
        } else if (typeOfCase.equals("lowercase")) {
            result = result.toLowerCase();
        } else if (typeOfCase.equals("Title_Case")) {
            StringBuffer strB = new StringBuffer(result.toLowerCase());
            Pattern pattern = Pattern.compile("(?<!\\p{InCombiningDiacriticalMarks}|\\p{L})\\p{L}");
            Matcher matcher = pattern.matcher(result);
            while (matcher.find()) {
                int index = matcher.start();
                strB.setCharAt(index, Character.toTitleCase(strB.charAt(index)));
            }
            result = strB.toString();
        } else if (typeOfCase.equals("Sentence_case")) {
            StringBuffer strB = new StringBuffer(result.toUpperCase().equals(result) ? result.toLowerCase() : result);
            Matcher matcher = Pattern.compile("\\p{L}(\\p{L}+)").matcher(result);
            while (matcher.find()) {
                if (!(matcher.group(0).toUpperCase().equals(matcher.group(0)) || matcher.group(1).toLowerCase().equals(matcher.group(1)))) {
                    for (int i = matcher.start(); i < matcher.end(); i++) {
                        strB.setCharAt(i, Character.toLowerCase(strB.charAt(i)));
                    }
                }
            }
            final String QUOTE = "\"'`,<>«»‘-›";
            matcher = Pattern.compile("(?:[.?!‼-⁉][])}" + QUOTE + "]*|^|\n|:\\s+[" + QUOTE + "])[-=_*‐-―\\s]*[" + QUOTE + "\\[({]*\\p{L}").matcher(result);
            while (matcher.find()) {
                int i = matcher.end() - 1;
                strB.setCharAt(i, Character.toUpperCase(strB.charAt(i)));
            }
            result = strB.toString();
        }
        undoSupport.beginUpdate();
        int start = m_editor.getSelectionStart();
        m_editor.replaceSelection(result);
        setSelection(start, start + result.length());
        undoSupport.endUpdate();
    }
