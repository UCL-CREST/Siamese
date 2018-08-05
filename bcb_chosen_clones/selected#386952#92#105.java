        private String extractData(String pattern, boolean multiLine) {
            StringBuffer result = new StringBuffer();
            Pattern p = null;
            if (multiLine) {
                p = Pattern.compile(pattern, Pattern.DOTALL);
            } else {
                p = Pattern.compile(pattern);
            }
            Matcher m = p.matcher(jobOfferHtml);
            while (m.find()) {
                result.append(jobOfferHtml.substring(m.start(), m.end()) + " ");
            }
            return result.toString();
        }
