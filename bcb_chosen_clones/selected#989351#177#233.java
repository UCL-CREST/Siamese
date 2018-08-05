    private void initializeTree() {
        InputStreamReader reader = null;
        BufferedReader buffReader = null;
        try {
            for (int i = 0; i < ORDER.length; i++) {
                int index = ORDER[i];
                String indexName = index < 10 ? "0" + index : (index > 20 ? "big" : "" + index);
                URL url = EmptyClass.class.getResource("engchar" + indexName + ".dic");
                logger.info("... Loading: " + "engchar" + indexName + ".dic = {" + url + "}");
                reader = new InputStreamReader(url.openStream());
                buffReader = new BufferedReader(reader);
                String line = null;
                String word = null;
                do {
                    line = buffReader.readLine();
                    if (line != null) {
                        boolean plural = line.endsWith("/S");
                        boolean forbidden = line.endsWith("/X");
                        if (plural) {
                            int stringIndex = line.indexOf("/S");
                            word = new String(line.substring(0, stringIndex));
                        } else if (forbidden) {
                            int stringIndex = line.indexOf("/X");
                            word = new String(line.substring(0, stringIndex));
                        } else {
                            word = line.toString();
                        }
                        if (tree == null) {
                            tree = new BKTree();
                        }
                        tree.insertDictionaryWord(word, plural, forbidden);
                    }
                } while (line != null);
            }
            logger.debug("Loading supplemental dictionary...");
            List<String> listOfWords = KSupplementalDictionaryUtil.getWords();
            for (String word : listOfWords) {
                tree.insertDictionaryWord(word, false, false);
            }
            initialized = true;
        } catch (Exception exception) {
            logger.error("Error", exception);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ex) {
                }
            }
            if (buffReader != null) {
                try {
                    buffReader.close();
                } catch (Exception ex) {
                }
            }
        }
    }
