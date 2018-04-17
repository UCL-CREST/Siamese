    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        URL url;
        try {
            url = new URL("platform:/plugin/de.vogella.rcp.plugin.filereader/files/test.txt");
            InputStream inputStream = url.openConnection().getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
