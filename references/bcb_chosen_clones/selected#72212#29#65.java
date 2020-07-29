    public void download(RequestContext ctx) throws IOException {
        if (ctx.isRobot()) {
            ctx.forbidden();
            return;
        }
        long id = ctx.id();
        File bean = File.INSTANCE.Get(id);
        if (bean == null) {
            ctx.not_found();
            return;
        }
        String f_ident = ctx.param("fn", "");
        if (id >= 100 && !StringUtils.equals(f_ident, bean.getIdent())) {
            ctx.not_found();
            return;
        }
        if (bean.IsLoginRequired() && ctx.user() == null) {
            StringBuilder login = new StringBuilder(LinkTool.home("home/login?goto_page="));
            ctx.redirect(login.toString());
            return;
        }
        FileInputStream fis = null;
        try {
            java.io.File file = StorageService.FILES.readFile(bean.getPath());
            fis = new FileInputStream(file);
            ctx.response().setContentLength((int) file.length());
            String ext = FilenameUtils.getExtension(bean.getPath());
            String mine_type = Multimedia.mime_types.get(ext);
            if (mine_type != null) ctx.response().setContentType(mine_type);
            String ua = ctx.header("user-agent");
            if (ua != null && ua.indexOf("Firefox") >= 0) ctx.header("Content-Disposition", "attachment; filename*=\"utf8''" + LinkTool.encode_url(bean.getName()) + "." + ext + "\""); else ctx.header("Content-Disposition", "attachment; filename=" + LinkTool.encode_url(bean.getName() + "." + ext));
            IOUtils.copy(fis, ctx.response().getOutputStream());
            bean.IncDownloadCount(1);
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }
