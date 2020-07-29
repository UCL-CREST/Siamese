    public void replaceAllInMixedTextBinaryFile(String file, String searchExpression, String replacement) throws Exception {
        if (file == null) {
            throw new Exception("Failed to replace. File not set.");
        }
        if (searchExpression == null) {
            searchExpression = ".*";
        }
        if (replacement == null) {
            replacement = "";
        }
        String fileContent = this.getFileContent(new File(file));
        List indicesAndTextToInsert = new ArrayList();
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(searchExpression);
        java.util.regex.Matcher matcher = pattern.matcher(fileContent);
        while (matcher.find()) {
            String occurance = matcher.group();
            int startIndex = matcher.start();
            int endIndex = matcher.end();
            java.util.regex.Matcher m = pattern.matcher(occurance);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                m.appendReplacement(sb, replacement);
            }
            m.appendTail(sb);
            String textToInsert = sb.toString();
            List indicesAndText = new ArrayList();
            indicesAndText.add(new Integer(startIndex));
            indicesAndText.add(new Integer(endIndex));
            indicesAndText.add(textToInsert);
            indicesAndTextToInsert.add(indicesAndText);
        }
        int size = indicesAndTextToInsert.size();
        for (int i = (size - 1); i >= 0; i--) {
            List indicesAndText = (List) indicesAndTextToInsert.get(i);
            int startIndex = ((Integer) indicesAndText.get(0)).intValue();
            int endIndex = ((Integer) indicesAndText.get(1)).intValue();
            String textToInsert = (String) indicesAndText.get(2);
            this.insertStringInFile(file, textToInsert, (long) startIndex, (long) endIndex);
        }
    }
