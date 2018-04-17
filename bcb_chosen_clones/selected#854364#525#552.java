    protected int sendData(String submitName, String submitValue) throws HttpException, IOException, SAXException {
        PostMethod postMethod = null;
        try {
            postMethod = new PostMethod(getDocumentBase().toString());
            postMethod.getParams().setCookiePolicy(org.apache.commons.httpclient.cookie.CookiePolicy.IGNORE_COOKIES);
            postMethod.addRequestHeader("Cookie", getWikiPrefix() + "_session=" + getSession() + "; " + getWikiPrefix() + "UserID=" + getUserId() + "; " + getWikiPrefix() + "UserName=" + getUserName() + "; ");
            List<Part> parts = new ArrayList<Part>();
            for (String s : new String[] { "wpSection", "wpEdittime", "wpScrolltop", "wpStarttime", "wpEditToken" }) {
                parts.add(new StringPart(s, StringEscapeUtils.unescapeJava(getNonNullParameter(s))));
            }
            parts.add(new StringPart("action", "edit"));
            parts.add(new StringPart("wpTextbox1", getArticleContent()));
            parts.add(new StringPart("wpSummary", getSummary()));
            parts.add(new StringPart("wpAutoSummary", Digest.MD5.isImplemented() ? Digest.MD5.encrypt(getSummary()) : ""));
            parts.add(new StringPart(submitName, submitValue));
            MultipartRequestEntity requestEntity = new MultipartRequestEntity(parts.toArray(new Part[parts.size()]), postMethod.getParams());
            postMethod.setRequestEntity(requestEntity);
            int status = getHttpClient().executeMethod(postMethod);
            IOUtils.copyTo(postMethod.getResponseBodyAsStream(), System.err);
            return status;
        } catch (HttpException err) {
            throw err;
        } catch (IOException err) {
            throw err;
        } finally {
            if (postMethod != null) postMethod.releaseConnection();
        }
    }
