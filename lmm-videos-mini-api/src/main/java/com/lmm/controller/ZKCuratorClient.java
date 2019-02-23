package com.lmm.controller;

import com.lmm.config.ResourceConfig;
import com.lmm.enums.BGMOperatorTypeEnum;
import com.lmm.utils.JsonUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

@Component
public class ZKCuratorClient {
//    @Autowired
//    private BgmService bgmService;
    @Autowired
    private ResourceConfig resourceConfig;

    //    zk客户端
    private CuratorFramework client = null;
    private static Logger log = LoggerFactory.getLogger(ZKCuratorClient.class);

//    private static final String ZOOKEEPER_SERVER = "10.112.74.71:2181";

    public void init() {
        if (client != null) {
            return;
        }
//        重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
//        创建客户端
        client = CuratorFrameworkFactory.builder().connectString(resourceConfig.getZookeeperServer()).
                sessionTimeoutMs(10000).retryPolicy(retryPolicy).namespace("admin").
                build();
//        启动客户端
        client.start();

        try {
//            String testNodeDat = new String(
//                    client.getData().forPath("/bgm/18052674D26HH33P"));
//            log.info("测试的节点数据为：{}",testNodeDat);
            addChildWatch("/bgm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //监听事件
    public void addChildWatch(String nodePath) throws Exception {

        final PathChildrenCache cache = new PathChildrenCache(client, nodePath, true);
        //启动
        cache.start();
        //获取监听器的列表
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
                    throws Exception {

                if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
                    log.info("监听到事件：CHILD_ADDED");
                    //1.从数据库查询bgm对象，获取路劲
                    String path = event.getData().getPath();

                    String operatorObjStr = new String(event.getData().getData());
                    log.info("bgm的路径：{}",event.getData().getData().toString());
                    Map<String, String> map = JsonUtils.jsonToPojo(operatorObjStr, Map.class);
                    String operatorType = map.get("operType");
                    String songPath = map.get("path");

//                    String arr[] = path.split("/");
//                    String bgmId = arr[arr.length-1];

//                    Bgm bgm = bgmService.queryBgmById(bgmId);
//                    if (bgm ==null){
//                        return;
//                    }
                    //1.1 bgm所在的相对路径
//                    String songPath = bgm.getPath();
                    //2.定义保存到本地的bgm路径
//                    String filePath = "F:\\lmm_videos" + songPath;
                    String filePath = resourceConfig.getFileSpace() + songPath;

                    //3.定义下载的路径（播放URL）
                    String arrPath[] = songPath.split("\\\\");
                    String finalPath = "";
                    //3.1 处理URL路径的斜杆
                    for (int i = 0; i < arrPath.length; i++) {
                        if (StringUtils.isNotBlank(arrPath[i])) {
                            finalPath += "/";
//                            String transPath = URLEncoder.encode(arrPath[i], "UTF-8");
//                            transPath = URLEncoder.encode(arrPath[i], "UTF-8");
                            finalPath += URLEncoder.encode(arrPath[i], "UTF-8") ;
                        }
                    }

                    String bgmUrl = resourceConfig.getBgmServer() + finalPath;
//                    String bgmUrl = "http://10.112.74.71:8080/mvc" + finalPath;
                    if (operatorType.equals(BGMOperatorTypeEnum.ADD.type)) {
                        //下载bgm到springboot服务器
                        URL url = new URL(bgmUrl);

                        File file = new File(filePath);
                        FileUtils.copyURLToFile(url, file);
                        client.delete().forPath(path);
                    } else if (operatorType.equals(BGMOperatorTypeEnum.DELETE.type)) {
                        File file = new File(filePath);
                        FileUtils.forceDelete(file);
                        client.delete().forPath(path);
                    }
                }
            }
        });
    }
}
