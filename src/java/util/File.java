/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Blob;

/**
 *
 * @author canku
 */
public class File {

    private String user;
    private String permission;
    private String status;
    private Blob uploadedFile;

    public File(String user, String permission, String status, Blob fileVal) {
        this.user = user;
        this.permission = permission;
        this.status = status;
        this.uploadedFile = fileVal;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Blob getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(Blob uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

}
