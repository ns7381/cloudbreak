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

    public Route get(String url, Route responseHandler) {
        Route route = overrideResponseByUrlWithSimple(HttpMethod.GET, url, responseHandler);
        service.get(url, route);
        return route;
    }

    public Route put(String url, Route responseHandler) {
        Route route = overrideResponseByUrlWithSimple(HttpMethod.PUT, url, responseHandler);
        service.put(url, route);
        return route;
    }

    public Route post(String url, Route responseHandler) {
        Route route = overrideResponseByUrlWithSimple(HttpMethod.POST, url, responseHandler);
        service.post(url, route);
        return route;
    }

    public Route delete(String url, Route responseHandler) {
        Route route = overrideResponseByUrlWithSimple(HttpMethod.DELETE, url, responseHandler);
        service.delete(url, route);
        return route;
    }

    public Route get(String url, StatefulRoute responseHandler) {
        Route route = overrideResponseByUrlWithStateful(HttpMethod.GET, url, responseHandler);
        service.get(url, route);
        return route;
    }

    public Route put(String url, StatefulRoute responseHandler) {
        Route route = overrideResponseByUrlWithStateful(HttpMethod.PUT, url, responseHandler);
        service.put(url, route);
        return route;
    }

    public Route post(String url, StatefulRoute responseHandler) {
        Route route = overrideResponseByUrlWithStateful(HttpMethod.POST, url, responseHandler);
        service.post(url, route);
        return route;
    }

    public Route delete(String url, StatefulRoute responseHandler) {
        Route route = overrideResponseByUrlWithStateful(HttpMethod.DELETE, url, responseHandler);
        service.delete(url, route);
        return route;
    }

    private Route overrideResponseByUrlWithSimple(HttpMethod method, String url, Route responseHandler) {
        RouteKey key = new RouteKey(method, url);
        if (mockResponders.get(key) == null) {
            CustomizeableDynamicRoute route = new CustomizeableDynamicRoute(responseHandler);
            mockResponders.put(key, route);
        }
        CustomizeableDynamicRoute modifiableRoute = mockResponders.get(key);
        modifiableRoute.setSimpleRouteImplementation(responseHandler);
        return modifiableRoute;
    }

    private Route overrideResponseByUrlWithStateful(HttpMethod method, String url, StatefulRoute responseHandler) {
        RouteKey key = new RouteKey(method, url);
        if (mockResponders.get(key) == null) {
            CustomizeableDynamicRoute route = new CustomizeableDynamicRoute(responseHandler, model);
            mockResponders.put(key, route);
        }
        CustomizeableDynamicRoute modifiableRoute = mockResponders.get(key);
        modifiableRoute.setRouteImplementation(responseHandler, model);
        return modifiableRoute;
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
