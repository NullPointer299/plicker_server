package main;

import java.io.Serializable;

public class Image implements Serializable {

    private static final long serialVersionUID = 2400495331819982206L;
    private final boolean status;
    private final byte[] image;

    Image(final boolean status, final byte[] image) {
        this.status = status;
        this.image = image;
    }

    public boolean getStatus() {
        return status;
    }

    public byte[] getImage() {
        return image;
    }
}
