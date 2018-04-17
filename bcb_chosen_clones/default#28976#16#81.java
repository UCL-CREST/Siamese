    void run() throws Exception {
        Context context = new Context();
        JavacFileManager.preRegister(context);
        names = Names.instance(context);
        symtab = Symtab.instance(context);
        Scope emptyScope = new Scope(symtab.unnamedPackage);
        Field sHashMask = Scope.class.getDeclaredField("hashMask");
        sHashMask.setAccessible(true);
        scopeHashMask = sHashMask.getInt(emptyScope);
        log("scopeHashMask: " + scopeHashMask);
        Name entry = names.fromString("Entry");
        Name outerName;
        Name innerName;
        StringBuilder sb = new StringBuilder("C");
        int i = 0;
        do {
            sb.append(Integer.toString(i % 10));
            innerName = names.fromString(sb + "$Entry");
        } while (!clash(entry, innerName) && (++i) < MAX_TRIES);
        if (clash(entry, innerName)) {
            log("Detected expected hash collision for " + entry + " and " + innerName + " after " + i + " tries");
        } else {
            throw new Exception("No potential collision found after " + i + " tries");
        }
        outerName = names.fromString(sb.toString());
        ClassSymbol cc = createClass(names.fromString("C"), symtab.unnamedPackage);
        ClassSymbol ce = createClass(entry, cc);
        PackageSymbol p = new PackageSymbol(names.fromString("p"), symtab.rootPackage);
        p.members_field = new Scope(p);
        ClassSymbol inner = createClass(innerName, p);
        ClassSymbol outer = createClass(outerName, p);
        log("createStarImportScope");
        Scope starImportScope;
        Method importAll;
        PackageSymbol pkg = new PackageSymbol(names.fromString("pkg"), symtab.rootPackage);
        try {
            Class<?> c = Class.forName("com.sun.tools.javac.code.Scope$StarImportScope");
            Constructor ctor = c.getDeclaredConstructor(new Class[] { Symbol.class });
            importAll = c.getDeclaredMethod("importAll", new Class[] { Scope.class });
            starImportScope = (Scope) ctor.newInstance(new Object[] { pkg });
        } catch (ClassNotFoundException e) {
            starImportScope = new ImportScope(pkg);
            importAll = null;
        }
        dump("initial", starImportScope);
        Scope p_members = p.members();
        if (importAll != null) {
            importAll.invoke(starImportScope, p_members);
        } else {
            Scope fromScope = p_members;
            Scope toScope = starImportScope;
            for (Scope.Entry e = fromScope.elems; e != null; e = e.sibling) {
                if (e.sym.kind == TYP && !toScope.includes(e.sym)) toScope.enter(e.sym, fromScope);
            }
        }
        dump("imported p", starImportScope);
        starImportScope.enter(ce, cc.members_field);
        dump("imported ce", starImportScope);
        p.members_field.remove(inner);
        inner.name = entry;
        inner.owner = outer;
        outer.members_field.enter(inner);
        Scope.Entry e = starImportScope.lookup(entry);
        dump("final", starImportScope);
        if (e.sym == null) throw new Exception("symbol not found: " + entry);
    }
