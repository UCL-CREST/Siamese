    public void sendResponseData() {
        try {
            if (store.timerStatus == -1) {
                String timerError = "<html><head><title>Timer Thread Error</title></head><body>" + "<h1>Timer Thread Error</h1>The main timer thread is not running, it has crash with the following error:<p>" + "<pre>StackTrace:\n" + store.timerThreadErrorStack + "</pre><p>" + "Please post this error on the forum." + "</body>";
                outStream.write(timerError.getBytes());
                return;
            }
            AccessControl ac = AccessControl.getInstance();
            if (ac.authenticateUser(headers, urlData) == false) {
                System.out.println("Access denied from IP : " + headers.get("RemoteAddress"));
                StringBuffer out = new StringBuffer(4096);
                out.append("HTTP/1.0 401 Unauthorized\r\n");
                out.append("WWW-Authenticate: BASIC realm=\"TV Scheduler Pro\"\r\n");
                out.append("Cache-Control: no-cache\r\n\r\nAccess denied for area.");
                outStream.write(out.toString().getBytes());
                return;
            } else if (urlData.getRequestType() == 3) {
                Class paramTypes[] = {};
                java.lang.reflect.Constructor c = Class.forName(urlData.getServletClass()).getConstructor(paramTypes);
                Object params[] = {};
                HTTPResponse resp = (HTTPResponse) c.newInstance(params);
                resp.getResponse(urlData, outStream, headers);
                return;
            } else if (urlData.getRequestType() == 2) {
                returnFileContent(store.getProperty("path.httproot") + urlData.getReqString());
                return;
            } else if (urlData.getRequestType() == 1) {
                SystemStatusData sd = new SystemStatusData();
                outStream.write(sd.getStatusXML(urlData, headers));
                return;
            }
            PageTemplate page = new PageTemplate(store.getProperty("path.template") + File.separator + "error.html");
            page.replaceAll("$error", "Request not known\n\n" + requestInfo());
            outStream.write(page.getPageBytes());
        } catch (Exception e) {
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            PrintWriter err = new PrintWriter(ba);
            try {
                e.printStackTrace(err);
                err.flush();
                PageTemplate page = new PageTemplate(store.getProperty("path.template") + File.separator + "error.html");
                page.replaceAll("$error", HTMLEncoder.encode(urlData.toString()) + "\n\n" + HTMLEncoder.encode(ba.toString()));
                outStream.write(page.getPageBytes());
            } catch (Exception e2) {
                try {
                    outStream.write(ba.toString().getBytes());
                } catch (Exception e3) {
                }
            }
            System.out.println("HTTP Request Exception: " + e);
            e.printStackTrace();
        }
    }
