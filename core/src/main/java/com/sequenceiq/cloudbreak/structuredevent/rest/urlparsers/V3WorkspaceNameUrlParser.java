package com.sequenceiq.cloudbreak.structuredevent.rest.urlparsers;

import java.util.regex.Matcher;

import org.springframework.stereotype.Component;

@Component
public class V3WorkspaceNameUrlParser extends RestUrlParser {

    public static final int RESOURCE_NAME_GROUP_NUMBER = 1;

    // /v3/workspaces/name/abcdabcd/addUsers
    @Override
    public String getPattern() {
        return "v3\\/workspaces\\/name\\/([^\\d][^\\/]+)";
    }

    @Override
    protected String getWorkspaceId(Matcher matcher) {
        return null;
    }

    @Override
    protected String getResourceName(Matcher matcher) {
        return matcher.group(RESOURCE_NAME_GROUP_NUMBER);
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
