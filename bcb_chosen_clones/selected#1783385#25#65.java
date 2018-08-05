    @Override
    public void render(IContentNode contentNode, Request req, Response resp, Application app, ServerInfo serverInfo) {
        Node fileNode = contentNode.getNode();
        try {
            Node res = fileNode.getNode("jcr:content");
            if (checkLastModified(res, req.getServletRequset(), resp.getServletResponse())) {
                return;
            }
            Property data = res.getProperty("jcr:data");
            InputStream is = data.getBinary().getStream();
            int contentLength = (int) data.getBinary().getSize();
            String mime;
            if (res.hasProperty("jcr:mimeType")) {
                mime = res.getProperty("jcr:mimeType").getString();
            } else {
                mime = serverInfo.getSerlvetContext().getMimeType(fileNode.getName());
            }
            if (mime != null && mime.startsWith("image")) {
                int w = req.getInt("w", 0);
                int h = req.getInt("h", 0);
                String fmt = req.get("fmt");
                if (w != 0 || h != 0 || fmt != null) {
                    Resource imgRes = ImageResource.create(is, mime.substring(6), w, h, req.getInt("cut", 0), fmt);
                    imgRes.process(serverInfo);
                    return;
                }
            }
            resp.getServletResponse().setContentType(mime);
            resp.getServletResponse().setContentLength(contentLength);
            OutputStream os = resp.getServletResponse().getOutputStream();
            IOUtils.copy(is, os);
            os.flush();
            os.close();
        } catch (PathNotFoundException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
