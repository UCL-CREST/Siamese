    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        IOUtils.copy(request.getInputStream(), response.getOutputStream());
        return null;
    }
