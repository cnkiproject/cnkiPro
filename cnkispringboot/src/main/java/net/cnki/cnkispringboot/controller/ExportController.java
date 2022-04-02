package net.cnki.cnkispringboot.controller;


import net.cnki.cnkispringboot.Model.ExcelResultModel;
import net.cnki.cnkispringboot.Model.ResultModel;
import net.cnki.cnkispringboot.Utilitys.DateUtil;
import net.cnki.cnkispringboot.Utilitys.ExcelExportUtil;
import net.cnki.cnkispringboot.Utilitys.ExceptionUtil;
import net.cnki.cnkispringboot.Utilitys.KbaseConnect;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/export")
@ResponseBody
public class ExportController {
    private static final Logger logger = LogManager.getLogger(ExportController.class);

    @RequestMapping("/view")
    public String Views() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
       // String a="select * from BSAD0511,BSADLAST where 表名=BSAD2012";
        //String  nowheresql= a.substring(0,a.toLowerCase().indexOf("where"));
        //System.err.println(nowheresql);
        Export();
        stopWatch.stop();
        logger.info("OK，执行时间："+stopWatch.getTime(TimeUnit.SECONDS)+"s");
        return "OK，执行时间："+stopWatch.getTime(TimeUnit.SECONDS)+"s";
    }

    public static void Export() {
        try {
            int size = 512;
            //String ips = "10.15.11.1";
            String ips="10.25.11.1#10.15.11.1#10.25.11.101#10.15.11.101";
            //String ips="10.25.11.1#10.25.11.2#10.25.11.3#10.25.11.4#10.25.11.5#10.25.11.6#10.25.11.7#10.25.11.8#10.25.11.101#10.25.11.102#10.25.11.103#10.25.11.104#10.25.11.105#10.25.11.201#10.25.11.202#10.25.11.11#10.25.11.12#10.25.11.13#10.25.11.14#10.25.11.15#10.25.11.16#10.25.11.17#10.25.11.18#10.25.11.19#10.27.11.201#10.27.11.202#10.27.11.203#10.27.11.204\n" + "10.15.11.1#10.15.11.2#10.15.11.3#10.15.11.4#10.15.11.5#10.15.11.6#10.15.11.7#10.15.11.8#10.15.11.101#10.15.11.102#10.15.11.103#10.15.11.104#10.15.11.105#10.15.11.201#10.15.11.202#10.15.11.11#10.15.11.12#10.15.11.13#10.15.11.14#10.15.11.15#10.15.11.16#10.15.11.17 ";
            String[] iparr = ips.split("#");
            HashMap<String, LinkedList<ExcelResultModel>> map = new HashMap<>();
            for (String ip :
                    iparr) {
                HashMap<String, String> viewsMap = new HashMap<>();//所有视图
                HashMap<String, String> altabsMap = new HashMap<>();//所有表

                int allcount = KbaseConnect.GetSearchResultCount("SELECT * FROM sys_hotstar_system  where ISVIEW=Y  ORDER BY TABLENAME ASC", ip, "DBOWN", "");
                if(allcount<=0){
                    logger.error("IP="+ip+"：查询视图失败！");
                    continue;
                }
                int alltabcount = KbaseConnect.GetSearchResultCount("SELECT TABLENAME FROM sys_hotstar_system  where ISVIEW=N  ", ip, "DBOWN", "");
                int tabpage = (alltabcount - 1) / size + 1;//总页数
                for (int i = 0; i < tabpage; i++) {
                    String sql = "SELECT TABLENAME FROM sys_hotstar_system  where ISVIEW=N" + " LIMIT " + i*size + "," + size + "";
                    List<ResultModel> list = KbaseConnect.GetSearchResultListExport(sql, ip, "DBOWN", "");
                    for (ResultModel item :
                            list) {
                        altabsMap.put(item.getTABLENAME().toLowerCase().trim(),"1");

                    }
                }
                 alltabcount = KbaseConnect.GetSearchResultCount("SELECT TABLENAME FROM SYS_TABLE ", ip, "DBOWN", "");
                 tabpage = (alltabcount - 1) / size + 1;//总页数
                for (int i = 0; i < tabpage; i++) {
                    String sql = "SELECT TABLENAME FROM SYS_TABLE" + " LIMIT " + i*size + "," + size + "";
                    List<ResultModel> list = KbaseConnect.GetSearchResultListExport(sql, ip, "DBOWN", "");
                    for (ResultModel item :
                            list) {
                        altabsMap.put(item.getTABLENAME().toLowerCase().trim(),"1");

                    }
                }
                //System.err.println(ip + "共 " + allcount + "个视图");
                int page = (allcount - 1) / size + 1;//总页数
                //System.err.println(ip + "共 " + page + "页，size=" + size);
                LinkedList<ExcelResultModel> excelList = new LinkedList<>();
                for (int i = 0; i < page; i++) {
                    String sql = "SELECT TABLENAME FROM sys_hotstar_system  where ISVIEW=Y  ORDER BY TABLENAME ASC" + " LIMIT " + i*size + "," + size + "";
                    List<ResultModel> list = KbaseConnect.GetSearchResultListExport(sql, ip, "DBOWN", "");
                    for (ResultModel item :
                            list) {
                        viewsMap.put(item.getTABLENAME().toLowerCase().trim(),"1");
                    }
                }
                HashMap<String, String> maptab = new HashMap<>();//以查过的有问题表或视图
                for (int index = 0; index < page; index++) {/**/
                    String sql = "SELECT * FROM sys_hotstar_system  where ISVIEW=Y  ORDER BY TABLENAME ASC" + " LIMIT " + index*size + "," + size + "";
                    List<ResultModel> list = KbaseConnect.GetSearchResultListExport(sql, ip, "DBOWN", "");
                    for (ResultModel item :
                            list) {
                        ExcelResultModel excel = new ExcelResultModel();
                        excel.setVIEWNAME(item.getTABLENAME());
                        excel.setVIEWSQL(item.getEXP());
                        //视图是否有数据
                        int viewcount = KbaseConnect.GetSearchResultCount("SELECT * FROM " + item.getTABLENAME()+" limit 0,1", ip, "DBOWN", "");
                        if (viewcount <= 0) {
                            excel.setISVALID("否");
                            viewsMap.put(item.getTABLENAME().toLowerCase().trim(),"1");
                            String viewsql=item.getEXP();
                            if(viewsql.toLowerCase().contains("where")) {
                                String  nowheresql= viewsql.toLowerCase().substring(0,viewsql.toLowerCase().indexOf("where"));
                                int nowherecount = KbaseConnect.GetSearchResultCount(nowheresql+" limit 0,1", ip, "DBOWN", "");
                                if(nowherecount>0){
                                    excel.setREASON("包含WHERE条件语句");
                                }
                            }else{
                                int nowherecount = KbaseConnect.GetSearchResultCount(item.getEXP()+" limit 0,1", ip, "DBOWN", "");
                                if(nowherecount>0){
                                    excel.setREASON("需排查");
                                }
                            }

                        } else {
                            excel.setISVALID("是");
                        }
                        if (!StringUtils.isEmpty(item.getEXP())) {
                            String viewSql = item.getEXP().trim();

                            String tableNames ="";
                            if(viewSql.toLowerCase().contains("where")) {
                                tableNames= viewSql.substring(viewSql.toLowerCase().indexOf("from") + 4, viewSql.toLowerCase().indexOf("where"));
                            }else{
                                tableNames= viewSql.substring(viewSql.toLowerCase().indexOf("from") + 4);
                            }
                            String[] tabArr = tableNames.split(",");
                            String novalid = "";//不存在的数据表
                            String nodata = "";//无数据的数据表
                            for (String tab :
                                    tabArr) {
                                tab=tab.trim();
                                Boolean ishas=maptab.containsKey(tab.toLowerCase());
                                if(ishas){
                                    if(maptab.get(tab.toLowerCase().trim()).equals("noview")){//00不存在 视图
                                        novalid += tab + "(视图);";
                                    }else if(maptab.get(tab.toLowerCase().trim()).equals("notab")){//01 不存在 表
                                        novalid += tab + ";";
                                    }else if(maptab.get(tab.toLowerCase().trim()).equals("nodataview")){//10无数据 视图
                                        nodata+=tab + "(视图);";
                                    }
                                    else if(maptab.get(tab.toLowerCase().trim()).equals("nodatatab")){//11无数据 表
                                        nodata+=tab + ";";
                                    }
                                }else{
                                    Boolean isview=viewsMap.containsKey(tab.toLowerCase().trim());//是否是试图;
                                    if(isview){//shitu
                                        //int isviewscount = KbaseConnect.GetSearchResultCount("SELECT * FROM sys_hotstar_system  where ISVIEW=Y and TABLENAME = " + tab+" limit 0,1", ip, "DBOWN", "");
                                        if(!viewsMap.containsKey(tab.toLowerCase().trim())){//视图不存在
                                            maptab.put(tab.toLowerCase().trim(),"noview");//0/1 不存在/无数据    0/1 视图/表
                                            novalid += tab + "(视图);";
                                        }else{
                                            int datacount = KbaseConnect.GetSearchResultCount("SELECT * FROM "+tab+" "+" limit 0,1" , ip, "DBOWN", "");
                                            if(datacount<=0){
                                                nodata += tab + "(视图);";
                                                maptab.put(tab.toLowerCase().trim(),"nodataview");//1无数据 0视图
                                            }
                                        }
                                    }else{


                                        //这个语句可查数据表是否存在
                                        //int count = KbaseConnect.GetSearchResultCount("SELECT * FROM SYS_TABLE WHERE TABLENAME=" + tab+" limit 0,1", ip, "DBOWN", "");
                                        //0 不存在
                                        if (!altabsMap.containsKey(tab.toLowerCase().trim())) {
                                            //
                                            novalid += tab + ";";
                                            maptab.put(tab.toLowerCase().trim(),"notab");//0不存在 1表

                                        }else{
                                            int datacount = KbaseConnect.GetSearchResultCount("SELECT * FROM "+tab+" "+" limit 0,1" , ip, "DBOWN", "");
                                            if(datacount<=0){
                                                nodata += tab + ";";
                                                maptab.put(tab.toLowerCase().trim(),"nodatatab");//1无数据 1 表
                                            }
                                        }
                                    }
                                }
                            }
                            excel.setREMARK(novalid);
                            excel.setNODATA(nodata);
                        }

                        excelList.add(excel);
                    }
                }
                map.put(ip, excelList);
            }
            exportExcel(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtil.getStackTrace(e));
        }


    }

    private static void exportExcel(HashMap<String, LinkedList<ExcelResultModel>> map) {
        try {
            OutputStream out = new FileOutputStream("D:\\并行表统计" + DateUtil.getCurrentTimestamp() + ".xls");
            String[] headers = {"视图名", "视图SQL", "是否有效", "无条件检索","数据表不存在","存在但无数据"};
            ExcelExportUtil eeu = new ExcelExportUtil();
            HSSFWorkbook workbook = new HSSFWorkbook();
            int i = 0;
            HashMap<String, String> tablemap = new HashMap<>();

            for (String key : map.keySet()) {
                Set setnodata = new HashSet();
                Set setnotab = new HashSet();
                Set setView = new HashSet();
                List<List<String>> data = new ArrayList<List<String>>();
                LinkedList<ExcelResultModel> value = map.get(key);
                int valid = 0;
                for (ExcelResultModel info :
                        value) {
                    List rowData = new ArrayList();
                    rowData.add(info.getVIEWNAME());
                    rowData.add(info.getVIEWSQL());
                    rowData.add(info.getISVALID());
                    rowData.add(info.getREASON());
                    rowData.add(info.getREMARK());
                    rowData.add(info.getNODATA());
                    if (info.getISVALID().equals("是")) {
                        valid += 1;
                    } else {
                        setView.add(info.getVIEWNAME());
                }
                    if (!StringUtils.isEmpty(info.getREMARK())) {
                        for (String tab:
                                info.getREMARK().split(";")) {
                            setnotab.add(tab);
                        }
                    }
                    if (!StringUtils.isEmpty(info.getNODATA())) {
                        for (String tab:
                             info.getNODATA().split(";")) {
                            setnodata.add(tab);
                        }
                    }


                    data.add(rowData);
                }
                List rowData = new ArrayList();
                rowData.add("据统计" + key + "服务器，视图共 " + data.size() + "个，其中有效视图 " + valid + " 个"+(setView.size()!=0?"，无效视图 " + setView.size() + " 个":"。"));
                data.add(rowData);
                List rowData2 = new ArrayList();
                if(setView!=null&&setView.size()>0){
                    rowData2.add("其中无效视图的为:：" + String.join(",", setView) + "");
                    data.add(rowData2);
                }
                List rowData3 = new ArrayList();
                if(setnotab!=null&&setnotab.size()>0){
                    rowData3.add("其中数据表不存在的为： " + String.join(", ", setnotab) + "");
                    data.add(rowData3);
                }
                List rowData4 = new ArrayList();
                if(setnodata!=null&&setnodata.size()>0){
                    rowData4.add("其中存在但无数据的为： " + String.join(", ", setnodata) + "");
                    data.add(rowData4);
                }

                eeu.exportExcel(workbook, i, key, headers, data, out);
                i++;

            }
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtil.getStackTrace(e));
        }
    }
}
