///**
// * Copyright (C) 2017 Newland Group Holding Limited
// * <p>
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// * <p>
// * http://www.apache.org/licenses/LICENSE-2.0
// * <p>
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.github.wxz.rpc.netty.jmx;
//
//import com.github.wxz.rpc.netty.core.MsgRecvExecutor;
//import org.springframework.util.ReflectionUtils;
//
//import java.util.*;
//
//
//public class HashModuleMetricsVisitor {
//    private List<List<ModuleMetricsVisitor>> hashVisitorList = new ArrayList<List<ModuleMetricsVisitor>>();
//
//    private static final HashModuleMetricsVisitor INSTANCE = new HashModuleMetricsVisitor();
//
//    private HashModuleMetricsVisitor() {
//        init();
//    }
//
//    public static HashModuleMetricsVisitor getInstance() {
//        return INSTANCE;
//    }
//
//    public int getHashModuleMetricsVisitorListSize() {
//        return hashVisitorList.size();
//    }
//
//    private void init() {
//        Map<String, Object> map = MsgRecvExecutor.getInstance().getHandlerMap();
//        ReflectionUtils utils = new ReflectionUtils();
//        Set<String> s = (Set<String>) map.keySet();
//        Iterator<String> iter = s.iterator();
//        String key;
//        while (iter.hasNext()) {
//            key = iter.next();
//            try {
//                List<String> list = utils.getClassMethodSignature(Class.forName(key));
//                for (String signature : list) {
//                    List<ModuleMetricsVisitor> visitorList = new ArrayList<ModuleMetricsVisitor>();
//                    for (int i = 0; i < RpcSystemConfig.SYSTEM_PROPERTY_JMX_METRICS_HASH_NUMS; i++) {
//                        ModuleMetricsVisitor visitor = new ModuleMetricsVisitor(key, signature);
//                        visitor.setHashKey(i);
//                        visitorList.add(visitor);
//                    }
//                    hashVisitorList.add(visitorList);
//                }
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void signal() {
//        ModuleMetricsHandler.getInstance().getLatch().countDown();
//    }
//
//    public List<List<ModuleMetricsVisitor>> getHashVisitorList() {
//        return hashVisitorList;
//    }
//
//    public void setHashVisitorList(List<List<ModuleMetricsVisitor>> hashVisitorList) {
//        this.hashVisitorList = hashVisitorList;
//    }
//}
//
