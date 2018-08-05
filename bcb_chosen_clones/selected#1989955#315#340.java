        public void run() {
            try {
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setDoInput(true);
                byte[] encodedPassword = (username + ":" + password).getBytes();
                BASE64Encoder encoder = new BASE64Encoder();
                con.setRequestProperty("Authorization", "Basic " + encoder.encode(encodedPassword));
                InputStream is = con.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\n');
                    lastIteraction = System.currentTimeMillis();
                }
                rd.close();
                is.close();
                con.disconnect();
                result = response.toString();
                finish = true;
            } catch (Throwable e) {
                this.e = e;
            }
        }
