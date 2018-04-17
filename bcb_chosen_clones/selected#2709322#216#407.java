    public NamedList<Object> request(final SolrRequest request, ResponseParser processor) throws SolrServerException, IOException {
        HttpMethod method = null;
        InputStream is = null;
        SolrParams params = request.getParams();
        Collection<ContentStream> streams = requestWriter.getContentStreams(request);
        String path = requestWriter.getPath(request);
        if (path == null || !path.startsWith("/")) {
            path = "/select";
        }
        ResponseParser parser = request.getResponseParser();
        if (parser == null) {
            parser = _parser;
        }
        ModifiableSolrParams wparams = new ModifiableSolrParams();
        wparams.set(CommonParams.WT, parser.getWriterType());
        wparams.set(CommonParams.VERSION, parser.getVersion());
        if (params == null) {
            params = wparams;
        } else {
            params = new DefaultSolrParams(wparams, params);
        }
        if (_invariantParams != null) {
            params = new DefaultSolrParams(_invariantParams, params);
        }
        int tries = _maxRetries + 1;
        try {
            while (tries-- > 0) {
                try {
                    if (SolrRequest.METHOD.GET == request.getMethod()) {
                        if (streams != null) {
                            throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "GET can't send streams!");
                        }
                        method = new GetMethod(_baseURL + path + ClientUtils.toQueryString(params, false));
                    } else if (SolrRequest.METHOD.POST == request.getMethod()) {
                        String url = _baseURL + path;
                        boolean isMultipart = (streams != null && streams.size() > 1);
                        if (streams == null || isMultipart) {
                            PostMethod post = new PostMethod(url);
                            post.getParams().setContentCharset("UTF-8");
                            if (!this.useMultiPartPost && !isMultipart) {
                                post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                            }
                            List<Part> parts = new LinkedList<Part>();
                            Iterator<String> iter = params.getParameterNamesIterator();
                            while (iter.hasNext()) {
                                String p = iter.next();
                                String[] vals = params.getParams(p);
                                if (vals != null) {
                                    for (String v : vals) {
                                        if (this.useMultiPartPost || isMultipart) {
                                            parts.add(new StringPart(p, v, "UTF-8"));
                                        } else {
                                            post.addParameter(p, v);
                                        }
                                    }
                                }
                            }
                            if (isMultipart) {
                                int i = 0;
                                for (ContentStream content : streams) {
                                    final ContentStream c = content;
                                    String charSet = null;
                                    String transferEncoding = null;
                                    parts.add(new PartBase(c.getName(), c.getContentType(), charSet, transferEncoding) {

                                        @Override
                                        protected long lengthOfData() throws IOException {
                                            return c.getSize();
                                        }

                                        @Override
                                        protected void sendData(OutputStream out) throws IOException {
                                            Reader reader = c.getReader();
                                            try {
                                                IOUtils.copy(reader, out);
                                            } finally {
                                                reader.close();
                                            }
                                        }
                                    });
                                }
                            }
                            if (parts.size() > 0) {
                                post.setRequestEntity(new MultipartRequestEntity(parts.toArray(new Part[parts.size()]), post.getParams()));
                            }
                            method = post;
                        } else {
                            String pstr = ClientUtils.toQueryString(params, false);
                            PostMethod post = new PostMethod(url + pstr);
                            final ContentStream[] contentStream = new ContentStream[1];
                            for (ContentStream content : streams) {
                                contentStream[0] = content;
                                break;
                            }
                            if (contentStream[0] instanceof RequestWriter.LazyContentStream) {
                                post.setRequestEntity(new RequestEntity() {

                                    public long getContentLength() {
                                        return -1;
                                    }

                                    public String getContentType() {
                                        return contentStream[0].getContentType();
                                    }

                                    public boolean isRepeatable() {
                                        return false;
                                    }

                                    public void writeRequest(OutputStream outputStream) throws IOException {
                                        ((RequestWriter.LazyContentStream) contentStream[0]).writeTo(outputStream);
                                    }
                                });
                            } else {
                                is = contentStream[0].getStream();
                                post.setRequestEntity(new InputStreamRequestEntity(is, contentStream[0].getContentType()));
                            }
                            method = post;
                        }
                    } else {
                        throw new SolrServerException("Unsupported method: " + request.getMethod());
                    }
                } catch (NoHttpResponseException r) {
                    method.releaseConnection();
                    method = null;
                    if (is != null) {
                        is.close();
                    }
                    if ((tries < 1)) {
                        throw r;
                    }
                }
            }
        } catch (IOException ex) {
            throw new SolrServerException("error reading streams", ex);
        }
        method.setFollowRedirects(_followRedirects);
        method.addRequestHeader("User-Agent", AGENT);
        if (_allowCompression) {
            method.setRequestHeader(new Header("Accept-Encoding", "gzip,deflate"));
        }
        try {
            int statusCode = _httpClient.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                StringBuilder msg = new StringBuilder();
                msg.append(method.getStatusLine().getReasonPhrase());
                msg.append("\n\n");
                msg.append(method.getStatusText());
                msg.append("\n\n");
                msg.append("request: " + method.getURI());
                throw new SolrException(statusCode, java.net.URLDecoder.decode(msg.toString(), "UTF-8"));
            }
            String charset = "UTF-8";
            if (method instanceof HttpMethodBase) {
                charset = ((HttpMethodBase) method).getResponseCharSet();
            }
            InputStream respBody = method.getResponseBodyAsStream();
            if (_allowCompression) {
                Header contentEncodingHeader = method.getResponseHeader("Content-Encoding");
                if (contentEncodingHeader != null) {
                    String contentEncoding = contentEncodingHeader.getValue();
                    if (contentEncoding.contains("gzip")) {
                        respBody = new GZIPInputStream(respBody);
                    } else if (contentEncoding.contains("deflate")) {
                        respBody = new InflaterInputStream(respBody);
                    }
                } else {
                    Header contentTypeHeader = method.getResponseHeader("Content-Type");
                    if (contentTypeHeader != null) {
                        String contentType = contentTypeHeader.getValue();
                        if (contentType != null) {
                            if (contentType.startsWith("application/x-gzip-compressed")) {
                                respBody = new GZIPInputStream(respBody);
                            } else if (contentType.startsWith("application/x-deflate")) {
                                respBody = new InflaterInputStream(respBody);
                            }
                        }
                    }
                }
            }
            return processor.processResponse(respBody, charset);
        } catch (HttpException e) {
            throw new SolrServerException(e);
        } catch (IOException e) {
            throw new SolrServerException(e);
        } finally {
            method.releaseConnection();
            if (is != null) {
                is.close();
            }
        }
    }
