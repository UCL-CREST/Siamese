        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            req.setCharacterEncoding("UTF-8");
            resp.setContentType(req.getContentType());
            IOUtils.copy(req.getReader(), resp.getWriter());
        }
