    @Override
    public void view(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean found = false;
        String name = getArgument(request.getPathInfo());
        if (StringUtils.contains(name, '/')) {
            File file = new File(config.getProperty(Config.MULTIMEDIA_PATH) + Config.FILE_SEPARATOR + name);
            if (file.exists() && file.isFile()) {
                found = true;
                MagicMatch match = Magic.getMagicMatch(file, true);
                response.setContentType(match.getMimeType());
                FileInputStream in = new FileInputStream(file);
                IOUtils.copyLarge(in, response.getOutputStream());
                in.close();
            }
        } else if (!StringUtils.isBlank(name)) {
            int articleId = NumberUtils.toInt(name);
            if (articleId > 0) {
                Article article = articleDao.load(articleId);
                if (article != null) {
                    found = true;
                    sendArticle(request, response, article);
                }
            }
        } else {
            int page = NumberUtils.toInt(request.getParameter("page"), 0);
            Date fromDate = null;
            String from = request.getParameter("from");
            if (StringUtils.isNotBlank(from)) {
                try {
                    fromDate = dayMonthYearEn.parse(from);
                } catch (ParseException e) {
                }
            }
            Date untilDate = null;
            String until = request.getParameter("until");
            if (StringUtils.isNotBlank(until)) {
                try {
                    untilDate = dayMonthYearEn.parse(until);
                } catch (ParseException e) {
                }
            }
            sendArticleList(request, response, articleDao.list(request.getParameter("query"), request.getParameter("author"), request.getParameter("tags"), request.getParameterValues("types"), fromDate, untilDate, page, HITS_PER_PAGE, null));
            found = true;
        }
        if (found != true) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
