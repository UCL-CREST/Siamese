    public static String getTextFromUrl(final String url) {
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            final StringBuilder result = new StringBuilder();
            inputStreamReader = new InputStreamReader(new URL(url).openStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(HtmlUtil.quoteHtml(line)).append("\r");
            }
            return result.toString();
        } catch (final IOException exception) {
            return exception.getMessage();
        } finally {
            InputOutputUtil.close(bufferedReader);
            InputOutputUtil.close(inputStreamReader);
        }
    }
