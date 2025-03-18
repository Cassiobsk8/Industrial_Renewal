package net.cassiokf.industrialrenewal.util.enums;

public enum EnumCouplingType {
    COUPLING_1("indRC1M", "indRC1L"), COUPLING_2("indRC2M", "indRC2L");
    public static final EnumCouplingType[] VALUES = values();
    public final String tagMostSigBits;
    public final String tagLeastSigBits;
    
    EnumCouplingType(String tagMostSigBits, String tagLeastSigBits) {
        this.tagMostSigBits = tagMostSigBits;
        this.tagLeastSigBits = tagLeastSigBits;
    }
}
