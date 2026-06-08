package com.james.renderEngine.texturing;

public class TextureBlendPack {

    private static final String defaultTexturePath = "/misc/black-image.png";
    private static ImageTexture defaultTexture;

    private ImageTexture bdChannelTexture;
    private ImageTexture rChannelTexture;
    private ImageTexture gChannelTexture;
    private ImageTexture bChannelTexture;
    private ImageTexture blendMap;
    public float tiledTextureCoordsMultiplier = 1;

    public void setBdChannelTexture(String path) {
        this.bdChannelTexture = ImageTexture.getOrCreate(path);
    }

    public void setRChannelTexture(String path) {
        this.rChannelTexture = ImageTexture.getOrCreate(path);
    }

    public void setGChannelTexture(String path) {
        this.gChannelTexture = ImageTexture.getOrCreate(path);
    }

    public void setBChannelTexture(String path) {
        this.bChannelTexture = ImageTexture.getOrCreate(path);
    }

    public void setBlendMap(String path) {
        this.blendMap = ImageTexture.getOrCreate(path);
    }

    public void setTiledTextureCoordsMultiplier(float tiledTextureCoordsMultiplier) {
        this.tiledTextureCoordsMultiplier = tiledTextureCoordsMultiplier;
    }

    public ImageTexture getBdChannelTexture() {
        if (bdChannelTexture == null) {
            bdChannelTexture = getOrCreateDefaultTexture();
        }

        return bdChannelTexture;
    }

    public ImageTexture getRChannelTexture() {
        if (rChannelTexture == null) {
            rChannelTexture = getOrCreateDefaultTexture();
        }

        return rChannelTexture;
    }

    public ImageTexture getGChannelTexture() {
        if (gChannelTexture == null) {
            gChannelTexture = getOrCreateDefaultTexture();
        }

        return gChannelTexture;
    }

    public ImageTexture getBChannelTexture() {
        if (bChannelTexture == null) {
            bChannelTexture = getOrCreateDefaultTexture();
        }

        return bChannelTexture;
    }

    public ImageTexture getBlendMap() {
        if (blendMap == null) {
            blendMap = getOrCreateDefaultTexture();
        }

        return blendMap;
    }

    private static ImageTexture getOrCreateDefaultTexture() {
        if (defaultTexture == null) {
            defaultTexture = ImageTexture.getOrCreate(defaultTexturePath);
        }

        return defaultTexture;
    }

}
