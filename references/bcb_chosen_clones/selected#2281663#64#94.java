    public ActionForward execute(ActionMapping mapping, ActionForm form, ContextAdapter context) throws Exception {
        SubmissionCriteria criteria = new SubmissionCriteria();
        ArrayList judgeReplies = new ArrayList();
        judgeReplies.add(JudgeReply.ACCEPTED);
        criteria.setJudgeReplies(judgeReplies);
        criteria.setContestId((long) 1);
        List runs = PersistenceManager.getInstance().getSubmissionPersistence().searchSubmissions(criteria, 0, Long.MAX_VALUE, Integer.MAX_VALUE, true);
        System.out.println(runs.size());
        HttpServletResponse response = context.getResponse();
        response.setContentType("application/zip");
        response.setHeader("Content-disposition", "attachment; filename=StudentCode.zip");
        ZipOutputStream out = null;
        StringBuilder sb = new StringBuilder();
        try {
            out = new ZipOutputStream(response.getOutputStream());
            for (Object obj : runs) {
                Submission s = (Submission) obj;
                out.putNextEntry(new ZipEntry(s.getProblemCode() + "/" + s.getUserName() + "/" + s.getId()));
                byte[] data = s.getContent().getBytes();
                out.write(data);
                out.closeEntry();
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return null;
    }
