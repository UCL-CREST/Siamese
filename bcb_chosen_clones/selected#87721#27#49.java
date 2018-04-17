    @Override
    public Response callService(RemoteCallUrl urlModel) throws RemoteCallFailedException {
        String path;
        if (urlModel.getRootPath() != null) path = urlModel.getRootPath() + urlModel.getServicePath(); else path = urlModel.getServicePath();
        try {
            URL url = new URL(HTTP_PROTOCOL, urlModel.getHost(), urlModel.getPort(), path);
            URLConnection urlConnection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            String data = "";
            while ((inputLine = in.readLine()) != null) data += inputLine;
            Response remoteData = new Response(new String(data));
            remoteData.setStatus(urlConnection.getHeaderField(Response.RPC_STATUS_NAME));
            in.close();
            return remoteData;
        } catch (MalformedURLException e) {
            logger.error("Illegal URL. Cannot connect to this remote object", e);
            throw new RemoteCallFailedException("Illegal URL:" + e.getMessage(), e);
        } catch (IOException e) {
            logger.error("Cannot read from source. Please check remote object address", e);
            throw new RemoteCallFailedException("Fatal transport error:" + e.getMessage(), e);
        }
    }
