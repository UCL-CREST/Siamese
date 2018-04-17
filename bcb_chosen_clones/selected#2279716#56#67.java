    public void play(File file) {
        try {
            URL url = new URL("http://127.0.0.1:8081/play.html?type=4&file=" + file.getAbsolutePath() + "&name=toto");
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) System.out.println(inputLine);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
