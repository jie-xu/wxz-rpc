//package com.github.wxz.rpc.netty.jmx;
//
//import com.github.wxz.rpc.config.RpcSystemConfig;
//import com.github.wxz.rpc.netty.core.recv.MsgRevExecutor;
//import org.springframework.util.ReflectionUtils;
//
//import java.util.*;
//
///**
// * @author xianzhi.wang
// * @date 2017/12/20 -21:11
// */
//public class HashModuleMetricsVisitor {
//    private static final HashModuleMetricsVisitor INSTANCE = new HashModuleMetricsVisitor();
//    private List<List<ModuleMetricsVisitor>> hashVisitorList = new ArrayList<List<ModuleMetricsVisitor>>();
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
//        Map<String, Object> map = MsgRevExecutor.getInstance().getHandlerMap();
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
