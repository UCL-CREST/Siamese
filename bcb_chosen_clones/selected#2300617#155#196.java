    private void split() {
        this.length = 0;
        if (isEmptyString(this.stringToSplit)) {
            return;
        }
        if (isEmptyString(this.regexp)) {
            this.delimiters = new String[] { this.stringToSplit };
            return;
        }
        final Pattern aPattern = Pattern.compile(this.regexp);
        final Matcher aMatcher = aPattern.matcher(this.stringToSplit);
        if (aMatcher.find() == false) {
            this.delimiters = new String[] { this.stringToSplit };
            return;
        }
        aMatcher.reset();
        final ArrayList<String> someDelimiters = new ArrayList<String>();
        final ArrayList<String> someValues = new ArrayList<String>();
        int lastEnd = 0;
        while (aMatcher.find()) {
            final String aDelimiter = this.stringToSplit.substring(aMatcher.start(), aMatcher.end());
            if (aMatcher.start() > lastEnd) {
                someValues.add(this.stringToSplit.substring(lastEnd, aMatcher.start()));
                if (lastEnd == 0) {
                    someDelimiters.add("");
                }
                someDelimiters.add(aDelimiter);
            } else {
                if (lastEnd > 0) {
                    someValues.add("");
                }
                someDelimiters.add(aDelimiter);
            }
            lastEnd = aMatcher.end();
        }
        if (lastEnd < this.stringToSplit.length()) {
            someValues.add(this.stringToSplit.substring(lastEnd));
        }
        this.delimiters = someDelimiters.toArray(this.delimiters);
        this.values = someValues.toArray(this.values);
        this.length = someValues.size();
    }
