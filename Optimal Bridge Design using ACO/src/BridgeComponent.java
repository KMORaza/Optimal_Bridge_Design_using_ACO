public class BridgeComponent {
    private Material material;
    private double length; 
    private double crossSectionArea; 
    private boolean isCritical; 
    private double height; 
    public BridgeComponent(Material material, double length, double crossSectionArea, boolean isCritical, double height) {
        this.material = material;
        this.length = length;
        this.crossSectionArea = crossSectionArea;
        this.isCritical = isCritical;
        this.height = height;
    }
    public double getWeight() {
        return material.getDensity() * length * crossSectionArea / 1000; 
    }
    public double getStrength() {
        return material.getStrength() * crossSectionArea; 
    }
    public double getFatigueLimit() {
        return material.getFatigueResistance() * getStrength(); 
    }

    public Material getMaterial() { 
        return material; 
    }
    public double getLength() { 
        return length; 
    }
    public double getCrossSectionArea() { 
        return crossSectionArea; 
    }
    public boolean isCritical() { 
        return isCritical; 
    }
    public double getHeight() { 
        return height; 
    }
}