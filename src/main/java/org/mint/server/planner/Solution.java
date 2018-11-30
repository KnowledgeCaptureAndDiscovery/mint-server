package org.mint.server.planner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.mint.server.classes.graph.GraphPosition;
import org.mint.server.classes.graph.Variable;
import org.mint.server.classes.graph.VariableGraph;
import org.mint.server.classes.graph.VariableProvenance;
import org.mint.server.classes.graph.VariableProvider;
import org.mint.server.classes.model.Model;
import org.mint.server.classes.model.ModelIO;
import org.mint.server.classes.model.ModelVariable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Solution {
  
  ArrayList<Variable> variables;
  ArrayList<Model> models;
  
  @JsonIgnore
  HashMap<String, Variable> varhash;
  
  public Solution(ArrayList<Variable> variables) {
    this.variables = new ArrayList<Variable>();
    this.varhash = new HashMap<String, Variable>();
    for (Variable v : variables) {
      // Copy graph variable
      Variable nv = new Variable();
      nv.copyFrom(v);
      this.variables.add(nv);
      this.varhash.put(nv.getCanonical_name(), nv);
      v.setProvenance(new HashMap<String, VariableProvenance>());
    }
    this.models = new ArrayList<Model>();
  }

  public void copy(Solution solution) {
    this.variables = new ArrayList<Variable>();
    this.varhash = new HashMap<String, Variable>();

    for (Variable v : solution.getVariables()) {
      // Deep copy solution variable
      Variable nv = new Variable();
      nv.copyFrom(v);
      this.variables.add(nv);
      this.varhash.put(nv.getCanonical_name(), nv);
    }
    this.models = new ArrayList<Model>(solution.getModels());
  }

  public ArrayList<Variable> getVariables() {
    return variables;
  }

  public void setVariables(ArrayList<Variable> variables) {
    this.variables = variables;
  }

  public ArrayList<Model> getModels() {
    return models;
  }

  public void setModels(ArrayList<Model> models) {
    this.models = models;
  }

  public HashMap<String, Variable> getVarhash() {
    return varhash;
  }

  public void setVarhash(HashMap<String, Variable> varhash) {
    this.varhash = varhash;
  }
  
  public ArrayList<Variable> addModel(Model model) {
    Model c = new Model();
    c.copyFrom(model);
    this.models.add(c);
    
    ArrayList<Variable> newvars = new ArrayList<Variable>();
    for (ModelIO ip : c.getInputs()) {
      for(ModelVariable v : ip.getVariables()) {
        String hashid = v.getCanonical_name();
        if(this.varhash.containsKey(hashid)) {
          VariableProvenance prov = new VariableProvenance(c.getID(), ip.getID(), v.getMetadata(), true);
          HashMap<String, VariableProvenance> provmap = this.varhash.get(hashid).getProvenance();
          if(provmap == null)
            provmap = new HashMap<String, VariableProvenance>();
          provmap.put(c.getID(), prov);
          this.varhash.get(hashid).setProvenance(provmap);
        }
        else {
          Variable newv = this.createNewVariable(v, c, ip, true);
          newvars.add(newv);
        }
      }
    }
    
    for (ModelIO op : c.getOutputs()) {
      for(ModelVariable v : op.getVariables()) {
        String hashid = v.getCanonical_name();
        Variable newv;
        if(this.varhash.containsKey(hashid)) {
          VariableProvenance prov = new VariableProvenance(c.getID(), op.getID(), v.getMetadata(), false);
          newv = this.varhash.get(hashid);
          HashMap<String, VariableProvenance> provmap = newv.getProvenance();
          if(provmap == null)
            provmap = new HashMap<String, VariableProvenance>();
          provmap.put(c.getID(), prov);
          newv.setProvenance(provmap);
        }
        else {
          newv = this.createNewVariable(v, c, op, true);
          newvars.add(newv);
        }
        
        VariableProvider provider = new VariableProvider(c.getID(), VariableProvider.Type.MODEL, c.getCategory());
        newv.setResolved(true);
        newv.setProvider(provider);
      }
    }

    // System.out.println(" - Adding "+c.id+" created "+newvars.length+" new variables");
    // System.out.println(newvars);
    for(Variable v : newvars) {
      String hashid = v.getCanonical_name();
      this.varhash.put(hashid, v);
      this.variables.add(v);
    }
    return newvars;
  }

  private Variable getVariableFromModelVariable(String vid, ModelVariable v, 
      HashMap<String, VariableProvenance> provenance) {
    Variable newv = new Variable();
    ArrayList<String> snames = new ArrayList<String>();
    snames.add(v.getStandard_name());
    String cname = v.getCanonical_name();
    if(cname == null)
      cname = v.getStandard_name();
    
    GraphPosition pos = new GraphPosition();
    pos.setX(100); pos.setY(100);
    
    newv.setID(vid);
    newv.setLabel(v.getLabel());
    newv.setStandard_names(snames);
    newv.setCanonical_name(cname);
    newv.setProvenance(provenance);
    newv.setLabel(v.getLabel());
    newv.setPosition(pos);
    return newv;
  }
  
  public Variable createNewVariable(ModelVariable v, 
      Model c, ModelIO f, boolean isinput) {
    // Create new variable
    String vid = "v_" + UUID.randomUUID().toString().substring(2, 9);
    HashMap<String, VariableProvenance> provenance = new HashMap<String, VariableProvenance>();
    provenance.put(c.getID(), new VariableProvenance(c.getID(), f.getID(), v.getMetadata(), isinput));
    return this.getVariableFromModelVariable(vid, v, provenance);
  }

  public Workflow createWorkflow(VariableGraph graph, MintPlanner clib) {
    Workflow workflow = new Workflow();
    workflow.setID(graph.getID());
    workflow.setModels(this.models);
    workflow.setVariables(this.variables);

    // TODO: reverse variables
    
    ArrayList<WorkflowLink> links = new ArrayList<WorkflowLink>();
    HashMap<String, Boolean> varlinks = new HashMap<String, Boolean>(); 
    for(Model c : this.models) {
      for(ModelIO ip : c.getInputs()) {
        for(ModelVariable mv : ip.getVariables()) {
          String hashid = mv.getCanonical_name();
          Variable v = this.varhash.get(hashid);
          if(v == null || !v.isResolved() || v.getProvider() == null) {
            System.out.println("problem with " + c.getID() + " (" + hashid + ")");
            return null;
          }
          WorkflowLink link = 
              new WorkflowLink(v.getProvider().getID(), c.getID(), v.getID(), v.getProvider().getType());
          links.add(link);
          varlinks.put(v.getID(), true);
        }
      }
    }
    
    for(Model c : this.models) {
      for(ModelIO op : c.getOutputs()) {
        for(ModelVariable mv : op.getVariables()) {
          String hashid = mv.getCanonical_name();
          Variable v = this.varhash.get(hashid);
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
    /*

    workflow.model_graph = this.createModelGraph(workflow);
    workflow.wings_workflow = this.createWingsWorkflow(workflow, clib);
    workflow.graph = this.createGraph(workflow);
    workflow.graph = this.diffGraph(workflow.graph, graph);

    // Do a diff between original graph and new graph
    return workflow;
    */
  }

  /*
  diffGraph(newgraph, oldgraph) {
    var linkhash = {};
    var varhash = {};
    for(var i=0; i<oldgraph.variables.length; i++) {
      var v = oldgraph.variables[i];
      varhash[v.id] = true;
    }
    for(var i=0; i<oldgraph.links.length; i++) {
      var l = oldgraph.links[i];
      linkhash[l.from + "-" + l.to] = true;
    }
    // Annotate new graph
    for(var i=0; i<newgraph.variables.length; i++) {
      var v = newgraph.variables[i];
      if(!varhash[v.id])
        newgraph.variables[i].new = true;
    }
    for(var i=0; i<newgraph.links.length; i++) {
      var l = newgraph.links[i];
      if(!linkhash[l.from + "-" + l.to])
        newgraph.links[i].new = true;
    }
    return newgraph;
  }

  createGraph(wflow) {
    if(wflow == null)
      return null;
    var graph = {
      id: wflow.id,
      label: wflow.id,
      variables: [],
      links: []
    }
    var gvars = wflow.model_graph.template.Variables;

    var varid_hash = {};
    var cname_hash = {};
    for(var i=0; i<wflow.variables.length; i++) {
      var v = wflow.variables[i];
      if(!v.resolved)
        continue;
      var gvar = gvars[v.id];
      var vcat = gvar.category;
      var nv = {
        id: v.id, label: v.label, category: vcat,
        standard_name: v.standard_name,
        canonical_name: v.canonical_name,
        position: v.position
      }
      cname_hash[nv.canonical_name] = nv;
      varid_hash[nv.id] = nv;
      graph.variables.push(nv);
    }

    var compid_hash = {};
    for(var i=0; i<wflow.models.length; i++) {
      var c = wflow.models[i];
      compid_hash[c.id] = c;
    }

    for(var i=0; i<wflow.links.length; i++) {
      var l = wflow.links[i];
      var v = varid_hash[l.variable];
      if(l.from && l.type != "data") {
        var c = compid_hash[l.from];
        for(var j=0; j<c.inputs.length; j++) {
          var ip = c.inputs[j];
          for(var k=0; k<ip.variables.length; k++) {
            var fromvar = cname_hash[ip.variables[k].canonical_name];
            graph.links.push({from: fromvar.id, to: v.id});
            if(!fromvar.category)
              fromvar.category = c.category;
          }
        }
        if(!v.category)
          v.category = c.category;
      }
      if(l.to) {
        var c = compid_hash[l.to];
        for(var j=0; j<c.outputs.length; j++) {
          var op = c.outputs[j];
          for(var k=0; k<op.variables.length; k++) {
            var vname = op.variables[k].canonical_name;
            var tovar = cname_hash[vname];
            graph.links.push({from: v.id, to: tovar.id});
            if(!tovar.category)
              tovar.category = c.category;
          }
        }
        if(!v.category)
          v.category = c.category;
      }
    }
    return graph;
  }

  createModelGraph(wflow) {
    if(wflow == null)
      return null;
    var wingsw = {
      template: {
        version: 0,
        Nodes: {},
        Links: {},
        Variables: {}
      }
    }
    var tpl = wingsw.template;
    var varid_hash = {};
    var cname_hash = {};
    for(var i=0; i<wflow.variables.length; i++) {
      var v = wflow.variables[i];
      if(!v.resolved)
        continue;
      cname_hash[v.canonical_name] = v;
      varid_hash[v.id] = v;
      tpl.Variables[v.id] = {
        id: v.id,
        name: v.label,
        type: 1 //FIXME: use category here
      }
    }
    for(var i=0; i<wflow.models.length; i++) {
      var c = wflow.models[i];
      var nodeid = c.id;
      var node = {
        id: nodeid,
        name: c.label,
        inputPorts: {},
        outputPorts: {},
        category: c.category,
        componentVariable: {
          binding: {
            id: c.label,
            type: "uri"
          },
          id: "model_" + c.id
        }
      }
      for(var j=0; j<c.inputs.length; j++) {
        var ip = c.inputs[j];
        for(var k=0; k<ip.variables.length; k++) {
          var roleid = ip.variables[k].canonical_name; // Standard name
          var portid = "inport_" + roleid;
          node.inputPorts[portid] = {
            id: portid,
            role: {
              type: 1,
              roleid: roleid
            }
          }
        }
      }
      for(var j=0; j<c.outputs.length; j++) {
        var op = c.outputs[j];
        for(var k=0; k<op.variables.length; k++) {
          var roleid = op.variables[k].canonical_name; // Standard name
          var portid = "outport_" + roleid;
          node.outputPorts[portid] = {
            id: portid,
            role: {
              type: 1,
              roleid: roleid
            }
          }
        }
      }
      tpl.Nodes[nodeid] = node;
    }
    for(var i=0; i<wflow.links.length; i++) {
      var l = wflow.links[i];
      var v = varid_hash[l.variable];
      if(l.type == "data") {
        var lid = l.to+"_"+v.canonical_name;
        var link = {
          id: lid,
          toNode: {id: l.to},
          toPort: {id: "inport_" + v.canonical_name},
          variable: {id: v.id}
        }
        var cat = tpl.Nodes[l.to].category;
        if(cat)
          tpl.Variables[v.id].category = cat;

        tpl.Links[lid] = link;
      }
      else if(l.type == "output") {
        var lid = l.from+"_"+v.canonical_name;
        var link = {
          id: lid,
          fromNode: {id: l.from},
          fromPort: {id: "outport_" + v.canonical_name},
          variable: {id: v.id}
        }
        var cat = tpl.Nodes[l.from].category;
        if(!tpl.Variables[v.id].category && cat)
          tpl.Variables[v.id].category = cat;

        tpl.Links[lid] = link;
      }
      else if(l.type == "model") {
        var lid = l.from+"_"+l.to+"_"+v.canonical_name;
        var link = {
          id: lid,
          fromNode: {id: l.from},
          fromPort: {id: "outport_" + v.canonical_name},
          toNode: {id: l.to},
          toPort: {id: "inport_" + v.canonical_name},
          variable: {id: v.id}
        }
        var cat = tpl.Nodes[l.from].category;
        if(!tpl.Variables[v.id].category && cat)
          tpl.Variables[v.id].category = cat;
        else {
          cat = tpl.Nodes[l.to].category;
          if(!tpl.Variables[v.id].category && cat)
            tpl.Variables[v.id].category = cat;
        }

        tpl.Links[lid] = link;
      }
    }
    return wingsw;
  }

  createWingsWorkflow(wflow, clib) {
    if(wflow == null)
      return null;

    // FIXME: This is currently hardcoded. Should be configurable
    clib.storage = "/home/varun/.wings/storage";
    var dotpath = "/usr/bin/dot";
    var ontpfx = "http://www.wings-workflows.org/ontology";

    var tname = wflow.name + "_" + Math.random().toString(36).substr(2, 9);
    var usfx = "/users/" + clib.userid + "/" + clib.wingsDomain;
    var pfx = clib.wingsServer + "/export" + usfx;
    var tns = pfx + "/workflows/" + tname + ".owl#";
    var tid = tns + tname;

    var clibns = pfx + "/components/library.owl#";
    var purl = clib.wingsServer + usfx;
    var pdir = clib.storage + usfx;

    var wingsw = {
      template: {
        id: tid,
        Nodes: {},
        Links: {},
        Variables: {},
        inputRoles: {},
        outputRoles: {},
        onturl: ontpfx + "/workflow.owl",
        wflowns: ontpfx + "/workflow.owl#",
        version: 0,
        subtemplates: {},
        metadata: {},
        rules: {},
        props: {
          "lib.concrete.url": pfx + "/components/library.owl",
          "lib.domain.execution.url": pfx + "/executions/library.owl",
          "lib.domain.code.storage": pdir + "/code/library",
          "domain.workflows.dir.url": pfx + "/workflows",
          "user.id": clib.userid,
          "tdb.repository.dir": clib.storage + "/TDB",
          "viewer.id": clib.userid,
          "domain.executions.dir.url": pfx + "/executions",
          "lib.domain.data.url": pfx + "/data/library.owl",
          "ont.domain.data.url": pfx + "/data/ontology.owl",
          "lib.abstract.url": pfx + "/components/abstract.owl",
          "lib.provenance.url": clib.wingsServer + "/export/common/provenance/library.owl",
          "ont.data.url": ontpfx + "/data.owl",
          "lib.domain.data.storage": pdir + "/data",
          "lib.domain.workflow.url": pfx + "/workflows/library.owl",
          "lib.resource.url": clib.wingsServer + "/export/common/resource/library.owl",
          "ont.component.url": ontpfx + "/component.owl",
          "ont.workflow.url": ontpfx + "/workflow.owl",
          "ont.dir.url": ontpfx,
          "dot.path": dotpath,
          "ont.domain.component.ns": clibns,
          "ont.execution.url": ontpfx + "/execution.owl",
          "ont.resource.url": ontpfx + "/resource.owl"
        }
      }
    }
    var tpl = wingsw.template;

    // Create Workflow Nodes & Ports
    for(var i=0; i<wflow.models.length; i++) {
      var c = wflow.models[i];
      var nodeid = tns + c.id + "_node";
      var node = {
        id: nodeid,
        name: c.label,
        inputPorts: {},
        outputPorts: {},
        category: c.category,
        componentVariable: {
          binding: {
            id: clibns + c.label,
            type: "uri"
          },
          id: nodeid + "_component"
        }
      }
      for(var j=0; j<c.inputs.length; j++) {
        var ip = c.inputs[j];
        var roleid = ip.id;
        var portid = tns + roleid + "_inport";
        node.inputPorts[portid] = {
          id: portid,
          role: {
            type: 1,
            roleid: roleid,
            dimensionality: 0,
            id: portid + "_role"
          }
        }
      }
      for(var j=0; j<c.outputs.length; j++) {
        var op = c.outputs[j];
        var roleid = op.id;
        var portid = tns + roleid + "_outport";
        node.outputPorts[portid] = {
          id: portid,
          role: {
            type: 1,
            roleid: roleid,
            dimensionality: 0,
            id: portid + "_role"
          }
        }
      }
      tpl.Nodes[nodeid] = node;
    }
    // Workflow Variables
    var varid_hash = {};
    var filevars = {};
    for(var i=0; i<wflow.variables.length; i++) {
      var v = wflow.variables[i];
      if(!v.resolved)
        continue;
      // Create a hash of variable id to variable object
      varid_hash[v.id] = v;
      var fid = null;
      // Create a mapping of file ids to variables it contains
      for(var cid in v.provenance) {
        var fname = v.provenance[cid].file_id;
        var fid = tns + fname;
        if(!(fid in filevars))
          filevars[fid] = [];
        filevars[fid].push(v.id);
        tpl.Variables[fid] = {
          id: fid,
          name: fname,
          extra: {
            graph_variables: filevars[fid]
          },
          type: 1 //FIXME: use category here
        }
      }
    }

    // Create Workflow Links
    var vars_done = {};
    for(var i=0; i<wflow.links.length; i++) {
      var l = wflow.links[i];
      if(l.variable in vars_done)
        continue;

      var v = varid_hash[l.variable];
      var from = v.provenance[l.from];
      var to = v.provenance[l.to];

      // Create fully qualified uris for use in wings template
      l.from_uri = tns + l.from + "_node";
      l.to_uri = tns + l.to + "_node";
      if(from)
        from.file_uri = tns + from.file_id;
      if(to)
        to.file_uri = tns + to.file_id;

      if(l.type == "data") {
        var lid = l.to_uri+"_"+to.file_id;
        var link = {
          id: lid,
          toNode: {id: l.to_uri},
          toPort: {id: to.file_uri + "_inport"},
          variable: {id: to.file_uri}
        };
        var cat = tpl.Nodes[l.to_uri].category;
        if(!tpl.Variables[to.file_uri].category && cat)
          tpl.Variables[to.file_uri].category = cat;

        tpl.Links[lid] = link;
      }
      else if(l.type == "output") {
        var lid = l.from_uri+"_"+from.file_id;
        var link = {
          id: lid,
          fromNode: {id: l.from_uri},
          fromPort: {id: from.file_uri + "_outport"},
          variable: {id: from.file_uri}
        };
        var cat = tpl.Nodes[l.from_uri].category;
        if(!tpl.Variables[from.file_uri].category && cat)
          tpl.Variables[from.file_uri].category = cat;

        tpl.Links[lid] = link;
      }
      else if(l.type == "model") {
        // TODO:
        // Check from.metadata with to.metadata
        // If not same, then add conversion fragment if possible
        var frommeta = JSON.stringify(from.metadata);
        var tometa = JSON.stringify(to.metadata);
        if(frommeta != tometa) {
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
        }

        var lid = l.from_uri+"_"+l.to+"_"+from.file_id;
        var link = {
          id: lid,
          fromNode: {id: l.from_uri},
          fromPort: {id: from.file_uri + "_outport"},
          toNode: {id: l.to_uri},
          toPort: {id: to.file_uri + "_inport"},
          variable: {id: from.file_uri}
        }

        var cat = tpl.Nodes[l.from_uri].category;
        if(!tpl.Variables[from.file_uri].category && cat)
          tpl.Variables[from.file_uri].category = cat;
        else {
          var cat = tpl.Nodes[l.to_uri].category;
          if(tpl.Variables[to.file_uri] && !tpl.Variables[to.file_uri].category && cat)
            tpl.Variables[to.file_uri].category = cat;
        }

        tpl.Links[lid] = link;
        // Since we are combining 2 variables into 1 here as they are compatible
        // Delete one of them
        if(to.file_uri != from.file_uri)
          delete tpl.Variables[to.file_uri];
      }

      if(from) {
        for(var j=0; j<filevars[from.file_uri]; j++) {
          var vid = filevars[from.file_uri][j];
          vars_done[vid] = true;
        }
      }
      if(to) {
        for(var j=0; j<filevars[to.file_uri]; j++) {
          var vid = filevars[to.file_uri][j];
          vars_done[vid] = true;
        }
      }
    }

    // Create Input and Output links for unclaimed ports
    for(var nid in tpl.Nodes) {
      var n = tpl.Nodes[nid];
      for(var portid in n.inputPorts) {
        var p = n.inputPorts[portid];
        var fname = p.role.roleid;
        var lid = n.id+"_"+fname;
        var fid = tns + fname;
        var portid = fid + "_inport";
        // Check for links to port
        if(!this.hasLinksToPort(tpl, n.id, portid)) {
          tpl.Variables[fid] = {
            id: fid,
            name: fname,
            type: 1,
            category: n.category
          }
          tpl.Links[lid] = {
            id: lid,
            toNode: {id: n.id},
            toPort: {id: portid},
            variable: {id: fid}
          };
        }
      }
      for(var portid in n.outputPorts) {
        var p = n.outputPorts[portid];
        var fname = p.role.roleid;
        var lid = n.id+"_"+fname;
        var fid = tns + fname;
        var portid = fid + "_outport";
        if(!this.hasLinksFromPort(tpl, n.id, portid)) {
          tpl.Variables[fid] = {
            id: fid,
            name: fname,
            type: 1,
            category: n.category
          }
          tpl.Links[lid] = {
            id: lid,
            fromNode: {id: n.id},
            fromPort: {id: portid},
            variable: {id: fid}
          };
        }
      }
    }

    // Create Input and Output roles
    for(var lid in tpl.Links) {
      var link = tpl.Links[lid];
      var v = tpl.Variables[link.variable.id];
      if(!link.fromNode) {
        tpl.inputRoles[v.id] = {
          type: v.type,
          roleid: getLocalName(v.id),
          dimensionality: 0,
          id: v.id + "_irole"
        }
      }
      if(!link.toNode) {
        tpl.outputRoles[v.id] = {
          type: v.type,
          roleid: getLocalName(v.id),
          dimensionality: 0,
          id: v.id + "_orole"
        }
      }
    }
    return wingsw;
  }

  hasLinksToPort(tpl, nid, pid) {
    for(var lid in tpl.Links) {
      var l = tpl.Links[lid];
      if(l.toNode && l.toNode.id == nid &&
          l.toPort && l.toPort.id == pid)
        return true;
    }
    return false;
  }

  hasLinksFromPort(tpl, nid, pid) {
    for(var lid in tpl.Links) {
      var l = tpl.Links[lid];
      if(l.fromNode && l.fromNode.id == nid &&
          l.fromPort && l.fromPort.id == pid)
        return true;
    }
    return false;
  }
  */
}
