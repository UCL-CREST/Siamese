    public void openSelected() {
        if (Desktop.isDesktopSupported() == false) return;
        TableSorter ts = (TableSorter) table.getModel();
        int[] indices = table.getSelectedRows();
        for (int i = 0; i < indices.length; ++i) {
            int index = indices[i];
            try {
                Integer key = (Integer) ts.getValueAt(index, 1);
                Link at = LinkModel.getReference().getLink(key.intValue());
                if (at.getLinkType().equals(LinkModel.FILELINK.toString())) {
                    File file = new File(at.getPath());
                    if (!file.exists()) {
                        Errmsg.notice(Resource.getPlainResourceString("att_not_found"));
                        return;
                    }
                    Desktop.getDesktop().open(file);
                } else if (at.getLinkType().equals(LinkModel.ATTACHMENT.toString())) {
                    File file = new File(LinkModel.attachmentFolder() + "/" + at.getPath());
                    if (!file.exists()) {
                        Errmsg.notice(Resource.getPlainResourceString("att_not_found"));
                        return;
                    }
                    Desktop.getDesktop().open(file);
                } else if (at.getLinkType().equals(LinkModel.URL.toString())) {
                    Desktop.getDesktop().browse(new URI(at.getPath()));
                } else if (at.getLinkType().equals(LinkModel.APPOINTMENT.toString())) {
                    Appointment ap = AppointmentModel.getReference().getAppt(Integer.parseInt(at.getPath()));
                    if (ap == null) {
                        Errmsg.notice(Resource.getPlainResourceString("att_not_found"));
                        return;
                    }
                    GregorianCalendar cal = new GregorianCalendar();
                    cal.setTime(ap.getDate());
                    AppointmentListView ag = new AppointmentListView(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                    ag.showApp(ap.getKey());
                    MultiView.getMainView().addView(ag);
                } else if (at.getLinkType().equals(LinkModel.PROJECT.toString())) {
                    Project ap = TaskModel.getReference().getProject(Integer.parseInt(at.getPath()));
                    if (ap == null) {
                        Errmsg.notice(Resource.getPlainResourceString("att_not_found"));
                        return;
                    }
                    MultiView.getMainView().addView(new ProjectView(ap, ProjectView.T_CHANGE, null));
                } else if (at.getLinkType().equals(LinkModel.TASK.toString())) {
                    Task ap = TaskModel.getReference().getTask(Integer.parseInt(at.getPath()));
                    if (ap == null) {
                        Errmsg.notice(Resource.getPlainResourceString("att_not_found"));
                        return;
                    }
                    MultiView.getMainView().addView(new TaskView(ap, TaskView.T_CHANGE, null));
                } else if (at.getLinkType().equals(LinkModel.ADDRESS.toString())) {
                    Address ap = AddressModel.getReference().getAddress(Integer.parseInt(at.getPath()));
                    if (ap == null) {
                        Errmsg.notice(Resource.getPlainResourceString("att_not_found"));
                        return;
                    }
                    MultiView.getMainView().addView(new AddressView(ap));
                } else if (at.getLinkType().equals(LinkModel.MEMO.toString())) {
                    MultiView.getMainView().showMemos(at.getPath());
                }
            } catch (Exception e) {
                Errmsg.errmsg(e);
            }
        }
    }
