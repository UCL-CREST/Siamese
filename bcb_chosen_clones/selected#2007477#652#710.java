    private static void sendExceptionToServer(String server, Throwable ex, String config, String prob) {
        try {
            StringBuilder dataSB = new StringBuilder();
            dataSB.append(URLEncoder.encode("secret", "UTF-8"));
            dataSB.append('=');
            dataSB.append(URLEncoder.encode("badsecurity", "UTF-8"));
            dataSB.append('&');
            dataSB.append(URLEncoder.encode("version", "UTF-8"));
            dataSB.append('=');
            dataSB.append(URLEncoder.encode(BuildInfo.revisionNumber, "UTF-8"));
            dataSB.append('&');
            dataSB.append(URLEncoder.encode("os", "UTF-8"));
            dataSB.append('=');
            dataSB.append(URLEncoder.encode(System.getProperty("os.name") + " " + System.getProperty("os.version"), "UTF-8"));
            dataSB.append('&');
            dataSB.append(URLEncoder.encode("user", "UTF-8"));
            dataSB.append('=');
            dataSB.append(URLEncoder.encode(System.getProperty("user.name"), "UTF-8"));
            dataSB.append('&');
            dataSB.append(URLEncoder.encode("msg", "UTF-8"));
            dataSB.append('=');
            dataSB.append(URLEncoder.encode(ex.getMessage(), "UTF-8"));
            ByteArrayOutputStream trace = new ByteArrayOutputStream();
            ex.printStackTrace(new PrintStream(trace));
            dataSB.append('&');
            dataSB.append(URLEncoder.encode("trace", "UTF-8"));
            dataSB.append('=');
            dataSB.append(URLEncoder.encode(trace.toString(), "UTF-8"));
            if (config != null) {
                dataSB.append('&');
                dataSB.append(URLEncoder.encode("config", "UTF-8"));
                dataSB.append('=');
                dataSB.append(URLEncoder.encode(config, "UTF-8"));
            }
            if (prob != null) {
                dataSB.append('&');
                dataSB.append(URLEncoder.encode("problem", "UTF-8"));
                dataSB.append('=');
                dataSB.append(URLEncoder.encode(prob, "UTF-8"));
            }
            URL url = new URL(errorServerURL);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(dataSB.toString());
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = null;
            String line = null;
            while ((line = rd.readLine()) != null) {
                if (response == null) response = line; else System.out.println(line);
            }
            wr.close();
            rd.close();
            if (response.equals("success")) System.out.println("Exception sent to maRla development team"); else System.out.println("Unable to send exception to development team: " + response);
        } catch (IOException ex2) {
            System.out.println("Unable to send exception to development team: " + ex2.getMessage());
        }
    }
