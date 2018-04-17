    private void OpenSchedule() {
        jtp.addTab("Schedule", jpShedule);
        jtp.addTab("ScheduleSource", jpsSource);
        JFileChooser chooser = new JFileChooser(DirS);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        ExtensionFileFilter filter = new ExtensionFileFilter("sch", "Schedule graph representation files (*.sch)");
        chooser.setFileFilter(filter);
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        DirS = chooser.getSelectedFile().getParent();
        PathS = chooser.getSelectedFile().getPath();
        TabS = chooser.getSelectedFile().getName();
        InitSchedule();
    }
