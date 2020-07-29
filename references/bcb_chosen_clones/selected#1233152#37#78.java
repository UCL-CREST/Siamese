    public void writeSelectedPlaylistsToZip(IProgressMonitor monitor) {
        File tmpFile = new File(_path);
        if (tmpFile.exists()) {
            tmpFile.delete();
        }
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(_path));
            Playlist playlist;
            InputStream in;
            String name;
            Iterator<String> iter = _playlists.iterator();
            while (iter.hasNext()) {
                if (monitor.isCanceled()) {
                    out.close();
                    return;
                }
                playlist = Controller.getInstance().getPlaylistController().getPlaylistByName(iter.next());
                monitor.subTask(Messages.getString("PlaylistExporter.ExportPlaylist") + " " + playlist.getName());
                if (playlist != null) {
                    if (_format == PlaylistFormat.XSPF) {
                        name = playlist.getName() + SystemUtils.playlistXSPFExtension;
                        in = playlist.getPlaylistStreamAsXSPF();
                    } else {
                        name = playlist.getName() + SystemUtils.playlistM3UExtension;
                        in = playlist.getPlaylistStreamAsM3U();
                    }
                    out.putNextEntry(new ZipEntry(name));
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                    in.close();
                    monitor.worked(1);
                }
            }
            out.close();
        } catch (Exception e) {
            Log.getInstance(PlaylistExporter.class).error("Error while writting file : " + _path);
        }
    }
