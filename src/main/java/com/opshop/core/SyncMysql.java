package com.opshop.core;

import com.alibaba.fastjson.JSON;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.opshop.entity.ProductDetailModel;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * created by wangzelong 2019/3/11 17:26
 */
@Service
public class SyncMysql {
    @Autowired
    private TransportClient client;


    public void addEs() throws IOException {
        final BinaryLogClient client = new BinaryLogClient("127.0.0.1", 3306, "root", "123456");
        client.setBinlogFilename("mysql-bin.000001");
        client.setBinlogPosition(0);
        client.registerEventListener(new BinaryLogClient.EventListener() {
            @Override
            public void onEvent(Event event) {
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

            }
        });
        client.connect();
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
