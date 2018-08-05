    public void run() {
        String s, s2;
        s = "";
        s2 = "";
        try {
            URL url = new URL("http://www.m-w.com/dictionary/" + Word);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while (((str = in.readLine()) != null) && (!stopped)) {
                s = s + str;
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        Pattern pattern = Pattern.compile("popWin\\('/cgi-bin/(.+?)'", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(s);
        if ((!stopped) && (matcher.find())) {
            String newurl = "http://m-w.com/cgi-bin/" + matcher.group(1);
            try {
                URL url2 = new URL(newurl);
                BufferedReader in2 = new BufferedReader(new InputStreamReader(url2.openStream()));
                String str;
                while (((str = in2.readLine()) != null) && (!stopped)) {
                    s2 = s2 + str;
                }
                in2.close();
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            }
            Pattern pattern2 = Pattern.compile("<A HREF=\"http://(.+?)\">Click here to listen with your default audio player", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher matcher2 = pattern2.matcher(s2);
            if ((!stopped) && (matcher2.find())) {
                if (getWave("http://" + matcher2.group(1))) label.setEnabled(true);
            }
        }
        button.setEnabled(true);
    }
