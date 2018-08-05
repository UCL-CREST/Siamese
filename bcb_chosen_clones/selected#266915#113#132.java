    public static String compileTemplateFile(HashMap<String, String> variables, Reader source) throws IOException {
        String tag = "\\{%([\\w _-]+)%\\}";
        Pattern patternTag = Pattern.compile(tag);
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(source);
        String line = null;
        while ((line = reader.readLine()) != null) {
            Matcher matcher = patternTag.matcher(line);
            int lastIndex = 0;
            while (matcher.find()) {
                String key = matcher.group(1).trim();
                builder.append(line.substring(lastIndex, matcher.start()));
                builder.append(variables.get(key));
                lastIndex = matcher.end();
            }
            builder.append(line.substring(lastIndex));
            builder.append("\n");
        }
        return builder.toString();
    }
