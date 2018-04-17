    public List<JobOfferHtml> extractJobOfferHtmlList() {
        String jobOfferHtml = null;
        List<JobOfferHtml> jobOfferHtmlList = new ArrayList<JobOfferHtml>();
        Pattern p = Pattern.compile(JobOfferHtmlPatterns.JOB_OFFER_HTML, Pattern.DOTALL);
        Matcher m = p.matcher(this.content);
        while (m.find()) {
            jobOfferHtml = this.content.substring(m.start(), m.end());
            jobOfferHtmlList.add(new JobOfferHtml(jobOfferHtml));
        }
        return jobOfferHtmlList;
    }
