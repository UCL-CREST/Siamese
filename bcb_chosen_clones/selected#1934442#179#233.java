        void execute(HttpClient client, MonitoredService svc) {
            try {
                URI uri = getURI(svc);
                PageSequenceHttpMethod method = getMethod();
                method.setURI(uri);
                if (getVirtualHost() != null) {
                    method.getParams().setVirtualHost(getVirtualHost());
                }
                if (getUserAgent() != null) {
                    method.addRequestHeader("User-Agent", getUserAgent());
                }
                if (m_parms.length > 0) {
                    method.setParameters(m_parms);
                }
                if (m_page.getUserInfo() != null) {
                    String userInfo = m_page.getUserInfo();
                    String[] streetCred = userInfo.split(":", 2);
                    if (streetCred.length == 2) {
                        client.getState().setCredentials(new AuthScope(AuthScope.ANY), new UsernamePasswordCredentials(streetCred[0], streetCred[1]));
                        method.setDoAuthentication(true);
                    }
                }
                int code = client.executeMethod(method);
                if (!getRange().contains(code)) {
                    throw new PageSequenceMonitorException("response code out of range for uri:" + uri + ".  Expected " + getRange() + " but received " + code);
                }
                InputStream inputStream = method.getResponseBodyAsStream();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    IOUtils.copy(inputStream, outputStream);
                } finally {
                    IOUtils.closeQuietly(inputStream);
                    IOUtils.closeQuietly(outputStream);
                }
                String responseString = outputStream.toString();
                if (getFailurePattern() != null) {
                    Matcher matcher = getFailurePattern().matcher(responseString);
                    if (matcher.find()) {
                        throw new PageSequenceMonitorException(getResolvedFailureMessage(matcher));
                    }
                }
                if (getSuccessPattern() != null) {
                    Matcher matcher = getSuccessPattern().matcher(responseString);
                    if (!matcher.find()) {
                        throw new PageSequenceMonitorException("failed to find '" + getSuccessPattern() + "' in page content at " + uri);
                    }
                }
            } catch (URIException e) {
                throw new IllegalArgumentException("unable to construct URL for page: " + e, e);
            } catch (HttpException e) {
                throw new PageSequenceMonitorException("HTTP Error " + e, e);
            } catch (IOException e) {
                throw new PageSequenceMonitorException("I/O Error " + e, e);
            }
        }
