    public static boolean postData(URL url, String parameters) {
        HttpURLConnection hpcon = null;
        try {
            hpcon = (HttpURLConnection) url.openConnection();
            hpcon.setRequestMethod("POST");
            hpcon.setRequestProperty("Content-Length", "" + Integer.toString(parameters.getBytes().length));
            hpcon.setUseCaches(false);
            hpcon.setDoInput(true);
            hpcon.setDoOutput(true);
            DataOutputStream printout = new DataOutputStream(hpcon.getOutputStream());
            printout.writeBytes(parameters);
            printout.flush();
            printout.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(hpcon.getInputStream()));
            String input;
            boolean success = false;
            while ((input = in.readLine()) != null) {
                if (input.contains("OK")) success = true;
            }
            return success;
        } catch (Exception e) {
            try {
                if (hpcon != null) hpcon.disconnect();
            } catch (Exception e2) {
            }
            return false;
        }
    }
