package com.sequenceiq.it.cloudbreak.newway.entity;

public interface CloudbreakEntity<T extends CloudbreakEntity<T>> {

    T valid();

    String getName();
}
