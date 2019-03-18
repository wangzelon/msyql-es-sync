package com.opshop.core;

import com.alibaba.fastjson.JSON;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.opshop.config.ConfigProperties;
import com.opshop.entity.ProductDetailModel;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.get.GetField;
import org.springframework.beans.factory.annotation.Autowired;
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
    private TransportClient client;
    @Autowired
    private ConfigProperties configProperties;

    volatile String fileName = "mysql-bin.000001";

    volatile long position = 0;

    public void addEs() throws IOException {
        final BinaryLogClient client = new BinaryLogClient(configProperties.getIp(), configProperties.getPort(), configProperties.getUsername(), configProperties.getPassword());
        Map filePosition = getFilePosition();
        if (filePosition == null) {
            client.setBinlogFilename(fileName);
            client.setBinlogPosition(position);
        } else {
            client.setBinlogFilename((String) filePosition.get("fileName"));
            client.setBinlogPosition((Long) filePosition.get("position"));
        }
        client.registerEventListener(event -> {
            EventHeader header = event.getHeader();
            EventType eventType = header.getEventType();
            EventData data = event.getData();
            if (getFlag(eventType)) {
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
                changeFilePosition(client.getBinlogFilename(), client.getBinlogPosition());
                fileName = client.getBinlogFilename();
                position = client.getBinlogPosition();
            }
        });
        client.connect();

    }

    private void changeFilePosition(String binlogFilename, long binlogPosition) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("fileName", binlogFilename);
        map.put("position", binlogPosition);
        String jsonString = JSON.toJSONString(map);
        IndexResponse result = client.prepareIndex("filePosition", "map")
                .setSource(JSON.parseObject(jsonString)).setId("1")
                .get();
        System.out.println(result.getId());
    }

    private Map getFilePosition() {
        String binlogFilename;
        long binlogPosition;
        try {
            GetResponse response = client.prepareGet("filePosition", "map", "1").get();
            if (!response.isExists()) {
                return null;
            }
            GetField fileName = response.getField("fileName");
            GetField position = response.getField("position");
            binlogFilename = (String) fileName.getValue();
            binlogPosition = (long) position.getValue();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Map<String, Object> map = new HashMap<>(2);
        map.put("fileName", binlogFilename);
        map.put("position", binlogPosition);
        return map;
    }

    private void addProduct(ProductDetailModel productDetailModel) {
        String jsonString = JSON.toJSONString(productDetailModel);
        IndexResponse result = client.prepareIndex("productList", "mysql")
                .setSource(JSON.parseObject(jsonString))
                .get();
        System.out.println(result.getId());
    }

    private void updateProduct(ProductDetailModel productDetailModel) {
        String jsonString = JSON.toJSONString(productDetailModel);
        UpdateResponse result = client.prepareUpdate("productList", "mysql", productDetailModel.getProductId().toString()).
                setDoc(JSON.parseObject(jsonString)).get();
        System.out.println(result.getId());
    }

    private void deleteProduct(ProductDetailModel productDetailModel) {
        DeleteResponse result = client.prepareDelete("productList", "mysql", productDetailModel.getProductId().toString())
                .setId(productDetailModel.getProductId().toString()).get();
        System.out.println(result.getId());
    }

    private ProductDetailModel getProduct(String id) {
        ProductDetailModel productDetailModel = new ProductDetailModel();

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

    String database = "";
    String tableName = "";
}
