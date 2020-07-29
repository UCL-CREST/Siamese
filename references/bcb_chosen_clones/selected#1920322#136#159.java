    public boolean load() {
        if (getFilename() != null && getFilename().length() > 0) {
            try {
                File file = new File(PreferencesManager.getDirectoryLocation("macros") + File.separator + getFilename());
                URL url = file.toURL();
                InputStreamReader isr = new InputStreamReader(url.openStream());
                BufferedReader br = new BufferedReader(isr);
                String line = br.readLine();
                String macro_text = "";
                while (line != null) {
                    macro_text = macro_text.concat(line);
                    line = br.readLine();
                    if (line != null) {
                        macro_text = macro_text.concat(System.getProperty("line.separator"));
                    }
                }
                code = macro_text;
            } catch (Exception e) {
                System.err.println("Exception at StoredMacro.load(): " + e.toString());
                return false;
            }
        }
        return true;
    }
