package net.cnki.cnkispringboot.Utilitys;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: yanp
 * @date: 2020/12/15 15:02
 * @description:
 */
public class GetPropertyByModel {
    /**
     * 根据实体类获取属性名(修改)
     * @param object
     * @return 返回修改语句+属性个数，目前的解决办法，如有新思路，请告知！！！
     */
    public  static Map<String,String> getPropertyString(Object object){
        if(object!=null){
            String str="";
            List<String> strList=new ArrayList<String>();
            Class kclass=(Class)object.getClass();
            Field[] declaredFields = kclass.getDeclaredFields();
            if(declaredFields!=null&&declaredFields.length>0){
                for (int i = 0; i < declaredFields.length; i++){
                   declaredFields[i].setAccessible(true);
                    try {
                        if(declaredFields[i].get(object)!=null){
                            strList.add(declaredFields[i].getName()+"='"+declaredFields[i].get(object)+"'");
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if(strList!=null&&strList.size()>0){
                    Map<String,String> map=new HashMap<>();
                    String updateSql=String.join(",",strList);
                    map.put("procount",declaredFields.length+"");//属性个数
                    map.put("prostr",updateSql);//属性拼接的修改串
                    return map;
                }
            }
        }
        return null;
    }

    /**
     * 根据实体类获取属性名(新增)
     * @param object
     * @return 返回新增语句+属性个数，目前的解决办法，如有新思路，请告知！！！
     */
    public  static Map<String,String> getAddPropertyString(Object object){
        if(object!=null){
            List<String> strList=new ArrayList<String>();
            List<String> strListVal=new ArrayList<String>();
            Class kclass=(Class)object.getClass();
            Field[] declaredFields = kclass.getDeclaredFields();
            if(declaredFields!=null&&declaredFields.length>0){
                for (int i = 0; i < declaredFields.length; i++){
                    declaredFields[i].setAccessible(true);
                    try {
                        if(declaredFields[i].get(object)!=null){
                            strList.add(declaredFields[i].getName());
                            strListVal.add(declaredFields[i].get(object).toString());
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if(strListVal!=null&&strListVal.size()>0){
                    Map<String,String> map=new HashMap<>();
                    String addProSql=String.join(",",strList);
                    String addValSql="'"+String.join("','",strListVal)+"'";
                    map.put("procount",declaredFields.length+"");//属性个数
                    map.put("prostr",addProSql);//添加的字段，拼接的新增串
                    map.put("prostrval",addValSql);//添加的字段所对应的值，拼接的新增串
                    return map;
                }
            }
        }
        return null;
    }
}
