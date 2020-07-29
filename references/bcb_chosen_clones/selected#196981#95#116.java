    public void write(ZipOutputStream os, ISarPackagingMonitor monitor) throws IOException, CoreException {
        Iterator it = entries.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            PathAndStream pns = (PathAndStream) entry.getValue();
            InputStream is = pns.getStream();
            if (monitor != null) monitor.savingEntry((IPath) entry.getKey());
            os.putNextEntry(new ZipEntry(((IPath) entry.getKey()).toPortableString()));
            System.out.println("Writing " + ((IPath) entry.getKey()).toPortableString());
            byte buffer[] = new byte[10240];
            while (true) {
                int nRead = is.read(buffer, 0, buffer.length);
                if (nRead <= 0) {
                    break;
                }
                os.write(buffer, 0, nRead);
            }
            is.close();
            pns.complete();
            if (monitor != null) monitor.entryCompleted((IPath) entry.getKey());
        }
    }
