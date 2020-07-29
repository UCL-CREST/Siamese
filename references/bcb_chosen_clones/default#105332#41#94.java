    @SuppressWarnings("unchecked")
    public void sendResponseData() {
        try {
            if (store.timerStatus == -1 || store.adminStatus == -1) {
                String timerError = "<html><head><title>Thread Error</title></head><body>" + "<h1>Thread Error</h1>The main admin or timer thread is not running, it has crash with the following error:<p>" + "<hr>" + "<pre>Timer Thread StackTrace:\n" + store.timerThreadErrorStack + "</pre><p>" + "<hr>" + "<pre>Admin Thread StackTrace:\n" + store.adminThreadErrorStack + "</pre><p>" + "<hr>" + "Use this information in any error report you submit." + "</body>";
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
                Constructor c = Class.forName(urlData.getServletClass()).getConstructor(paramTypes);
                Object params[] = {};
                HTTPResponse resp = (HTTPResponse) c.newInstance(params);
                resp.getResponse(urlData, outStream, headers);
                return;
            } else if (urlData.getRequestType() == 2) {
                returnFileContent(urlData.getReqString());
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
