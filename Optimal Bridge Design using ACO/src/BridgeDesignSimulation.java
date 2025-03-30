public class BridgeDesignSimulation {
    public static void main(String[] args) {
        double requiredDeadLoad = 200;    
        double requiredLiveLoad = 800;   
        double requiredDynamicLoad = 300; 
        double windLoad = 100;          
        double seismicFactor = 0.5;     
        double maxCost = 5000;          
        int numComponents = 10;
        double minSpanLength = 20;      
        double maxSpanLength = 60;       
        BridgeOptimizer optimizer = new BridgeOptimizer(requiredDeadLoad, requiredLiveLoad, requiredDynamicLoad,
                windLoad, seismicFactor, maxCost, numComponents, minSpanLength, maxSpanLength);
        BridgeDesign optimalDesign = optimizer.optimize();
        System.out.printf("Optimal Bridge Design:%nWeight: %.2f kg%nLoad Capacity: %.2f kN%nCost: $%.2f%nSpan: %.2f m%nComplexity: %.2f%n",
                optimalDesign.getTotalWeight(), optimalDesign.getMaxLoadCapacity(), optimalDesign.getCost(),
                optimalDesign.getSpanLength(), optimalDesign.getConstructionComplexity());
        for (int i = 0; i < optimalDesign.getComponents().size(); i++) {
            BridgeComponent comp = optimalDesign.getComponents().get(i);
            System.out.printf("Component %d: Material=%s, Length=%.2f m, Area=%.2f sq cm, Height=%.2f m, Critical=%b%n",
                    i, comp.getMaterial(), comp.getLength(), comp.getCrossSectionArea(), comp.getHeight(), comp.isCritical());
        }
        new BridgeVisualizer(optimalDesign);
    }
}