package io.katharsis.example.dropwizardSimple.jsonapi.model;

import com.github.jasminb.jsonapi.annotations.Id;
import com.github.jasminb.jsonapi.annotations.Links;
import com.github.jasminb.jsonapi.annotations.Meta;

/**
 *
 */
public class BaseResource {

    @Id
    private String id;

    @Meta
    private Meta meta;

    @Links
    private com.github.jasminb.jsonapi.Links links;
}
