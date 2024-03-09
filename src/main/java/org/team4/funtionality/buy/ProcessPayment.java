package org.team4.funtionality.buy;

public class ProcessPayment {
    private Modes modes;
    private ItemToPurchase itemToPurchase;

    public ProcessPayment(Modes modes, ItemToPurchase itemToPurchase) {
        this.modes = modes;
        this.itemToPurchase = itemToPurchase;
    }

    public PurchaseComplete getPurchaseCompleted(){
        if(modes.isValid()){
            return new PurchaseComplete(itemToPurchase, modes);
        }else{
            return null;
        }
    }

    @Override
    public String toString() {
        return "ProcessPayment{" +
                "modes=" + modes +
                ", itemToPurchase=" + itemToPurchase +
                '}';
    }
}