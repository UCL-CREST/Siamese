    final void importSelectedEntity() {
        TreePath path = projectsTree.getSelectionPath();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        Object userObject = selectedNode.getUserObject();
        if (userObject instanceof ProjectSet) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setFileFilter(new FileNameExtensionFilter("JFPSM Projects", "jfpsm.zip"));
            int result = fileChooser.showOpenDialog(mainWindow.getApplicativeFrame());
            if (result == JFileChooser.APPROVE_OPTION) {
                String fullname = fileChooser.getSelectedFile().getName();
                String projectName = fullname.substring(0, fullname.length() - Project.getFileExtension().length());
                ProjectSet workspace = (ProjectSet) userObject;
                boolean confirmLoad = true;
                if (Arrays.asList(workspace.getProjectNames()).contains(projectName)) {
                    confirmLoad = JOptionPane.showConfirmDialog(mainWindow.getApplicativeFrame(), "Overwrite project \"" + projectName + "\"" + "?", "Overwrite project", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
                    if (confirmLoad) {
                        final int count = selectedNode.getChildCount();
                        DefaultMutableTreeNode projectNode = null;
                        for (int i = 0; i < count; i++) if (((Project) ((DefaultMutableTreeNode) selectedNode.getChildAt(i)).getUserObject()).getName().equals(projectName)) {
                            projectNode = (DefaultMutableTreeNode) selectedNode.getChildAt(i);
                            break;
                        }
                        Project project = (Project) projectNode.getUserObject();
                        for (FloorSet floorSet : project.getLevelSet().getFloorSetsList()) for (Floor floor : floorSet.getFloorsList()) mainWindow.getEntityViewer().closeEntityView(floor);
                        for (Tile tile : project.getTileSet().getTilesList()) mainWindow.getEntityViewer().closeEntityView(tile);
                        workspace.removeProject(project);
                        ((DefaultTreeModel) projectsTree.getModel()).removeNodeFromParent(projectNode);
                    }
                }
                if (confirmLoad) {
                    File projectFile = new File(workspace.createProjectPath(projectName));
                    boolean success = true;
                    try {
                        success = projectFile.createNewFile();
                        if (success) {
                            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileChooser.getSelectedFile()));
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(projectFile));
                            byte[] buf = new byte[1024];
                            int len;
                            while ((len = bis.read(buf)) > 0) bos.write(buf, 0, len);
                            bis.close();
                            bos.close();
                        }
                    } catch (Throwable throwable) {
                        displayErrorMessage(throwable, false);
                        success = false;
                    }
                    if (success) addProject(projectName);
                }
            }
        } else if (userObject instanceof Map) {
            Map map = (Map) userObject;
            Floor floor = (Floor) ((DefaultMutableTreeNode) selectedNode.getParent()).getUserObject();
            importImageForSelectedMap(floor, map);
        }
    }
