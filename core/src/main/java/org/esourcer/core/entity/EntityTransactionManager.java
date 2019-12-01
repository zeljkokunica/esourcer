package org.esourcer.core.entity;

import java.util.function.Supplier;

public interface EntityTransactionManager {
    <R> R executeInTransaction(final Supplier<R> runnable);

    class VoidEntityTransactionManager implements EntityTransactionManager {

        @Override
        public <R> R executeInTransaction(final Supplier<R> runnable) {
            return runnable.get();
        }
    }
}
