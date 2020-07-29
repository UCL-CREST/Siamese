    protected void endPage(PDPage pdPage) throws IOException {
        textWriter.flush();
        String page = new String(textOS.toByteArray(), "UTF-16");
        textOS.reset();
        if (page.indexOf("a") != -1) {
            page = page.replaceAll("a[0-9]{1,3}", ".");
        }
        for (int i = 0; i < searchedWords.length; i++) {
            Pattern pattern = Pattern.compile(searchedWords[i], Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(page);
            while (matcher.find()) {
                int begin = matcher.start();
                int end = matcher.end();
                highlighterOutput.write("    <loc " + "pg=" + (getCurrentPageNo() - 1) + " pos=" + begin + " len=" + (end - begin) + ">\n");
            }
        }
    }
