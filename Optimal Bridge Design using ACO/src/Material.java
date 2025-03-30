public enum Material {
    STEEL(7.85, 500, 50, 0.8, 0.6),  
    ALUMINUM(2.7, 300, 80, 0.7, 0.8),
    CONCRETE(2.4, 40, 20, 0.9, 0.5);
    private final double density, strength, cost, fatigueResistance, corrosionResistance;
    Material(double density, double strength, double cost, double fatigueResistance, double corrosionResistance) {
        this.density = density;
        this.strength = strength;
        this.cost = cost;
        this.fatigueResistance = fatigueResistance; 
        this.corrosionResistance = corrosionResistance; 
    }
    public double getDensity() { 
        return density; 
    }
    public double getStrength() { 
        return strength; 
    }
    public double getCost() { 
        return cost; 
    }
    public double getFatigueResistance() { 
        return fatigueResistance; 
    }
    public double getCorrosionResistance() { 
        return corrosionResistance; 
    }
}