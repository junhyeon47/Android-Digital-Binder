package team.code.effect.digitalbinder.photobook;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.zip.ZipFile;

public class SerializableZipFile implements Serializable{
    private static final long serialVersionUID = -993454385883667876L;
    private ZipFile zipFile;

    public SerializableZipFile(String filename) throws IOException {
        this.zipFile = new ZipFile(filename);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(zipFile.getName());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String filename = (String) in.readObject();
        zipFile = new ZipFile(filename);
    }

    public ZipFile getZipFile() {
        return zipFile;
    }

    public void setZipFile(ZipFile zipFile) {
        this.zipFile = zipFile;
    }
}
