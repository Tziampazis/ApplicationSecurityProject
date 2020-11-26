/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

/**
 *
 * @author canku
 */
public class File {

    private String user;
    private String permission;
    private String status;
    private String uploadedFile;
    private int id;
    private String name;
    private Date uploadDate;
    private String base64File;

    public File(int id, String user, String permission, String status, String filePath, Date uploadDate, String base64File) {
        this.id = id;
        this.user = user;
        this.permission = permission;
        this.status = status;
        this.uploadedFile = filePath;
        this.uploadDate = uploadDate;
        this.base64File = base64File;
    }

    public File(int id, String user, String permission, String status, String filePath, Date uploadDate) {
        this.id = id;
        this.user = user;
        this.permission = permission;
        this.status = status;
        this.uploadedFile = filePath;
        this.uploadDate = uploadDate;
    }

    public File(String encodedtxt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        String result = null;
        if (this.uploadedFile != null) {
            Path path = Paths.get(this.uploadedFile);
            Path fileName = path.getFileName();
            result = fileName.toString();
        }
        return result;
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

    public String getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(String uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getBase64File() {
        return base64File;
    }

    public void setBase64File(String base64File) {
        this.base64File = base64File;
    }
}
