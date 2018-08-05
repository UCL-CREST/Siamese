    public String doAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UploadFileForm vo = (UploadFileForm) form;
        FormFile file = vo.getFile();
        String inforId = request.getParameter("inforId");
        System.out.println("inforId=" + inforId);
        if (file != null) {
            String realpath = getServlet().getServletContext().getRealPath("/");
            realpath = realpath.replaceAll("\\\\", "/");
            String rootFilePath = getServlet().getServletContext().getRealPath(request.getContextPath());
            rootFilePath = (new StringBuilder(String.valueOf(rootFilePath))).append(UploadFileOne.strPath).toString();
            String strAppend = (new StringBuilder(String.valueOf(UUIDGenerator.nextHex()))).append(UploadFileOne.getFileType(file)).toString();
            if (file.getFileSize() != 0) {
                file.getInputStream();
                String name = file.getFileName();
                String fullPath = realpath + "attach/" + strAppend + name;
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
                System.out.println("file name is :" + name);
            }
        }
        request.setAttribute("operating-status", "�����ɹ�!  ��ӭ����ʹ�á�");
        System.out.println("in the end....");
        return "aftersave";
    }
