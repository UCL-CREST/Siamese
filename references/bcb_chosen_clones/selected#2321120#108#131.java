    public GEItem lookup(final String itemName) {
        try {
            URL url = new URL(GrandExchange.HOST + "/m=itemdb_rs/results.ws?query=" + itemName + "&price=all&members=");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String input;
            while ((input = br.readLine()) != null) {
                if (input.contains("<div id=\"search_results_text\">")) {
                    input = br.readLine();
                    if (input.contains("Your search for")) {
                        return null;
                    }
                } else if (input.startsWith("<td><img src=")) {
                    Matcher matcher = GrandExchange.PATTERN.matcher(input);
                    if (matcher.find()) {
                        if (matcher.group(2).contains(itemName)) {
                            return lookup(Integer.parseInt(matcher.group(1)));
                        }
                    }
                }
            }
        } catch (IOException ignored) {
        }
        return null;
    }
