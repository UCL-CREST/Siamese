    public HTTPResponseMessage getHTTPResponseMessage() throws HTTPSRequestException {
        try {
            HTTPMessageHeader[] httpRequestMessage1Headers1 = httpRequestMessage.getHTTPMessageHeaders();
            HTTPMessageHeader httpRequestMessage1Header1 = httpRequestMessage1Headers1[0];
            String httpRequestMessage1Header1Key1 = httpRequestMessage1Header1.getKey();
            String httpRequestMessage1Header1Value1 = httpRequestMessage1Header1.getValue();
            logger.log(2, "HTTPS_REQUEST/GET_RESPONSE_MESSAGE: REQUEST: " + httpRequestMessage1Header1Value1);
            String[] httpRequestMessage1Header1Values1 = httpRequestMessage1Header1Value1.split(" ");
            String httpRequestMessage1Header1Value2 = httpRequestMessage1Header1Values1[1];
            String[] httpRequestMessage1Header1Values2 = httpRequestMessage1Header1Value2.split("/", -1);
            String httpRequestMessage1Header1Value3 = httpRequestMessage1Header1Values2[0];
            if (httpRequestMessage1Header1Value3.equalsIgnoreCase("https:")) {
                httpRequestMessage1Header1Value2 = "";
                for (int j = 3; j < httpRequestMessage1Header1Values2.length; j = j + 1) {
                    httpRequestMessage1Header1Value2 = httpRequestMessage1Header1Value2 + "/" + httpRequestMessage1Header1Values2[j];
                }
                httpRequestMessage1Header1Values1[1] = httpRequestMessage1Header1Value2;
            }
            httpRequestMessage1Header1Values1[2] = "HTTP/1.0";
            httpRequestMessage1Header1Value1 = httpRequestMessage1Header1Values1[0];
            for (int j = 1; j < httpRequestMessage1Header1Values1.length; j = j + 1) {
                httpRequestMessage1Header1Value1 = httpRequestMessage1Header1Value1 + " " + httpRequestMessage1Header1Values1[j];
            }
            httpRequestMessage1Header1.setValue(httpRequestMessage1Header1Value1);
            for (int j = 1; j < httpRequestMessage1Headers1.length; j = j + 1) {
                httpRequestMessage1Header1 = httpRequestMessage1Headers1[j];
                httpRequestMessage1Header1Key1 = httpRequestMessage1Header1.getKey();
                httpRequestMessage1Header1Value1 = httpRequestMessage1Header1.getValue();
                if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Accept")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Accept-Charset")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Accept-Encoding")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Accept-Language")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Allow")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Authorization")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Cache-Control")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Connection")) {
                    httpRequestMessage.removeHTTPMessageHeader(httpRequestMessage1Header1);
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Content-Encoding")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Content-Language")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Content-Length")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Content-Location")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Content-MD5")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Content-Range")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Content-Type")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Date")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Expect")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Expires")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("From")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Host")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("If-Match")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("If-Modified-Since")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("If-None-Match")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("If-Range")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("If-Unmodified-Since")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Keep-Alive")) {
                    httpRequestMessage.removeHTTPMessageHeader(httpRequestMessage1Header1);
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Last-Modified")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Max-Forwards")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Pragma")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Proxy-Authorization")) {
                    httpRequestMessage.removeHTTPMessageHeader(httpRequestMessage1Header1);
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Proxy-Connection")) {
                    httpRequestMessage.removeHTTPMessageHeader(httpRequestMessage1Header1);
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Range")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Referer")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("TE")) {
                    httpRequestMessage.removeHTTPMessageHeader(httpRequestMessage1Header1);
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Trailer")) {
                    httpRequestMessage.removeHTTPMessageHeader(httpRequestMessage1Header1);
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Transfer-Encoding")) {
                    httpRequestMessage.removeHTTPMessageHeader(httpRequestMessage1Header1);
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Upgrade")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("User-Agent")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Via")) {
                } else if (httpRequestMessage1Header1Key1.equalsIgnoreCase("Warning")) {
                }
                logger.log(3, "HTTPS_REQUEST/GET_RESPONSE_MESSAGE: REQUEST: " + httpRequestMessage1Header1Key1 + ": " + httpRequestMessage1Header1Value1);
            }
            httpRequestMessage.addHTTPMessageHeader(new HTTPMessageHeader("Connection", "close"));
            SecretKeySpec secretKeySpec = new SecretKeySpec(APJP.APJP_KEY.getBytes(), "ARCFOUR");
            Cipher outputCipher = Cipher.getInstance("ARCFOUR");
            outputCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(urlConnection.getOutputStream(), outputCipher);
            HTTPMessageHeader[] httpRequestMessage1Headers2 = httpRequestMessage.getHTTPMessageHeaders();
            HTTPMessageHeader httpRequestMessage1Header2 = httpRequestMessage1Headers2[0];
            String httpRequestMessage1Header2Key1 = httpRequestMessage1Header2.getKey();
            String httpRequestMessage1Header2Value1 = httpRequestMessage1Header2.getValue();
            cipherOutputStream.write((httpRequestMessage1Header2Value1 + "\r\n").getBytes());
            for (int j = 1; j < httpRequestMessage1Headers2.length; j = j + 1) {
                httpRequestMessage1Header2 = httpRequestMessage1Headers2[j];
                httpRequestMessage1Header2Key1 = httpRequestMessage1Header2.getKey();
                httpRequestMessage1Header2Value1 = httpRequestMessage1Header2.getValue();
                cipherOutputStream.write((httpRequestMessage1Header2Key1 + ": " + httpRequestMessage1Header2Value1 + "\r\n").getBytes());
            }
            cipherOutputStream.write(("\r\n").getBytes());
            httpRequestMessage.read(cipherOutputStream);
            Cipher inputCipher = Cipher.getInstance("ARCFOUR");
            inputCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            CipherInputStream cipherInputStream = new CipherInputStream(urlConnection.getInputStream(), inputCipher);
            HTTPResponseMessage httpResponseMessage1 = new HTTPResponseMessage(httpRequestMessage, cipherInputStream);
            httpResponseMessage1.read();
            HTTPMessageHeader[] httpResponseMessage1Headers1 = httpResponseMessage1.getHTTPMessageHeaders();
            HTTPMessageHeader httpResponseMessage1Header1 = httpResponseMessage1Headers1[0];
            String httpResponseMessage1Header1Key1 = httpResponseMessage1Header1.getKey();
            String httpResponseMessage1Header1Value1 = httpResponseMessage1Header1.getValue();
            logger.log(2, "HTTPS_REQUEST/GET_RESPONSE_MESSAGE: RESPONSE: " + httpResponseMessage1Header1Value1);
            String[] httpResponseMessage1Header1Values1 = httpResponseMessage1Header1Value1.split(" ");
            httpResponseMessage1Header1Values1[0] = "HTTP/1.0";
            httpResponseMessage1Header1Value1 = httpResponseMessage1Header1Values1[0];
            for (int j = 1; j < httpResponseMessage1Header1Values1.length; j = j + 1) {
                httpResponseMessage1Header1Value1 = httpResponseMessage1Header1Value1 + " " + httpResponseMessage1Header1Values1[j];
            }
            httpResponseMessage1Header1.setValue(httpResponseMessage1Header1Value1);
            for (int j = 1; j < httpResponseMessage1Headers1.length; j = j + 1) {
                httpResponseMessage1Header1 = httpResponseMessage1Headers1[j];
                httpResponseMessage1Header1Key1 = httpResponseMessage1Header1.getKey();
                httpResponseMessage1Header1Value1 = httpResponseMessage1Header1.getValue();
                if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Accept-Ranges")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Age")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Allow")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Cache-Control")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Connection")) {
                    httpResponseMessage1.removeHTTPMessageHeader(httpResponseMessage1Header1);
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Content-Encoding")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Content-Language")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Content-Length")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Content-Location")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Content-MD5")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Content-Range")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Content-Type")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Date")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("ETag")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Expires")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Keep-Alive")) {
                    httpResponseMessage1.removeHTTPMessageHeader(httpResponseMessage1Header1);
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Last-Modified")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Location")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Pragma")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Proxy-Authenticate")) {
                    httpResponseMessage1.removeHTTPMessageHeader(httpResponseMessage1Header1);
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Proxy-Connection")) {
                    httpResponseMessage1.removeHTTPMessageHeader(httpResponseMessage1Header1);
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Retry-After")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Server")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Trailer")) {
                    httpResponseMessage1.removeHTTPMessageHeader(httpResponseMessage1Header1);
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Transfer-Encoding")) {
                    httpResponseMessage1.removeHTTPMessageHeader(httpResponseMessage1Header1);
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Vary")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Upgrade")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Via")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("Warning")) {
                } else if (httpResponseMessage1Header1Key1.equalsIgnoreCase("WWW-Authenticate")) {
                }
                logger.log(3, "HTTPS_REQUEST/GET_RESPONSE_MESSAGE: RESPONSE: " + httpResponseMessage1Header1Key1 + ": " + httpResponseMessage1Header1Value1);
            }
            httpResponseMessage1.addHTTPMessageHeader(new HTTPMessageHeader("Connection", "close"));
            return httpResponseMessage1;
        } catch (Exception e) {
            throw new HTTPSRequestException("HTTPS_REQUEST/GET_HTTP_RESPONSE_MESSAGE", e);
        }
    }
