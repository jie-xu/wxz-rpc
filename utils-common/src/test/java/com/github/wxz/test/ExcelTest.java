package com.github.wxz.test;

import com.alibaba.fastjson.JSON;
import com.github.wxz.other.excel.ExcelExportUtil;
import com.github.wxz.other.excel.ExcelImportUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author xianzhi.wang
 * @date 2017/12/19 -9:54
 */
public class ExcelTest {

    public static void main(String[] args) {

        /**
         * Mock数据，Java对象列表
         */
        List<ShopDTO> shopDTOList = new ArrayList<ShopDTO>();
        for (int i = 0; i < 100; i++) {
            ShopDTO shop = new ShopDTO(true, "商户" + i, (short) i, 1000 + i, 10000 + i, (float) (1000 + i), (double) (10000 + i), new Date(), LocalDateTime.now());
            shopDTOList.add(shop);
        }
        String filePath = "e:/demo-sheet.xls";

        /**
         * Excel导出：Object 转换为 Excel
         */
        ExcelExportUtil.exportToFile(filePath, shopDTOList, shopDTOList);

        /**
         * Excel导入：Excel 转换为 Object
         */
        List<Object> list = ExcelImportUtil.importExcel(ShopDTO.class, filePath);

        System.out.println(JSON.toJSONString(list));

    }
}
