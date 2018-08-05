    public static void parseConfigurationFile(String file, Map settingsMap, Map actionHandlers, Map mimeTypes) {
        Map specialKeyWords = new HashMap();
        specialKeyWords.put("addtype", "1");
        specialKeyWords.put("action", "2");
        BufferedReader config = null;
        try {
            if (new File(file).exists() == true) {
                config = new BufferedReader(new FileReader(file));
                String aLine, keyWord, value;
                while (config.ready()) {
                    aLine = config.readLine();
                    if (aLine != null && aLine.length() > 0) {
                        aLine = aLine.replace('\t', ' ').replace('\"', ' ').trim();
                        if (aLine.length() > 0 && aLine.charAt(0) != '#') {
                            int positionOfSpace = aLine.indexOf(' ');
                            if (positionOfSpace == -1) Misc.putSysMessage(8, "ConfProcessor: error: unknown entry " + aLine + " found in " + file + ". Ignoring."); else {
                                keyWord = aLine.substring(0, positionOfSpace).trim().toLowerCase();
                                value = aLine.substring(positionOfSpace + 1).trim();
                                if (specialKeyWords.get(keyWord) != null) {
                                    switch(Integer.parseInt(specialKeyWords.get(keyWord).toString())) {
                                        case 1:
                                            {
                                                positionOfSpace = value.indexOf(' ');
                                                if (positionOfSpace != -1) {
                                                    String Mime = value.substring(0, positionOfSpace).trim();
                                                    String extvalue = value.substring(positionOfSpace + 1).trim();
                                                    String aValue;
                                                    StringTokenizer extTokened = new StringTokenizer(extvalue);
                                                    while (extTokened.hasMoreTokens() == true) {
                                                        aValue = extTokened.nextToken();
                                                        if (aValue.charAt(0) == '.') aValue = aValue.substring(1);
                                                        mimeTypes.put(aValue, Mime);
                                                    }
                                                }
                                                break;
                                            }
                                        case 2:
                                            {
                                                aLine = new String(value);
                                                positionOfSpace = aLine.indexOf(' ');
                                                if (positionOfSpace != -1) {
                                                    String Mime = aLine.substring(0, positionOfSpace).trim();
                                                    String preProcessor = aLine.substring(positionOfSpace + 1).trim();
                                                    actionHandlers.put(Mime, preProcessor);
                                                }
                                                break;
                                            }
                                    }
                                } else {
                                    settingsMap.put(keyWord, value);
                                }
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            try {
                if (config != null) config.close();
            } catch (IOException e) {
            }
        }
    }
