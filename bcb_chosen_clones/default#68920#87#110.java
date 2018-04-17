                public void run() {
                    try {
                        int id = getID() - 1;
                        String file = id + ".dem";
                        String data = URLEncoder.encode("file", "UTF-8") + "=" + URLEncoder.encode(file, "UTF-8");
                        data += "&" + URLEncoder.encode("hash", "UTF-8") + "=" + URLEncoder.encode(getMD5Digest("tf2invite" + file), "UTF-8");
                        URL url = new URL("http://94.23.189.99/ftp.php");
                        final URLConnection conn = url.openConnection();
                        conn.setDoOutput(true);
                        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                        wr.write(data);
                        wr.flush();
                        String line;
                        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line = rd.readLine()) != null) {
                            System.out.println(line);
                            if (line.startsWith("demo=")) msg("2The last gather demo has been uploaded successfully: " + line.split("=")[1]);
                        }
                        rd.close();
                        wr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
