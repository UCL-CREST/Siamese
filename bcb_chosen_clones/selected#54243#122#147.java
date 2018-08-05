    private String doExecute(AbortableHttpRequest method) throws Throwable {
        HttpClient client = CLIENT.newInstance();
        HttpResponse rsp = client.execute((HttpUriRequest) method);
        HttpEntity entity = rsp.getEntity();
        if (entity == null) throw new RequestError("No entity in method");
        InputStream in = null;
        try {
            in = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder inStr = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                inStr.append(line).append("\r\n");
            }
            entity.consumeContent();
            return inStr.toString();
        } catch (IOException ex) {
            LOG.error("IO exception: " + ex.getMessage());
            throw ex;
        } catch (RuntimeException ex) {
            method.abort();
            throw ex;
        } finally {
            if (in != null) in.close();
        }
    }
