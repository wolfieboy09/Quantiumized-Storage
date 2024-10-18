package dev.wolfieboy09.qstorage.registries;

import com.tterrag.registrate.AbstractRegistrate;

public class QSRegistrate extends AbstractRegistrate<QSRegistrate> {

    protected QSRegistrate(String mod_id) {
        super(mod_id);
    }

    public static QSRegistrate create(String mod_id) {
        return new QSRegistrate(mod_id);
    }
}