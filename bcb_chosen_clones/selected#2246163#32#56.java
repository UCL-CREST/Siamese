    public String getProperty(String propertyName, List<String> processedProperties) {
        checkCycle(propertyName, processedProperties);
        String property = super.getProperty(propertyName);
        if (property == null) {
            property = System.getProperty(propertyName);
        }
        if (property == null) {
            property = System.getenv(propertyName);
        }
        String result = property;
        if (result != null) {
            Pattern compile = Pattern.compile(ReferenceRegex);
            Matcher matcher = compile.matcher(result);
            if (matcher.find()) {
                do {
                    String refPropertyName = result.substring(matcher.start() + 2, matcher.end() - 1);
                    processedProperties.add(propertyName);
                    result = result.replace("${" + refPropertyName + "}", getProperty(refPropertyName, processedProperties));
                    processedProperties.remove(propertyName);
                    matcher = compile.matcher(result);
                } while (matcher.find());
            }
        }
        return result;
    }
