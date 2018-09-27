package com.sequenceiq.it.cloudbreak.newway.wait;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.sequenceiq.cloudbreak.api.endpoint.v3.StackV3Endpoint;
import com.sequenceiq.cloudbreak.client.CloudbreakClient;
import com.sequenceiq.it.cloudbreak.WaitResult;

@Component
public class WaitUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(WaitUtil.class);

    private static final int MAX_RETRY = 360;

    @Value("${integrationtest.testsuite.pollingInterval:1000}")
    private long pollingInterval;

    public Map<String, String> waitAndCheckStatuses(CloudbreakClient cloudbreakClient, Long workspaceId, String stackName, Map<String, String> desiredStatuses) {
        Map<String, String> ret = new HashMap<>();
//        for (int i = 0; i < 3; i++) {
        WaitResult waitResult = waitForStatuses(cloudbreakClient, workspaceId, stackName, desiredStatuses);
        if (waitResult == WaitResult.FAILED) {
            StringBuilder builder = new StringBuilder("The stack has failed: ").append(System.lineSeparator());
            Map<String, Object> statusByNameInWorkspace = cloudbreakClient.stackV3Endpoint().getStatusByNameInWorkspace(workspaceId, stackName);
            if (statusByNameInWorkspace != null) {
                desiredStatuses.forEach((key, value) -> {
                    Object o = statusByNameInWorkspace.get(key);
                    if (o != null) {
                        ret.put(key, o.toString());
                    }
                });

                ret.forEach((key, value) -> {
                    builder.append(key).append(',').append(value).append(System.lineSeparator());
                });
                builder.append("statusReason: ").append(statusByNameInWorkspace.get("statusReason"));
            }
            throw new RuntimeException(builder.toString());
        }
        if (waitResult == WaitResult.TIMEOUT) {
            throw new RuntimeException("Timeout happened");
        }
//        }
        return ret;
    }

    private WaitResult waitForStatuses(CloudbreakClient cloudbreakClient, Long workspaceId, String stackName, Map<String, String> desiredStatuses) {
        WaitResult waitResult = WaitResult.SUCCESSFUL;
        Map<String, String> currentStatuses = new HashMap<>();

        int retryCount = 0;
        while (!checkStatuses(currentStatuses, desiredStatuses) && !checkFailedStatuses(currentStatuses) && retryCount < MAX_RETRY) {
            LOGGER.info("Waiting for status(es) {}, stack id: {}, current status(es) {} ...", desiredStatuses, stackName, currentStatuses);

            sleep();
            StackV3Endpoint stackV3Endpoint = cloudbreakClient.stackV3Endpoint();
            try {
                Map<String, Object> statusResult = stackV3Endpoint.getStatusByNameInWorkspace(workspaceId, stackName);
                for (String statusPath : desiredStatuses.keySet()) {
                    String currStatus = "DELETE_COMPLETED";
                    if (!CollectionUtils.isEmpty(statusResult)) {
                        currStatus = (String) statusResult.get(statusPath);
                    }
                    currentStatuses.put(statusPath, currStatus);
                }
            } catch (RuntimeException ignore) {
                continue;
            }

            retryCount++;
        }

        if (currentStatuses.values().stream().anyMatch(cs -> cs.contains("FAILED")) || checkNotExpectedDelete(currentStatuses, desiredStatuses)) {
            waitResult = WaitResult.FAILED;
            LOGGER.info("Desired status(es) are {} for {} but status(es) are {}", desiredStatuses, stackName, currentStatuses);
        } else  if (retryCount == MAX_RETRY) {
            waitResult = WaitResult.TIMEOUT;
            LOGGER.info("Timeout: Desired tatus(es) are {} for {} but status(es) are {}", desiredStatuses, stackName, currentStatuses);
        } else {
            LOGGER.info("{} are in desired status(es) {}", stackName, currentStatuses);
        }
        return waitResult;
    }

    private void sleep() {
        try {
            Thread.sleep(pollingInterval);
        } catch (InterruptedException e) {
            LOGGER.warn("Ex during wait", e);
        }
    }

    private boolean checkStatuses(Map<String, String> currentStatuses, Map<String, String> desiredStatuses) {
        boolean result = true;
        for (Map.Entry<String, String> desiredStatus : desiredStatuses.entrySet()) {
            if (!desiredStatus.getValue().equals(currentStatuses.get(desiredStatus.getKey()))) {
                result = false;
                break;
            }
        }
        return result;
    }

    private boolean checkFailedStatuses(Map<String, String> currentStatuses) {
        boolean result = false;
        List<String> failedStatuses = Arrays.asList("FAILED", "DELETE_COMPLETED");
        for (Map.Entry<String, String> desiredStatus : currentStatuses.entrySet()) {
            if (failedStatuses.stream().anyMatch(fs -> desiredStatus.getValue().contains(fs))) {
                LOGGER.info("In FAILED status: {}", currentStatuses);
                result = true;
                break;
            }
        }
        return result;
    }

    private boolean checkNotExpectedDelete(Map<String, String> currentStatuses, Map<String, String> desiredStatuses) {
        boolean result = false;
        for (Map.Entry<String, String> desiredStatus : desiredStatuses.entrySet()) {
            if (!"DELETE_COMPLETED".equals(desiredStatus.getValue()) && "DELETE_COMPLETED".equals(currentStatuses.get(desiredStatus.getKey()))) {
                result = true;
                break;
            }
        }
        return result;
    }
}
