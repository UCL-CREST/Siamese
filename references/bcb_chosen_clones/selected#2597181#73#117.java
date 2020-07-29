    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userAgentGroup = processUserAgent(request);
        final LiwenxRequest lRequest = new LiwenxRequestImpl(request, response, messageSource, userAgentGroup);
        Locator loc = router.route(lRequest);
        if (loc instanceof RedirectLocator) {
            response.sendRedirect(((RedirectLocator) loc).getPage());
        } else {
            ((AbstractLiwenxRequest) lRequest).setRequestedLocator(loc);
            try {
                LiwenxResponse resp = processPage(lRequest, lRequest.getRequestedLocator(), maxRedirections);
                processHeaders(resp, response);
                processCookies(resp, response);
                if (resp instanceof ExternalRedirectionResponse) {
                    response.sendRedirect(((ExternalRedirectionResponse) resp).getRedirectTo());
                } else if (resp instanceof BinaryResponse) {
                    BinaryResponse bResp = (BinaryResponse) resp;
                    response.setContentType(bResp.getMimeType().toString());
                    IOUtils.copy(bResp.getInputStream(), response.getOutputStream());
                } else if (resp instanceof XmlResponse) {
                    final Element root = ((XmlResponse) resp).getXml();
                    Document doc = root.getDocument();
                    if (doc == null) {
                        doc = new Document(root);
                    }
                    final Locator l = lRequest.getCurrentLocator();
                    final Device device = l.getDevice();
                    response.setContentType(calculateContentType(device));
                    response.setCharacterEncoding(encoding);
                    if (device == Device.HTML) {
                        view.processView(doc, l.getLocale(), userAgentGroup, response.getWriter());
                    } else {
                        Serializer s = new Serializer(response.getOutputStream(), encoding);
                        s.write(doc);
                    }
                }
            } catch (PageNotFoundException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } catch (TooManyRedirectionsException e) {
                throw e;
            } catch (Exception e) {
                throw e;
            }
        }
    }
