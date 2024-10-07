package com.sphenon.basics.operations.classes;

/****************************************************************************
  Copyright 2001-2024 Sphenon GmbH

  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  License for the specific language governing permissions and limitations
  under the License.
*****************************************************************************/

import com.sphenon.basics.context.*;
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.debug.*;
import com.sphenon.basics.monitoring.*;
import com.sphenon.basics.processing.*;
import com.sphenon.basics.processing.classes.*;
import com.sphenon.basics.system.*;
import com.sphenon.basics.data.*;

import com.sphenon.ui.annotations.*;

import com.sphenon.basics.operations.*;
import com.sphenon.basics.operations.factories.*;

import java.io.PrintStream;
import java.util.List;
import java.util.Vector;

@UIParts("js:instance.getExecutions(context)")
public class Execution_BasicSequence implements Execution, ExecutionSequence, Dumpable, ContextAware {

    // static protected int next_instance_id = 0;
    // static protected Execution_BasicSequence its_me = null;
    // protected volatile int instance_id;

    // static synchronized protected int getNextInstanceId() {
    //     return next_instance_id++;
    // }

    // public String getObjectIdHex(CallContext context) {
    //     String hex = Integer.toHexString(instance_id).toUpperCase();
    //     if (its_me != null) {
    //         System.err.println("Well: " + (this.cache == null ? "null" : this.cache));
    //     }
    //     if (its_me != null && hex.equals("4A6") && its_me != this) {
    //         System.err.println("I'm not myself... :-(");
    //     }
    //     if (its_me == null && hex.equals("4A6")) {
    //         System.err.println("Found Me!");
    //         its_me = this;
    //     }
    //     return hex;
    // }

    protected Execution_BasicSequence (CallContext context, Instruction instruction, boolean initialise, Execution... executions) {
        // this.instance_id = getNextInstanceId();
        this.instruction         = instruction;
        this.expected_executions = -1;
        this.weights             = null;
        this.ignore_idle_skipped = false;
        if (executions != null) {
            for (Execution execution : executions) {
                assert(execution != null);
                this.addExecution(context, execution, false);
            }
        }
        if (initialise) {
            // System.err.println(getObjectIdHex(context) + " constructor (1)");
            this.cacheState(context);
        }
    }

    public Execution_BasicSequence (CallContext context, Instruction instruction, Execution... executions) {
        this(context, instruction, true, executions);
    }

    public Execution_BasicSequence (CallContext context, Instruction instruction, ExecutionCache cache, List<Execution> executions) {
        // this.instance_id = getNextInstanceId();
        this.instruction         = instruction;
        // System.err.println(getObjectIdHex(context) + " constructor (2)");
        this.cache               = cache;
        this.executions          = executions;

        this.closed              = true;

        this.expected_executions = -1;
        this.weights             = null;
        this.ignore_idle_skipped = false;
    }

    protected List<Execution> executions;

    public List<Execution> getExecutions(CallContext context) {
        if (this.executions == null) {
            this.executions = new Vector<Execution>();
        }
        return this.executions;
    }

    protected DataSink<Execution> execution_add_sink;

    public DataSink<Execution> getExecutionAddSink(CallContext context) {
        if (execution_add_sink == null) {
            execution_add_sink = new DataSink<Execution>() {
                public void setObject(CallContext context, Object data) {
                    set(context, (Execution) data);
                }
                public void set(CallContext context, Execution execution) {
                    addExecution(context, execution);
                }
            };
        }
        return execution_add_sink;
    }

    public void addExecution(CallContext context, Execution execution) {
        this.addExecution(context, execution, true, -1);
    }

    public void addExecution(CallContext context, Execution execution, int position) {
        this.addExecution(context, execution, true, position);
    }

    public void addExecution(CallContext context, Execution execution, boolean notify) {
        this.addExecution(context, execution, notify, -1);
    }

    protected void onAdd(CallContext context, Execution execution) {
    }

    public void addExecution(CallContext context, Execution execution, boolean notify, int position) {
        int s;
        if (position == -1 || position == (s = this.getExecutions(context).size())) {
            this.getExecutions(context).add(execution);
        } else if (position < s) {
            this.getExecutions(context).set(position, execution);
        } else {
            while (position > s) {
                this.getExecutions(context).add(Factory_Execution.createExecution(context, "?", ProblemState.IDLE_INCOMPLETE, ProblemCategory.UNKNOWN, null, ActivityState.ATREST, Class_Progression.NO_PROGRESS, null));
            }
            this.getExecutions(context).add(execution);
        }
        this.onAdd(context, execution);
        if (notify && this.hasChanged(context)) {
            this.notifyChanged(context);
        } else {
            // System.err.println(getObjectIdHex(context) + " addExecution -> null");
            this.setCache(context, null);
        }
    }

    public void notifyChanged(CallContext context) {
    }

    protected int expected_executions;

    public int getExpectedExecutions (CallContext context) {
        return this.expected_executions;
    }

    public void setExpectedExecutions (CallContext context, int expected_executions) {
        this.expected_executions = expected_executions;
    }

    protected float[] weights;

    public float[] getWeights (CallContext context) {
        return this.weights;
    }

    public void setWeights (CallContext context, float[] weights) {
        this.weights = weights;
    }

    public void setWeight (CallContext context, float weight, int position) {
        if (this.weights == null) {
            this.weights = new float[position+1];
            for (int i=0; i<position; i++) { this.weights[i] = 1.0F; }
        } else if (this.weights.length <= position) {
            float[] nw = new float[position+1];
            for (int i=0; i<this.weights.length; i++) { nw[i] = this.weights[i]; }
            for (int i=this.weights.length; i<position; i++) { nw[i] = 1.0F; }
            this.weights = nw;
        }
        this.weights[position] = weight;
    }

    protected boolean ignore_idle_skipped;

    public boolean getIgnoreIdleSkipped (CallContext context) {
        return this.ignore_idle_skipped;
    }

    public void setIgnoreIdleSkipped (CallContext context, boolean ignore_idle_skipped) {
        this.ignore_idle_skipped = ignore_idle_skipped;
    }

    static protected class ExecutionCache {
        public ProblemState    problem_state;
        public ProblemCategory problem_category;
        public Problem         problem;
        public ActivityState   activity_state;
        public Progression     progression;
        public Record          record;
    }

    protected volatile ExecutionCache cache;

    protected ExecutionCache getCache(CallContext context) {
        return this.getCache(context, true);
    }

    protected synchronized ExecutionCache getCache(CallContext context, boolean create) {
        if (this.cache == null && create) {
            // System.err.println(getObjectIdHex(context) + " getCache (1) -> " + this.cache);
            // System.err.println(getObjectIdHex(context) + " getCache -> new");
            this.cache = new ExecutionCache();
        }
        // System.err.println(getObjectIdHex(context) + " getCache (2) -> " + this.cache);
        return this.cache;
    }

    protected synchronized void setCache(CallContext context, ExecutionCache ec) {
        this.cache = ec;
        // System.err.println(getObjectIdHex(context) + " setCache -> " + this.cache);
    }

    protected ExecutionCache calculateState(CallContext context) {
        ExecutionCache ec = new ExecutionCache();
        ec.problem_state    = this.getProblemState(context, false);
        ec.problem_category = this.getProblemCategory(context, false);
        // ec.problem          = this.getProblem(context, false);
        ec.activity_state   = this.getActivityState(context, false);
        ec.progression      = this.getProgressionSnapshot(context, false);
        // System.err.println(getObjectIdHex(context) + " calculateState -> " + ec.problem_state);
        // ec.record           = this.getRecord(context, false);
        return ec;
    }

    protected void cacheState(CallContext context) {
        this.setCache(context, calculateState(context));
        // System.err.println(getObjectIdHex(context) + " cacheState -> " + this.cache.problem_state);
    }

    protected boolean hasChanged(CallContext context) {
        ExecutionCache nec = this.calculateState(context);
        ExecutionCache cec = this.getCache(context, false);
        // System.err.println(getObjectIdHex(context) + " hasChanged (1) -> " + nec.problem_state);
        boolean has_changed =
                    (    ( cec == null || cec.problem_state == null ?
                             nec.problem_state == null
                           : cec.problem_state.equals(nec.problem_state)
                         )
                      && ( cec == null || cec.problem_category == null ?
                             nec.problem_category == null
                           : cec.problem_category.equals(nec.problem_category)
                         )
                      && ( cec == null || cec.activity_state == null ?
                             nec.activity_state == null
                           : cec.activity_state.equals(nec.activity_state)
                         )
                      && ( cec == null || cec.progression == null ?
                             nec.progression == null
                           : cec.progression.equals(nec.progression)
                         )
                    ) ? false : true;
        this.setCache(context, nec);
        // System.err.println(getObjectIdHex(context) + " hasChanged (2) -> " + this.cache.problem_state);
        return has_changed;
    }

    protected boolean closed;

    /**
       Indicates that this sequence is processed and
       no further childs will be added via 'addExecution'
    */
    public void close(CallContext context) {
        this.closed = true;

        // so that it is recalculated one more time
        // System.err.println(getObjectIdHex(context) + " close -> " + null);
        this.setCache(context, null);
    }

    protected Instruction instruction;

    public Instruction getInstruction (CallContext context) {
        return this.instruction;
    }

    public ProblemState getProblemState (CallContext context) {
        return this.getProblemState(context, true);
    }

    public ProblemState getProblemState (CallContext context, boolean use_cache) {
        if (use_cache && this.cache != null && this.cache.problem_state != null) {
            // System.err.println(getObjectIdHex(context) + " getProblemState (1) -> " + this.cache.problem_state);
            return this.cache.problem_state;
        }
        ProblemState problem_state = ProblemState.IDLE;
        if (this.closed && (this.executions == null || this.executions.size() == 0)) {
            return ProblemState.OK;
        }
        for (Execution execution : this.getExecutions(context)) {
            problem_state = problem_state.combineWith(context, execution.getProblemState(context));
        }
        if (use_cache) {
            this.getCache(context).problem_state = problem_state;
            // System.err.println(getObjectIdHex(context) + " getProblemState (2) -> " + problem_state);
        }
        return problem_state;
    }

    public ProblemCategory getProblemCategory (CallContext context) {
        return this.getProblemCategory(context, true);
    }

    public ProblemCategory getProblemCategory (CallContext context, boolean use_cache) {
        if (use_cache && this.cache != null && this.cache.problem_category != null) {
            return this.cache.problem_category;
        }
        ProblemCategory problem_category = ProblemCategory.UNKNOWN;
        for (Execution execution : this.executions) {
            problem_category = problem_category.combineWith(context, execution.getProblemCategory(context));
        }
        if (use_cache) {
            this.getCache(context).problem_category = problem_category;
        }
        return problem_category;
    }

    public Problem getProblem (CallContext context) {
        return this.getProblem(context, true);
    }

    public Problem getProblem (CallContext context, boolean use_cache) {
        if (use_cache && this.cache != null && this.cache.problem != null) {
            return this.cache.problem;
        }
        Problem[] problems = new Problem[this.getExecutions(context).size()];
        boolean got_one = false;
        int i=0;
        for (Execution execution : this.getExecutions(context)) {
            if ((problems[i++] = execution.getProblem(context)) != null) {
                got_one = true;
            }
        }
        Problem problem = got_one ? new ProblemGroup(context, problems) : null;
        if (use_cache) {
            this.getCache(context).problem = (problem != null ? problem : ProblemEmpty.get(context));
        }
        return problem;
    }

    public ActivityState getActivityState (CallContext context) {
        return this.getActivityState(context, true);
    }

    public ActivityState getActivityState (CallContext context, boolean use_cache) {
        if (use_cache && this.cache != null && this.cache.activity_state != null) {
            return this.cache.activity_state;
        }
        ActivityState activity_state;
        if (this.getExecutions(context).size() == 0) {
            activity_state = (this.closed || this.expected_executions ==  0) ? ActivityState.SKIPPED /* 05/24 - was: ActivityState.COMPLETED */
                                           : this.expected_executions == -1  ? ActivityState.UNREADY : ActivityState.INPROGRESS;
        } else {
            activity_state = this.expected_executions == -1 || this.getExecutions(context).size() >= this.expected_executions ? ActivityState.COMPLETED : ActivityState.INPROGRESS;
            for (Execution execution : this.getExecutions(context)) {
                activity_state = activity_state.combineWith(context, execution.getActivityState(context));
            }
        }
        if (use_cache) {
            this.getCache(context).activity_state = activity_state;
        }
        return activity_state;
    }

    public Progression getProgression (CallContext context) {
        return this.getProgression(context, true);
    }

    static public boolean dumpcalc = false;

    public Progression getProgression (CallContext context, boolean use_cache) {
        if (use_cache && this.cache != null && this.cache.progression != null) {
            return this.cache.progression;
        }

        List<Execution> es = this.getExecutions(context);

        int cursize = es.size();
        int expsize = this.expected_executions;

        if (dumpcalc) { SystemContext.err.println(context, Integer.toHexString(System.identityHashCode(this)).toUpperCase() + " ES - getProgression, UC: " + use_cache + " ..."); }
        if (dumpcalc) { SystemContext.err.println(context, "#E: " + cursize + ", expected: " + expsize + ", closed: " + this.closed); }

        Progression progression = null;
        if (    cursize == 0
             && (this.closed || expsize ==  0)) {
            progression = Class_Progression.SKIPPED;
        } else {
            int maxsize = (cursize > expsize ? cursize : expsize);
            Progression[] progressions = new Progression[maxsize];
            boolean got_one = false;
            int i=0;
            for (Execution execution : es) {
                if ((progressions[i] = execution.getProgression(context)) != null) {
                    if (dumpcalc) { SystemContext.err.println(context, "got #" + i); }
                    got_one = true;
                } else {
                    if (dumpcalc) { SystemContext.err.println(context, "filling #" + i); }
                    progressions[i] = Class_Progression.NO_PROGRESS;
                }
                i++;
            }
            if (cursize < expsize) {
                for (int e=cursize; e<expsize; e++) {
                    if (dumpcalc) { SystemContext.err.println(context, "filling #" + i); }
                    progressions[i++] = Class_Progression.NO_PROGRESS;
                }
            }

            float[] adjusted_weights = null;

            // align weights so that skipped or completed idle steps do not count
            if (ignore_idle_skipped) {
                if (es != null && cursize != 0) {
                    for (int ei=0; ei<cursize; ei++) {
                        Execution     e  = es.get(ei);
                        ActivityState as = e.getActivityState(context);
                        ProblemState  ps = e.getProblemState(context);
                        if (     as == ActivityState.SKIPPED
                             || (    (    as == ActivityState.COMPLETED
                                       || as == ActivityState.VERIFIED)
                                  && ps == ProblemState.IDLE)) {
                            if (adjusted_weights == null) {
                                if (cursize > this.weights.length) {
                                    adjusted_weights = new float[cursize];
                                    for (int wi=0; wi<this.weights.length; wi++) { adjusted_weights[wi] = this.weights[wi]; }
                                    for (int wi=this.weights.length; wi<cursize; wi++) { adjusted_weights[wi] = 1.0F; }
                                } else {
                                    adjusted_weights = new float[this.weights.length];
                                    for (int wi=0; wi<this.weights.length; wi++) { adjusted_weights[wi] = this.weights[wi]; }
                                }
                            }
                            if (dumpcalc) { SystemContext.err.println(context, "ignoring #" + ei); }
                            adjusted_weights[ei] = 0.0F;
                        }
                    }
                }
            }

            if (adjusted_weights == null) {
                adjusted_weights = this.weights;
            }

            float[] normalised_weights = adjusted_weights;
            if (adjusted_weights != null && adjusted_weights.length > 0) {
                float weights_sum = 0.0F;
                for (float weight : adjusted_weights) { weights_sum += weight; }
                if (weights_sum != 0.0F && weights_sum != 1.0F) {
                    normalised_weights = new float[adjusted_weights.length];
                    for (int ei=0; ei<this.weights.length; ei++) { normalised_weights[ei] = adjusted_weights[ei] /= weights_sum; }
                }
            }

            progression = (got_one || this.expected_executions >= 0) ? new ProgressionGroup(context, normalised_weights, progressions) : null;
        }
        if (use_cache && progression != null) {
            if (dumpcalc) { SystemContext.err.println(context, "caching..."); }
            progression = progression.getSnapshot(context);
            this.getCache(context).progression = progression;
        }
        if (dumpcalc) { SystemContext.err.println(context, "ES - getProgression - done."); }
        return progression;
    }

    public Progression getProgressionSnapshot (CallContext context, boolean use_cache) {
        Progression p = this.getProgression(context, use_cache);
        return (p != null ? p.getSnapshot(context) : null);
    }

    public Record getRecord (CallContext context) {
        return this.getRecord(context, true);
    }

    public Record getRecord (CallContext context, boolean use_cache) {
        if (use_cache && this.cache != null && this.cache.record != null) {
            return this.cache.record;
        }
        Record[] records = new Record[this.getExecutions(context).size() + (this.record != null ? 1 : 0)];
        boolean got_one = false;
        int i=0;
        if (this.record != null) {
            if ((records[i++] = this.record) != null) {
                got_one = true;
            }
        }
        for (Execution execution : this.getExecutions(context)) {
            if ((records[i++] = execution.getRecord(context)) != null) {
                got_one = true;
            }
        }
        Record record = got_one ? new RecordGroup(context, records) : null;
        if (use_cache) {
            this.getCache(context).record = (record != null ? record : RecordEmpty.get(context));
        }
        return record;
    }

    protected Record record;

    public void setRecord (CallContext context, Record record) {
        this.record = record;
    }

    public Record defaultRecord (CallContext context) {
        return null;
    }

    /* workaround: disabled for performance reasons, since UI dumped whole tree
       not dynamically; possibly reenable as well as annotate all other attribute
       like in Class_Execution
       @UIAttribute(Name="Performance",Value="js:var value = instance.getPerformance(context); Packages.com.sphenon.basics.debug.Dumper.dumpToString(context, null, value == null ? '' : value)",Classifier="Performance")
    */
    public Performance getPerformance (CallContext context) {
        return this.getPerformance(context, true);
    }

    public Performance getPerformance (CallContext context, boolean use_cache) {
        if (this.getExecutions(context).size() == 0) {
            return null;
        }
        Performance performance = null; // ...
        // for (Execution execution : this.getExecutions(context)) {
        //     performance = performance.add(context, execution.getPerformance(context));
        // }
        return performance;
    }

    protected Object result;

    public Object getResult (CallContext context) {
        return this.result;
    }

    public Object defaultResult (CallContext context) {
        return null;
    }

    public void setResult (CallContext context, Object result) {
        this.result = result;
    }

    public Execution wait (CallContext context) {
        for (Execution execution : this.getExecutions(context)) {
            execution.wait(context);
        }
        return this;
    }

    public String toString(CallContext context) {
        ProblemState  problem_state  = getProblemState(context);
        Problem       problem        = getProblem(context);
        ActivityState activity_state = getActivityState(context);
        Progression   progression    = getProgression(context);
        Record        record         = getRecord(context);
        Instruction   instruction    = getInstruction(context);

        // System.err.println(getObjectIdHex(context) + " toString -> " + problem_state);
        return problem_state + "/" + activity_state + (progression != null ? ("/" + ContextAware.ToString.convert(context, progression)) : "") + (instruction != null ? ("/" + ContextAware.ToString.convert(context, instruction)) : "") + (record != null ? ("/" + ContextAware.ToString.convert(context, record)) : "");
    }

    public String toString() {
        return toString(RootContext.getFallbackCallContext());
    }

    public String toMessage() {
        return toString();
    }

    public void dump(CallContext context, DumpNode dump_node) {
        ProblemState  problem_state  = getProblemState(context);
        Problem       problem        = getProblem(context);
        ActivityState activity_state = getActivityState(context);
        Progression   progression    = getProgression(context);
        Record        record         = getRecord(context);

        // System.err.println(getObjectIdHex(context) + " dump -> " + problem_state);
        dump_node.dump(context, "Execution    ", problem_state + "/" + activity_state);
        if (progression != null) {
            dump_node.dump(context, "- Progression", progression);
        }
        if (this.getProblemState(context) != null && this.getProblemState(context).isOk(context) == false) {
            if (problem != null) {
                dump_node.dump(context, "- Problem    ", problem);
            }
            if (record != null) {
                dump_node.dump(context, "- Record     ", record);
            }
            DumpNode dn = dump_node.openDump(context, "- Executions ");
            int i=1;
            for (Execution execution : this.getExecutions(context)) {
                dn.dump(context, (new Integer(i++)).toString(), execution);
            }
            dn.close(context);
        }
    }

    public void finish(CallContext context) {
    }
}
