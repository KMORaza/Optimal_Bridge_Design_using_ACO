public class Ant {
    private BridgeDesign design;
    private double fitness;
    private double requiredDeadLoad, requiredLiveLoad, requiredDynamicLoad, windLoad, seismicFactor, maxCost;
    public Ant(BridgeDesign design, double requiredDeadLoad, double requiredLiveLoad, double requiredDynamicLoad,
               double windLoad, double seismicFactor, double maxCost) {
        this.design = design;
        this.requiredDeadLoad = requiredDeadLoad;
        this.requiredLiveLoad = requiredLiveLoad;
        this.requiredDynamicLoad = requiredDynamicLoad;
        this.windLoad = windLoad;
        this.seismicFactor = seismicFactor;
        this.maxCost = maxCost;
        evaluateFitness();
    }
    private void evaluateFitness() {
        if (design.meetsRequirements(requiredDeadLoad, requiredLiveLoad, requiredDynamicLoad, windLoad, seismicFactor) &&
            design.getCost() <= maxCost) {
            fitness = design.getTotalWeight() + design.getConstructionComplexity() * 100; 
        } else {
            fitness = Double.MAX_VALUE;
        }
    }
    public BridgeDesign getDesign() { 
        return design; 
    }
    public double getFitness() { 
        return fitness; 
    }
}