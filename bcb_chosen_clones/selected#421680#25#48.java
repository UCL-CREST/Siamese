    @Override
    protected void processImport() throws SudokuInvalidFormatException {
        importFolder(mUri.getLastPathSegment());
        try {
            URL url = new URL(mUri.toString());
            InputStreamReader isr = new InputStreamReader(url.openStream());
            BufferedReader br = null;
            try {
                br = new BufferedReader(isr);
                String s;
                while ((s = br.readLine()) != null) {
                    if (!s.equals("")) {
                        importGame(s);
                    }
                }
            } finally {
                if (br != null) br.close();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
