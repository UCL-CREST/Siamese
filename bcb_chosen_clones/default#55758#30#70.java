    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
        String queryCommand = null;
        if (request.getParameter("command") != null) queryCommand = request.getParameter("command"); else queryCommand = request.getParameter("cmd");
        String queryAction = request.getParameter("action");
        String queryContentType = "text/plain";
        if (request.getParameter("contentType") != null) queryContentType = request.getParameter("contentType");
        response.setContentType(queryContentType);
        java.io.Writer w = response.getWriter();
        String remoteResult = null;
        String[] remoteResultList;
        StringBuffer buffer = new StringBuffer();
        try {
            if (queryAction == null) {
                remoteResultList = remoteIndsObject.getCommandList();
                for (String remoteResponse : remoteResultList) buffer.append(remoteResponse + "\n");
                remoteResult = buffer.toString();
            } else {
                if (queryCommand != null) {
                    Method action = remoteClass.getMethod(queryAction, queryCommand.getClass());
                    remoteResult = action.invoke(remoteIndsObject, queryCommand).toString();
                } else {
                    Method action = remoteClass.getMethod(queryAction);
                    remoteResult = action.invoke(remoteIndsObject).toString();
                }
            }
        } catch (java.lang.NoSuchMethodException e) {
            w.write("Query action=" + queryAction + " is not a method of com.rbnb.inds.exec.Remote\nException:\n\t" + e.getMessage());
        } catch (java.lang.IllegalAccessException e) {
        } catch (java.lang.reflect.InvocationTargetException e) {
        }
        if (queryContentType.equals("text/plain")) {
            if (remoteResult != null) w.write(remoteResult);
        } else {
            w.write("<html><body><br />INDS Execution Manager Servlet Version 0.6<br />");
            w.write("queryCommand: " + queryCommand + "<br /> queryAction: " + queryAction + "<br />");
            if (remoteResult != null) w.write("Response:<code><pre>" + remoteResult.replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "</pre></code>");
            w.write("</body></html>");
        }
    }
