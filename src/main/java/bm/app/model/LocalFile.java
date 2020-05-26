package bm.app.model;

public class LocalFile {

    private String name;
    private String creationTime;
    private String lastModified;
    private Long size;
    private String downloadURI;
    private String deleteURI;
    private String fileType;

    public LocalFile(String name, String creationTime, String lastModified, Long size, String downloadURI, String deleteURI, String fileType) {
        this.name = name;
        this.creationTime = creationTime;
        this.lastModified = lastModified;
        this.size = size;
        this.downloadURI = downloadURI;
        this.deleteURI = deleteURI;
        this.fileType = fileType;
    }

    public LocalFile() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getDownloadURI() {
        return downloadURI;
    }

    public void setDownloadURI(String downloadURI) {
        this.downloadURI = downloadURI;
    }

    public String getDeleteURI() {
        return deleteURI;
    }

    public void setDeleteURI(String deleteURI) {
        this.deleteURI = deleteURI;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
