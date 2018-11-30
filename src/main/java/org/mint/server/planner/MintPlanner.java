package org.mint.server.planner;

import java.util.ArrayList;
import java.util.HashMap;

import org.mint.server.classes.DataSpecification;
import org.mint.server.classes.graph.Variable;
import org.mint.server.classes.graph.VariableGraph;
import org.mint.server.classes.graph.VariableProvider;
import org.mint.server.classes.model.Model;
import org.mint.server.classes.model.ModelIO;
import org.mint.server.classes.model.ModelVariable;


public class MintPlanner {

  public MintPlanner() { 
    
  }
  
  public ArrayList<Workflow> composeModelGraphs(VariableGraph graph, DataSpecification ds, 
      ArrayList<Model> models) {
    ArrayList<Workflow> mgraphs = new ArrayList<Workflow>();
    
    // Copy variables array
    ArrayList<Variable> variables = new ArrayList<Variable>(graph.getVariables());
    // System.out.println(ds.getVariableDatasetMap());

    // Get datasets that provide variables
    HashMap<String, ArrayList<String>> data_providers = 
        new HashMap<String, ArrayList<String>>(ds.getVariableDatasetMap());

    HashMap<String, ArrayList<Model>> comp_providers = 
        new HashMap<String, ArrayList<Model>>();
    
    // Get models that provide variables
    for(Model c : models) {
      for(ModelIO op : c.getOutputs()) {
        if(op.getVariables() == null) {
          op.setVariables(new ArrayList<ModelVariable>());
        }
        for(ModelVariable mv : op.getVariables()) {
          if(mv.getCanonical_name() == null) 
            mv.setCanonical_name(mv.getStandard_name());

          String sname = mv.getCanonical_name();
          ArrayList<Model> providers = comp_providers.get(sname);
          if(providers == null)
            providers = new ArrayList<Model>();
          providers.add(c);
          comp_providers.put(sname, providers);
        }
      }
      for(ModelIO ip : c.getInputs()) {
        if(ip.getVariables() == null) {
          ip.setVariables(new ArrayList<ModelVariable>());
        }
        for(ModelVariable mv : ip.getVariables()) {
          if(mv.getCanonical_name() == null)
            mv.setCanonical_name(mv.getStandard_name());
        }
      }
    }

    // Create solutions
    ArrayList<Solution> solution_queue = new ArrayList<Solution>();
    solution_queue.add(new Solution(variables));

    while(solution_queue.size() > 0) {
      Solution solution = solution_queue.remove(0);
      // System.out.println("Expanding Solution---");
      
      ArrayList<Variable> varqueue = new ArrayList<Variable>(solution.getVariables());

      while(varqueue.size() > 0) {
        Variable v = varqueue.remove(0);
        // System.out.println("Checking variable " + v.getLabel());
        
        String cname = v.getCanonical_name();
        // ArrayList<String> stdnames = v.getStandard_names();
        
        // Skip if variable already resolved
        if(v.isResolved()) {
          // System.out.println(v.getLabel() + " already resolved. Continue.");
          continue;
        }

        // Check data provider
        if(data_providers.containsKey(cname)) {
          // Mark variable as resolved
          v.setResolved(true);
          ArrayList<String> dataprovs = data_providers.get(cname);
          for(String dataprov : dataprovs) {
            v.setProvider(new VariableProvider(dataprov, VariableProvider.Type.DATA, null));
          }
          // System.out.println(v.getLabel() + " has a data provider. Variable resolved. Continue.");
          continue;
        }
        // System.out.println(comp_providers);

        // Check model provider
        if(comp_providers.containsKey(cname)) {
          ArrayList<Model> comps = comp_providers.get(cname);
          // System.out.println(v.getLabel() + " has "+ comps.size()+" model providers");

          // If more than one model, create extra solutions
          // from 2 onwards
          for(int i=1; i<comps.size(); i++) {
            Model m = comps.get(i);
            Solution newsolution = new Solution(new ArrayList<Variable>());
            newsolution.copy(solution);
            newsolution.addModel(m);
            solution_queue.add(newsolution);
            
            // Mark variable as resolved
            String varhash = v.getCanonical_name();
            Variable newv = newsolution.getVarhash().get(varhash);
            newv.setResolved(true);
            VariableProvider provider = new VariableProvider(m.getID(), VariableProvider.Type.MODEL, m.getCategory());
            newv.setProvider(provider);
          }

          // Use the first model to modify current solution
          Model comp = comps.get(0);
          ArrayList<Variable> newvars = solution.addModel(comp);
          for(Variable newvar : newvars) {
            varqueue.add(newvar);
          }
          // Mark variable as resolved
          v.setResolved(true);
          VariableProvider provider = new VariableProvider(comp.getID(), VariableProvider.Type.MODEL, comp.getCategory());
          v.setProvider(provider);
        }
      }
      // System.out.println(solution);
      
      // Create workflow for solution
      Workflow workflow = solution.createWorkflow(graph, this);
      if(workflow != null)
        mgraphs.add(workflow);
    }
    return mgraphs;
  }
}
