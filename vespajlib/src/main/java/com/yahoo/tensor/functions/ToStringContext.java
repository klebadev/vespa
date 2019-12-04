// Copyright 2017 Yahoo Holdings. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
package com.yahoo.tensor.functions;

/**
 * A context which is passed down to all nested functions when returning a string representation.
 *
 * @author bratseth
 */
public interface ToStringContext {

    static ToStringContext empty() { return new EmptyStringContext(); }

    /** Returns the name an identifier is bound to, or null if not bound in this context */
    String getBinding(String name);

    /** Returns another context this wraps, or null if none is wrapped */
    ToStringContext wrapped();

    class EmptyStringContext implements ToStringContext {

        @Override
        public String getBinding(String name) { return null; }

        @Override
        public ToStringContext wrapped() { return null; }

    }

}
