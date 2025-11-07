package dev.wolfieboy09.qtech.api.util;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@NothingNullByDefault
public abstract class TriEither<L, M, R> {
    private TriEither() {}

    public static <L, M, R> TriEither<L, M, R> left(L value) {
        return new Left<>(value);
    }

    public static <L, M, R> TriEither<L, M, R> middle(M value) {
        return new Middle<>(value);
    }

    public static <L, M, R> TriEither<L, M, R> right(R value) {
        return new Right<>(value);
    }

    public abstract <T> T map(
            Function<? super L, ? extends T> leftFn,
            Function<? super M, ? extends T> middleFn,
            Function<? super R, ? extends T> rightFn
    );

    public abstract boolean isLeft();
    public abstract boolean isMiddle();
    public abstract boolean isRight();

    public abstract TriEither<L, M, R> ifLeft(Consumer<? super L> action);
    public abstract TriEither<L, M, R> ifMiddle(Consumer<? super M> action);
    public abstract TriEither<L, M, R> ifRight(Consumer<? super R> action);

    public abstract Optional<L> left();
    public abstract Optional<M> middle();
    public abstract Optional<R> right();

    public static final class Left<L, M, R> extends TriEither<L, M, R> {
        private final L value;
        private Left(L value) { this.value = value; }

        @Override
        public <T> T map(
                Function<? super L, ? extends T> leftFn,
                Function<? super M, ? extends T> middleFn,
                Function<? super R, ? extends T> rightFn
        ) {
            return leftFn.apply(value);
        }

        @Override public boolean isLeft() { return true; }
        @Override public boolean isMiddle() { return false; }
        @Override public boolean isRight() { return false; }

        @Override
        public TriEither<L, M, R> ifLeft(Consumer<? super L> action) {
            action.accept(value);
            return this;
        }

        @Override
        public TriEither<L, M, R> ifMiddle(Consumer<? super M> action) {
            return this;
        }

        @Override
        public TriEither<L, M, R> ifRight(Consumer<? super R> action) {
            return this;
        }

        @Override
        public Optional<L> left() {
            return Optional.of(value);
        }

        @Override
        public Optional<M> middle() {
            return Optional.empty();
        }

        @Override
        public Optional<R> right() {
            return Optional.empty();
        }

        public L get() { return value; }
    }

    public static final class Middle<L, M, R> extends TriEither<L, M, R> {
        private final M value;
        private Middle(M value) { this.value = value; }

        @Override
        public <T> T map(
                Function<? super L, ? extends T> leftFn,
                Function<? super M, ? extends T> middleFn,
                Function<? super R, ? extends T> rightFn
        ) {
            return middleFn.apply(value);
        }

        @Override public boolean isLeft() { return false; }
        @Override public boolean isMiddle() { return true; }
        @Override public boolean isRight() { return false; }

        @Override
        public TriEither<L, M, R> ifLeft(Consumer<? super L> action) {
            return this;
        }

        @Override
        public TriEither<L, M, R> ifMiddle(Consumer<? super M> action) {
            action.accept(value);
            return this;
        }

        @Override
        public TriEither<L, M, R> ifRight(Consumer<? super R> action) {
            return this;
        }

        @Override
        public Optional<L> left() {
            return Optional.empty();
        }

        @Override
        public Optional<M> middle() {
            return Optional.of(value);
        }

        @Override
        public Optional<R> right() {
            return Optional.empty();
        }

        public M get() { return value; }
    }

    public static final class Right<L, M, R> extends TriEither<L, M, R> {
        private final R value;
        private Right(R value) { this.value = value; }

        @Override
        public <T> T map(
                Function<? super L, ? extends T> leftFn,
                Function<? super M, ? extends T> middleFn,
                Function<? super R, ? extends T> rightFn
        ) {
            return rightFn.apply(value);
        }

        @Override public boolean isLeft() { return false; }
        @Override public boolean isMiddle() { return false; }
        @Override public boolean isRight() { return true; }

        @Override
        public TriEither<L, M, R> ifLeft(Consumer<? super L> action) {
            return this;
        }

        @Override
        public TriEither<L, M, R> ifMiddle(Consumer<? super M> action) {
            return this;
        }

        @Override
        public TriEither<L, M, R> ifRight(Consumer<? super R> action) {
            action.accept(value);
            return this;
        }

        @Override
        public Optional<L> left() {
            return Optional.empty();
        }

        @Override
        public Optional<M> middle() {
            return Optional.empty();
        }

        @Override
        public Optional<R> right() {
            return Optional.of(value);
        }

        public R get() { return value; }
    }
}

