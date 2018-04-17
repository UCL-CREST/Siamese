    public MultiplicityItem(String value) {
        if (!MultiplicityItem.syntaxIsValid(value)) throw new InvalidMultiplicityValueException("Invalid syntax of value.");
        String left_bound = "\\d+";
        String right_bound = "([\\d&&[^0]]\\d*|[*])";
        String bound = "((" + left_bound + "\\.\\." + right_bound + ")|" + left_bound + ")";
        Pattern pattern = Pattern.compile(bound);
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            addMultiplicityRange(value.substring(start, end));
        }
    }
