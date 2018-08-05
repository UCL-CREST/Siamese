    public final void copyFiles() {
        IOUtils.copyWithRsync(_project.styleDirectory(), _project.styleOutputDirectory(), String.format("(%s->%s)   ", _project.projectDirectory().getName(), _project.styleOutputDirectory().getName()));
    }
