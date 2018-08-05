    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(JFileChooser.FILE_FILTER_CHANGED_PROPERTY)) {
            evt.getNewValue();
            FiltreFichier ff = (FiltreFichier) this.getFileFilter();
            if (null != ff) {
                try {
                    Constructor c = Class.forName(ff.getExportComponentName()).getConstructor(new Class[] { javax.swing.JFileChooser.class, Convert[].class });
                    this.setAccessory((JComponent) c.newInstance(new Object[] { this, ((FiltreFichier) ff).getConverters() }));
                } catch (Exception e) {
                    this.setAccessory(new ChooseExportOptions(this, ((FiltreFichier) ff).getConverters()));
                }
            }
        }
        this.validate();
    }
