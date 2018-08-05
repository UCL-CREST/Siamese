    public String doAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UploadFileForm vo = (UploadFileForm) form;
        String review = request.getParameter("review");
        String realpath = getServlet().getServletContext().getRealPath("/");
        realpath = realpath.replaceAll("\\\\", "/");
        String inforId = request.getParameter("inforId");
        request.setAttribute("id", inforId);
        String str_postFIX = "";
        int i_p = 0;
        if (null == review) {
            FormFile file = vo.getFile();
            if (file != null) {
                String rootFilePath = getServlet().getServletContext().getRealPath(request.getContextPath());
                rootFilePath = (new StringBuilder(String.valueOf(rootFilePath))).append(UploadFileOne.strPath).toString();
                String strAppend = (new StringBuilder(String.valueOf(UUIDGenerator.nextHex()))).append(UploadFileOne.getFileType(file)).toString();
                if (file.getFileSize() != 0) {
                    file.getInputStream();
                    String name = file.getFileName();
                    i_p = file.getFileName().lastIndexOf(".");
                    str_postFIX = file.getFileName().substring(i_p, file.getFileName().length());
                    String fullPath = realpath + "attach/" + strAppend + str_postFIX;
                    t_attach attach = new t_attach();
                    attach.setAttach_fullname(fullPath);
                    attach.setAttach_name(name);
                    attach.setInfor_id(Integer.parseInt(inforId));
                    attach.setInsert_day(new Date());
                    attach.setUpdate_day(new Date());
                    t_attach_EditMap attachEdit = new t_attach_EditMap();
                    attachEdit.add(attach);
                    File sysfile = new File(fullPath);
                    if (!sysfile.exists()) {
                        sysfile.createNewFile();
                    }
                    java.io.OutputStream out = new FileOutputStream(sysfile);
                    org.apache.commons.io.IOUtils.copy(file.getInputStream(), out);
                    out.close();
                }
            }
            request.setAttribute("operating-status", "�����ɹ�!  ��ӭ����ʹ�á�");
            return "editsave";
        } else {
            String rootFilePath = getServlet().getServletContext().getRealPath(request.getContextPath());
            rootFilePath = (new StringBuilder(String.valueOf(rootFilePath))).append(UploadFileOne.strPath).toString();
            FormFile file = vo.getFile();
            FormFile file2 = vo.getFile2();
            FormFile file3 = vo.getFile3();
            t_infor_review newreview = new t_infor_review();
            String content = request.getParameter("content");
            newreview.setContent(content);
            if (null != inforId) newreview.setInfor_id(Integer.parseInt(inforId));
            newreview.setInsert_day(new Date());
            UserDetails user = LoginUtils.getLoginUser(request);
            newreview.setCreate_name(user.getUsercode());
            if (null != file.getFileName() && !"".equals(file.getFileName())) {
                newreview.setAttachname1(file.getFileName());
                String strAppend1 = (new StringBuilder(String.valueOf(UUIDGenerator.nextHex()))).append(UploadFileOne.getFileType(file)).toString();
                i_p = file.getFileName().lastIndexOf(".");
                str_postFIX = file.getFileName().substring(i_p, file.getFileName().length());
                newreview.setAttachfullname1(realpath + "attach/" + strAppend1 + str_postFIX);
                saveFile(file.getInputStream(), realpath + "attach/" + strAppend1 + str_postFIX);
            }
            if (null != file2.getFileName() && !"".equals(file2.getFileName())) {
                newreview.setAttachname2(file2.getFileName());
                String strAppend2 = (new StringBuilder(String.valueOf(UUIDGenerator.nextHex()))).append(UploadFileOne.getFileType(file)).toString();
                i_p = file2.getFileName().lastIndexOf(".");
                str_postFIX = file2.getFileName().substring(i_p, file2.getFileName().length());
                newreview.setAttachfullname2(realpath + "attach/" + strAppend2 + str_postFIX);
                saveFile(file2.getInputStream(), realpath + "attach/" + strAppend2 + str_postFIX);
            }
            if (null != file3.getFileName() && !"".equals(file3.getFileName())) {
                newreview.setAttachname3(file3.getFileName());
                String strAppend3 = (new StringBuilder(String.valueOf(UUIDGenerator.nextHex()))).append(UploadFileOne.getFileType(file)).toString();
                i_p = file3.getFileName().lastIndexOf(".");
                str_postFIX = file3.getFileName().substring(i_p, file3.getFileName().length());
                newreview.setAttachfullname3(realpath + "attach/" + strAppend3 + str_postFIX);
                saveFile(file3.getInputStream(), realpath + "attach/" + strAppend3 + str_postFIX);
            }
            t_infor_review_EditMap reviewEdit = new t_infor_review_EditMap();
            reviewEdit.add(newreview);
            request.setAttribute("review", "1");
            return "aftersave";
        }
    }
