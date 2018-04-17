    public boolean loadExisting() throws IOException, InterruptedIOException {
        reset();
        String curDir = CommandThread.core.getPreference(OConsts.PREF_CUR_DIR);
        JFileChooser pfc = new JFileChooser(curDir);
        pfc.setFileFilter(new OTFileFilter());
        pfc.setFileView(new ProjectFileView());
        int res = pfc.showOpenDialog(this);
        if (res == JFileChooser.CANCEL_OPTION) throw new InterruptedIOException();
        CommandThread.core.setPreference(OConsts.PREF_CUR_DIR, pfc.getSelectedFile().getParent());
        try {
            ProjFileReader pfr = new ProjFileReader();
            m_projName = pfc.getCurrentDirectory().getName();
            m_projRoot = pfc.getCurrentDirectory().getAbsolutePath() + File.separator;
            pfr.loadProjectFile(m_projRoot + OConsts.PROJ_FILENAME);
            m_srcRoot = pfr.getSource();
            m_locRoot = pfr.getTarget();
            m_glosRoot = pfr.getGlossary();
            m_tmRoot = pfr.getTM();
            m_projInternal = m_projRoot + OConsts.DEFAULT_INTERNAL + File.separator;
            m_srcLang = pfr.getSourceLang();
            m_locLang = pfr.getTargetLang();
            CommandThread.core.setPreference(OConsts.PREF_SRCLANG, m_srcLang);
            CommandThread.core.setPreference(OConsts.PREF_LOCLANG, m_locLang);
            m_projFile = m_projRoot + OConsts.PROJ_FILENAME;
            res = verifyProject();
            if (res != 0) {
                MNewProject prj = new MNewProject(this, m_projFile, res);
                boolean abort = false;
                while (true) {
                    prj.show();
                    if (m_dialogOK == false) {
                        abort = true;
                        break;
                    }
                    if ((res = verifyProject()) != 0) {
                        prj.setMessageCode(res);
                    } else {
                        buildProjFile();
                        break;
                    }
                }
                prj.dispose();
                if (abort == true) {
                    reset();
                    return false;
                }
            }
            return true;
        } catch (ParseException e) {
            reset();
            throw new IOException("Unable to read project file\n" + e);
        }
    }
