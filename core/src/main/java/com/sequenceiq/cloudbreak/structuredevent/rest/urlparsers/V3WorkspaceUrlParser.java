package com.sequenceiq.cloudbreak.structuredevent.rest.urlparsers;

import java.util.regex.Matcher;

import org.springframework.stereotype.Component;

@Component
public class V3WorkspaceUrlParser extends RestUrlParser {

    @Override
    public String getPattern() {
        return "v3\\/workspaces";
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
        return "workspaces";
    }

    @Override
    protected String getResourceEvent(Matcher matcher) {
        return null;
    }
}
