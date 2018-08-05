    private void processRequest() throws Exception {
        boolean validRequest = true;
        int CGIHeaderCount = 0;
        String headerLine = br.readLine();
        dkjs.Message(headerLine, "rh");
        StringTokenizer s = new StringTokenizer(headerLine);
        if (s.hasMoreTokens()) requestMethod = s.nextToken(); else {
            output.write(Error(400, "Your Browser send an invalid request").getBytes());
            closeConnection();
            validRequest = false;
        }
        if (validRequest && s.hasMoreTokens()) requestString = s.nextToken(); else {
            output.write(Error(400, "Your Browser send an invalid request").getBytes());
            closeConnection();
            validRequest = false;
        }
        if (validRequest && (!s.hasMoreElements() || !s.nextToken().equals("HTTP/1.1"))) {
            output.write(Error(505, "").getBytes());
            closeConnection();
            validRequest = false;
        }
        if (validRequest) {
            if (requestMethod.equals("GET")) dkjs.Message("GET request identified", "im"); else if (requestMethod.equals("POST")) dkjs.Message("POST request identified", "im"); else if (requestMethod.equals("HEAD")) dkjs.Message("HEAD request indentified. " + "No Body will be send.", "im"); else if (requestMethod.equals("OPTIONS") || requestMethod.equals("PUT") || requestMethod.equals("DELETE") || requestMethod.equals("TRACE") || requestMethod.equals("CONNECT")) {
                output.write(Error(501, requestMethod).getBytes());
                closeConnection();
                validRequest = false;
            } else {
                output.write(Error(400, "Your browser send a bad method. Giving up").getBytes());
                closeConnection();
                validRequest = false;
            }
        }
        while (validRequest) {
            headerLine = br.readLine();
            if (headerLine.equals(CRLF) || headerLine.equals("")) break;
            if (CGIHeaderCount < 30 && headerLine.indexOf(":") != -1) CGIHeader[CGIHeaderCount++] = "HTTP_" + headerLine.substring(0, headerLine.indexOf(":")).toUpperCase() + "=" + headerLine.substring(headerLine.indexOf(":") + 1).trim();
            if (headerLine.length() > 17 && headerLine.substring(0, 18).equalsIgnoreCase("If-Modified-Since:")) {
                dkjs.Message("\"If-Modified-Since\"-header identified", "im");
                ifModifiedSince = parseDate.getMillisFromDateString(headerLine.substring(18));
            } else if (headerLine.length() > 12 && headerLine.substring(0, 13).equalsIgnoreCase("Content-Type:")) {
                if (requestMethod.equals("POST")) {
                    CGIContentType = headerLine.substring(13).trim();
                    dkjs.Message("\"Content-Type\"-header indentified", "im");
                }
            } else if (headerLine.length() > 14 && headerLine.substring(0, 15).equalsIgnoreCase("Content-Length:")) {
                if (requestMethod.equals("POST")) {
                    try {
                        CGIContentLength = Integer.parseInt(headerLine.substring(15).trim());
                        dkjs.Message("\"Content-Length\"-header identified", "im");
                    } catch (NumberFormatException e) {
                    }
                }
            } else dkjs.Message("Ignoring bad or unsupported header: " + headerLine, "wl");
        }
        if (validRequest) {
            if (requestString.length() > 7 && requestString.substring(0, 7).equalsIgnoreCase("http://")) {
                requestedURI = requestString.substring(7);
                requestedHost = requestedURI.substring(0, requestedURI.indexOf("/"));
                requestedURI = requestedURI.substring(requestedURI.indexOf("/"));
            } else requestedURI = requestString;
            if (requestedURI.indexOf("?") >= 0) {
                requestParameter = requestedURI.substring(requestedURI.indexOf("?") + 1);
                requestedURI = requestedURI.substring(0, requestedURI.indexOf("?"));
            }
            String serverRoot = "";
            try {
                serverRoot = dkjs.getServerRootByHostName(requestedHost);
            } catch (UnknownHostException e) {
                output.write(Error(400, "The requested Host " + requestedHost + " is not available on this server.").getBytes());
                closeConnection();
                validRequest = false;
            } catch (MalformedURLException e) {
                output.write(Error(400, "The requested host " + requestedHost + " is invalid.").getBytes());
                closeConnection();
                validRequest = false;
            }
            if (validRequest) {
                File tmpFile;
                String tmpURI = requestedURI, tmpPath = "", tmpToken, tmpFileName;
                int tmpCountSubdirectories = 0;
                while (!tmpURI.equals("") && validRequest) {
                    if (tmpURI.indexOf("/", 1) > 0) {
                        tmpToken = tmpURI.substring(0, tmpURI.indexOf("/", 1));
                        tmpURI = tmpURI.substring(tmpURI.indexOf("/", 1));
                    } else {
                        tmpToken = tmpURI;
                        tmpURI = "";
                    }
                    tmpFileName = serverRoot + tmpPath + tmpToken;
                    tmpFile = new File(tmpFileName);
                    if (tmpFile.isDirectory()) {
                        if (tmpToken.equals("/..")) tmpCountSubdirectories--; else tmpCountSubdirectories++;
                        tmpPath += tmpToken;
                    } else {
                        if (tmpFile.isFile()) {
                            CGIPathInfo = URLDecoder.decode(tmpURI);
                            tmpPath += tmpToken;
                        } else {
                            CGIPathInfo = URLDecoder.decode(tmpToken + tmpURI);
                        }
                        tmpURI = "";
                    }
                    if (tmpCountSubdirectories < 0) {
                        output.write(Error(403, requestedURI).getBytes());
                        closeConnection();
                        validRequest = false;
                    }
                }
                requestedURI = tmpPath;
            }
            File requestedFile = new File(serverRoot + requestedURI);
            if (validRequest) {
                if (requestedFile.exists()) {
                    if (requestedFile.isDirectory()) requestedFile = dkjs.getDefaultDocument(requestedFile);
                    if (!requestedFile.isFile()) {
                        output.write(Error(403, requestedURI).getBytes());
                        closeConnection();
                        validRequest = false;
                    } else if (!requestedFile.canRead()) {
                        output.write(Error(403, requestedURI).getBytes());
                        closeConnection();
                        validRequest = false;
                    }
                } else {
                    output.write(Error(404, requestedURI).getBytes());
                    closeConnection();
                    validRequest = false;
                }
            }
            if (validRequest) {
                if (requestedFile.getName().endsWith(".cgi") || requestedFile.getName().endsWith(".php")) {
                    dkjs.Message("The requested URI is a script", "im");
                    String Command[] = { "", "", "" };
                    String Env[] = new String[EnvLength];
                    for (int a = 0; a < EnvLength; a++) Env[a] = "";
                    int i = 0;
                    try {
                        Env[i++] = "GATEWAY_INTERFACE=CGI/1.1";
                        if (!CGIPathInfo.equals("")) Env[i++] = "PATH_INFO=" + CGIPathInfo;
                        if (!requestParameter.equals("")) Env[i++] = "QUERY_STRING=" + requestParameter;
                        Env[i++] = "REMOTE_ADDR=" + remoteAddr;
                        Env[i++] = "REMOTE_HOST=" + remoteHostName;
                        Env[i++] = "REMOTE_PORT=" + remotePort;
                        Env[i++] = "REQUEST_METHOD=" + requestMethod;
                        Env[i++] = "SCRIPT_NAME=" + requestedURI;
                        Env[i++] = "SERVER_ADDR=" + serverAddr;
                        Env[i++] = "SERVER_NAME=" + serverHostName;
                        Env[i++] = "SERVER_PORT=" + serverPort;
                        Env[i++] = "SERVER_PROTOCOL=HTTP/1.1";
                        Env[i++] = "SERVER_SOFTWARE=" + dkjs.getName() + "/" + dkjs.getVersion();
                        Env[i++] = "DOCUMENT_ROOT=" + serverRoot;
                        Env[i++] = "SCRIPT_FILENAME=" + serverRoot + requestedURI;
                        if (requestMethod.equals("POST")) {
                            Env[i++] = "CONTENT_LENGTH=" + CGIContentLength;
                            Env[i++] = "CONTENT_TYPE=" + CGIContentType;
                        }
                        if (i > EnvLength - 30) dkjs.Message("There are many environment variables. " + "If a client sends many header\nlines, " + "the server can NOT provide the header " + "lines from the request to\nthe script " + "file. It is strogly recommend to " + "increment the static\nvariable " + "EnvLenght at the top of " + "httpRequestHandler.java to solve" + "\nthis problem.", "wh");
                        for (int a = 0; a < CGIHeaderCount; a++) Env[i++] = CGIHeader[a];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        dkjs.Message("There are to many environment variables. " + "The server can NOT provide\nall header " + "lines from the request to the script " + "file. It is strongly\nrecommend to " + "increment the static variable EnvLenght " + "at the top of\nhttpRequestHandler.java " + "to solve this " + "problem.", "eh");
                    }
                    if (requestedURI.endsWith(".php")) {
                        Command[0] = dkjs.getPhpPath();
                        Command[1] = requestedFile.getName();
                    } else Command[0] = requestedFile.getName();
                    try {
                        dkjs.Message("Starting " + Command[0] + " " + Command[1] + " " + Command[2], "im");
                        Process pr = Runtime.getRuntime().exec(Command, Env);
                        if (requestMethod.equals("POST")) {
                            BufferedWriter prIn = new BufferedWriter(new OutputStreamWriter(pr.getOutputStream()));
                            char Char[] = new char[100];
                            int Pos = 0, length;
                            dkjs.Message("Sending POST data to the script", "im");
                            try {
                                while (Pos < CGIContentLength && (length = br.read(Char, 0, 100)) != -1) {
                                    prIn.write(Char, 0, 100);
                                    Pos += length;
                                }
                            } catch (IOException e) {
                            }
                            prIn.close();
                        }
                        BufferedReader prOut = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                        String line = prOut.readLine();
                        if (line.startsWith("HTTP/1.1")) {
                            output.write(line.getBytes());
                            while ((line = prOut.readLine()) != null) {
                                output.write(line.getBytes());
                            }
                        } else {
                            String contentType = "", location = "", status = "200 OK";
                            String server = dkjs.getName() + "/" + dkjs.getVersion();
                            String date = parseDate.getDateStringFromMillis(new Date().getTime());
                            String responseHeader[] = new String[20];
                            int responseHeaderCount = 0;
                            do {
                                dkjs.Message("Header send by the script: " + line, "il");
                                if (line.length() > 8 && line.substring(0, 9).equalsIgnoreCase("Location:")) location = line.substring(9).trim(); else if (line.length() > 12 && line.substring(0, 13).equalsIgnoreCase("Content-Type:")) contentType = line.substring(13).trim(); else if (line.length() > 6 && line.substring(0, 7).equalsIgnoreCase("Status:")) status = line.substring(7).trim(); else if (line.length() > 4 && line.substring(0, 5).equalsIgnoreCase("Date:")) date = line.substring(5).trim(); else if (responseHeaderCount < 20) responseHeader[responseHeaderCount++] = line.trim();
                            } while ((line = prOut.readLine()) != null && !line.equals("") && !line.equals(CRLF));
                            if (!location.equals("")) output.write(Error(303, location).getBytes()); else if (status.equals("200 OK") && contentType.equals("")) output.write(Error(500, "").getBytes()); else {
                                String header = "HTTP/1.1 " + status + CRLF;
                                if (!contentType.equals("")) header += "Content-Type: " + contentType + CRLF;
                                header += "Server: " + server + CRLF + "Date: " + date + CRLF + "Connection: close" + CRLF;
                                for (int a = 0; a < responseHeaderCount; a++) header += responseHeader[a] + CRLF;
                                output.write((header + CRLF).getBytes());
                                if (!requestMethod.equals("HEAD")) {
                                    dkjs.Message("Sending \"" + status + "\" and data " + "from the script.", "sh");
                                    while ((line = prOut.readLine()) != null) output.write((line + "\n").getBytes());
                                } else dkjs.Message("Sending \"" + status + "\" without body.", "sh");
                            }
                        }
                        dkjs.Message("Killing the process if still running.", "im");
                        pr.destroy();
                        closeConnection();
                    } catch (IOException e) {
                        output.write(Error(500, requestedURI).getBytes());
                        closeConnection();
                    }
                } else {
                    Date date = new Date();
                    if (!CGIPathInfo.equals("")) {
                        output.write(Error(404, requestedURI + CGIPathInfo).getBytes());
                        closeConnection();
                    } else if (ifModifiedSince >= requestedFile.lastModified()) {
                        dkjs.Message("The file was not modified", "im");
                        dkjs.Message("Sending \"304 Not Modified\"", "sh");
                        String header = "HTTP/1.1 304 Not Modified" + CRLF + "Server: " + dkjs.getName() + "/" + dkjs.getVersion() + CRLF + "Date: " + parseDate.getDateStringFromMillis(date.getTime()) + CRLF + "Connection: close" + CRLF;
                        output.write((header + CRLF).getBytes());
                        closeConnection();
                    } else {
                        String header = "HTTP/1.1 200 OK" + CRLF + "Server: " + dkjs.getName() + "/" + dkjs.getVersion() + CRLF + "Date: " + parseDate.getDateStringFromMillis(date.getTime()) + CRLF;
                        if (requestedFile.getName().endsWith(".html") || requestedFile.getName().endsWith(".htm") || requestedFile.getName().endsWith(".xhtml")) header += "Content-Type: text/html" + CRLF; else if (requestedFile.getName().endsWith(".xml")) header += "Content-Type: text/xml" + CRLF; else if (requestedFile.getName().endsWith(".txt")) header += "Content-Type: text/plain" + CRLF; else if (requestedFile.getName().endsWith(".css")) header += "Content-Type: text/css" + CRLF; else if (requestedFile.getName().endsWith(".png")) header += "Content-Type: image/png" + CRLF; else if (requestedFile.getName().endsWith(".jpg") || requestedFile.getName().endsWith(".jpeg")) header += "Content-Type: image/jpeg" + CRLF; else if (requestedFile.getName().endsWith(".gif")) header += "Content-Type: image/gif" + CRLF; else header += "Content-Type: application/octet-stream" + CRLF;
                        header += "Content-Length: " + requestedFile.length() + CRLF + "Last-Modified: " + parseDate.getDateStringFromMillis(requestedFile.lastModified()) + CRLF + "Connection: close" + CRLF;
                        FileInputStream fis = null;
                        try {
                            fis = new FileInputStream(requestedFile);
                            if (requestMethod.equals("GET")) {
                                dkjs.Message("Everything is looking fine.", "il");
                                dkjs.Message("Sending \"200 OK\" and " + requestedFile.toString(), "sh");
                            } else {
                                dkjs.Message("Everything is looking fine.", "il");
                                dkjs.Message("Sending \"200 OK\" without body", "sh");
                            }
                            output.write((header + CRLF).getBytes());
                        } catch (FileNotFoundException e) {
                            output.write(Error(404, requestedURI).getBytes());
                            closeConnection();
                            validRequest = false;
                        }
                        if (validRequest && requestMethod.equals("GET")) {
                            byte[] buffer = new byte[1024];
                            int bytes = 0;
                            while ((bytes = fis.read(buffer)) != -1) output.write(buffer, 0, bytes);
                        }
                        closeConnection();
                    }
                }
            }
        }
    }
