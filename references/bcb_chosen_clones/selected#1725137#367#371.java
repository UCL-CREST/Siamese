    protected void streamResponse(HttpServletRequest request, HttpServletResponse response, HttpRequest proxyRequest, HttpResponse proxyResponse) throws IOException {
        InputStream input = proxyResponse.getEntity().getContent();
        ServletOutputStream output = response.getOutputStream();
        IOUtils.copy(input, output);
    }
