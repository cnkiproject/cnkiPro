package net.cnki.cnkispringboot.Utilitys;



import com.kbase.jdbc.ConnectionImpl;
import com.kbase.jdbc.PreparedStatement;
import com.kbase.jdbc.ResultSetImpl;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cnki.cnkispringboot.Model.ResultModel;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KbaseConnect {
    private static final Logger logger = LogManager.getLogger(KbaseConnect.class);
    private static String driverName = "com.kbase.jdbc.Driver";

    private static String url = "jdbc:kbase://10.4.7.11";

    private static String userName = "DBOWN";

    private static String userPwd = "";

    public static ConnectionImpl getConnection() throws SQLException {
        ConnectionImpl con = null;
        try {
            loadDriver();
            con = (ConnectionImpl) DriverManager.getConnection(url, userName, userPwd);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
        }
        return con;
    }

    public static ConnectionImpl getConnection(String ip, String userName, String userPwd) throws SQLException {
        ConnectionImpl con = null;
        try {
            loadDriver();
            con = (ConnectionImpl) DriverManager.getConnection("jdbc:kbase://" + ip, userName, userPwd);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
        }
        return con;
    }

    public static int executeSql(Connection conn, PreparedStatement pre, String sql) {
        int temp = 0;
        try {
            pre = (PreparedStatement) conn.prepareStatement(sql);
            temp = pre.executeUpdate();
        } catch (SQLException ex) {
            release(null, pre, conn);
            System.out.println(ex.getMessage().toString());
            ex.printStackTrace();
        }
        return temp;
    }

    public static ResultSetImpl getRestult(Connection conn, PreparedStatement pre, ResultSet rs, String sql) {
        ResultSetImpl result = null;
        try {
            pre = (PreparedStatement) conn.prepareStatement(sql);

            result = (ResultSetImpl) pre.executeQuery(false);
        } catch (SQLException e) {
            release(rs, pre, conn);
            System.out.println(e.getMessage().toString());
            e.printStackTrace();
        }
        return result;
    }

    public static void loadDriver() {
        try {
            Class.forName(driverName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void release(ResultSet rs, PreparedStatement stat, Connection con) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            rs = null;
        }
        if (stat != null) {
            try {
                stat.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            stat = null;
        }
        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            con = null;
        }
    }


    public static Map<String,List<String>> GetSearchResult(String ip,String user,String pwd) {
        String sql="SELECT TABLENAME,DBASE FROM sys_hotstar_system where ISVIEW=N";
        Map<String,List<String>> maps=new HashMap<>();
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            List<String> list=null;
            ConnectionImpl connectionImpl = getConnection(ip,user,pwd);
            ResultSetImpl rst = getRestult((Connection) connectionImpl, pre, rs, sql);
            if (rst != null) {
                while (rst.next()) {
                    String tablename=rst.getString(1);
                    String dbase=rst.getString(2);
                    if(maps.containsKey(dbase)) {
                        maps.get(dbase).add(tablename);
                    }else {
                        list=new ArrayList<String>();
                        list.add(tablename);
                        maps.put(dbase, list);
                    }
                }
            }
            release(null, pre, (Connection) connectionImpl);
        } catch (Exception e) {
            System.out.println(e.getMessage().toString());
            e.printStackTrace();
        } finally {
        }
        return maps;
    }
    public static int GetSearchResult(String sql,String ip,String user,String pwd) {
        int num=0;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            ConnectionImpl connectionImpl = getConnection(ip,user,pwd);
            ResultSetImpl rst = getRestult((Connection) connectionImpl, pre, rs, sql);
            if (rst != null) {
                while (rst.next()) {
                    num=rst.getInt(1);
                }
            }
            release(null, pre, (Connection) connectionImpl);
        } catch (Exception e) {
            System.out.println(e.getMessage().toString());
            e.printStackTrace();
        } finally {
        }
        return num;
    }
    public static int GetSearchResultCount(String sql,String ip,String user,String pwd) {
        logger.info("IP="+ip+"；  SQL="+sql);
        int  count=0;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            ConnectionImpl connectionImpl = getConnection(ip,user,pwd);
            ResultSetImpl rst = getRestult((Connection) connectionImpl, pre, rs, sql);
           count= rst.getHitCount();
            release(null, pre, (Connection) connectionImpl);
        } catch (Exception e) {
            System.out.println(e.getMessage().toString());
            e.printStackTrace();
        } finally {
        }
        return count;
    }
    public static String GetSearchResultStr(String sql,String ip,String user,String pwd) {
        String str="";
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            ConnectionImpl connectionImpl = getConnection(ip,user,pwd);
            ResultSetImpl rst = getRestult((Connection) connectionImpl, pre, rs, sql);
            if (rst != null) {
                while (rst.next()) {
                    str=rst.getString(1);
                }
            }
            release(null, pre, (Connection) connectionImpl);
        } catch (Exception e) {
            System.out.println(e.getMessage().toString());
            e.printStackTrace();
        } finally {
        }
        return str;
    }
    public static List<String> GetSearchResultList(String sql,String ip,String user,String pwd) {
        List<String>  list=new ArrayList<>();
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            ConnectionImpl connectionImpl = getConnection(ip,user,pwd);
            ResultSetImpl rst = getRestult((Connection) connectionImpl, pre, rs, sql);
            if (rst != null) {
                while (rst.next()) {
                    list.add(rst.getString(1));
                }
            }
            release(null, pre, (Connection) connectionImpl);
        } catch (Exception e) {
            System.out.println(e.getMessage().toString());
            e.printStackTrace();
        } finally {
        }
        return list;
    }

    public static List<ResultModel> GetSearchResultListExport(String sql, String ip, String user, String pwd) {
        List<ResultModel>  list=new ArrayList<>();
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            ConnectionImpl connectionImpl = getConnection(ip,user,pwd);
            ResultSetImpl rst = getRestult((Connection) connectionImpl, pre, rs, sql);
            if (rst != null) {
                while (rst.next()) {
                    ResultModel model=new ResultModel();
                    model.setTABLENAME(rst.getString("TABLENAME"));
                    model.setTABLEPATH(rst.getString("TABLEPATH"));
                    model.setEXP(rst.getString("EXP"));
                    list.add(model);
                }
            }
            release(null, pre, (Connection) connectionImpl);
        } catch (Exception e) {
            System.out.println(e.getMessage().toString());
            e.printStackTrace();
        } finally {
        }
        return list;
    }
    public static List<String> GetResultByList(String sql,String ip,String user,String pwd) {

        List<String> list=new ArrayList<>();
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            ConnectionImpl connectionImpl = getConnection(ip,user,pwd);
            ResultSetImpl rst = getRestult((Connection) connectionImpl, pre, rs, sql);
            if (rst != null) {
                while (rst.next()) {
                    //�汾�仯,��Ҫ���¼����ϣ���ļ���С: 2 MB,ʵ�ʷ����仯�����ݿ��С: 1 MB
                    String temp=rst.getString(1).replace("�汾�仯,��Ҫ���¼����ϣ���ļ���С: ", "").replace("ʵ�ʷ����仯�����ݿ��С:", "");
                    System.out.println(temp);
                }
            }
            release(null, pre, (Connection) connectionImpl);
        } catch (Exception e) {
            System.out.println(e.getMessage().toString());
            e.printStackTrace();
        } finally {
        }
        return list;
    }
    public static List<String> GetSearchSingleResult(String sql,String ip,String user,String pwd) {
        List<String> list=new ArrayList<>();
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            ConnectionImpl connectionImpl = getConnection(ip,user,pwd);
            ResultSetImpl rst = getRestult((Connection) connectionImpl, pre, rs, sql);
            if (rst != null) {
                while (rst.next()) {
                    String info=String.format("DBUM REFRESH SORTFILE OF TABLE  %s", rst.getString(1));
                    list.add(info);
                    list.add("GO");
                }
            }
            release(null, pre, (Connection) connectionImpl);
        } catch (Exception e) {
            System.out.println(e.getMessage().toString());
            e.printStackTrace();
        } finally {
        }
        return list;
    }
    public static String GetSearchMultResult(String sql,String ip,String user,String pwd,String table) {
        String str= "δͬ�������쳣:     "+table;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            ConnectionImpl connectionImpl = getConnection(ip,user,pwd);
            ResultSetImpl rst = getRestult((Connection) connectionImpl, pre, rs, sql);
            //��ʼ�߼�
            int rowsCount=rst.getHitCount();
            if (rst != null) {

                rst.last();
                str+="  "+rst.getString(1);
                str +="--------";
                str+=rst.getString(2);
                str +="--------";
                str+=rst.getString(3);
                str +="--------";
                str+=rst.getString(4);
                System.out.println(str);
            }
            release(null, pre, (Connection) connectionImpl);
        } catch (Exception e) {

        } finally {
            return str;
        }

    }
    public static void logstashResult(String sql,String ip,String user,String pwd,File file) {
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            ConnectionImpl connectionImpl = getConnection(ip,user,pwd);
            ResultSetImpl rst = getRestult((Connection) connectionImpl, pre, rs, sql);
            List<String> list=new ArrayList<>();
            while(rst.next()) {
                String info=String.format("[%s]-[%s]-[%s]-%s", rst.getString(1),rst.getString(2),rst.getString(3),rst.getString(4).replaceAll("\\r|\\n",""));
                System.out.println(info);
                list.add(info);
                FileUtils.writeLines(file,list ,true);
                list.clear();
            }

            release(null, pre, (Connection) connectionImpl);
        } catch (Exception e) {

        } finally {

        }

    }
    //���㷢��ָ��Ͱ汾һ��
    public static int GetSearchByList(String sql,String ip,String user,String pwd){
        int rowsCount=0;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            ConnectionImpl connectionImpl = getConnection(ip,user,pwd);
            ResultSetImpl rst = getRestult((Connection) connectionImpl, pre, rs, sql);
            rowsCount=rst.getHitCount();
            release(null, pre, (Connection) connectionImpl);
        } catch (Exception e) {

        } finally {
            return rowsCount;
        }
    }
    public static Map<String,List<String>> GetSearchByMap(String sql,String ip,String user,String pwd){
        Map<String,List<String>> hashMap=new HashMap<>();
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            ConnectionImpl connectionImpl = getConnection(ip,user,pwd);
            ResultSetImpl rst = getRestult((Connection) connectionImpl, pre, rs, sql);
            while(rst.next()) {
                String tablename=rst.getString(1);
                String content=rst.getString(2).replace("ͬ����ʼ, ͬ���ڵ��б�Ϊ��", "").trim();
                hashMap.put(tablename,Arrays.asList(content.split(",")));
            }
            release(null, pre, (Connection) connectionImpl);
        } catch (Exception e) {

        } finally {
            return hashMap;
        }
    }
    public static Map<String,String> GetResultByMap(String sql,String ip,String user,String pwd){
        Map<String,String> hashMap=new HashMap<>();
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            ConnectionImpl connectionImpl = getConnection(ip,user,pwd);
            ResultSetImpl rst = getRestult((Connection) connectionImpl, pre, rs, sql);
            while(rst.next()) {
                String tablename=rst.getString(1);
                String content=rst.getString(2);
                hashMap.put(tablename,content);
            }
            release(null, pre, (Connection) connectionImpl);
        } catch (Exception e) {

        } finally {
            return hashMap;
        }
    }

    public static int commonFunction(String sql,String ip,String user,String pwd) {
        ConnectionImpl connectionImpl = null;
        sql = sql.replace(", ", ",");
        PreparedStatement pre = null;
        Connection conn = null;
        int num = 0;
        try {
            connectionImpl = getConnection(ip,user,pwd);
            num = executeSql((Connection) connectionImpl, pre, sql);
            release(null, pre, (Connection) connectionImpl);
        } catch (Exception ex) {
            release(null, pre, (Connection) connectionImpl);
            System.out.println(ex.getMessage().toString());
            ex.printStackTrace();
        } finally {
        }
        return num;
    }
    public static Map<String,String> GetSearchResultByMap(String sql,String ip,String user,String pwd) {
        List<String> list=new ArrayList<>();
        PreparedStatement pre = null;
        ResultSet rs = null;
        Map<String,String> map=null;
        try {
            map=new HashMap<>();
            ConnectionImpl connectionImpl = getConnection(ip,user,pwd);
            ResultSetImpl rst = getRestult((Connection) connectionImpl, pre, rs, sql);
            if (rst != null) {
                while (rst.next()) {
                    map.put(rst.getString(1),rst.getString(2));
                }
            }
            release(null, pre, (Connection) connectionImpl);
        } catch (Exception e) {
            System.out.println(e.getMessage().toString());
            e.printStackTrace();
        } finally {
        }
        return map;
    }


    public static String GetSearchResultByList(String ip,String user,String pwd,String tableName) {
        String sql ="SELECT key,value FROM SYS_table where tablename="+tableName;
        String backStr="";
        String teleIp="0.0.0.0";
        String path="0";
        String keyType="0";
        String dbName="";
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            ConnectionImpl connectionImpl = getConnection(ip,user,pwd);
            if(connectionImpl!=null) {
                ResultSetImpl rst = getRestult((Connection) connectionImpl, pre, rs, sql);
                if (rst != null) {
                    while (rst.next()) {
                        String key=rst.getString(1).trim();
                        String value=rst.getString(2).trim();

                        if(key.equals("path")) path=value.substring(0,value.lastIndexOf("\\"));
                        if(key.equals("type"))  keyType=value;
                        if(key.equals("host_ip")) teleIp=value;
                    }
                }
                String sql1="SELECT DBASE FROM sys_hotstar_system where  tablename="+tableName+" AND ISVIEW=N";
                ResultSetImpl rst1 = getRestult((Connection) connectionImpl, pre, rs, sql1);
                if (rst1 != null) {
                    while (rst1.next()) {
                        dbName=rst1.getString(1);
                    }
                }
                if(keyType.equals("TELE")) backStr=ip+"#"+tableName+"#"+path+"#"+ teleIp+"#"+dbName;
                release(null, pre, (Connection) connectionImpl);
            }else
                backStr=ip+"#"+tableName+"#"+path+"#"+ teleIp+"#"+dbName;
        } catch (Exception e) {
            System.out.println(e.getMessage().toString());

        } finally {
            backStr=ip+"#"+tableName+"#"+path+"#"+ teleIp+"#"+dbName;
        }
        return backStr;
    }
    public static boolean GetSearchResultByList(String info) {
        String[] arr=info.split("#");
        String ip=arr[0];
        String tableName=arr[1];
        String backInfo=GetSearchResultByList(ip,"DBOWN","",tableName).trim();
        return info.trim().equals(backInfo);
    }

    public static boolean dropAndCreate(String info) {
        String[] arr=info.split("#");
        String ip=arr[0];
        String tableName=arr[1];
        String path=arr[2];
        String teleIp=arr[3];
        String db=arr[4];

        int num = 0;
        String dropTable="DROP TABLE " +tableName;
        String conncetTalbe="CONNECT TELE TABLE "+tableName+" PATH '"+path+"' '"+teleIp+"' 4567 DBOWN '' AT "+db+"";
        System.out.println(dropTable +"   "+conncetTalbe);
        int dropNum=commonFunction(dropTable,ip,"DBOWN","");
        System.out.println("DROP�����״̬: "+dropNum);
        int connNum=commonFunction(conncetTalbe,ip,"DBOWN","");
        System.out.println("���Ӳ��б�״̬: "+connNum);
        return  dropNum==1 && connNum==0;

    }
    //SELECT TABLENAME,ASSISTTABLE,COMMITTABLE,SYNODES,* FROM SYS_PREPUB_WRITE
    /*********************�鿴ʵʱ����������***************************/
    public static List<String> GetSearchPubTableByList(String ip,String user) {
        String sql ="SELECT TABLENAME,ASSISTTABLE,COMMITTABLE,SYNODES FROM SYS_PREPUB_WRITE" ;
        List<String> list=null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            ConnectionImpl connectionImpl = getConnection(ip,user,"");
            if(connectionImpl!=null) {
                ResultSetImpl rst = getRestult((Connection) connectionImpl, pre, rs, sql);
                if (rst != null) {
                    list=new ArrayList<>();
                    while (rst.next()) {
                        list.add(ip+"  "+rst.getString(1).trim()+"  "+rst.getString(2).trim()+"  "+rst.getString(3).trim()+"  "+rst.getString(4).trim());
                    }
                }

            }

        } catch (Exception e) {

        } finally {
        }
        return list;
    }

    /*********************���ʵ��Ƿ����***************************/
    public static List<String> GetSearchResultByList(String ip,String user) {
        String sql ="SELECT tablename FROM SYS_SYNC_CONFIG" ;
        List<String> list=null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            ConnectionImpl connectionImpl = getConnection(ip,user,"");
            if(connectionImpl!=null) {
                ResultSetImpl rst = getRestult((Connection) connectionImpl, pre, rs, sql);
                if (rst != null) {
                    list=new ArrayList<>();
                    while (rst.next()) {
                        String key=rst.getString(1).trim();
                        list.add(key);
                    }
                }

            }

        } catch (Exception e) {

        } finally {
        }
        return list;
    }
    public static List<String> ProductSqlByDict(String ip,String user,String sql) {
        List<String> list=null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            ConnectionImpl connectionImpl = getConnection(ip,user,"");
            if(connectionImpl!=null) {
                ResultSetImpl rst = getRestult((Connection) connectionImpl, pre, rs, sql);
                if (rst != null) {
                    list=new ArrayList<>();
                    while (rst.next()) {
                        String col=rst.getString(1).trim();
                        String dict=rst.getString(2).trim();
                        list.add(String.format("dbum make sortcol by '%s' ( CMFD202102.%s )", dict,col));
                        list.add("GO");
                    }
                }

            }

        } catch (Exception e) {

        } finally {
        }
        return list;
    }
    public static int CheckDictIsExist(String ip,String user,String tablename) {
        String sql ="SELECT ROWNUM FROM sys_sortcol  WHERE  tablename="+tablename+"  and DICT=num1000" ;
        int key=-1;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            ConnectionImpl connectionImpl = getConnection(ip,user,"");
            if(connectionImpl!=null) {
                ResultSetImpl rst = getRestult((Connection) connectionImpl, pre, rs, sql);
                if (rst != null) {
                    while (rst.next()) {
                        key=rst.getInt(1);
                        //if(key >0) flag=true;
                    }
                }

            }

        } catch (Exception e) {

        } finally {
        }
        return key;
    }

    public static List<String> CheckTableIndexMode(String ip,String user,String tablename) {
        String sql ="select FIELDNAME from sys_field where tablename="+tablename+" AND INDEXMODE=NON" ;
        List<String> keys=new ArrayList<>();
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            ConnectionImpl connectionImpl = getConnection(ip,user,"");
            if(connectionImpl!=null) {
                ResultSetImpl rst = getRestult((Connection) connectionImpl, pre, rs, sql);
                if (rst != null) {
                    while (rst.next()) {
                        keys.add(rst.getString(1));
                    }
                }

            }

        } catch (Exception e) {

        } finally {
        }
        return keys;
    }
}

