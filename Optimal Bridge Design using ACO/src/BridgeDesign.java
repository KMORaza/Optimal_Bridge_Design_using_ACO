import java.util.List;
public class BridgeDesign {
    private List<BridgeComponent> components;
    private double spanLength;
    private double totalWeight, maxLoadCapacity, maxDynamicLoad;
    private double constructionComplexity;
    private double cost;
    private static final double SAFETY_FACTOR = 1.5;
    private static final double REDUNDANCY_FACTOR = 1.2;
    public BridgeDesign(List<BridgeComponent> components, double spanLength) {
        this.components = components;
        this.spanLength = spanLength;
        calculateProperties();
    }
    private void calculateProperties() {
        totalWeight = components.stream().mapToDouble(BridgeComponent::getWeight).sum();
        maxLoadCapacity = components.stream()
                .filter(BridgeComponent::isCritical)
                .mapToDouble(BridgeComponent::getStrength)
                .min().orElse(0) * REDUNDANCY_FACTOR / SAFETY_FACTOR;
        maxDynamicLoad = components.stream()
                .filter(BridgeComponent::isCritical)
                .mapToDouble(BridgeComponent::getFatigueLimit)
                .min().orElse(0) / SAFETY_FACTOR;
        cost = components.stream()
                .mapToDouble(c -> c.getMaterial().getCost() * c.getWeight()) 
                .sum();
        constructionComplexity = components.size() / 50.0;
    }
    public boolean meetsRequirements(double deadLoad, 
                                     double liveLoad, 
                                     double dynamicLoad, 
                                     double windLoad, 
                                     double seismicFactor) {
        double totalStaticLoad = deadLoad + liveLoad;
        return maxLoadCapacity >= totalStaticLoad &&
               maxDynamicLoad >= dynamicLoad &&
               checkEnvironmentalStability(windLoad, seismicFactor);
    }
    private boolean checkEnvironmentalStability(double windLoad, double seismicFactor) {
        double envFactor = 1 - (windLoad / 1000 + seismicFactor / 10);
        return maxLoadCapacity * envFactor >= windLoad;
    }
    public double getTotalWeight() { 
        return totalWeight; 
    }
    public double getMaxLoadCapacity() { 
        return maxLoadCapacity; 
    }
    public double getCost() { 
        return cost; 
    }
    public double getConstructionComplexity() { 
        return constructionComplexity; 
    }
    public List<BridgeComponent> getComponents() { 
        return components; 
    }
    public double getSpanLength() { 
        return spanLength; 
    }
}