package net.cassiokf.industrialrenewal.obj;

public class CapResult {
    
    private int outPut = 0;
    private int validReceivers = 0;
    public CapResult(int outPut, int validReceivers){
        setOutPut(outPut);
        setValidReceivers(validReceivers);
    }
    
    public int getOutPut() {
        return outPut;
    }
    
    public void setOutPut(int outPut) {
        this.outPut = outPut;
    }
    
    public int getValidReceivers() {
        return validReceivers;
    }
    
    public void setValidReceivers(int validReceivers) {
        this.validReceivers = validReceivers;
    }
}
