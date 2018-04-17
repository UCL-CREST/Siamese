    public static String changeCase(String text, String typeOfCase) {
        String result;
        if (typeOfCase.equals("UPPERCASE")) {
            result = text.toUpperCase();
        } else if (typeOfCase.equals("lowercase")) {
            result = text.toLowerCase();
        } else if (typeOfCase.equals("Title_Case")) {
            StringBuilder strB = new StringBuilder(text.toLowerCase());
            Pattern pattern = Pattern.compile("(?<!\\p{InCombiningDiacriticalMarks}|\\p{L})\\p{L}");
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                int index = matcher.start();
                strB.setCharAt(index, Character.toTitleCase(strB.charAt(index)));
            }
            result = strB.toString();
        } else if (typeOfCase.equals("Sentence_case")) {
            StringBuilder strB = new StringBuilder(text.toUpperCase().equals(text) ? text.toLowerCase() : text);
            Matcher matcher = Pattern.compile("\\p{L}(\\p{L}+)").matcher(text);
            while (matcher.find()) {
                if (!(matcher.group(0).toUpperCase().equals(matcher.group(0)) || matcher.group(1).toLowerCase().equals(matcher.group(1)))) {
                    for (int i = matcher.start(); i < matcher.end(); i++) {
                        strB.setCharAt(i, Character.toLowerCase(strB.charAt(i)));
                    }
                }
            }
            final String QUOTE = "\"'`,<>«»‘-›";
            matcher = Pattern.compile("(?:[.?!‼-⁉][])}" + QUOTE + "]*|^|\n|:\\s+[" + QUOTE + "])[-=_*‐-―\\s]*[" + QUOTE + "\\[({]*\\p{L}").matcher(text);
            while (matcher.find()) {
                int i = matcher.end() - 1;
                strB.setCharAt(i, Character.toUpperCase(strB.charAt(i)));
            }
            result = strB.toString();
        } else {
            result = text;
        }
        return result;
    }
