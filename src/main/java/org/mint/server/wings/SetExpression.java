/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mint.server.wings;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SetExpression {
  public enum SetOperator {
    XPRODUCT, NWISE, INCREASEDIM, REDUCEDIM, SHIFT
  };
  
	String port;
	List<SetExpression> children;
	SetOperator op;

	public SetExpression() {}
	
	public SetExpression(SetOperator op) {
	  this.children = new ArrayList<SetExpression>();
		this.op = op;
	}

	public SetExpression(SetOperator op, SetExpression s) {
	  this.children = new ArrayList<SetExpression>();
    this.op = op;	  
		this.add(s);
	}

	public SetExpression(SetOperator op, String obj) {
	  this.children = new ArrayList<SetExpression>();
		this.port = obj;
		this.op = op;
	}

	public SetExpression(SetOperator op, String[] objs) {
	  this.children = new ArrayList<SetExpression>();
		this.op = op;
		for (String p : objs) {
			this.add(new SetExpression(op, p));
		}
	}

	public SetOperator getOperator() {
		return this.op;
	}

	public void setOperator(SetOperator op) {
		this.op = op;
	}

	public List<SetExpression> getChildren() {
    return children;
  }

  public void setChildren(List<SetExpression> children) {
    this.children = children;
  }

	public int hashCode() {
		if (isSet())
			return children.hashCode(); // FIXME: Check if this works in equality checks
		else if (port != null)
			return port.hashCode();
		return 0;
	}

	public String getPort() {
		return this.port;
	}

	public String toString() {
		if (!isSet())
			return port;
		int i = 0;
		String str = (op == SetOperator.SHIFT ? "Shift" : "");
		str += (op == SetOperator.INCREASEDIM ? "[ " : (op == SetOperator.REDUCEDIM ? "/" : "( "));
		for (SetExpression s : this.children) {
			if (i > 0)
				str += (op == SetOperator.XPRODUCT ? " x " : (op == SetOperator.NWISE ? " || "
						: " "));
			str += s.toString();
			i++;
		}
		str += (op == SetOperator.INCREASEDIM ? "]" : (op == SetOperator.REDUCEDIM ? "/" : ")"));
		return str;
	}
	
	public void add(SetExpression child) {
	  this.children.add(child);
	}

  @JsonIgnore
  public boolean isSet() {
    if (!this.isEmpty())
      return true;
    return false;
  }
  
	@JsonIgnore
	public boolean isEmpty() {
	  return this.children.isEmpty();
	}
}
