package com.vimalsagarji.vimalsagarjiapp;

/**
 * Created by Grapes-Pradip on 12/27/2016.
 */

@SuppressWarnings("ALL")
public class ImageItemSplash {
    private String image;
    private String imageUrl;

    public ImageItemSplash(String image, String imageUrl) {
        this.image = image;
        this.imageUrl = imageUrl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
