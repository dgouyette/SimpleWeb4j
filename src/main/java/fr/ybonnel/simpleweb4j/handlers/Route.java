/*
 * Copyright 2013- Yan Bonnel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.ybonnel.simpleweb4j.handlers;

import com.google.common.base.Splitter;
import fr.ybonnel.simpleweb4j.exception.HttpErrorException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Route for SimpleWeb4j.
 * @see fr.ybonnel.simpleweb4j.SimpleWeb4j#get(Route)
 * @see fr.ybonnel.simpleweb4j.SimpleWeb4j#post(Route)
 * @see fr.ybonnel.simpleweb4j.SimpleWeb4j#put(Route)
 * @see fr.ybonnel.simpleweb4j.SimpleWeb4j#delete(Route)
 * @param <P> type of the object in request's body.
 * @param <R> type of the object to serialize in response body.
 */
public abstract class Route<P, R> {

    /**
     * Path of the route.
     */
    private String routePath;
    /**
     * Path in segment (split on '/').
     */
    private List<String> pathInSegments;
    /**
     * Class of the parameter (object in request's body).
     */
    private Class<P> paramType;

    /**
     * Constructor of a route.
     * @param routePath routePath of the route.
     * @param paramType class of the object in request's body.
     */
    public Route(String routePath, Class<P> paramType) {
        this.routePath = routePath;
        pathInSegments = newArrayList(Splitter.on('/').omitEmptyStrings().trimResults().split(routePath));
        this.paramType = paramType;
    }

    /**
     * @return class of the object in request's body.
     */
    public Class<P> getParamType() {
        return paramType;
    }

    /**
     * Used to know is a route is valid for a routePath.
     * @param path the routePath.
     * @return true if the route is valid.
     */
    protected boolean isThisPath(String path) {
        if (this.routePath.equals(path)) {
            return true;
        }
        List<String> queryPath = newArrayList(Splitter.on('/').omitEmptyStrings().trimResults().split(path));
        if (queryPath.size() == pathInSegments.size()) {
            boolean same = true;
            for (int index = 0; index < queryPath.size(); index++) {
                if (!pathInSegments.get(index).startsWith(":")
                        && !pathInSegments.get(index).equals(queryPath.get(index))) {
                    same = false;
                    break;
                }
            }
            return same;
        } else {
            return false;
        }

    }

    /**
     * Get the parameters in route.
     * @param pathInfo the routePath.
     * @return the map of parameters in routePath.
     */
    protected Map<String, String> getRouteParams(String pathInfo) {
        Map<String, String> params = new HashMap<>();
        List<String> queryPath = newArrayList(Splitter.on('/').omitEmptyStrings().trimResults().split(pathInfo));
        for (int index = 0; index < queryPath.size(); index++) {
            if (pathInSegments.get(index).startsWith(":")) {
                params.put(pathInSegments.get(index).substring(1), queryPath.get(index));
            }
        }
        return Collections.unmodifiableMap(params);
    }

    /**
     * Method to implement to handle a request on the route.
     * @param param the parameter object in request's body.
     * @param routeParams parameters in the routePath.
     * @return the response to send.
     * @throws HttpErrorException use it for any http error, like new HttpErrorException(404) for "not found" error.
     */
    public abstract Response<R> handle(P param, RouteParameters routeParams) throws HttpErrorException;
}