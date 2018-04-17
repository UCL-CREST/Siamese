    private void loadMap() {
        final String wordList = "vietwordlist.txt";
        try {
            File dataFile = new File(supportDir, wordList);
            if (!dataFile.exists()) {
                final ReadableByteChannel input = Channels.newChannel(ClassLoader.getSystemResourceAsStream("dict/" + dataFile.getName()));
                final FileChannel output = new FileOutputStream(dataFile).getChannel();
                output.transferFrom(input, 0, 1000000L);
                input.close();
                output.close();
            }
            long fileLastModified = dataFile.lastModified();
            if (map == null) {
                map = new HashMap<String, String>();
            } else {
                if (fileLastModified <= mapLastModified) {
                    return;
                }
                map.clear();
            }
            mapLastModified = fileLastModified;
            BufferedReader bs = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile), "UTF-8"));
            String accented;
            while ((accented = bs.readLine()) != null) {
                String plain = VietUtilities.stripDiacritics(accented);
                map.put(plain.toLowerCase(), accented);
            }
            bs.close();
        } catch (IOException e) {
            map = null;
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, myResources.getString("Cannot_find_\"") + wordList + myResources.getString("\"_in\n") + supportDir.toString(), VietPad.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
    }
