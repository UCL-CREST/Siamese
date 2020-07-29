    public void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try {
            httpServletResponse.setStatus(200);
            for (int i = 0; i < APJP_REMOTE_HTTPS_SERVER_RESPONSE_PROPERTY_KEY.length; i = i + 1) {
                if (APJP_REMOTE_HTTPS_SERVER_RESPONSE_PROPERTY_KEY[i].equalsIgnoreCase("") == false) {
                    httpServletResponse.addHeader(APJP_REMOTE_HTTPS_SERVER_RESPONSE_PROPERTY_KEY[i], APJP_REMOTE_HTTPS_SERVER_RESPONSE_PROPERTY_VALUE[i]);
                }
            }
            SecretKeySpec secretKeySpec = new SecretKeySpec(APJP_KEY.getBytes(), "ARCFOUR");
            Cipher inputStreamCipher = Cipher.getInstance("ARCFOUR");
            inputStreamCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            CipherInputStream httpRequestInputStream = new CipherInputStream(httpServletRequest.getInputStream(), inputStreamCipher);
            Cipher outputStreamCipher = Cipher.getInstance("ARCFOUR");
            outputStreamCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            CipherOutputStream httpResponseOutputStream = new CipherOutputStream(httpServletResponse.getOutputStream(), outputStreamCipher);
            HTTPRequestMessage httpRequestMessage1 = new HTTPRequestMessage(httpRequestInputStream);
            httpRequestMessage1.read();
            HTTPSRequest httpsRequest1 = new HTTPSRequest(httpRequestMessage1);
            httpsRequest1.open();
            try {
                HTTPResponseMessage httpResponseMessage1 = httpsRequest1.getHTTPResponseMessage();
                HTTPMessageHeader[] httpResponseMessage1Headers1 = httpResponseMessage1.getHTTPMessageHeaders();
                HTTPMessageHeader httpResponseMessage1Header1 = httpResponseMessage1Headers1[0];
                String httpResponseMessage1Header1Key1 = httpResponseMessage1Header1.getKey();
                String httpResponseMessage1Header1Value1 = httpResponseMessage1Header1.getValue();
                httpResponseOutputStream.write((httpResponseMessage1Header1Value1 + "\r\n").getBytes());
                for (int i = 1; i < httpResponseMessage1Headers1.length; i = i + 1) {
                    httpResponseMessage1Header1 = httpResponseMessage1Headers1[i];
                    httpResponseMessage1Header1Key1 = httpResponseMessage1Header1.getKey();
                    httpResponseMessage1Header1Value1 = httpResponseMessage1Header1.getValue();
                    httpResponseOutputStream.write((httpResponseMessage1Header1Key1 + ": " + httpResponseMessage1Header1Value1 + "\r\n").getBytes());
                }
                httpResponseOutputStream.write(("\r\n").getBytes());
                httpResponseMessage1.read(httpResponseOutputStream);
            } catch (Exception e) {
                throw e;
            } finally {
                try {
                    httpsRequest1.close();
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            logger.log(Level.INFO, "EXCEPTION", e);
            httpServletResponse.setStatus(500);
        }
    }
