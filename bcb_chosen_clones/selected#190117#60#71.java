    @RequestMapping(value = "/image/{fileName}", method = RequestMethod.GET)
    public void getImage(@PathVariable String fileName, HttpServletRequest req, HttpServletResponse res) throws Exception {
        File file = new File(STORAGE_PATH + fileName + ".jpg");
        res.setHeader("Cache-Control", "no-store");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);
        res.setContentType("image/jpg");
        ServletOutputStream ostream = res.getOutputStream();
        IOUtils.copy(new FileInputStream(file), ostream);
        ostream.flush();
        ostream.close();
    }
