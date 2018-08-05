    protected void handleUrl(URL url) throws Exception {
        File file = new File(dir.getAbsolutePath() + "/" + new Date().getTime() + "." + this.ext);
        FileWriter writer = new FileWriter(file);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String s;
        while ((s = in.readLine()) != null) {
            writer.write(s + "\n");
        }
        in.close();
        writer.close();
    }
