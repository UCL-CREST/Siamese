    protected Reader getText() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String readLine;
        do {
            readLine = br.readLine();
        } while (readLine != null && readLine.indexOf("</table><br clear=all>") < 0);
        return br;
    }
