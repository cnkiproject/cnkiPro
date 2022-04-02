package net.cnki.cnkispringboot.Model;

public class ResultModel {
    private String TABLENAME;
    private String TABLEPATH;
    private String ISVIEW;
    private String EXP;
    private String DBASE;
    private String FILDCACHE;

    public String getTABLENAME() {
        return TABLENAME;
    }

    public void setTABLENAME(String TABLENAME) {
        this.TABLENAME = TABLENAME;
    }

    @Override
    public String toString() {
        return "ResultModel{" +
                "TABLENAME='" + TABLENAME + '\'' +
                ", TABLEPATH='" + TABLEPATH + '\'' +
                ", ISVIEW='" + ISVIEW + '\'' +
                ", EXP='" + EXP + '\'' +
                ", DBASE='" + DBASE + '\'' +
                ", FILDCACHE='" + FILDCACHE + '\'' +
                ", QUERYCACHE='" + QUERYCACHE + '\'' +
                ", TABLEPATH2='" + TABLEPATH2 + '\'' +
                ", TABLEPATH3='" + TABLEPATH3 + '\'' +
                ", CACHELEVEL='" + CACHELEVEL + '\'' +
                '}';
    }

    public String getTABLEPATH() {
        return TABLEPATH;
    }

    public void setTABLEPATH(String TABLEPATH) {
        this.TABLEPATH = TABLEPATH;
    }

    public String getISVIEW() {
        return ISVIEW;
    }

    public void setISVIEW(String ISVIEW) {
        this.ISVIEW = ISVIEW;
    }

    public String getEXP() {
        return EXP;
    }

    public void setEXP(String EXP) {
        this.EXP = EXP;
    }

    public String getDBASE() {
        return DBASE;
    }

    public void setDBASE(String DBASE) {
        this.DBASE = DBASE;
    }

    public String getFILDCACHE() {
        return FILDCACHE;
    }

    public void setFILDCACHE(String FILDCACHE) {
        this.FILDCACHE = FILDCACHE;
    }

    public String getQUERYCACHE() {
        return QUERYCACHE;
    }

    public void setQUERYCACHE(String QUERYCACHE) {
        this.QUERYCACHE = QUERYCACHE;
    }

    public String getTABLEPATH2() {
        return TABLEPATH2;
    }

    public void setTABLEPATH2(String TABLEPATH2) {
        this.TABLEPATH2 = TABLEPATH2;
    }

    public String getTABLEPATH3() {
        return TABLEPATH3;
    }

    public void setTABLEPATH3(String TABLEPATH3) {
        this.TABLEPATH3 = TABLEPATH3;
    }

    public String getCACHELEVEL() {
        return CACHELEVEL;
    }

    public void setCACHELEVEL(String CACHELEVEL) {
        this.CACHELEVEL = CACHELEVEL;
    }

    private String QUERYCACHE;
    private String TABLEPATH2;
    private String TABLEPATH3;
    private String CACHELEVEL;

}
