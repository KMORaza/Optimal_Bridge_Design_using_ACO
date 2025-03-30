import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class BridgeOptimizer {
    private int numAnts = 50;
    private int numIterations = 200; 
    private double pheromoneEvaporation = 0.5;
    private double pheromoneDeposit = 1.0;
    private double requiredDeadLoad, requiredLiveLoad, requiredDynamicLoad, windLoad, seismicFactor, maxCost;
    private double minSpanLength, maxSpanLength;
    private List<Material> materialOptions = List.of(Material.values());
    private Random rand = new Random();
    private double[][] materialPheromones;
    private double[][] sectionPheromones;
    private double[] heightPheromones;
    private double[] spanPheromones;
    public BridgeOptimizer(double requiredDeadLoad, double requiredLiveLoad, double requiredDynamicLoad,
                           double windLoad, double seismicFactor, double maxCost, int numComponents,
                           double minSpanLength, double maxSpanLength) {
        this.requiredDeadLoad = requiredDeadLoad;
        this.requiredLiveLoad = requiredLiveLoad;
        this.requiredDynamicLoad = requiredDynamicLoad;
        this.windLoad = windLoad;
        this.seismicFactor = seismicFactor;
        this.maxCost = maxCost;
        this.minSpanLength = minSpanLength;
        this.maxSpanLength = maxSpanLength;
        materialPheromones = new double[numComponents][materialOptions.size()];
        sectionPheromones = new double[numComponents][5];
        heightPheromones = new double[numComponents];
        spanPheromones = new double[5];
        initializePheromones();
    }
    private void initializePheromones() {
        for (int i = 0; i < materialPheromones.length; i++) {
            for (int j = 0; j < materialPheromones[i].length; j++) materialPheromones[i][j] = 1.0;
            for (int j = 0; j < sectionPheromones[i].length; j++) sectionPheromones[i][j] = 1.0;
            heightPheromones[i] = 1.0;
        }
        for (int i = 0; i < spanPheromones.length; i++) spanPheromones[i] = 1.0;
    }
    private BridgeDesign constructDesign() {
        double span = chooseSpanLength();
        int numComponents = materialPheromones.length;
        List<BridgeComponent> components = new ArrayList<>();
        double componentLength = span / (numComponents / 2);
        for (int i = 0; i < numComponents; i++) {
            Material material = chooseMaterial(i);
            double area = chooseCrossSectionArea(i);
            double height = chooseHeight(i);
            components.add(new BridgeComponent(material, componentLength, area, i % 2 == 0, height));
        }
        return new BridgeDesign(components, span);
    }
    private Material chooseMaterial(int componentIdx) {
        double total = 0;
        for (double p : materialPheromones[componentIdx]) total += p;
        double r = rand.nextDouble() * total;
        double sum = 0;
        for (int j = 0; j < materialOptions.size(); j++) {
            sum += materialPheromones[componentIdx][j];
            if (r <= sum) return materialOptions.get(j);
        }
        return materialOptions.get(materialOptions.size() - 1);
    }
    private double chooseCrossSectionArea(int componentIdx) {
        double total = 0;
        for (double p : sectionPheromones[componentIdx]) total += p;
        double r = rand.nextDouble() * total * 1.5; // Enhanced exploration
        double sum = 0;
        double[] areas = {10, 20, 30, 40, 50};
        for (int j = 0; j < areas.length; j++) {
            sum += sectionPheromones[componentIdx][j];
            if (r <= sum) return areas[j];
        }
        return areas[areas.length - 1];
    }
    private double chooseHeight(int componentIdx) {
        return 1 + rand.nextDouble() * 4 * (heightPheromones[componentIdx] / (heightPheromones[componentIdx] + 1));
    }
    private double chooseSpanLength() {
        double total = 0;
        for (double p : spanPheromones) total += p;
        double r = rand.nextDouble() * total;
        double sum = 0;
        double[] spans = {minSpanLength, minSpanLength + 10, minSpanLength + 20, minSpanLength + 30, maxSpanLength};
        for (int j = 0; j < spans.length; j++) {
            sum += spanPheromones[j];
            if (r <= sum) return spans[j];
        }
        return maxSpanLength;
    }
    private void updatePheromones(List<Ant> ants) {
        for (int i = 0; i < materialPheromones.length; i++) {
            for (int j = 0; j < materialPheromones[i].length; j++) materialPheromones[i][j] *= (1 - pheromoneEvaporation);
            for (int j = 0; j < sectionPheromones[i].length; j++) sectionPheromones[i][j] *= (1 - pheromoneEvaporation);
            heightPheromones[i] *= (1 - pheromoneEvaporation);
        }
        for (int i = 0; i < spanPheromones.length; i++) spanPheromones[i] *= (1 - pheromoneEvaporation);
        for (Ant ant : ants) {
            if (ant.getFitness() == Double.MAX_VALUE) continue;
            double deposit = pheromoneDeposit / ant.getFitness();
            for (int i = 0; i < ant.getDesign().getComponents().size(); i++) {
                BridgeComponent comp = ant.getDesign().getComponents().get(i);
                int matIdx = materialOptions.indexOf(comp.getMaterial());
                materialPheromones[i][matIdx] += deposit;
                int areaIdx = (int) ((comp.getCrossSectionArea() - 10) / 10);
                sectionPheromones[i][areaIdx] += deposit;
                heightPheromones[i] += deposit * (comp.getHeight() - 1) / 4;
            }
            int spanIdx = (int) ((ant.getDesign().getSpanLength() - minSpanLength) / 10);
            spanPheromones[spanIdx] += deposit;
        }
    }
    public BridgeDesign optimize() {
        BridgeDesign bestDesign = null;
        double bestFitness = Double.MAX_VALUE;
        for (int iter = 0; iter < numIterations; iter++) {
            List<Ant> ants = new ArrayList<>();
            for (int i = 0; i < numAnts; i++) {
                BridgeDesign design = constructDesign();
                ants.add(new Ant(design, requiredDeadLoad, requiredLiveLoad, requiredDynamicLoad, windLoad, seismicFactor, maxCost));
            }
            for (Ant ant : ants) {
                if (ant.getFitness() < bestFitness) {
                    bestFitness = ant.getFitness();
                    bestDesign = ant.getDesign();
                }
            }
            updatePheromones(ants);
        }
        return bestDesign;
    }
}