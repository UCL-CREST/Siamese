    public void go() {
        DataOutputStream outStream = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Length", new Integer(sendData.length()).toString());
            connection.setRequestProperty("Content-type", "text/html");
            connection.setRequestProperty("User-Agent", "Pago HTTP cartridge");
            outStream = new DataOutputStream(connection.getOutputStream());
            outStream.writeBytes(sendData);
            System.out.println(1);
            InputStream is = connection.getInputStream();
            System.out.println(2);
            inReader = new BufferedReader(new InputStreamReader(is));
            String result;
            System.out.println(3);
            if ((result = inReader.readLine()) != null) {
                System.out.println(result);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(0);
        } finally {
            try {
                if (outStream != null) outStream.close();
                if (inReader != null) inReader.close();
            } catch (IOException ioe) {
                System.err.println("Error closing Streams!");
                ioe.printStackTrace();
            }
            connection.disconnect();
        }
    }
