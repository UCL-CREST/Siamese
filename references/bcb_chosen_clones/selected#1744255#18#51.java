    @Override
    public LispObject execute(LispObject first, LispObject second) throws ConditionThrowable {
        Pathname zipfilePathname = coerceToPathname(first);
        byte[] buffer = new byte[4096];
        try {
            String zipfileNamestring = zipfilePathname.getNamestring();
            if (zipfileNamestring == null) return error(new SimpleError("Pathname has no namestring: " + zipfilePathname.writeToString()));
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfileNamestring));
            LispObject list = second;
            while (list != NIL) {
                Pathname pathname = coerceToPathname(list.CAR());
                String namestring = pathname.getNamestring();
                if (namestring == null) {
                    out.close();
                    File zipfile = new File(zipfileNamestring);
                    zipfile.delete();
                    return error(new SimpleError("Pathname has no namestring: " + pathname.writeToString()));
                }
                File file = new File(namestring);
                FileInputStream in = new FileInputStream(file);
                ZipEntry entry = new ZipEntry(file.getName());
                out.putNextEntry(entry);
                int n;
                while ((n = in.read(buffer)) > 0) out.write(buffer, 0, n);
                out.closeEntry();
                in.close();
                list = list.CDR();
            }
            out.close();
        } catch (IOException e) {
            return error(new LispError(e.getMessage()));
        }
        return zipfilePathname;
    }
