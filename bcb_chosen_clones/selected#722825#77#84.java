    public boolean showDialog(int dialogType, int activeUser, int itemId, JFrame parent) throws Exception {
        DialogData tmp = map.get(dialogType);
        Object frm = tmp.getEditAct().execute(pool, activeUser, itemId);
        Constructor dialogCon = tmp.getDialogClass().getConstructor(JFrame.class, DialogManager.class);
        EditDialog dialog = (EditDialog) dialogCon.newInstance(parent, this);
        frm = dialog.initAndShow(frm);
        return tmp.getEditSaveAct().execute(pool, frm);
    }
