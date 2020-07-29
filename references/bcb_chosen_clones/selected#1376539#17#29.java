    RawTrainingValues(String input) {
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher result = pattern.matcher(input);
        int i = 0;
        while (result.find()) {
            values[i++] = Float.parseFloat(input.substring(result.start(), result.end()));
        }
        pattern = Pattern.compile("([a-zA-Z]+)");
        result = pattern.matcher(input);
        if (result.find()) {
            classifier = input.substring(result.start(), result.end());
        }
    }
