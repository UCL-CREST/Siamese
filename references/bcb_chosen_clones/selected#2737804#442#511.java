        private void serveCGI(TinyCGI script) throws IOException, TinyWebThreadException {
            parseHTTPHeaders();
            OutputStream cgiOut = null;
            File tempFile = null;
            try {
                if (script == null) sendError(500, "Internal Error", "Couldn't load script.");
                if (script instanceof TinyCGIHighVolume) {
                    tempFile = File.createTempFile("cgi", null);
                    cgiOut = new FileOutputStream(tempFile);
                } else {
                    cgiOut = new ByteArrayOutputStream();
                }
                script.service(inputStream, cgiOut, env);
            } catch (Exception cgie) {
                this.exceptionEncountered = cgie;
                if (tempFile != null) tempFile.delete();
                if (clientSocket == null) {
                    return;
                } else if (cgie instanceof TinyCGIException) {
                    TinyCGIException tce = (TinyCGIException) cgie;
                    sendError(tce.getStatus(), tce.getTitle(), tce.getText(), tce.getOtherHeaders());
                } else {
                    StringWriter w = new StringWriter();
                    cgie.printStackTrace(new PrintWriter(w));
                    sendError(500, "CGI Error", "Error running script: " + "<PRE>" + w.toString() + "</PRE>");
                }
            } finally {
                if (script != null) doneWithScript(script);
            }
            InputStream cgiResults = null;
            long totalSize = 0;
            if (tempFile == null) {
                byte[] results = ((ByteArrayOutputStream) cgiOut).toByteArray();
                totalSize = results.length;
                cgiResults = new ByteArrayInputStream(results);
            } else {
                cgiOut.close();
                totalSize = tempFile.length();
                cgiResults = new FileInputStream(tempFile);
            }
            String contentType = null, statusString = "OK", line, header;
            StringBuffer otherHeaders = new StringBuffer();
            StringBuffer text = new StringBuffer();
            int status = 200;
            int headerLength = 0;
            while (true) {
                line = readLine(cgiResults, true);
                headerLength += line.length();
                if (line.charAt(0) == '\r' || line.charAt(0) == '\n') break;
                header = parseHeader(line, text);
                if (header.toUpperCase().equals("STATUS")) {
                    statusString = text.toString();
                    status = Integer.parseInt(statusString.substring(0, 3));
                    statusString = statusString.substring(4);
                } else if (header.toUpperCase().equals("CONTENT-TYPE")) contentType = text.toString(); else {
                    if (header.toUpperCase().equals("LOCATION")) status = 302;
                    otherHeaders.append(header).append(": ").append(text.toString()).append(CRLF);
                }
            }
            sendHeaders(status, statusString, contentType, totalSize - headerLength, -1, otherHeaders.toString());
            byte[] buf = new byte[2048];
            int bytesRead;
            while ((bytesRead = cgiResults.read(buf)) != -1) outputStream.write(buf, 0, bytesRead);
            outputStream.flush();
            try {
                cgiResults.close();
                if (tempFile != null) tempFile.delete();
            } catch (IOException ioe) {
            }
        }
