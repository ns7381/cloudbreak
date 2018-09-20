package com.sequenceiq.it.cloudbreak.newway.ger;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.sequenceiq.it.cloudbreak.newway.mock.ImageCatalogServiceMock;
import com.sequenceiq.it.spark.ITResponse;
import com.sequenceiq.it.verification.Call;

import spark.Response;
import spark.Service;

@Component
@Scope("prototype")
public class SparkServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SparkServer.class);

    private String hostname;

    private int port;

    @Value("${mock.server.address}")
    private String mockServerAddress;

    @Value("#{'${integrationtest.cloudbreak.server}' + '${server.contextPath:/cb}'}")
    private String cloudbreakServerRoot;

    private Service sparkService;

    private final Map<Call, Response> requestResponseMap = new HashMap<>();

    private final java.util.Stack<Call> callStack = new java.util.Stack<Call>();

    public Map<Call, Response> getRequestResponseMap() {
        return requestResponseMap;
    }

    public Stack<Call> getCallStack() {
        return callStack;
    }

    public SparkServer(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void initSparkService() {
        if (sparkService == null) {
            sparkService = Service.ignite();
        }
        sparkService.port(port);
        File keystoreFile = createTempFileFromClasspath("/keystore_server");
        sparkService.secure(keystoreFile.getPath(), "secret", null, null);
        sparkService.before((req, res) -> res.type("application/json"));
        sparkService.after(
                (request, response) -> requestResponseMap.put(Call.fromRequest(request), response));
        sparkService.after(
                (request, response) -> callStack.push(Call.fromRequest(request))
        );

        callStack.clear();
        requestResponseMap.clear();
    }


    public String startImageCatalog(int port) {
        String imageCatalogAddress = String.join("", "https://", hostname, ":", port + "", ITResponse.IMAGE_CATALOG);
        ImageCatalogServiceMock imageCatalogServiceMock = new ImageCatalogServiceMock(sparkService);
        imageCatalogServiceMock.mockImageCatalogResponse(cloudbreakServerRoot);
        return imageCatalogAddress;
    }

    public String getEndpoint() {
        return "https://" + hostname + ":" + port;
    }

    protected static File createTempFileFromClasspath(String file) {
        try {
            InputStream sshPemInputStream = new ClassPathResource(file).getInputStream();
            File tempKeystoreFile = File.createTempFile(file, ".tmp");
            try (OutputStream outputStream = new FileOutputStream(tempKeystoreFile)) {
                IOUtils.copy(sshPemInputStream, outputStream);
            } catch (IOException e) {
                LOGGER.error("can't write " + file, e);
            }
            return tempKeystoreFile;
        } catch (IOException e) {
            throw new RuntimeException(file + " not found", e);
        }
    }

    public Service getSparkService() {
        return sparkService;
    }

    public int getPort() {
        return port;
    }

    public void stop() {
        if (sparkService != null) {
            sparkService.stop();
        }
    }
}
