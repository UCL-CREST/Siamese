    public static String getTextFromUrl(final String url) throws IOException {
        final String lineSeparator = System.getProperty("line.separator");
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            final StringBuilder result = new StringBuilder();
            inputStreamReader = new InputStreamReader(new URL(url).openStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append(lineSeparator);
            }
            return result.toString();
        } finally {
            InputOutputUtil.close(bufferedReader, inputStreamReader);
        }
    }
