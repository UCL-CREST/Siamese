    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpClientInfo clientInfo = HttpUtil.parseClientInfo((HttpServletRequest) request);
        if (request.getParameter("_debug_") != null) {
            StringBuffer buffer = new StringBuffer();
            Enumeration iter = request.getHeaderNames();
            while (iter.hasMoreElements()) {
                String name = (String) iter.nextElement();
                buffer.append(name + "=" + request.getHeader(name)).append("\n");
            }
            buffer.append("\n");
            iter = request.getParameterNames();
            while (iter.hasMoreElements()) {
                String name = (String) iter.nextElement();
                String value = request.getParameter(name);
                if (!"ISO-8859-1".equalsIgnoreCase(clientInfo.getPreferCharset())) value = new String(value.getBytes("ISO-8859-1"), clientInfo.getPreferCharset());
                buffer.append(name).append("=").append(value).append("\n");
            }
            response.setContentType("text/plain; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(buffer.toString());
            return null;
        }
        Object resultObj = handleRequest(request);
        if (resultObj == null) {
            String requestException = (String) request.getAttribute("XSMP.handleRequest.Exception");
            if (requestException != null) response.sendError(500, requestException); else response.setContentLength(0);
        } else {
            if (resultObj instanceof DataHandler) {
                response.setContentType(((DataHandler) resultObj).getContentType());
                response.setContentLength(((DataHandler) resultObj).getInputStream().available());
                IOUtils.copy(((DataHandler) resultObj).getInputStream(), response.getOutputStream());
            } else {
                String temp = resultObj.toString();
                if (temp.startsWith("<") && temp.endsWith(">")) response.setContentType("text/html; charset=" + clientInfo.getPreferCharset()); else response.setContentType("text/plain; charset=" + clientInfo.getPreferCharset());
                byte[] buffer = temp.getBytes(clientInfo.getPreferCharset());
                response.setContentLength(buffer.length);
                response.getOutputStream().write(buffer);
            }
        }
        return null;
    }
