    protected UnicodeList(URL url) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new GZIPInputStream(url.openStream())));
            String line;
            line = br.readLine();
            chars = new ArrayList();
            while ((line = br.readLine()) != null) {
                String[] parts = GUIHelper.split(line, ";");
                if (parts[0].length() >= 5) continue;
                if (parts.length < 2 || parts[0].length() != 4) {
                    System.out.println("Strange line: " + line);
                } else {
                    if (parts.length > 10 && parts[1].equals("<control>")) {
                        parts[1] = parts[1] + ": " + parts[10];
                    }
                    try {
                        Integer.parseInt(parts[0], 16);
                        chars.add(parts[0] + parts[1]);
                    } catch (NumberFormatException ex) {
                        System.out.println("No number: " + line);
                    }
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
