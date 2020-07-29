    public String doAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("doAdd(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - start");
        }
        t_information_EditMap editMap = new t_information_EditMap();
        try {
            t_information_Form vo = null;
            vo = (t_information_Form) form;
            vo.setCompany(vo.getCounty());
            if ("����".equals(vo.getInfo_type())) {
                vo.setInfo_level(null);
                vo.setAlert_level(null);
            }
            String str_postFIX = "";
            int i_p = 0;
            editMap.add(vo);
            try {
                logger.info("���͹�˾�鱨��");
                String[] mobiles = request.getParameterValues("mobiles");
                vo.setMobiles(mobiles);
                SMSService.inforAlert(vo);
            } catch (Exception e) {
                logger.error("doAdd(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)", e);
            }
            String filename = vo.getFile().getFileName();
            if (null != filename && !"".equals(filename)) {
                FormFile file = vo.getFile();
                String realpath = getServlet().getServletContext().getRealPath("/");
                realpath = realpath.replaceAll("\\\\", "/");
                String inforId = vo.getId();
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
        } catch (HibernateException e) {
            logger.error("doAdd(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)", e);
            ActionErrors errors = new ActionErrors();
            errors.add("org.apache.struts.action.GLOBAL_ERROR", new ActionError("error.database.save", e.toString()));
            saveErrors(request, errors);
            e.printStackTrace();
            request.setAttribute("t_information_Form", form);
            if (logger.isDebugEnabled()) {
                logger.debug("doAdd(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - end");
            }
            return "addpage";
        }
        if (logger.isDebugEnabled()) {
            logger.debug("doAdd(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - end");
        }
        return "aftersave";
    }
