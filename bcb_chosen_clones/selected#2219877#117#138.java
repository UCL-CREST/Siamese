    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL("http://www.foamsnet.com/smsapi/send.php?username=" + username + "&password=" + password + "&to=" + to + "&msg=" + URLEncoder.encode(msg));
            URLConnection urlc = url.openConnection();
            BufferedReader sin = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            String inputLine = sin.readLine();
            inputLine = inputLine == null ? "null" : inputLine;
            sin.close();
            output = inputLine;
            if (logsent) {
                ContentResolver contentResolver = cr;
                ContentValues values = new ContentValues();
                values.put("address", "+91" + inputLine.split(" ")[3]);
                values.put("body", msg);
                contentResolver.insert(Uri.parse("content://sms/sent"), values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
