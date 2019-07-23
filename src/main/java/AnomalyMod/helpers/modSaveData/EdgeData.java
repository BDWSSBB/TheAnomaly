package AnomalyMod.helpers.modSaveData;

public class EdgeData {

    public int srcX;
    public int srcY;
    public int dstX;
    public int dstY;
    public int durability;

    public EdgeData(int srcX, int srcY, int dstX, int dstY, int durability) {
        this.srcX = srcX;
        this.srcY = srcY;
        this.dstX = dstX;
        this.dstY = dstY;
        this.durability = durability;
    }
}
