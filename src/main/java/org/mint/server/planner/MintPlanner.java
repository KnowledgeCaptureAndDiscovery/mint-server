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
import org.mint.server.classes.question.ModelingQuestion;
import org.mint.server.repository.impl.MintVocabularyJSON;


public class MintPlanner {

  public MintPlanner() { 
    
  }
    
  
  public ArrayList<WorkflowSolution> createWorkflowSolutions(
      ModelingQuestion question,
      VariableGraph graph,
      DataSpecification ds, 
      ArrayList<Model> origmodels) {
    ArrayList<WorkflowSolution> mgraphs = new ArrayList<WorkflowSolution>();
    String userid = ds.getID().replaceAll(".*/users/", "").replaceAll("/.*", "");
    MintVocabularyJSON vocabulary = MintVocabularyJSON.get();
    
    // Clone models 
    // - For model io with no variables, create dummy variable from type
    // - For model variable with no standard name, don't add
    ArrayList<Model> models = new ArrayList<Model>();
    for(Model om : origmodels) {
      Model m = new Model();
      m.copyFrom(om);
      if(m.getInputs() != null) {
        for(ModelIO io : m.getInputs()) {
          ArrayList<ModelVariable> newvars = new ArrayList<ModelVariable>();
          for(ModelVariable ov: io.getVariables()) {
            if(ov.getStandard_name() != null) {
              newvars.add(ov);
            }
          }
          io.setVariables(newvars);
        }
      }
      if(m.getOutputs() != null) {
        for(ModelIO io : m.getOutputs()) {
          ArrayList<ModelVariable> newvars = new ArrayList<ModelVariable>();
          for(ModelVariable ov: io.getVariables()) {
            if(ov.getStandard_name() != null) {
              newvars.add(ov);
            }
          }
          io.setVariables(newvars);
        }
      }
      models.add(m);
    }
 

    // Initialze initial list of variables
    ArrayList<SolutionVariable> variables = new ArrayList<SolutionVariable>();
    for(GVariable gvar : graph.getVariables()) {
      if(question.getResponseVariables() != null && 
          question.getResponseVariables().contains(gvar.getID()))
        variables.add(new SolutionVariable(gvar));
      else if(question.getDrivingVariables() != null && 
          question.getDrivingVariables().contains(gvar.getID()))
        variables.add(new SolutionVariable(gvar));
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

    HashMap<String, ArrayList<Model>> comp_type_providers = 
        new HashMap<String, ArrayList<Model>>();
    
    // Get models that provide variables & types
    for(Model c : models) {
      /*if(c.getLocalName().equals("cycles_multiple_points")) // HACK
        continue;*/
      for(ModelIO op : c.getOutputs()) {
        if(op.getVariables() == null || op.getVariables().size() == 0) {
          op.setVariables(this.createDummyVariables(op));
        }
        for(ModelVariable mv : op.getVariables()) {
          String cname = vocabulary.getCanonicalName(mv.getStandard_name());
          ArrayList<Model> providers = comp_providers.get(cname);
          if(providers == null)
            providers = new ArrayList<Model>();
          if(!providers.contains(c))
            providers.add(c);
          comp_providers.put(cname, providers);
        }
        if(op.getType() != null) {
          ArrayList<Model> type_providers = comp_type_providers.get(op.getType());
          if(type_providers == null)
            type_providers = new ArrayList<Model>();
          if(!type_providers.contains(c))
            type_providers.add(c);
          comp_type_providers.put(op.getType(), type_providers);
        }
      }
      for(ModelIO ip : c.getInputs()) {
        if(ip.getVariables() == null || ip.getVariables().size() == 0) {
          ip.setVariables(this.createDummyVariables(ip));
        }
      }
    }
    /*
    System.out.println(comp_providers);
    System.out.println(comp_type_providers);
    System.out.println(data_providers);
    */

    int numsolutions = 0;
    int maxsolutions = 10;
    
    // Create solutions
    ArrayList<Solution> temp_list = new ArrayList<Solution>();
    TreeSet<Solution> solution_queue = new TreeSet<Solution>();
    solution_queue.add(new Solution(variables));

    while(solution_queue.size() > 0) {
      Solution solution = solution_queue.first();
      solution_queue.remove(solution);
      //System.out.println(solution);
      
      if(temp_list.contains(solution)) {
        continue;
      }
      temp_list.add(solution);
      
      // System.out.println("*** Expanding Solution ---");
      
      ArrayList<SolutionVariable> varqueue = new ArrayList<SolutionVariable>(solution.getVariables());

      while(varqueue.size() > 0) {
        SolutionVariable v = varqueue.remove(0);
        //System.out.println("Checking variable " + cname);
        
        // Skip if variable already resolved
        if(v.isResolved()) {
          //System.out.println(v.getVariable().getLabel() + " already resolved. Continue.");
          continue;
        }
        
        ArrayList<String> stdnames = v.getVariable().getStandard_names();
        ArrayList<String> cnames = new ArrayList<String>();
        if(stdnames != null) {
          for(String stdname : stdnames) {
            String cname = vocabulary.getCanonicalName(stdname);
            cnames.add(cname);
          }
        }
        if(cnames.size() == 0)
          cnames.add(v.getVariable().getLocalName());

        for(String cname : cnames) {
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
              //System.out.println(cname + " has a data provider. Variable resolved. Continue.");
              break;
            }
          }
        }
        if(v.isResolved())
          continue;

        // Check model provider
        ArrayList<Model> comps = new ArrayList<Model>();
        
        // If type exists, then check for type matches
        String vtype = v.getType();
        if(vtype != null) {
          if(comp_type_providers.containsKey(vtype)) {
            comps = comp_type_providers.get(vtype);
          }
        }
        else {
          for(String cname : cnames) {
            if(comp_providers.containsKey(cname)) {
              comps = comp_providers.get(cname);
              break;
            }
          }
        }
        if(comps.size() > 0) {
          //System.out.println(cname + " has "+ comps.size()+" model providers");
          //System.out.println(comps);

          // If more than one model, create extra solutions
          // from 2 onwards
          for(int i=1; i<comps.size(); i++) {
            Model m = comps.get(i);
            Solution newsolution = new Solution(new ArrayList<SolutionVariable>());
            newsolution.copy(solution);
            newsolution.addModel(m, graph.getID());
            solution_queue.add(newsolution);
            
            // Mark variable as resolved
            String varname = vocabulary.getCanonicalName(v.getVariable().getStandard_names());
            SolutionVariable newv = newsolution.findVariable(vtype, varname);
            newv.setResolved(true);
            
            String category = m.getType() != null ? m.getType().getCategory() : null;
            VariableProvider provider = new VariableProvider(m.getID(), VariableProvider.Type.MODEL, 
                category);
            newv.setProvider(provider);
          }

          // Use the first model to modify current solution
          Model comp = comps.get(0);
          ArrayList<SolutionVariable> newvars = solution.addModel(comp, graph.getID());
          for(SolutionVariable newvar : newvars) {
            varqueue.add(newvar);
          }
          // Mark variable as resolved
          v.setResolved(true);
          VariableProvider provider = new VariableProvider(comp.getID(), VariableProvider.Type.MODEL, 
              (comp.getType() != null ? comp.getType().getCategory() : null));
          v.setProvider(provider);
        }
      }
      //System.out.println(solution);
      
      // solution should contain all the models specified
      boolean ok = true;
      if(question.getModels() != null) {
        for(String mid : question.getModels()) {
          boolean found = false;
          for(Model m : solution.getModels()) {
            if(mid.equals(m.getID())) {
              found = true;
              break;
            }
          }
          if(!found) {
            ok = false;
            break;
          }
        }
      }
      if(!ok)
        continue;
      
      numsolutions++;
      if(numsolutions > maxsolutions) {
        break;
      }
      // Create workflow for solution
      WorkflowSolution workflow = new WorkflowSolution();
      WorkflowSolution wflowtmp = solution.createWorkflow(graph, this);
      if(wflowtmp != null) {
        workflow.setModelGraph(solution.createModelGraph(wflowtmp, userid));
        workflow.setWingsWorkflow(solution.createWingsWorkflow(wflowtmp, userid, this));
        //workflow.setGraph(solution.createGraph(workflow));
        //solution.diffGraph(workflow.getGraph(), graph);      
        mgraphs.add(workflow);
      }
      //break;
    }
    //System.out.println(mgraphs.size());
    return mgraphs;
  }
 
  private ArrayList<ModelVariable> createDummyVariables(ModelIO io) {
    ArrayList<ModelVariable> list = new ArrayList<ModelVariable>();
    ModelVariable mv = new ModelVariable();
    mv.setID(io.getID());
    mv.setStandard_name(io.getType().replaceAll(".*#", ""));
    list.add(mv);
    return list;
  }
}
