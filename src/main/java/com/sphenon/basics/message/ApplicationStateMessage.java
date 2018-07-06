package com.sphenon.basics.message;

/****************************************************************************
  Copyright 2001-2018 Sphenon GmbH

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
import com.sphenon.basics.monitoring.*;

/** {@EntitySecurityClass User}

    @doclet {@Category Definition} {@SecurityClass User} {@Maturity Final}

    Messages related to the application domain, classified by problem
    categories (<quote>ok</quote>, <quote>warning</quote>,
    <quote>error</quote>). See
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/monitoring/ProblemState">ProblemState</link>
    for details.
*/
public class ApplicationStateMessage extends ApplicationMessage implements StateMessage {
    protected ProblemState problem_state;

    protected ApplicationStateMessage (CallContext cc, MessageText text, ProblemState problem_state) {
        super(cc, text);
        this.problem_state = problem_state;
        text.setAttribute("problem_state", problem_state);
    }

    static public ApplicationStateMessage create (CallContext cc, MessageText text, ProblemState problem_state) {
        return new ApplicationStateMessage(cc, text, problem_state);
    }

    public ProblemState getProblemState (CallContext cc) {
        return this.problem_state;
    }

    public String toString() {
        return this.problem_state.toString() + ": " + super.toString();
    }

    public String toString(CallContext cc) {
        return this.problem_state.toString() + ": " + super.toString(cc);
    }
}

