    public String parse() {
        try {
            URL url = new URL(mUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            boolean flag1 = false;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!flag1 && line.contains("</center>")) flag1 = true;
                if (flag1 && line.contains("<br><center>")) break;
                if (flag1) {
                    mText.append(line);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mText.toString();
    }
