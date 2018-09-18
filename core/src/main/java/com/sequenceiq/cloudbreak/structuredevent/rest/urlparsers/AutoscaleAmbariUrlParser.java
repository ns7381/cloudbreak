package com.sequenceiq.cloudbreak.structuredevent.rest.urlparsers;

import java.util.regex.Matcher;

import org.springframework.stereotype.Component;

@Component
public class AutoscaleAmbariUrlParser extends RestUrlParser {

    @Override
    public String getPattern() {
        return "autoscale\\/ambari";
    }

    @Override
    protected String getWorkspaceId(Matcher matcher) {
        return null;
    }

    @Override
    protected String getResourceName(Matcher matcher) {
        return null;
    }

    @Override
    protected String getResourceId(Matcher matcher) {
        return null;
    }

    @Override
    protected String getResourceType(Matcher matcher) {
        return "autoscale";
    }

    @Override
    protected String getResourceEvent(Matcher matcher) {
        return "ambari";
    }
}
