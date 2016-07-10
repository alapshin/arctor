package com.alapshin.arctor.sample.di.scopes;

import javax.inject.Scope;

@Scope
public @interface ScopeIn {
    Class<?> value();
}
