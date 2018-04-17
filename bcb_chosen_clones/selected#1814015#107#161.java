    private static void loadCommandList() {
        final URL url;
        try {
            url = IOUtils.getResource(null, PYTHON_MENU_FILE);
        } catch (final FileNotFoundException ex) {
            log.error("File '" + PYTHON_MENU_FILE + "': " + ex.getMessage());
            return;
        }
        final List<String> cmdList = new ArrayList<String>();
        try {
            final InputStream inputStream = url.openStream();
            try {
                final Reader reader = new InputStreamReader(inputStream, IOUtils.MAP_ENCODING);
                try {
                    final BufferedReader bufferedReader = new BufferedReader(reader);
                    try {
                        while (true) {
                            final String inputLine = bufferedReader.readLine();
                            if (inputLine == null) {
                                break;
                            }
                            final String line = inputLine.trim();
                            if (line.length() > 0 && !line.startsWith("#")) {
                                final int k = line.indexOf('(');
                                if (k > 0) {
                                    cmdList.add(line.substring(0, k) + "()");
                                } else {
                                    log.error("Parse error in " + url + ":");
                                    log.error("   \"" + line + "\" missing '()'");
                                    cmdList.add(line + "()");
                                }
                            }
                        }
                        Collections.sort(cmdList, String.CASE_INSENSITIVE_ORDER);
                        if (!cmdList.isEmpty()) {
                            menuEntries = cmdList.toArray(new String[cmdList.size()]);
                        }
                    } finally {
                        bufferedReader.close();
                    }
                } finally {
                    reader.close();
                }
            } finally {
                inputStream.close();
            }
        } catch (final FileNotFoundException ex) {
            log.error("File '" + url + "' not found: " + ex.getMessage());
        } catch (final EOFException ignored) {
        } catch (final UnsupportedEncodingException ex) {
            log.error("Cannot decode file '" + url + "': " + ex.getMessage());
        } catch (final IOException ex) {
            log.error("Cannot read file '" + url + "': " + ex.getMessage());
        }
    }
