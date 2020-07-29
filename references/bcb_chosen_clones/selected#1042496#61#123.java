    public void readLexFile() {
        try {
            Logger.getLogger(GlobalVariables.class.getName()).log(Level.INFO, "Loading resource file [" + resourceDirPath + "/timex.lex]");
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(resourceDirPath + "/timex.lex")));
            String sLine;
            String sPattern = "^(.*?)(?:(?:\\s*\\:\\:\\s*)|\t)(.*?)(?:(?:\t)(.*))?$";
            Pattern patternLex = Pattern.compile(sPattern);
            int iNr = 1;
            String[] arrsMatch = new String[3];
            while ((sLine = in.readLine()) != null) {
                Matcher m = patternLex.matcher(sLine);
                while (m.find()) {
                    try {
                        if (bPrintOutTheResults) {
                            System.out.println("Found " + m.group(0) + " at position " + m.start(0));
                        }
                        if (m.start(1) < m.end(1)) {
                            if (bPrintOutTheResults) {
                                System.out.println("group 1 " + m.group(1));
                            }
                            arrsMatch[0] = m.group(1);
                        } else {
                            arrsMatch[0] = "";
                        }
                        if (m.start(2) < m.end(2)) {
                            if (bPrintOutTheResults) {
                                System.out.println("group 2 " + m.group(2));
                            }
                            arrsMatch[1] = m.group(2);
                        } else {
                            arrsMatch[1] = "";
                        }
                        if (m.start(3) < m.end(3)) {
                            if (bPrintOutTheResults) {
                                System.out.println("group 3 " + m.group(3));
                            }
                            arrsMatch[2] = m.group(3);
                        } else {
                            arrsMatch[2] = "";
                        }
                        String[] ids = arrsMatch[1].split("\\s");
                        for (int i = 0; i < ids.length; i++) {
                            createLexVariables(ids[i], arrsMatch[0]);
                            if (arrsMatch[2].compareTo("") != 0) {
                                hashtblMapValue.put(arrsMatch[0] + "_" + ids[i], arrsMatch[2]);
                                hashtblMapValue.put(arrsMatch[0], arrsMatch[2]);
                                if (ids[i].compareTo("TIMEZONEFULL") == 0) {
                                    hashtblMapValue.put(arrsMatch[0].toLowerCase() + "_" + ids[i], arrsMatch[2]);
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                if (bPrintOutTheResults && sLine.matches(sPattern)) {
                    System.out.println(iNr + " -OK");
                    iNr++;
                }
            }
        } catch (IOException e) {
            Logger.getLogger(GlobalVariables.class.getName()).log(Level.SEVERE, "Failed to load resource file [" + resourceDirPath + "/timex.lex]", e);
        }
    }
