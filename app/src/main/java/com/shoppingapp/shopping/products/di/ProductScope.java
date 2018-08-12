package com.shoppingapp.shopping.products.di;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.Policy;

import javax.inject.Scope;

@Scope
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface ProductScope {
}
