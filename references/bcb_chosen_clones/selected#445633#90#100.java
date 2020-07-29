    public List<JobPageLinkHtml> extractJobPageLinkHtmlList() {
        String linkHtml = null;
        List<JobPageLinkHtml> jobPageLinkHtmlList = new ArrayList<JobPageLinkHtml>();
        Pattern p = Pattern.compile(JobPageLinkHtmlPatterns.A_HREF);
        Matcher m = p.matcher(this.content);
        while (m.find()) {
            linkHtml = this.content.substring(m.start(), m.end());
            jobPageLinkHtmlList.add(new JobPageLinkHtml(linkHtml));
        }
        return jobPageLinkHtmlList;
    }
