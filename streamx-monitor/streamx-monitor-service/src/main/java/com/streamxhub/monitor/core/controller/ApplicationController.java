/**
 * Copyright (c) 2019 The StreamX Project
 * <p>
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.streamxhub.monitor.core.controller;

import com.streamxhub.monitor.base.controller.BaseController;
import com.streamxhub.monitor.base.domain.RestRequest;
import com.streamxhub.monitor.base.domain.RestResponse;
import com.streamxhub.monitor.base.properties.StreamXProperties;
import com.streamxhub.monitor.core.entity.Application;
import com.streamxhub.monitor.core.enums.AppExistsState;
import com.streamxhub.monitor.core.service.ApplicationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.*;

@Slf4j
@Validated
@RestController
@RequestMapping("flink/app")
public class ApplicationController extends BaseController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private StreamXProperties properties;

    @RequestMapping("list")
    public RestResponse list(Application app, RestRequest request) {
        IPage<Application> applicationList = applicationService.list(app, request);
        return RestResponse.create().data(applicationList);
    }

    @RequestMapping("yarn")
    public RestResponse yarn() {
        return RestResponse.create().data(properties.getYarn());
    }

    @RequestMapping("name")
    public RestResponse yarnName(Application app) {
        String yarnName = applicationService.getYarnName(app);
        return RestResponse.create().data(yarnName);
    }

    @RequestMapping("exists")
    public RestResponse exists(Application app) {
        AppExistsState exists = applicationService.checkExists(app);
        return RestResponse.create().data(exists.get());
    }

    @RequestMapping("cancel")
    public RestResponse cancel(Application app) {
        applicationService.cancel(app);
        return RestResponse.create();
    }

    @RequestMapping("create")
    public RestResponse create(Application app) throws IOException {
        boolean saved = applicationService.create(app);
        return RestResponse.create().data(saved);
    }

    @RequestMapping("deploy")
    public RestResponse deploy(Application app) {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                applicationService.deploy(app,true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return RestResponse.create();
    }

    @RequestMapping("startUp")
    public RestResponse startUp(String id) throws Exception {
        boolean started = applicationService.startUp(id);
        return RestResponse.create().data(started);
    }

}