package com.sphenon.basics.operations.factories;

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
import com.sphenon.basics.message.*;
import com.sphenon.basics.monitoring.*;
import com.sphenon.basics.processing.*;
import com.sphenon.basics.processing.classes.*;

import com.sphenon.basics.function.*;
import com.sphenon.ui.annotations.*;

import com.sphenon.basics.operations.*;
import com.sphenon.basics.operations.classes.*;

public class Factory_Execution {

    static public class ExecutionCreator {
        public Execution create(CallContext context, Instruction instruction, ProblemState problem_state, ProblemCategory problem_category, Problem problem, ActivityState activity_state, Progression progression, Record record, Object result, Getter result_getter) {
            return new Execution_Basic(context, instruction, problem_state, problem_category, problem, activity_state, progression, record, result, result_getter);
        }

        public Execution_BasicSequence create(CallContext context, Instruction instruction, Execution... executions) {
            return new Execution_BasicSequence(context, instruction, executions);
        }
    }

    static public ExecutionCreator creator = new ExecutionCreator();

    static public Execution createExecutionSuccess(CallContext context) {
        return creator.create(context, null, ProblemState.OK, ProblemCategory.OK, (Problem) null, ActivityState.COMPLETED, Class_Progression.COMPLETED, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecutionSuccess(CallContext context, String instruction_description) {
        return creator.create(context, new Class_Instruction(context, instruction_description), ProblemState.OK, ProblemCategory.OK, (Problem) null, ActivityState.COMPLETED, Class_Progression.COMPLETED, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecutionSuccess(CallContext context, String instruction_description, Object result) {
        return creator.create(context, new Class_Instruction(context, instruction_description), ProblemState.OK, ProblemCategory.OK, (Problem) null, ActivityState.COMPLETED, Class_Progression.COMPLETED, (Record) null, result, (Getter) null);
    }

    static public Execution createExecutionSuccess(CallContext context, String instruction_description, Getter result_getter) {
        return creator.create(context, new Class_Instruction(context, instruction_description), ProblemState.OK, ProblemCategory.OK, (Problem) null, ActivityState.COMPLETED, Class_Progression.COMPLETED, (Record) null, (Object) null, result_getter);
    }

    static public Execution createExecutionFailure(CallContext context, Throwable exception) {
        return creator.create(context, null, ProblemState.ERROR, ProblemCategory.EXECUTION_FAILURE, new ProblemException(context, exception), ActivityState.ABORTED, (Progression) null, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecutionFailure(CallContext context, ProblemState problem_state, Problem problem) {
        return creator.create(context, null, problem_state, ProblemCategory.UNKNOWN, problem, ActivityState.ABORTED, (Progression) null, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecutionFailure(CallContext context, String problem_message) {
        return creator.create(context, null, ProblemState.ERROR, ProblemCategory.UNKNOWN, new ProblemMessage(context, problem_message), ActivityState.ABORTED, (Progression) null, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecutionFailure(CallContext context, MessageText problem_message) {
        return creator.create(context, null, ProblemState.ERROR, ProblemCategory.UNKNOWN, new ProblemMessageText(context, problem_message), ActivityState.ABORTED, (Progression) null, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecutionFailure(CallContext context, Throwable exception, String problem_message) {
        return creator.create(context, null, ProblemState.ERROR, ProblemCategory.UNKNOWN, new ProblemException(context, exception, problem_message), ActivityState.ABORTED, (Progression) null, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecutionFailure(CallContext context, Throwable exception, MessageText problem_message) {
        return creator.create(context, null, ProblemState.ERROR, ProblemCategory.UNKNOWN, new ProblemExceptionMessageText(context, exception, problem_message), ActivityState.ABORTED, (Progression) null, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecutionFailure(CallContext context, String instruction_description, Throwable exception) {
        return creator.create(context, new Class_Instruction(context, instruction_description), ProblemState.ERROR, ProblemCategory.EXECUTION_FAILURE, new ProblemException(context, exception), ActivityState.ABORTED, (Progression) null, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecutionFailure(CallContext context, ProblemCategory problem_category, Throwable exception) {
        return creator.create(context, null, ProblemState.ERROR, problem_category, new ProblemException(context, exception), ActivityState.ABORTED, (Progression) null, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecutionFailure(CallContext context, ProblemCategory problem_category, String problem_message) {
        return creator.create(context, null, ProblemState.ERROR, problem_category, new ProblemMessage(context, problem_message), ActivityState.ABORTED, (Progression) null, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecutionFailure(CallContext context, ProblemCategory problem_category, MessageText problem_message) {
        return creator.create(context, null, ProblemState.ERROR, problem_category, new ProblemMessageText(context, problem_message), ActivityState.ABORTED, (Progression) null, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecutionFailure(CallContext context, ProblemCategory problem_category, Throwable exception, String problem_message) {
        return creator.create(context, null, ProblemState.ERROR, problem_category, new ProblemException(context, exception, problem_message), ActivityState.ABORTED, (Progression) null, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecutionFailure(CallContext context, ProblemCategory problem_category, Throwable exception, MessageText problem_message) {
        return creator.create(context, null, ProblemState.ERROR, problem_category, new ProblemExceptionMessageText(context, exception, problem_message), ActivityState.ABORTED, (Progression) null, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecutionFailure(CallContext context, ProblemState problem_state, ProblemCategory problem_category, Problem problem) {
        return creator.create(context, null, problem_state, problem_category, problem, ActivityState.ABORTED, (Progression) null, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecutionFailure(CallContext context, ProblemCategory problem_category, String instruction_description, Throwable exception) {
        return creator.create(context, new Class_Instruction(context, instruction_description), ProblemState.ERROR, problem_category, new ProblemException(context, exception), ActivityState.ABORTED, (Progression) null, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecutionSkipped(CallContext context) {
        return creator.create(context, null, ProblemState.IDLE_INCOMPLETE, ProblemCategory.OK, (Problem) null, ActivityState.SKIPPED, Class_Progression.SKIPPED, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecutionSkipped(CallContext context, String instruction_description) {
        return creator.create(context, new Class_Instruction(context, instruction_description), ProblemState.IDLE_INCOMPLETE, ProblemCategory.OK, (Problem) null, ActivityState.SKIPPED, Class_Progression.SKIPPED, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecutionSkipped(CallContext context, ProblemCategory problem_category) {
        return creator.create(context, null, ProblemState.IDLE_INCOMPLETE, problem_category, (Problem) null, ActivityState.SKIPPED, Class_Progression.SKIPPED, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecutionSkipped(CallContext context, ProblemCategory problem_category, String instruction_description) {
        return creator.create(context, new Class_Instruction(context, instruction_description), ProblemState.IDLE_INCOMPLETE, problem_category, (Problem) null, ActivityState.SKIPPED, Class_Progression.SKIPPED, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecutionInProgress(CallContext context) {
        return creator.create(context, null, ProblemState.IDLE_INCOMPLETE, ProblemCategory.OK, (Problem) null, ActivityState.INPROGRESS, Class_Progression.NO_PROGRESS, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecutionInProgress(CallContext context, String instruction_description) {
        return creator.create(context, new Class_Instruction(context, instruction_description), ProblemState.IDLE_INCOMPLETE, ProblemCategory.OK, (Problem) null, ActivityState.INPROGRESS, Class_Progression.NO_PROGRESS, (Record) null, (Object) null, (Getter) null);
    }

    static public Execution createExecution(CallContext context, Instruction instruction, ProblemState problem_state, Problem problem, ActivityState activity_state, Progression progression, Record record) {
        return creator.create(context, instruction, problem_state, ProblemCategory.UNKNOWN, problem, activity_state, progression, record, (Object) null, (Getter) null);
    }

    static public Execution createExecution(CallContext context, String instruction_description, ProblemState problem_state, Problem problem, ActivityState activity_state, Progression progression, Record record) {
        return creator.create(context, new Class_Instruction(context, instruction_description), problem_state, ProblemCategory.UNKNOWN, problem, activity_state, progression, record, (Object) null, (Getter) null);
    }

    static public Execution createExecution(CallContext context, Instruction instruction, ProblemState problem_state, ProblemCategory problem_category, Problem problem, ActivityState activity_state, Progression progression, Record record) {
        return creator.create(context, instruction, problem_state, problem_category, problem, activity_state, progression, record, (Object) null, (Getter) null);
    }

    static public Execution createExecution(CallContext context, String instruction_description, ProblemState problem_state, ProblemCategory problem_category, Problem problem, ActivityState activity_state, Progression progression, Record record) {
        return creator.create(context, new Class_Instruction(context, instruction_description), problem_state, problem_category, problem, activity_state, progression, record, (Object) null, (Getter) null);
    }

    static public Execution_BasicSequence createExecutionSequence(CallContext context, Instruction instruction, Execution... executions) {
        return creator.create(context, instruction, executions);
    }

    static public Execution_BasicSequence createExecutionSequence(CallContext context, String instruction_description, Execution... executions) {
        return creator.create(context, new Class_Instruction(context, instruction_description), executions);
    }

}
