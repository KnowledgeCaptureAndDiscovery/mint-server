package org.mint.server.planner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.mint.server.classes.DataEnsemble;
import org.mint.server.classes.DataSpecification;
import org.mint.server.classes.Dataset;
import org.mint.server.classes.graph.GVariable;
import org.mint.server.classes.graph.VariableGraph;
import org.mint.server.classes.graph.VariableProvider;
import org.mint.server.classes.model.Model;
import org.mint.server.classes.model.ModelIO;
import org.mint.server.classes.model.ModelVariable;
import org.mint.server.repository.impl.MintVocabularyJSON;


public class MintPlanner {

  public MintPlanner() { 
    
  }
  
  public ArrayList<WorkflowSolution> createWorkflowSolutions(
      ArrayList<String> drivingVariables,
      ArrayList<String> responseVariables,
      VariableGraph graph,
      DataSpecification ds, 
      ArrayList<Model> models) {
    ArrayList<WorkflowSolution> mgraphs = new ArrayList<WorkflowSolution>();
    String userid = ds.getID().replaceAll(".*/users/", "").replaceAll("/.*", "");
    MintVocabularyJSON vocabulary = MintVocabularyJSON.get();
    
    // TODO: Use File Types here too

    // Initialze initial list of variables
    ArrayList<GVariable> variables = new ArrayList<GVariable>();
    for(GVariable gvar : graph.getVariables()) {
      if(responseVariables != null && responseVariables.contains(gvar.getID()))
        variables.add(gvar);
      else if(drivingVariables != null && drivingVariables.contains(gvar.getID()))
        variables.add(gvar);
    }

    // Get datasets that provide variables
    HashMap<String, ArrayList<String>> data_providers = 
        new HashMap<String, ArrayList<String>>();
    for(DataEnsemble ensemble : ds.getEnsemble()) {
      for(String varname : ensemble.getVariables()) {
        String cname = vocabulary.getCanonicalName(varname);
        ArrayList<String> datasets = data_providers.get(cname);
        if(datasets == null)
          datasets = new ArrayList<String>();
        for(Dataset dataset: ensemble.getDatasets()) {
          datasets.add(dataset.getName());
        }
        data_providers.put(cname, datasets);
      }
    }

    HashMap<String, ArrayList<Model>> comp_providers = 
        new HashMap<String, ArrayList<Model>>();
    
    // Get models that provide variables
    for(Model c : models) {
      for(ModelIO op : c.getOutputs()) {
        if(op.getVariables() == null) {
          op.setVariables(this.createDummyVariables(op));
        }
        for(ModelVariable mv : op.getVariables()) {
          String cname = vocabulary.getCanonicalName(mv.getStandard_name());
          ArrayList<Model> providers = comp_providers.get(cname);
          if(providers == null)
            providers = new ArrayList<Model>();
          providers.add(c);
          comp_providers.put(cname, providers);
        }
      }
      for(ModelIO ip : c.getInputs()) {
        if(ip.getVariables() == null) {
          ip.setVariables(this.createDummyVariables(ip));
        }
      }
    }
    // System.out.println(comp_providers);
    // System.out.println(data_providers);

    // Create solutions
    ArrayList<Solution> temp_list = new ArrayList<Solution>();
    TreeSet<Solution> solution_queue = new TreeSet<Solution>();
    solution_queue.add(new Solution(variables));

    while(solution_queue.size() > 0) {
      Solution solution = solution_queue.first();
      solution_queue.remove(solution);
      // System.out.println(solution);
      
      if(temp_list.contains(solution)) {
        continue;
      }
      temp_list.add(solution);
      
      // System.out.println("*** Expanding Solution ---");
      
      ArrayList<GVariable> varqueue = new ArrayList<GVariable>(solution.getVariables());

      while(varqueue.size() > 0) {
        GVariable v = varqueue.remove(0);
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
          if(dataprovs.size() > 0) {
            // TODO: Mark multiple solutions for multiple data providers ?
            for(String dataprov : dataprovs) {
              v.setProvider(new VariableProvider(dataprov, VariableProvider.Type.DATA, null));
            }
            // System.out.println(cname + " has a data provider. Variable resolved. Continue.");
            continue;
          }
        }

        // Check model provider
        if(comp_providers.containsKey(cname)) {
          ArrayList<Model> comps = comp_providers.get(cname);
          // System.out.println(cname + " has "+ comps.size()+" model providers");
          // System.out.println(comps);

          // If more than one model, create extra solutions
          // from 2 onwards
          for(int i=1; i<comps.size(); i++) {
            Model m = comps.get(i);
            Solution newsolution = new Solution(new ArrayList<GVariable>());
            newsolution.copy(solution);
            newsolution.addModel(m, graph.getID());
            solution_queue.add(newsolution);
            
            // Mark variable as resolved
            String varhash = v.getCanonical_name();
            GVariable newv = newsolution.getVarhash().get(varhash);
            newv.setResolved(true);
            VariableProvider provider = new VariableProvider(m.getID(), VariableProvider.Type.MODEL, 
                m.getType().getCategory());
            newv.setProvider(provider);
          }

          // Use the first model to modify current solution
          Model comp = comps.get(0);
          ArrayList<GVariable> newvars = solution.addModel(comp, graph.getID());
          for(GVariable newvar : newvars) {
            varqueue.add(newvar);
          }
          // Mark variable as resolved
          v.setResolved(true);
          VariableProvider provider = new VariableProvider(comp.getID(), VariableProvider.Type.MODEL, 
              (comp.getType() != null ? comp.getType().getCategory() : null));
          v.setProvider(provider);
        }
      }
      // System.out.println(solution);
      
      // Create workflow for solution
      WorkflowSolution workflow = solution.createWorkflow(graph, this);
      if(workflow != null) {
        workflow.setModelGraph(solution.createModelGraph(workflow, userid));
        workflow.setWingsWorkflow(solution.createWingsWorkflow(workflow, userid, this));
        workflow.setGraph(solution.createGraph(workflow));
        solution.diffGraph(workflow.getGraph(), graph);      
        mgraphs.add(workflow);
      }
    }
    return mgraphs;
  }
  
  private ArrayList<ModelVariable> createDummyVariables(ModelIO io) {
    ArrayList<ModelVariable> list = new ArrayList<ModelVariable>();
    ModelVariable mv = new ModelVariable();
    mv.setID(io.getID());
    mv.setStandard_name(io.getLabel());
    list.add(mv);
    return list;
  }
}
