package com.sphenon.basics.message;

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

/** {@EntitySecurityClass User}

    @doclet {@Category Definition} {@SecurityClass User} {@Maturity Final}

    A message providing details to some entity it is accompanying. Typical use
    is along with 
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/exception/ReturnCode">ReturnCodes</link>:
    the accompanying 
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/message/DetailMessage">DetailMessage</link>
    describes in detail why the respective return code resulted.

    Since the occurance (technically: throwing) of a
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/exception/ReturnCode">ReturnCode</link>
    is primarily a normal processing event, used for internal communication,
    and primarily not an abnormal condition, no
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/message/SystemStateMessage">SystemStateMessage</link>
    shall be used.

    Of course, after receiving a
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/exception/ReturnCode">ReturnCode</link>
    (technically: catching it), the application may decide that this is in
    fact a problem and in turn send a
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/message/SystemStateMessage">SystemStateMessage</link>
    to report the problematic situation. This 
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/message/SystemStateMessage">SystemStateMessage</link>
    can be accompanied with the received
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/message/DetailMessage">DetailMessage</link>
    to provide the details.
*/
public class DetailMessage extends SystemMessage {
    protected DetailMessage (CallContext cc, MessageText text) {
        super(cc, text);
    }

    static public Message create (CallContext cc, MessageText text) {
        return new DetailMessage(cc, text);
    }
}

