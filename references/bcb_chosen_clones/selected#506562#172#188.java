    private boolean isQuoted(IDocument doc, int offset) {
        Pattern pSingle = Pattern.compile("'[^']*('')*[^']*;+[^']*('')*[^']*'");
        Pattern pDouble = Pattern.compile("\"[^\"]*(\"\")*[^\"]*;+[^\"]*(\"\")*[^\"]*\"");
        Matcher mSingle = pSingle.matcher(doc.get());
        while (mSingle.find()) {
            if (mSingle.start() <= offset && mSingle.end() >= offset) {
                return true;
            }
        }
        Matcher mDouble = pDouble.matcher(doc.get());
        while (mDouble.find()) {
            if (mDouble.start() <= offset && mDouble.end() >= offset) {
                return true;
            }
        }
        return false;
    }
