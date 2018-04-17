    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        getLog().info("Process request " + pathInfo);
        if (null != pathInfo) {
            String pathId = getPathId(pathInfo);
            JobResources res = ContextUtil.getJobResource(pathId);
            if (null != res) {
                RequestType requType = getRequestType(request);
                ResultAccess access = new ResultAccess(res);
                Collection<Long> uIdColl = res.getUniqIds();
                boolean isFiltered = false;
                {
                    List<String> postSeqIds = getSeqList(request);
                    if (!postSeqIds.isEmpty()) {
                        isFiltered = true;
                        uIdColl = access.loadIds(postSeqIds);
                    }
                }
                try {
                    if ((requType.equals(RequestType.FASTA)) || (requType.equals(RequestType.SWISSPROT))) {
                        OutputStreamWriter out = null;
                        out = new OutputStreamWriter(response.getOutputStream());
                        for (Long uid : uIdColl) {
                            if (requType.equals(RequestType.FASTA)) {
                                SwissProt sw = access.getSwissprotEntry(uid);
                                if (null != sw) {
                                    PrintFactory.instance().print(ConvertFactory.instance().SwissProt2fasta(sw), out);
                                } else {
                                    System.err.println("Not able to read Swissprot entry " + uid + " in project " + res.getBaseDir());
                                }
                            } else if (requType.equals(RequestType.SWISSPROT)) {
                                File swissFile = res.getSwissprotFile(uid);
                                if (swissFile.exists()) {
                                    InputStream in = null;
                                    try {
                                        in = new FileInputStream(swissFile);
                                        IOUtils.copy(in, out);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        System.err.println("Problems with reading file to output stream " + swissFile);
                                    } finally {
                                        IOUtils.closeQuietly(in);
                                    }
                                } else {
                                    System.err.println("Swissprot file does not exist: " + swissFile);
                                }
                            }
                        }
                        out.flush();
                    } else {
                        if (uIdColl.size() <= 2) {
                            isFiltered = false;
                            uIdColl = res.getUniqIds();
                        }
                        Tree tree = access.getTreeByUniquId(uIdColl);
                        if (requType.equals(RequestType.TREE)) {
                            response.getWriter().write(tree.toNewHampshireX());
                        } else if (requType.equals(RequestType.PNG)) {
                            List<SwissProt> sp = access.getSwissprotEntriesByUniquId(uIdColl);
                            ImageMap map = ImageFactory.instance().createProteinCard(sp, tree, true, res);
                            response.setContentType("image/png");
                            response.addHeader("Content-Disposition", "filename=ProteinCards.png");
                            ImageFactory.instance().printPNG(map.getImage(), response.getOutputStream());
                            response.getOutputStream().flush();
                        } else if (requType.equals(RequestType.HTML)) {
                            List<SwissProt> sp = access.getSwissprotEntriesByUniquId(uIdColl);
                            createHtml(res, access, tree, request, response, sp, isFiltered);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    getLog().error("Problem with Request: " + pathInfo + "; type " + requType, e);
                }
            } else {
                getLog().error("Resource is null: " + pathId + "; path " + pathInfo);
            }
        } else {
            getLog().error("PathInfo is null!!!");
        }
    }
