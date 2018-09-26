package com.sequenceiq.it.spark;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpMethod;

import com.sequenceiq.it.cloudbreak.newway.mock.DefaultModel;

import spark.Route;
import spark.Service;

public class DynamicRouteStack {

    private Map<RouteKey, CustomizeableDynamicRoute> mockResponders = new HashMap<>();

    private Service service;

    private DefaultModel model;

    public DynamicRouteStack(Service service, DefaultModel model) {
        this.service = service;
        this.model = model;
    }

    public Route overrideResponseByUrlWithSimple(HttpMethod method, String url, Route responseHandler) {
        RouteKey key = new RouteKey(method, url);
        if (mockResponders.get(key) == null) {
            CustomizeableDynamicRoute route = new CustomizeableDynamicRoute(responseHandler);
            mockResponders.put(key, route);
            addToSpark(method, url, route);
        }
        CustomizeableDynamicRoute modifiableRoute = mockResponders.get(key);
        modifiableRoute.setSimpleRouteImplementation(responseHandler);
        return modifiableRoute;
    }

    public Route overrideResponseByUrlWithStateful(HttpMethod method, String url, StatefulRoute responseHandler) {
        RouteKey key = new RouteKey(method, url);
        if (mockResponders.get(key) == null) {
            CustomizeableDynamicRoute route = new CustomizeableDynamicRoute(responseHandler, model);
            mockResponders.put(key, route);
            addToSpark(method, url, route);
        }
        CustomizeableDynamicRoute modifiableRoute = mockResponders.get(key);
        modifiableRoute.setRouteImplementation(responseHandler, model);
        return modifiableRoute;
    }

    private void addToSpark(HttpMethod method, String url, Route route) {
        switch (method) {
            case GET:
                service.get(url, route);
                break;
            case HEAD:
                service.head(url, route);
                break;
            case POST:
                service.post(url, route);
                break;
            case PUT:
                service.put(url, route);
                break;
            case DELETE:
                service.delete(url, route);
                break;
            case OPTIONS:
                service.options(url, route);
                break;
            default: throw new UnsupportedOperationException();
        }
    }

    private class RouteKey {

        private HttpMethod method;

        private String url;

        RouteKey(HttpMethod method, String url) {
            this.method = method;
            this.url = url;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            RouteKey that = (RouteKey) o;
            return method == that.method && Objects.equals(url, that.url);
        }

        @Override
        public int hashCode() {
            return Objects.hash(method, url);
        }
    }
}
