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

/** {@EntitySecurityClass User}

    @doclet {@Category Definition} {@SecurityClass User} {@Maturity Final}

    Provides information about relevant parts of the system, as demanded by
    current scope of interest (processing) or explicit request.

    In contrast to
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/message/TraceMessage">TraceMessages</link>,
    a digest message always refers to some kind of system entity (an object, a
    package, a subsystem).

    To report classified system problems (warnings, malfunctioning) please use
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/message/SystemStateMessage">SystemStateMessages</link>.
*/
public class DigestMessage extends SystemMessage {
    protected DigestMessage (CallContext cc, MessageText text) {
        super(cc, text);
    }

    static public Message create (CallContext cc, MessageText text) {
        return new DigestMessage(cc, text);
    }
}
