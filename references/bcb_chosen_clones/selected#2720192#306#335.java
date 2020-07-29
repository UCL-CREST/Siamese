    public void loadFromFile() {
        if (filename == null) return;
        try {
            BufferedReader reader;
            try {
                File file = new File(filename);
                if (!file.exists()) return;
                reader = new BufferedReader(new FileReader(file));
            } catch (java.security.AccessControlException e) {
                URL url = new URL(filename);
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
            }
            String line;
            while (true) {
                line = reader.readLine();
                if (line == null) break;
                String name = line.substring(0, line.indexOf("\t"));
                String rest1 = line.substring(line.indexOf("\t") + 1);
                String guiname = rest1.substring(0, rest1.indexOf("\t"));
                String rest2 = rest1.substring(rest1.indexOf("\t") + 1);
                String type = rest2.substring(0, rest2.indexOf("\t"));
                String value = rest2.substring(rest2.indexOf("\t") + 1);
                defineField(name, guiname, type);
                setField(name, value, true);
            }
            reader.close();
        } catch (IOException e) {
            throw new JGameError("Error reading file '" + filename + "'.", false);
        }
    }
