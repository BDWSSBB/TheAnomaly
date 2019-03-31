package AnomalyMod.helpers.improbabilityDriveBuffs;

import com.megacrit.cardcrawl.core.AbstractCreature;

public abstract class AbstractImprobabilityDriveBuffModule {

    public AbstractCreature target;
    public float amountPerSelect;
    public float costPerSelect;
    public float selectionWeight;
    public float originalSelectionWeight;
    public boolean isUnique;
    public boolean isRemainderModule;
    public float freeridingFactor;
    public float totalAmount;

    public AbstractImprobabilityDriveBuffModule(AbstractCreature target, float amountPerSelect, float costPerSelect, float selectionWeight, boolean isUnique, int startingAmount, float freeridingFactor) {
        this.target = target;
        this.amountPerSelect = amountPerSelect;
        this.costPerSelect = costPerSelect;
        this.selectionWeight = this.originalSelectionWeight = selectionWeight;
        this.isUnique = isUnique;
        this.freeridingFactor = freeridingFactor;
        this.totalAmount = startingAmount;
    }

    public AbstractImprobabilityDriveBuffModule(AbstractCreature target, float amountPerSelect, float costPerSelect, float selectionWeight, boolean isUnique, int startingAmount) {
        this(target, amountPerSelect, costPerSelect, selectionWeight, isUnique, startingAmount, 0.0F);
    }

    public AbstractImprobabilityDriveBuffModule(AbstractCreature target, float amountPerSelect, float costPerSelect, float selectionWeight, boolean isUnique) {
        this(target, amountPerSelect, costPerSelect, selectionWeight, isUnique, 0);
    }

    public abstract void doAction();
}
