    private void post(String title, Document content, Set<String> tags) throws HttpException, IOException, TransformerException {
        PostMethod method = null;
        try {
            method = new PostMethod("http://www.blogger.com/feeds/" + this.blogId + "/posts/default");
            method.addRequestHeader("GData-Version", String.valueOf(GDataVersion));
            method.addRequestHeader("Authorization", "GoogleLogin auth=" + this.AuthToken);
            Document dom = this.domBuilder.newDocument();
            Element entry = dom.createElementNS(Atom.NS, "entry");
            dom.appendChild(entry);
            entry.setAttribute("xmlns", Atom.NS);
            Element titleNode = dom.createElementNS(Atom.NS, "title");
            entry.appendChild(titleNode);
            titleNode.setAttribute("type", "text");
            titleNode.appendChild(dom.createTextNode(title));
            Element contentNode = dom.createElementNS(Atom.NS, "content");
            entry.appendChild(contentNode);
            contentNode.setAttribute("type", "xhtml");
            contentNode.appendChild(dom.importNode(content.getDocumentElement(), true));
            for (String tag : tags) {
                Element category = dom.createElementNS(Atom.NS, "category");
                category.setAttribute("scheme", "http://www.blogger.com/atom/ns#");
                category.setAttribute("term", tag);
                entry.appendChild(category);
            }
            StringWriter out = new StringWriter();
            this.xml2ascii.transform(new DOMSource(dom), new StreamResult(out));
            method.setRequestEntity(new StringRequestEntity(out.toString(), "application/atom+xml", "UTF-8"));
            int status = getHttpClient().executeMethod(method);
            if (status == 201) {
                IOUtils.copyTo(method.getResponseBodyAsStream(), System.out);
            } else {
                throw new HttpException("post returned http-code=" + status + " expected 201 (CREATE)");
            }
        } catch (TransformerException err) {
            throw err;
        } catch (HttpException err) {
            throw err;
        } catch (IOException err) {
            throw err;
        } finally {
            if (method != null) method.releaseConnection();
        }
    }
