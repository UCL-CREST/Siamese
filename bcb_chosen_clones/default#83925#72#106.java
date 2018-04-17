    public static void parseMimeTypes(String file, Map mimeTypes) {
        BufferedReader mimefile = null;
        try {
            if (new File(file).exists() == true) {
                mimefile = new BufferedReader(new FileReader(file));
                String aLine, Mime, value;
                while (mimefile.ready()) {
                    aLine = mimefile.readLine();
                    if (aLine != null && aLine.length() > 0) {
                        aLine = aLine.trim().replace('\t', ' ');
                        if (aLine.length() > 0 && aLine.charAt(0) != '#') {
                            int positionOfSpace = aLine.indexOf(' ');
                            if (positionOfSpace != -1) {
                                Mime = aLine.substring(0, positionOfSpace).trim();
                                value = aLine.substring(positionOfSpace + 1).trim();
                                String aValue;
                                StringTokenizer valueTokened = new StringTokenizer(value);
                                while (valueTokened.hasMoreTokens() == true) {
                                    aValue = valueTokened.nextToken();
                                    mimeTypes.put(aValue, Mime);
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
                if (mimefile != null) mimefile.close();
            } catch (IOException e) {
            }
        }
    }
