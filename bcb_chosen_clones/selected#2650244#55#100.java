    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, ContextAdapter context) throws Exception {
        boolean isProblemset = context.getRequest().getRequestURI().endsWith("exportProblems.do");
        ActionForward forward = this.checkContestAdminPermission(mapping, context, isProblemset, false);
        if (forward != null) {
            return forward;
        }
        AbstractContest contest = context.getContest();
        List<Problem> problems = ContestManager.getInstance().getContestProblems(contest.getId());
        HttpServletResponse response = context.getResponse();
        response.setContentType("application/zip");
        response.setHeader("Content-disposition", "attachment; filename=" + this.convertFilename(contest.getTitle()) + ".zip");
        ZipOutputStream out = null;
        StringBuilder sb = new StringBuilder();
        try {
            out = new ZipOutputStream(response.getOutputStream());
            for (Object obj : problems) {
                Problem p = (Problem) obj;
                this.zipReference(p, "text", ReferenceType.DESCRIPTION, out);
                this.zipReference(p, "input", ReferenceType.INPUT, out);
                this.zipReference(p, "output", ReferenceType.OUTPUT, out);
                this.zipReference(p, "solution", ReferenceType.JUDGE_SOLUTION, out);
                this.zipReference(p, "checker", ReferenceType.HEADER, out);
                this.zipReference(p, "checker", ReferenceType.CHECKER_SOURCE, out);
                sb.append(toCsvString(p.getCode())).append(",");
                sb.append(toCsvString(p.getTitle())).append(",");
                sb.append(p.isChecker()).append(",");
                sb.append(p.getLimit().getTimeLimit()).append(",");
                sb.append(p.getLimit().getMemoryLimit()).append(",");
                sb.append(p.getLimit().getOutputLimit()).append(",");
                sb.append(p.getLimit().getSubmissionLimit()).append(",");
                sb.append(toCsvString(p.getAuthor())).append(",");
                sb.append(toCsvString(p.getSource())).append(",");
                sb.append(toCsvString(p.getContest())).append("\n");
            }
            out.putNextEntry(new ZipEntry("problems.csv"));
            out.write(sb.toString().getBytes());
        } catch (IOException e) {
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return null;
    }
