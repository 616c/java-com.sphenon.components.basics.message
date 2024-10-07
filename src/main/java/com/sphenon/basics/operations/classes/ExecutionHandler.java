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
import com.sphenon.basics.exception.*;
import com.sphenon.basics.function.*;
import com.sphenon.basics.monitoring.*;
import com.sphenon.basics.processing.*;
import com.sphenon.basics.processing.classes.*;
import com.sphenon.basics.data.*;

import com.sphenon.ui.annotations.*;

import com.sphenon.basics.operations.*;
import com.sphenon.basics.operations.classes.*;
import com.sphenon.basics.operations.factories.*;

import java.util.Set;
import java.util.HashSet;

// [Issue: JavaMethodInterface - ModelOverviewAndRefactoring.txt,Operation.java,ExecutionHandler.java,ExpressionEvaluator_EMOSUI.java]
// here: this is good material to be integrated into such a new interface

public class ExecutionHandler {

    protected DataSink<Execution>       execution_sink;
    protected Execution_BasicSequence   execution_sequence;
    protected Execution                 first_execution;
    protected Set<Throwable>            reported_exceptions;
    protected DataSink<Execution>       appending_sink;
    protected boolean                   always_use_sequence;
    protected boolean                   ignore_idle_skipped;
    protected Execution                 main_execution;

    public ExecutionHandler(CallContext context, DataSink<Execution> execution_sink) {
        this(context, execution_sink, null, true, false);
    }

    public ExecutionHandler(CallContext context, DataSink<Execution> execution_sink, String instruction) {
        this(context, execution_sink, instruction, true, false);
    }

    public ExecutionHandler(CallContext context, DataSink<Execution> execution_sink, boolean always_use_sequence) {
        this(context, execution_sink, null, always_use_sequence, false);
    }

    public ExecutionHandler(CallContext context, DataSink<Execution> execution_sink, String instruction, boolean always_use_sequence) {
        this(context, execution_sink, instruction, always_use_sequence, false);
    }

    public ExecutionHandler(CallContext context, DataSink<Execution> execution_sink, String instruction, boolean always_use_sequence, boolean ignore_idle_skipped) {
        this.execution_sink      = execution_sink;
        this.always_use_sequence = always_use_sequence;
        this.ignore_idle_skipped = ignore_idle_skipped;
        this.first_execution     = null;
        this.main_execution      = null;
        this.reported_exceptions = null;
        if (this.execution_sink != null && this.always_use_sequence) {
            this.execution_sequence = Factory_Execution.createExecutionSequence(context, instruction == null ? (Instruction) null : new Class_Instruction(context, instruction));
            this.execution_sequence.setIgnoreIdleSkipped(context, ignore_idle_skipped);
            this.execution_sink.set(context, this.main_execution = this.execution_sequence);
        }
    }

    protected boolean report(CallContext context, Execution execution, int position) {
        if (this.execution_sink != null) {
            // if we need a sequence but do not have one already
            if (this.first_execution != null && this.execution_sequence == null) {
                if (position != 0) {
                    this.execution_sequence = Factory_Execution.createExecutionSequence(context, (Instruction) null);
                    this.execution_sequence.setIgnoreIdleSkipped(context, ignore_idle_skipped);
                    this.execution_sequence.addExecution(context, this.first_execution);
                    this.execution_sink.set(context, this.main_execution = this.execution_sequence);
                    this.first_execution = null;
                }
            }

            if (this.execution_sequence != null && (this.always_use_sequence || position != 0)) {
                this.execution_sequence.addExecution(context, execution, position);
            } else {
                this.execution_sink.set(context, this.main_execution = execution);
                this.first_execution = execution;
            }
            return true;
        }
        return false;
    }

    protected void report(CallContext context, Throwable t, int position) {
        if (this.reported_exceptions == null || this.reported_exceptions.contains(t) == false) {
            if (this.report(context, Factory_Execution.createExecutionFailure(context, t), position)) {
                this.markAsReported(context, t);
            }
        }
    }

    protected void markAsReported(CallContext context, Throwable t) {
        if (this.reported_exceptions == null) {
            this.reported_exceptions = new HashSet<Throwable>();
        }
        this.reported_exceptions.add(t);
    }

    public<T extends Throwable> void reportAndThrow(CallContext context, T t) throws T {
        this.report(context, t, -1);
        throw t;
    }

    public<T extends Throwable> void throwButDoNotReport(CallContext context, T t) throws T {
        this.markAsReported(context, t);
        throw t;
    }

    public void reportSuccess(CallContext context) {
        this.report(context, Factory_Execution.createExecutionSuccess(context), -1);
    }

    public void reportSuccess(CallContext context, String instruction_description) {
        this.report(context, Factory_Execution.createExecutionSuccess(context, instruction_description), -1);
    }

    public void reportExecution(CallContext context, Execution execution) {
        this.report(context, execution, -1);
    }

    public Execution addSuccess(CallContext context, String instruction_description) {
        Execution execution = Factory_Execution.createExecutionSuccess(context, instruction_description);
        return add(context, execution);
    }

    public Execution add(CallContext context, Execution execution) {
        this.report(context, execution, -1);
        return execution;
    }

    public<T extends Throwable> T add(CallContext context, T t) {
        this.report(context, t, -1);
        return t;
    }

    public<T extends Throwable> void handleFinally(CallContext context, T t) throws T {
        this.report(context, t, -1);
        throw t;
    }

    public Execution getMainExecution(CallContext context) {
        return this.main_execution;
    }

    public Execution_BasicSequence getSequence(CallContext context) {
        return this.execution_sequence;
    }

    public class ReportingSink extends DataSinkBase<Execution> {
        protected int       position;
        protected boolean   ok;
        protected Execution cause;
        protected Hook1<ReportingSink> on_set;
        public ReportingSink(CallContext context, int position) {
            this(context, position, null);
        }
        public ReportingSink(CallContext context, int position, Hook1<ReportingSink> on_set) {
            this.position = position;
            this.ok       = true;
            this.on_set   = on_set;
        }
        public void set(CallContext context, Execution e) {
            report(context, e, position);
            if (    e.getProblemState(context) == null
                 || e.getProblemState(context).isOk(context) == false
               ) {
                this.ok = false;
                this.cause = e;
            }
            if (this.on_set != null) { this.on_set.callback(context, this); }
        }
        public boolean isOk(CallContext context) {
            return this.ok;
        }
        public Execution getCause(CallContext context) {
            return this.cause;
        }
        public int getPosition(CallContext context) {
            return this.position;
        }
    }

    public ReportingSink createReportingSink(CallContext context) {
        return createReportingSink(context, false, null);
    }

    public ReportingSink createReportingSink(CallContext context, boolean unconditionally) {
        return createReportingSink(context, unconditionally, null);
    }

    public ReportingSink createReportingSink(CallContext context, Hook1<ReportingSink> on_set) {
        return createReportingSink(context, false, on_set);
    }

    public ReportingSink createReportingSink(CallContext context, boolean unconditionally, Hook1<ReportingSink> on_set) {
        if (unconditionally || this.execution_sink != null) {
            int position = this.execution_sequence != null ?
                              this.execution_sequence.getExecutions(context).size()
                            : this.first_execution != null ? 1 : 0;
            return new ReportingSink(context, position, on_set);
        }
        return null;
    }
}
