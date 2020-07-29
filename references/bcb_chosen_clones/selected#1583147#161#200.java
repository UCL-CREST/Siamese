    private void split() {
        this.length = 0;
        if (isEmptyString(this.stringToSplit)) {
            return;
        }
        if (isEmptyString(this.regexp)) {
            this.values = new String[] { this.stringToSplit };
            return;
        }
        final Pattern aPattern = Pattern.compile(this.regexp);
        final Matcher aMatcher = aPattern.matcher(this.stringToSplit);
        if (aMatcher.find() == false) {
            this.values = new String[] { this.stringToSplit };
            return;
        }
        aMatcher.reset();
        final ArrayList<String> someValues = new ArrayList<String>();
        int lastEnd = 0;
        while (aMatcher.find()) {
            final String aDelimiter = this.stringToSplit.substring(aMatcher.start(), aMatcher.end());
            if (aMatcher.start() > lastEnd) {
                someValues.add(this.stringToSplit.substring(lastEnd, aMatcher.start()));
                if (lastEnd == 0) {
                    someValues.add("");
                }
                someValues.add(aDelimiter);
            } else {
                if (lastEnd > 0) {
                    someValues.add("");
                }
                someValues.add(aDelimiter);
            }
            lastEnd = aMatcher.end();
        }
        if (lastEnd < this.stringToSplit.length()) {
            someValues.add(this.stringToSplit.substring(lastEnd));
        }
        this.values = someValues.toArray(this.values);
        this.length = someValues.size();
    }
