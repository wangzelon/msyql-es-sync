package com.opshop.dao;

import com.opshop.entity.TestOne;
import org.springframework.stereotype.Component;

/**
 * created by wangzelong 2019/3/16 10:56
 */
@Component
public interface TestOneRepository  {
    /**
     * 根据ID
     *
     * @param id
     * @return
     */
    TestOne queryTestOneById(String id);
}
