package org.mint.server.planner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.mint.server.classes.graph.GraphPosition;
import org.mint.server.classes.graph.Relation;
import org.apache.commons.configuration.plist.PropertyListConfiguration;
import org.mint.server.classes.graph.GVariable;
import org.mint.server.classes.graph.VariableGraph;
import org.mint.server.classes.graph.VariableProvenance;
import org.mint.server.classes.graph.VariableProvider;
import org.mint.server.classes.graph.VariableProvider.Type;
import org.mint.server.classes.model.Model;
import org.mint.server.classes.model.ModelIO;
import org.mint.server.classes.model.ModelVariable;
import org.mint.server.util.Config;
import org.mint.server.classes.wings.Binding;
import org.mint.server.classes.wings.ComponentVariable;
import org.mint.server.classes.wings.Link;
import org.mint.server.classes.wings.Node;
import org.mint.server.classes.wings.Port;
import org.mint.server.classes.wings.Role;
import org.mint.server.classes.wings.Variable;
import org.mint.server.classes.wings.Workflow;
import org.mint.server.repository.impl.MintVocabularyJSON;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Solution implements Comparable<Solution> {
  
  ArrayList<GVariable> variables;
  ArrayList<Model> models;
  MintVocabularyJSON vocabulary = MintVocabularyJSON.get();
  
  @JsonIgnore
  HashMap<String, GVariable> varhash;
  
  public Solution(ArrayList<GVariable> variables) {
    this.variables = new ArrayList<GVariable>();
    this.varhash = new HashMap<String, GVariable>();
    for (GVariable v : variables) {
      // Copy graph variable
      GVariable nv = new GVariable(v);
      this.variables.add(nv);
      this.varhash.put(nv.getCanonical_name(), nv);
      v.setProvenance(new ArrayList<VariableProvenance>());
    }
    this.models = new ArrayList<Model>();
  }

  public void copy(Solution solution) {
    this.variables = new ArrayList<GVariable>();
    this.varhash = new HashMap<String, GVariable>();

    for (GVariable v : solution.getVariables()) {
      // Deep copy solution variable
      GVariable nv = new GVariable(v);
      this.variables.add(nv);
      this.varhash.put(nv.getCanonical_name(), nv);
    }
    this.models = new ArrayList<Model>(solution.getModels());
  }

  public ArrayList<GVariable> getVariables() {
    return variables;
  }

  public void setVariables(ArrayList<GVariable> variables) {
    this.variables = variables;
  }

  public ArrayList<Model> getModels() {
    return models;
  }

  public void setModels(ArrayList<Model> models) {
    this.models = models;
  }

  public HashMap<String, GVariable> getVarhash() {
    return varhash;
  }

  public void setVarhash(HashMap<String, GVariable> varhash) {
    this.varhash = varhash;
  }
  
  @Override
  public String toString() {
    return this.models.toString();
  }
  
  public ArrayList<GVariable> addModel(Model model, String graphid) {
    Model c = new Model();
    c.copyFrom(model);
    this.models.add(c);
    
    ArrayList<GVariable> newvars = new ArrayList<GVariable>();
    for (ModelIO ip : c.getInputs()) {
      for(ModelVariable v : ip.getVariables()) {
        String hashid = this.vocabulary.getCanonicalName(v.getStandard_name());
        if(this.varhash.containsKey(hashid)) {
          GVariable gvar = this.varhash.get(hashid);
          VariableProvenance prov = new VariableProvenance(c.getID(), ip.getID(), v.getMetadata(), true);
          ArrayList<VariableProvenance> provlist = gvar.getProvenance();
          if(provlist == null)
            provlist = new ArrayList<VariableProvenance>();
          provlist.add(prov);
          gvar.setProvenance(provlist);
        }
        else {
          GVariable newv = this.createNewVariable(v, c, ip, true, graphid);
          this.varhash.put(hashid, newv);
          this.variables.add(newv);
          newvars.add(newv);
        }
      }
    }
    
    for (ModelIO op : c.getOutputs()) {
      for(ModelVariable v : op.getVariables()) {
        String hashid = this.vocabulary.getCanonicalName(v.getStandard_name());
        GVariable newv;
        if(this.varhash.containsKey(hashid)) {
          VariableProvenance prov = new VariableProvenance(c.getID(), op.getID(), v.getMetadata(), false);
          newv = this.varhash.get(hashid);
          ArrayList<VariableProvenance> provlist = newv.getProvenance();
          if(provlist == null)
            provlist = new ArrayList<VariableProvenance>();
          provlist.add(prov);
          newv.setProvenance(provlist);
        }
        else {
          newv = this.createNewVariable(v, c, op, false, graphid);
          this.varhash.put(hashid, newv);
          this.variables.add(newv);
          newvars.add(newv);
        }
        
        VariableProvider provider = new VariableProvider(c.getID(), VariableProvider.Type.MODEL, 
            this.getModelCategory(c));
        newv.setResolved(true);
        newv.setProvider(provider);
      }
    }

    // System.out.println(" - Adding "+c.getLabel()+" created "+newvars.size()+" new variables");
    // System.out.println(newvars);
    return newvars;
  }
  
  private String getModelCategory(Model c) {
    if(c.getType() != null)
      return c.getType().getCategory();
    return null;
  }

  private GVariable getVariableFromModelVariable(String vid, ModelVariable v, 
      ArrayList<VariableProvenance> provenance) {
    GVariable newv = new GVariable();
    ArrayList<String> snames = new ArrayList<String>();
    snames.add(v.getStandard_name());
    String cname = this.vocabulary.getCanonicalName(v.getStandard_name());
    if(cname == null)
      cname = v.getStandard_name();
    
    GraphPosition pos = new GraphPosition();
    pos.setX(100); pos.setY(100);
    
    newv.setID(vid);
    newv.setLabel(v.getLabel());
    newv.setStandard_names(snames);
    newv.setCanonical_name(cname);
    newv.setLabel(v.getLabel());
    newv.setPosition(pos);
    newv.setProvenance(new ArrayList<VariableProvenance>(provenance));
    return newv;
  }
  
  public GVariable createNewVariable(ModelVariable v, 
      Model c, ModelIO f, boolean isinput, String graphid) {
    // Create new variable
    String vid = graphid + "#" + v.getLocalName();
    ArrayList<VariableProvenance> provenance = new ArrayList<VariableProvenance>();
    provenance.add(new VariableProvenance(c.getID(), f.getID(), v.getMetadata(), isinput));
    return this.getVariableFromModelVariable(vid, v, provenance);
  }

  public WorkflowSolution createWorkflow(VariableGraph graph, MintPlanner clib) {
    WorkflowSolution workflow = new WorkflowSolution();
    workflow.setID(graph.getID());
    workflow.setModels(this.models);
    workflow.setVariables(this.variables);

    // TODO: reverse variables
    
    ArrayList<WorkflowLink> links = new ArrayList<WorkflowLink>();
    HashMap<String, Boolean> varlinks = new HashMap<String, Boolean>(); 
    for(Model c : this.models) {
      for(ModelIO ip : c.getInputs()) {
        for(ModelVariable mv : ip.getVariables()) {
          String hashid = this.vocabulary.getCanonicalName(mv.getStandard_name());
          GVariable v = this.varhash.get(hashid);
          if(v == null || !v.isResolved() || v.getProvider() == null) {
            System.out.println("problem with " + c.getID() + " (" + hashid + ")");
            return null;
          }
          WorkflowLink link = 
              new WorkflowLink(v.getProvider().getID(), c.getID(), v.getID(), v.getProvider().getType());
          if(v.getProvider().getType() == Type.DATA)
            link.setFrom(null);
          links.add(link);
          varlinks.put(v.getID(), true);
        }
      }
    }
    
    for(Model c : this.models) {
      for(ModelIO op : c.getOutputs()) {
        for(ModelVariable mv : op.getVariables()) {
          String hashid = this.vocabulary.getCanonicalName(mv.getStandard_name());
          GVariable v = this.varhash.get(hashid);
          if(v == null)
            return null;
          
          if(varlinks.containsKey(v.getID())) {
            continue;
          }
          WorkflowLink link = 
              new WorkflowLink(c.getID(), null, v.getID(), VariableProvider.Type.OUTPUT);
          links.add(link);
          varlinks.put(v.getID(), true);
        }
      }
    }
    
    workflow.setLinks(links);
    return workflow;
  }
  
  @Override
  public int hashCode() {
    return (this.models.toString()+this.variables.toString()).hashCode();
  }
  
  @Override
  public int compareTo(Solution o) {
    return Integer.compare(this.hashCode(), o.hashCode());
  }

  private String getRandomID(String prefix) {
    return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
  }
  
  public Workflow createModelGraph(WorkflowSolution wflow, String userid) {
    if(wflow == null)
      return null;
    
    PropertyListConfiguration props = Config.get().getProperties();
    String wingsServer = props.getString("wings.server");
    String wingsDomain = props.getString("wings.domain");
    String basePath = wingsServer + "/export/users/" + userid + "/" + wingsDomain;

    String wflowName = this.getRandomID("workflow");
    String wflowNs = basePath + "/workflows/" + wflowName + ".owl#";
    String wflowId = wflowNs + wflowName;
    String clibNs = basePath + "/components/library.owl#";
    
    Workflow tpl = new Workflow(wflowId);

    HashMap<String, Variable> varid_hash = new HashMap<String, Variable>();
    HashMap<String, GVariable> gvarid_hash = new HashMap<String, GVariable>();
    HashMap<String, Port> port_hash = new HashMap<String, Port>();
    HashMap<String, Node> node_hash = new HashMap<String, Node>();
    
    for(GVariable v : wflow.variables) {
      if(!v.isResolved())
        continue;
      String varName = v.getLocalName();
      String varId = wflowNs + varName;
      Variable var = new Variable(varId, 1);
      var.setName(v.getLabel());
      tpl.addVariable(var);
      gvarid_hash.put(v.getID(), v);
      varid_hash.put(v.getID(), var);
    }
    
    for(Model c : wflow.getModels()) {
      String cvarName = c.getLocalName(); //this.getRandomID(c.getLocalName());
      /*if(c.getLabel() != null)
        cvarName = c.getLabel().replaceAll("\\s+", "_");*/
      String cvarId = wflowNs + cvarName;
      ComponentVariable cvar = new ComponentVariable(cvarId);
      String cId = clibNs + cvarName;
      Binding b = new Binding(cId);
      cvar.setBinding(b);
      //cvar.setLabel(c.getLabel());
      Node node = new Node(cvarId+"_node");
      node.setComponentVariable(cvar);
      node.setCategory(this.getModelCategory(c));
      tpl.addNode(node);
      
      for(ModelIO ip : c.getInputs()) {
        for(ModelVariable mv : ip.getVariables()) {
          String roleid = this.vocabulary.getCanonicalName(mv.getStandard_name());
          String portid =  node.getId() + "_in_" + roleid;
          Port port = new Port(portid);
          Role role = new Role(portid + "_role", 1);
          role.setRoleid(roleid);
          port.setRole(role);
          node.addInputPort(port);
          port_hash.put(portid, port);
        }
      }
      for(ModelIO op : c.getOutputs()) {
        for(ModelVariable mv : op.getVariables()) {
          String roleid = this.vocabulary.getCanonicalName(mv.getStandard_name());
          String portid = node.getId() + "_out_" + roleid;
          Port port = new Port(portid);
          Role role = new Role(portid + "_role", 1);
          role.setRoleid(roleid);
          port.setRole(role);
          node.addOutputPort(port);
          port_hash.put(portid, port);
        }
      }
      tpl.addNode(node);
      node_hash.put(c.getID(), node);
    }
    
    for(WorkflowLink l : wflow.getLinks()) {
      Variable var = varid_hash.get(l.getVariable());
      GVariable v = gvarid_hash.get(l.getVariable());
      if(l.getType() == Type.DATA) {
        String roleid = v.getCanonical_name();
        Node node = node_hash.get(l.getTo());
        String portid = node.getId() + "_in_" + roleid;
        String linkid = portid + "_to";
        Port port = port_hash.get(portid);
        Link link = new Link(linkid, null, null, node.getId(), port.getId(), var.getId());
        if(node.getCategory() != null)
          var.setCategory(node.getCategory());
        tpl.addLink(link);
      }
      else if(l.getType() == Type.OUTPUT) {
        String roleid = v.getCanonical_name();
        Node node = node_hash.get(l.getFrom());
        String portid = node.getId() + "_out_" + roleid;
        String linkid = portid + "_from";
        Port port = port_hash.get(portid);
        Link link = new Link(linkid, node.getId(), port.getId(), null, null, var.getId());
        if(var.getCategory() == null && node.getCategory() != null)
          var.setCategory(node.getCategory());
        tpl.addLink(link);
      }
      else if(l.getType() == Type.MODEL) {
        String roleid = v.getCanonical_name();
        Node fromNode = node_hash.get(l.getFrom());
        Node toNode = node_hash.get(l.getTo());
        Port fromPort = port_hash.get(fromNode.getId() + "_out_" + roleid);
        Port toPort = port_hash.get(toNode.getId() + "_in_" + roleid);
        String linkid = fromPort.getId() + "_to_" + toNode.getLocalName() + "_in_" + roleid;
        Link link = new Link(linkid, fromNode.getId(), fromPort.getId(), 
            toNode.getId(), toPort.getId(), var.getId());

        if(var.getCategory() == null && fromNode != null && fromNode.getCategory() != null)
          var.setCategory(fromNode.getCategory());
        else if(var.getCategory() == null && toNode != null && toNode.getCategory() != null)
          var.setCategory(toNode.getCategory());

        tpl.addLink(link);
      }
    }
    
    return tpl;
  }
  
  public Workflow createWingsWorkflow(WorkflowSolution wflow, String userid, MintPlanner clib) {
    if(wflow == null)
      return null;

    PropertyListConfiguration cprops = Config.get().getProperties();
    String wingsServer = cprops.getString("wings.server");
    String wingsDomain = cprops.getString("wings.domain");
    String wingsStorage = cprops.getString("wings.storage");
    String dotpath = cprops.getString("wings.dotpath");
    String ontpfx = cprops.getString("wings.ontology_prefix");
    
    String pfx = wingsServer + "/export/users/" + userid + "/" + wingsDomain;
    String tname = this.getRandomID("workflow");
    String tns = pfx + "/workflows/" + tname + ".owl#";
    String tid = tns + tname;
    String clibns = pfx + "/components/library.owl#";    

    Workflow tpl = new Workflow(tid);

    HashMap<String, ArrayList<String>> varfiles_hash = new HashMap<String, ArrayList<String>>();
    HashMap<String, GVariable> gvarid_hash = new HashMap<String, GVariable>();
    HashMap<String, Port> port_hash = new HashMap<String, Port>();
    HashMap<String, Node> node_hash = new HashMap<String, Node>();
    HashMap<String, ArrayList<String>> filevars = new HashMap<String, ArrayList<String>>();
    
    String usfx = "/users/" + userid + "/" + wingsDomain;
    //String purl = wingsServer + usfx;
    String pdir = wingsStorage + usfx;

    HashMap<String, String> props = new HashMap<String, String>();
    props.put("lib.concrete.url", pfx + "/components/library.owl");
    props.put("lib.domain.execution.url", pfx + "/executions/library.owl");
    props.put("lib.domain.code.storage", pdir + "/code/library");
    props.put("domain.workflows.dir.url", pfx + "/workflows");
    props.put("user.id", userid);
    props.put("tdb.repository.dir", wingsStorage + "/TDB");
    props.put("viewer.id", userid);
    props.put("domain.executions.dir.url", pfx + "/executions");
    props.put("lib.domain.data.url", pfx + "/data/library.owl");
    props.put("ont.domain.data.url", pfx + "/data/ontology.owl");
    props.put("lib.abstract.url", pfx + "/components/abstract.owl");
    props.put("lib.provenance.url", wingsServer + "/export/common/provenance/library.owl");
    props.put("ont.data.url", ontpfx + "/data.owl");
    props.put("lib.domain.data.storage", pdir + "/data");
    props.put("lib.domain.workflow.url", pfx + "/workflows/library.owl");
    props.put("lib.resource.url", wingsServer + "/export/common/resource/library.owl");
    props.put("ont.component.url", ontpfx + "/component.owl");
    props.put("ont.workflow.url", ontpfx + "/workflow.owl");
    props.put("ont.dir.url", ontpfx);
    props.put("dot.path", dotpath);
    props.put("ont.domain.component.ns", clibns);
    props.put("ont.execution.url", ontpfx + "/execution.owl");
    props.put("ont.resource.url", ontpfx + "/resource.owl");
    
    tpl.setProps(props);
    
    // Create Workflow Nodes & Ports
    for(Model c : wflow.getModels()) {
      String cvarName = c.getLocalName(); //this.getRandomID(c.getLocalName());
      /*if(c.getLabel() != null)
        cvarName = c.getLabel().replaceAll("\\s+", "_");*/
      String cvarId = tns + cvarName;
      ComponentVariable cvar = new ComponentVariable(cvarId);
      String cId = clibns + cvarName;
      Binding b = new Binding(cId);
      cvar.setBinding(b);
      //cvar.setLabel(c.getLabel());
      Node node = new Node(cvarId+"_node");
      node.setComponentVariable(cvar);
      node.setCategory(this.getModelCategory(c));

      for(ModelIO ip : c.getInputs()) {
        String roleid = ip.getLocalName();
        String portid =  node.getId() + "_in_" + roleid;
        Port port = new Port(portid);
        Role role = new Role(portid + "_role", 1);
        role.setRoleid(roleid);
        port.setRole(role);
        node.addInputPort(port);
        port_hash.put(portid, port);
      }
      for(ModelIO op : c.getOutputs()) {
        String roleid = op.getLocalName();
        String portid = node.getId() + "_out_" + roleid;
        Port port = new Port(portid);
        Role role = new Role(portid + "_role", 1);
        role.setRoleid(roleid);
        port.setRole(role);
        node.addOutputPort(port);
        port_hash.put(portid, port);
      }
      tpl.addNode(node);
      node_hash.put(c.getID(), node);
    }

    // Workflow Variables
    for(GVariable v : wflow.getVariables()) {
      if(!v.isResolved())
        continue;

      // Create a mapping of workflow variables to graph variables
      for(VariableProvenance prov: v.getProvenance()) {
        String fname = prov.getFileName();
        String fid = tns + fname;
        Variable var = tpl.getVariable(fid);
        if(var == null) {
          var = new Variable(fid, 1);
          //var.setLabel(v.getLabel());          
          tpl.addVariable(var);
        }
        // Add mapping of workflow variable to graph variable
        ArrayList<String> vars = filevars.get(fid);
        if(vars == null)
          vars = new ArrayList<String>();
        if(!vars.contains(v.getID()))
          vars.add(v.getID());
        filevars.put(fid, vars);
        
        // Add mapping of graph variable to workflow variable(s)
        ArrayList<String> files = varfiles_hash.get(v.getID());
        if(files == null)
          files = new ArrayList<String>();
        files.add(var.getLocalName());
        varfiles_hash.put(v.getID(), files);        
      }
      gvarid_hash.put(v.getID(), v);      
    }
    
    // Set graph variables mapping as extra information for workflow variable
    for(Variable var : tpl.getVariables().values()) {
      HashMap<String, ArrayList<String>> extra = new HashMap<String, ArrayList<String>>();
      extra.put("graph_variables", filevars.get(var.getId()));
      var.setExtra(extra);
    }
    
    // Create Workflow Links
    HashMap<String, Boolean> vars_done = new HashMap<String, Boolean>();
    for(WorkflowLink l : wflow.getLinks()) {
      if(vars_done.containsKey(l.getFrom()+"_"+l.getVariable()+"_"+l.getTo()))
        continue;
      
      ArrayList<String> filenames = varfiles_hash.get(l.getVariable());
      GVariable v = gvarid_hash.get(l.getVariable());
      
      // Get relevant provenance item
      VariableProvenance toprov=null, fromprov=null;
      Variable var = null;
      
      // Prefer output file id from the link's from node
      String tvarname = v.getMatchingFileName(l.getTo(), filenames, true);
      if(tvarname != null) {
        String tvarid = tns + tvarname;
        Variable fvar = tpl.getVariable(tvarid);
        if(fvar != null)
          var = fvar;
        toprov = v.getMatchingProvenanceItem(l.getTo(), tvarname);
      }
      String fvarname = v.getMatchingFileName(l.getFrom(), filenames, false);
      if(fvarname != null) {
        String fvarid = tns + fvarname;
        Variable tvar = tpl.getVariable(fvarid);
        if(tvar != null)
          var = tvar;
        fromprov = v.getMatchingProvenanceItem(l.getFrom(), fvarname);
      }
      
      if(l.getType() == Type.DATA) {
        String roleid = toprov.getFileName();
        Node node = node_hash.get(l.getTo());
        String portid = node.getId() + "_in_" + roleid;
        String linkid = portid + "_to";
        Port port = port_hash.get(portid);
        Link link = new Link(linkid, null, null, node.getId(), port.getId(), var.getId());
        if(node.getCategory() != null)
          var.setCategory(node.getCategory());
        tpl.addLink(link);
      }
      else if(l.getType() == Type.OUTPUT) {
        String roleid = fromprov.getFileName();
        Node node = node_hash.get(l.getFrom());
        String portid = node.getId() + "_out_" + roleid;
        String linkid = portid + "_from";
        Port port = port_hash.get(portid);
        Link link = new Link(linkid, node.getId(), port.getId(), null, null, var.getId());
        if(var.getCategory() == null && node.getCategory() != null)
          var.setCategory(node.getCategory());
        tpl.addLink(link);
      }
      else if(l.getType() == Type.MODEL) {
        String frommeta = fromprov.getMetadata().toString();
        String tometa = toprov.getMetadata().toString();
        if(!frommeta.equals(tometa)) {
          // TODO: Add transformation workflows (from Data Catalog ?)
          
          /*
          for(var j=0; j<clib.workflow_fragments.length; j++) {
            var wfrag = clib.workflow_fragments[j];
            if(wfrag.variable == v.canonical_name) {
              var wfrommeta = JSON.stringify(wfrag.from.metadata);
              var wtometa = JSON.stringify(wfrag.to.metadata);
              if(frommeta == wfrommeta && tometa == wtometa) {
                // Found a Conversion worklfow match

                var wtplstr = JSON.stringify(wfrag.workflow.template);

                // Convert ids to fully qualified uris
                wtplstr = wtplstr.replace(/\[wflowns\]#/g, tns);
                wtplstr = wtplstr.replace(/\[clibns\]#/g, clibns);
                var wtpl = JSON.parse(wtplstr);

                wfrag.from.workflow_variable_uri = tns + wfrag.from.workflow_variable;
                wfrag.to.workflow_variable_uri = tns + wfrag.to.workflow_variable;

                // Copy fragment nodes and variables
                tpl.Nodes = Object.assign({}, tpl.Nodes, wtpl.Nodes);
                tpl.Variables = Object.assign({}, tpl.Variables, wtpl.Variables);

                // Replace I/O variables with variables from parent template
                delete tpl.Variables[wfrag.from.workflow_variable_uri];
                delete tpl.Variables[wfrag.to.workflow_variable_uri];

                tpl.Links = Object.assign({}, tpl.Links, wtpl.Links);
                for(var lid in tpl.Links) {
                  var wl = tpl.Links[lid];
                  if(wl.variable.id == wfrag.from.workflow_variable_uri) {
                    wl.variable.id = from.file_uri;
                    wl.fromNode = {id: l.from_uri};
                    wl.fromPort = {id: from.file_uri + "_outport"};
                  }
                  else if(wl.variable.id == wfrag.to.workflow_variable_uri) {
                    wl.variable.id = to.file_uri;
                    wl.toNode = {id: l.to_uri};
                    wl.toPort = {id: to.file_uri + "_inport"};
                  }
                  tpl.Links[lid] = wl;
                }
                tpl.Variables[from.file_uri].category = tpl.Nodes[l.from_uri].category;
                tpl.Variables[to.file_uri].category = tpl.Nodes[l.to_uri].category;
                break;
              }
            }
          }
          continue;
          */
        }
        
        // If a link to itself, then don't add
        if(l.getFrom().equals(l.getTo())) {
          // Ignore
        }
        else {
          Node fromNode = node_hash.get(l.getFrom());
          Node toNode = node_hash.get(l.getTo());
          Port fromPort = port_hash.get(fromNode.getId() + "_out_" + fromprov.getFileName());
          Port toPort = port_hash.get(toNode.getId() + "_in_" + toprov.getFileName());
          String linkid = fromPort.getId() + "_to_" + toPort.getLocalName();
          Link link = new Link(linkid, fromNode.getId(), fromPort.getId(), 
              toNode.getId(), toPort.getId(), var.getId());
  
          if(var.getCategory() == null && fromNode != null && fromNode.getCategory() != null)
            var.setCategory(fromNode.getCategory());
          else if(var.getCategory() == null && toNode != null && toNode.getCategory() != null)
            var.setCategory(toNode.getCategory());
  
          if(!toprov.getFileName().equals(fromprov.getFileName())) {
            tpl.variables.remove(tns + toprov.getFileName());
            ArrayList<String> linkids = new ArrayList<String>();
            for(Link tl : tpl.getLinks().values()) {
              if(tl.getVariable().getId().equals(tns + toprov.getFileName()))
                linkids.add(tl.getId());
            }
            for(String lid : linkids)
              tpl.getLinks().remove(lid);
          }
          
          tpl.addLink(link);
        }
      }
      if(fromprov != null) {
        for(String vid : filevars.get(tns + fromprov.getFileName())) {
          vars_done.put(l.getFrom()+"_"+vid+"_"+l.getTo(), true);
        }
      }
      if(toprov != null) {
        for(String vid : filevars.get(tns + toprov.getFileName())) {
          vars_done.put(l.getFrom()+"_"+vid+"_"+l.getTo(), true);
        }
      }      
    }

    // Create Input and Output links for unclaimed ports
    for(String nid : tpl.getNodes().keySet()) {
      Node n = tpl.getNodes().get(nid);
      for(String portid : n.getInputPorts().keySet()) {
        Port p = n.getInputPorts().get(portid);

        String roleid = p.getRole().getRoleid();
        String pid = n.getId() + "_in_" + roleid;
        String linkid = portid + "_to";
        String fid = tns + roleid;
        
        // Check for links to port
        if(!this.hasLinksToPort(tpl, n.getId(), pid)) {
          Variable v = tpl.getVariable(fid);
          if(v == null) {
            v = new Variable(fid, 1);
            tpl.addVariable(v);
          }
          v.setCategory(n.getCategory());
          Link l = new Link(linkid, null, null, n.getId(), pid, fid);
          tpl.addLink(l);
        }
      }
      for(String portid : n.getOutputPorts().keySet()) {
        Port p = n.getOutputPorts().get(portid);

        String roleid = p.getRole().getRoleid();
        String pid = n.getId() + "_out_" + roleid;
        String linkid = portid + "_from";
        String fid = tns + roleid;
        
        // Check for links to port
        if(!this.hasLinksFromPort(tpl, n.getId(), pid)) {
          Variable v = tpl.getVariable(fid);
          if(v == null) {
            v = new Variable(fid, 1);
            tpl.addVariable(v);
          }
          v.setCategory(n.getCategory());
          Link l = new Link(linkid, n.getId(), pid, null, null, fid);
          tpl.addLink(l);
        }
      }
    }

    // Create Input and Output roles
    for(String lid : tpl.getLinks().keySet()) {
      Link link = tpl.getLink(lid);
      Variable v = tpl.getVariable(link.getVariable().getId());
      if(link.getFromNode() == null) {
        String roleid = v.getId() + "_irole";
        Role role = new Role(roleid, v.getType());
        tpl.addInputRole(v.getId(), role);
      }
      if(link.getToNode() == null) {
        String roleid = v.getId() + "_orole";
        Role role = new Role(roleid, v.getType());
        tpl.addOutputRole(v.getId(), role);
      }
    }
    return tpl;
  }

  boolean hasLinksToPort(Workflow tpl, String nid, String pid) {
    for(String lid : tpl.getLinks().keySet()) {
      Link l = tpl.getLinks().get(lid);
      if(l.getToNode() != null && l.getToNode().getId().equals(nid) &&
          l.getToPort() != null && l.getToPort().getId().equals(pid))
        return true;
    }
    return false;
  }

  boolean hasLinksFromPort(Workflow tpl, String nid, String pid) {
    for(String lid : tpl.getLinks().keySet()) {
      Link l = tpl.getLinks().get(lid);
      if(l.getFromNode() != null && l.getFromNode().getId().equals(nid) &&
          l.getFromPort() != null && l.getFromPort().getId().equals(pid))
        return true;
    }
    return false;
  }
  
  public VariableGraph createGraph(WorkflowSolution wflow) {
    if(wflow == null)
      return null;
    
    VariableGraph graph = new VariableGraph();
    graph.setID(wflow.getID());
    graph.setLabel(wflow.getLabel());

    Map<String, Variable> gvars = wflow.getModelGraph().getVariables();
    String tns = wflow.getModelGraph().getNamespace();
    HashMap<String, GVariable> varid_hash = new HashMap<String, GVariable>();
    HashMap<String, GVariable> cname_hash = new HashMap<String, GVariable>();
    HashMap<String, Model> compid_hash = new HashMap<String, Model>();
    
    for(GVariable v : wflow.getVariables()) {
      if(!v.isResolved())
        continue;
      GVariable nv = new GVariable(v);
      Variable gvar = gvars.get(tns + v.getLocalName());
      if(gvar != null)
        nv.setCategory(gvar.getCategory());
      cname_hash.put(nv.getCanonical_name(), nv);
      varid_hash.put(nv.getID(), nv);
      graph.getVariables().add(nv);
    }
    
    for(Model c : wflow.getModels()) {
      compid_hash.put(c.getID(), c);
    }
    
    for(WorkflowLink l : wflow.getLinks()) {
      GVariable v = varid_hash.get(l.getVariable());
      if(l.getFrom() != null && l.getType() != Type.DATA) {
        Model c = compid_hash.get(l.getFrom());
        for(ModelIO ip : c.getInputs()) {
          for(ModelVariable var : ip.getVariables()) {
            String cname = this.vocabulary.getCanonicalName(var.getStandard_name());
            GVariable fromvar = cname_hash.get(cname);
            Relation rel = new Relation();
            rel.setFrom(fromvar.getID());
            rel.setTo(v.getID());
            graph.getLinks().add(rel);
            
            if(fromvar.getCategory() == null && c.getType() != null)
              fromvar.setCategory(c.getType().getCategory());
          }
        }
        if(v.getCategory() == null && c.getType() != null)
          v.setCategory(c.getType().getCategory());
      }
      
      if(l.getTo() != null) {
        Model c = compid_hash.get(l.getTo());
        for(ModelIO op : c.getOutputs()) {
          for(ModelVariable var : op.getVariables()) {
            String cname = this.vocabulary.getCanonicalName(var.getStandard_name());
            GVariable tovar = cname_hash.get(cname);
            Relation rel = new Relation();
            rel.setFrom(v.getID());
            rel.setTo(tovar.getID());
            graph.getLinks().add(rel);
            
            if(tovar.getCategory() == null && c.getType() != null)
              tovar.setCategory(c.getType().getCategory());
          }
        }
        if(v.getCategory() == null && c.getType() != null)
          v.setCategory(c.getType().getCategory());
      }      
    }
    return graph;
  }
  
  public VariableGraph diffGraph(VariableGraph newgraph, VariableGraph oldgraph) {
    HashMap<String, Boolean> linkhash = new HashMap<String, Boolean>();
    HashMap<String, Boolean> varhash = new HashMap<String, Boolean>();
    for(GVariable v : oldgraph.getVariables()) {
      varhash.put(v.getID(), true);
    }
    for(Relation l : oldgraph.getLinks()) {
      linkhash.put(l.getFrom() + "-" + l.getTo(), true);
    }
    // Annotate new graph
    for(GVariable v : newgraph.getVariables()) {
      if(!varhash.containsKey(v.getID()))
        v.setAdded(true);
    }
    for(Relation l : newgraph.getLinks()) {
      if(!linkhash.containsKey(l.getFrom() + "-" + l.getTo()))
        l.setAdded(true);
    }
    return newgraph;
  }

}
