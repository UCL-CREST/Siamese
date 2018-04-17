    public void write(HttpServletRequest req, HttpServletResponse res, Object bean) throws IntrospectionException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException {
        res.setContentType(contentType);
        final Object r;
        if (HttpRpcServer.HttpRpcOutput.class.isAssignableFrom(bean.getClass())) {
            HttpRpcServer.HttpRpcOutput output = (HttpRpcServer.HttpRpcOutput) bean;
            r = output.getResult();
        } else r = bean;
        if (r != null) {
            final ServletOutputStream outputStream = res.getOutputStream();
            if (File.class.isAssignableFrom(r.getClass())) {
                File file = (File) r;
                InputStream in = null;
                try {
                    in = new FileInputStream(file);
                    IOUtils.copy(in, outputStream);
                } finally {
                    if (in != null) in.close();
                }
            } else if (InputStream.class.isAssignableFrom(r.getClass())) {
                InputStream in = null;
                try {
                    in = (InputStream) r;
                    if (ByteArrayInputStream.class.isAssignableFrom(r.getClass())) res.addHeader("Content-Length", Integer.toString(in.available()));
                    IOUtils.copy(in, outputStream);
                } finally {
                    if (in != null) in.close();
                }
            }
            outputStream.flush();
        }
    }
