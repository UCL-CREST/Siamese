    private void addRunScript(ZipOutputStream zos, boolean hasInstances, boolean hasSolvers, GridQueue q) throws IOException {
        String sRun = "#!/bin/bash\n" + "chmod a-rwx client\n" + "chmod u+rwx client\n" + "chmod a-rwx config\n" + "chmod u+rw config\n" + (hasSolvers ? "chmod a-rwx solvers/*\n" : "") + (hasSolvers ? "chmod u+rwx solvers/*\n" : "") + (hasInstances ? "chmod a-rwx instances/*\n" : "") + (hasInstances ? "chmod u+rw instances/*\n" : "") + "for (( i = 0; i < " + q.getNumCPUs() + "; i++ ))\n" + "do\n" + "    qsub start_client.pbs\n" + "done\n";
        ZipEntry entry = new ZipEntry("run.sh");
        zos.putNextEntry(entry);
        zos.write(sRun.getBytes());
        zos.closeEntry();
    }
