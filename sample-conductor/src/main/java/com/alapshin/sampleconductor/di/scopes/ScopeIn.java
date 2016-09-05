package com.alapshin.sampleconductor.di.scopes;

import javax.inject.Scope;

@Scope
public @interface ScopeIn {
    Class<?> value();
}
