package com.hackerkernel.firebaseconnect.Model;

public class PostModel {

  String clas;
   String nam;
String rollno;

    public PostModel(){
    }

    @Override
    public String toString() {
        return "PostModel{" +
                "clas='" + clas + '\'' +
                ", nam='" + nam + '\'' +
                ", rollno='" + rollno + '\'' +
                '}';
    }

    public String getClas() {
        return clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }

    public String getNam() {
        return nam;
    }

    public void setNam(String nam) {
        this.nam = nam;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }
}
