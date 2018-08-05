    private String reformatNumbers(String line, boolean convertDecimal, boolean useScale, double scale) {
        StringBuffer outLine = new StringBuffer();
        Pattern pattern = Pattern.compile("\\d+\\.\\d+(E[\\-\\d\\.]+)?");
        Matcher matcher = pattern.matcher(line);
        int lastEnd = 0;
        while (matcher.find()) {
            int start = matcher.start();
            String token = matcher.group();
            double value = Double.parseDouble(token);
            if (useScale) {
                value *= scale;
            }
            String outToken = (convertDecimal ? decimalFormatter.format(value) : scientificFormatter.format(value));
            outLine.append(line.substring(lastEnd, start));
            outLine.append(outToken);
            lastEnd = matcher.end();
        }
        outLine.append(line.substring(lastEnd));
        return outLine.toString();
    }
