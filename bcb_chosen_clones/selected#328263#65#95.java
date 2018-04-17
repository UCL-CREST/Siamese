    private void doMath() {
        StringMath convert = new StringMath();
        String math = "";
        String value;
        String newString = "";
        int pos = 0;
        int length;
        Pattern mathPattern = Pattern.compile("\\[.*?\\]");
        Matcher matcher = mathPattern.matcher(editMessage.text);
        try {
            while (matcher.find()) {
                math = editMessage.text.substring(matcher.start(), matcher.end());
                value = convert.eval(math);
                newString = newString + editMessage.text.substring(pos, matcher.end() - 1) + value + "]";
                pos = matcher.end();
            }
            length = editMessage.text.length();
            if (pos >= editMessage.text.length()) {
                editMessage.text = newString;
            } else {
                editMessage.text = newString + editMessage.text.substring(pos, length);
            }
            editMessage.text = editMessage.text.replaceAll("\\<", "&lt;");
            editMessage.text = editMessage.text.replaceAll("\\>", "&gt;");
        } catch (ArithmeticException ae) {
            editMessage.subType = "systemMessage";
            editMessage.text = math + " " + ae.getMessage();
            editMessage.text = editMessage.text.replaceAll("\\<", "&lt;");
            editMessage.text = editMessage.text.replaceAll("\\>", "&gt;");
        }
    }
