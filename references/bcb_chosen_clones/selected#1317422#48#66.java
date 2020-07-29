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
