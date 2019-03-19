package com.opshop.core;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.opshop.config.ConfigProperties;
import com.opshop.entity.ProductDetailModel;
import com.opshop.util.EsUtil;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by wangzelong 2019/3/11 17:26
 */
@Service
public class SyncMysql {
    @Autowired
    private ConfigProperties configProperties;
    @Autowired
    private EsUtil esUtil;

    volatile String fileName = "mysql-bin.000001";
    volatile String database = "";
    volatile String tableName = "";
    volatile long position = 0;

    @Async
    public void addEs() throws IOException {
        final BinaryLogClient client = new BinaryLogClient(configProperties.getIp(), configProperties.getPort(), configProperties.getUsername(), configProperties.getPassword());
        Map filePosition = getFilePosition();
        if (filePosition == null) {
            client.setBinlogFilename(fileName);
            client.setBinlogPosition(position);
        } else {
            fileName = (String) filePosition.get("fileName");
            position = (Long) filePosition.get("position");
            client.setBinlogFilename(fileName);
            client.setBinlogPosition(position);
        }
        client.registerEventListener(event -> {
            Boolean flag;
            Map positionMap = getFilePosition();
            EventHeader header = event.getHeader();
            EventType eventType = header.getEventType();
            EventData data = event.getData();
            if (positionMap != null && !getFlag(eventType)) {
                flag = (Boolean) positionMap.get("flag");
            } else {
                flag = getFlag(eventType);
            }
            if (flag) {
                if (EventType.TABLE_MAP.equals(eventType)) {
                    TableMapEventData eventData = (TableMapEventData) data;
                    database = eventData.getDatabase();
                    tableName = eventData.getTable();
                }
                if (EventType.isUpdate(eventType)) {
                    UpdateRowsEventData updateRowsEventData = (UpdateRowsEventData) data;
                    List<Map.Entry<Serializable[], Serializable[]>> rows = updateRowsEventData.getRows();

                    System.out.println(updateRowsEventData);
                }
                if (EventType.isWrite(eventType)) {
                    WriteRowsEventData writeRowsEventData = (WriteRowsEventData) data;
                    System.out.println(writeRowsEventData);
                }
                if (EventType.isDelete(eventType)) {
                    DeleteRowsEventData deleteRowsEventData = (DeleteRowsEventData) data;
                    System.out.println(deleteRowsEventData);
                }
            }
            if (!fileName.equals(client.getBinlogFilename())) {
                changeFilePosition(client.getBinlogFilename(), client.getBinlogPosition(), flag);
                fileName = client.getBinlogFilename();
                position = client.getBinlogPosition();
            }
        });
        client.connect();

    }

    private void changeFilePosition(String binlogFilename, long binlogPosition, Boolean flag) {
        Map<String, Object> map = new HashMap<>(3);
        map.put("fileName", binlogFilename);
        map.put("position", binlogPosition);
        map.put("flag", flag);
        IndexResponse result = esUtil.addDoc("fileposition", "map", "1", map);
        System.out.println(result.getVersion());
    }

    private Map getFilePosition() {
        String binlogFilename;
        long binlogPosition;
        boolean flag;
        try {
            Map<String, Object> sourceAsMap = esUtil.serachById("fileposition", "map", "1");
            if (sourceAsMap != null) {
                binlogFilename = (String) sourceAsMap.get("fileName");
                binlogPosition = Long.valueOf(sourceAsMap.get("position").toString());
                flag = (boolean) sourceAsMap.get("flag");
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Map<String, Object> map = new HashMap<>(3);
        map.put("fileName", binlogFilename);
        map.put("position", binlogPosition);
        map.put("flag", flag);
        return map;
    }

    private void addProduct(ProductDetailModel productDetailModel) {
        IndexResponse result = esUtil.addDoc("productlist", "list", productDetailModel.getProductId().toString(), productDetailModel);
        System.out.println(result.getId());
    }

    private void updateProduct(ProductDetailModel productDetailModel) {
        UpdateResponse result = esUtil.updateDoc("productlist", "list", productDetailModel.getProductId().toString(), productDetailModel);
        System.out.println(result.getId());
    }

    private void deleteProduct(String id) {
        DeleteResponse result = esUtil.delDoc("productlist", "list", id);
        System.out.println(result.getId());
    }

    private ProductDetailModel getProduct(String id) {
        ProductDetailModel productDetailModel = new ProductDetailModel();
        Map<String, Object> map = esUtil.serachById("productlist", "list", id);
        return productDetailModel;
    }

    volatile boolean isStart = false;

    private boolean getFlag(EventType eventType) {
        if (EventType.ANONYMOUS_GTID.equals(eventType)) {
            isStart = true;
        }
        if (EventType.XID.equals(eventType)) {
            isStart = false;
        }
        return isStart;
    }


}
