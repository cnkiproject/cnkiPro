package net.cnki.cnkispringboot.Model;

public class ExcelResultModel {
   private String VIEWNAME;
   private String VIEWSQL;
    private String REASON;//无效原因
   private String REMARK;//不存在数据表
    private String NODATA;//存在但无数据数据表
   private String ISVALID;

    public String getVIEWNAME() {
        return VIEWNAME;
    }

    public void setVIEWNAME(String VIEWNAME) {
        this.VIEWNAME = VIEWNAME;
    }

    public String getVIEWSQL() {
        return VIEWSQL;
    }

    public void setVIEWSQL(String VIEWSQL) {
        this.VIEWSQL = VIEWSQL;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getISVALID() {
        return ISVALID;
    }

    public void setISVALID(String ISVALID) {
        this.ISVALID = ISVALID;
    }

    public String getNODATA() {
        return NODATA;
    }

    public void setNODATA(String NODATA) {
        this.NODATA = NODATA;
    }

    public String getREASON() {
        return REASON;
    }

    public void setREASON(String REASON) {
        this.REASON = REASON;
    }
}
