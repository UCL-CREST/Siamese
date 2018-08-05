    private static void main(String mp3Path) throws IOException {
        String convPath = "http://android.adinterest.biz/wav2mp3.php?k=";
        String uri = convPath + mp3Path;
        URL rssurl = new URL(uri);
        InputStream is = rssurl.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String buf = "";
        while ((buf = br.readLine()) != null) {
        }
        is.close();
        br.close();
    }
