package dev.wolfieboy09.qstorage.registries;

import com.tterrag.registrate.AbstractRegistrate;
import dev.wolfieboy09.qstorage.QuantiumizedStorage;

public class QSRegistrate extends AbstractRegistrate<QSRegistrate> {
    protected QSRegistrate() {
        super(QuantiumizedStorage.MOD_ID);
    }

    public static QSRegistrate create() {
        return new QSRegistrate();
    }
}