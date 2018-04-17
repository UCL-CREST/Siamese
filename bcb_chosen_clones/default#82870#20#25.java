    public void getResponse(HTTPurl urlData, OutputStream outStream) throws Exception {
        String action = urlData.getParameter("action");
        Method m = this.getClass().getMethod(action, new Class[] { HTTPurl.class, OutputStream.class });
        Object ret = m.invoke(this, urlData, outStream);
        outStream.write((byte[]) ret);
    }
