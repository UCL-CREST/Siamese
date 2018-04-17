    public static void main(String[] args) throws Exception {
        SocketConnector socketConnector = new SocketConnector();
        socketConnector.setPort(6080);
        SslSocketConnector sslSocketConnector = new SslSocketConnector();
        sslSocketConnector.setPort(6443);
        String serverKeystore = MockHttpListenerWithAuthentication.class.getClassLoader().getResource("cert/serverkeystore.jks").getPath();
        sslSocketConnector.setKeystore(serverKeystore);
        sslSocketConnector.setKeyPassword("serverpass");
        String serverTruststore = MockHttpListenerWithAuthentication.class.getClassLoader().getResource("cert/servertruststore.jks").getPath();
        sslSocketConnector.setTruststore(serverTruststore);
        sslSocketConnector.setTrustPassword("serverpass");
        server.addConnector(socketConnector);
        server.addConnector(sslSocketConnector);
        SecurityHandler securityHandler = createBasicAuthenticationSecurityHandler();
        HandlerList handlerList = new HandlerList();
        handlerList.addHandler(securityHandler);
        handlerList.addHandler(new AbstractHandler() {

            @Override
            public void handle(String s, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
                System.out.println("uri: " + httpServletRequest.getRequestURI());
                System.out.println("queryString: " + httpServletRequest.getQueryString());
                System.out.println("method: " + httpServletRequest.getMethod());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                IOUtils.copy(httpServletRequest.getInputStream(), baos);
                System.out.println("body: " + baos.toString());
                PrintWriter writer = httpServletResponse.getWriter();
                writer.append("testsvar");
                Random r = new Random();
                for (int j = 0; j < 10; j++) {
                    int value = r.nextInt(Integer.MAX_VALUE);
                    writer.append(value + "");
                }
                System.out.println();
                writer.close();
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            }
        });
        server.addHandler(handlerList);
        server.start();
    }
