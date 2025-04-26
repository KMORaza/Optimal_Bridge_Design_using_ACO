## Optimizing structural design of bridge using ant colony optimization 

### Overview
  - Load Requirements:  
    - Dead Load = 200 kN  
    - Live Load = 800 kN  
    - Dynamic Load = 300 kN  
  - Environmental Factors:
    - Wind Load = 100 kN  
    - Seismic Factor = 0.5  
  - Cost Constraint: Max Budget = $5000  
  - Span Length: Between 20 m and 60 m  
  - Components: 10 structural elements  

### Objective
 
 Minimize:  
    1. Total weight of the bridge  
    2. Construction complexity  

Subject to:  
    1. Load capacity ≥ Total static load (Dead + Live)  
    2. Dynamic load capacity ≥ Required dynamic load  
    3. Cost ≤ $5000  


### Solution Steps

  - Define Material Choices  

| Material   | Density (g/cm³) | Strength (MPa) | Cost ($/kg) | Fatigue Resistance | Corrosion Resistance |
|------------|----------------|----------------|-------------|-------------------|----------------------|
| Steel      | 7.85           | 500            | 50          | 0.8               | 0.6                  |
| Aluminum   | 2.7            | 300            | 80          | 0.7               | 0.8                  |
| Concrete   | 2.4            | 40             | 20          | 0.9               | 0.5                  |

   - Determine Cross-Sectional Areas

  Possible cross-sectional areas:  A ∈ {10, 20, 30, 40, 50} cm²

   - Choose Component Heights

Height range:  h ∈ [1, 5] m

  - Select Span Length

Discrete options: L ∈ {20, 30, 40, 50, 60} m

  - Assign Critical vs. Non-Critical Components
    - Critical components (e.g., main beams) must support the primary loads.  
    - Non-critical components (e.g., bracings) contribute to stability.  

  - Structural Analysis   
    - Weight Calculation: W = Density × Length × A × 10^(-3) kg 
    - Strength Check: Max Load Capacity = min(Strength×A) × (Redundancy Factor/Safety Factor), where Redundancy Factor = 1.2, Safety Factor = 1.5
    - Dynamic Load Check: Fatigue Limit = Fatigue Resistance × Strength  
    - Environmental Stability: Env. Factor = 1 − (Wind Load/1000 + Seismic Factor/10); Adjusted Load Capacity = Max Load Capacity × Env. Factor

  - Cost Calculation
    - Total Cost = ∑(Material Cost × Weight)

  - Fitness Evaluation
    - If all constraints are satisfied: Fitness = Total Weight + (Construction Complexity × 100) 
    - Else: Fitness = ∞ (invalid solution)

---

### **Sample Calculation**  
 
- Span Length (L): 40 m  
- Components: 
  - 5 critical (steel, A = 30 cm², h = 3 m)  
  - 5 non-critical (aluminum, A = 20 cm², h = 2 m)  
- Component Length: Length per component = 40/5 = 8 m
- Weight:
   - Steel: 7.85 × 8 × 30 × 10^(-3) = 1.884 kg  
   - Aluminum: 2.7 × 8 × 20 × 10^(-3) = 0.432 kg 
   - Total Weight = 5 × 1.884 + 5 × 0.432 = 11.58 kg 
- Strength Check:
   - Steel: 500 × 30 = 15000 kN  
   - Max Load Capacity = (15000 × 1.2)/1.5 = 12000 kN  
   - Required Static Load = 200 + 800 = 1000 kN ⟶ **OK**  
- Dynamic Load Check:
   - Fatigue Limit = 0.8 × 15000 = 12000 kN  
   - Required Dynamic Load = 300 kN ⟶ **OK**  
- Environmental Stability:  
   - Env. Factor = 1 - (100/1000 + 0.5/10) = 0.85   
   - Adjusted Capacity = 12000 × 0.85 = 10200 kN > Wind Load (100 kN) ⟶ **OK**  
- Cost:  
   - Steel: 50 × 1.884 = 94.2$  
   - Aluminum: 80 × 0.432 = 34.56$  
   - Total Cost = 5 × 94.2 + 5 × 34.56 = 643.8$  < $5000 ⟶ **OK**  
- Fitness:
   - Construction Complexity 10/50 = 0.2   
   - Fitness = 11.58 + (0.2 × 100) = 31.58 

---

### **Optimal Solution**  
- After evaluating multiple designs, the best solution is selected based on:  
  - Minimum Fitness Value (lowest weight + complexity).  
  - Constraint Satisfaction (loads, cost).  
- Solution  
  - Span Length = 40 m  
  - Materials = Steel (critical), Aluminum (non-critical)  
  - Cross-Sectional Areas = 30 cm² (critical), 20 cm² (non-critical)  
  - Total Weight = 11.58 kg  
  - Cost = $643.80  
  - Fitness = 31.58  

---
