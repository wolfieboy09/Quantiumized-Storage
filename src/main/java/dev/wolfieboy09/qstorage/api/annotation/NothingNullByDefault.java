package dev.wolfieboy09.qstorage.api.annotation;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Nonnull
@TypeQualifierDefault({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.CLASS)
@NotNull
public @interface NothingNullByDefault {
}
