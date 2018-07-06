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

    Intended for process tracking purposes. Provides information about current
    position of control flow, intermediate results and beginning and finishing
    of processing steps.

    In contrast to
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/message/DigestMessage">DigestMessages</link>,
    a tracking message always refers to the actual processing, i.e. the
    process or a thread.

    To report processing problems (warnings, errors), process success and
    process measurands, please use
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/message/SystemStateMessage">SystemStateMessages</link>
    and
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/monitoring/MeterMessage">MeterMessages</link>.
*/
public class TraceMessage extends SystemMessage {
    static public boolean prepend_depth_number = false;
    static public boolean prepend_depth_whitespace = false;
    static public boolean prepend_caller_info = false;
    static public String  stack_trace_reg_exp = null;

    protected String              caller_info_class;
    protected String              caller_info_method;
    protected String              caller_info_file;
    protected int                 caller_info_line;
    protected int                 caller_info_depth;

    protected TraceMessage (CallContext cc, MessageText text, int stack_frame_offset) {
        super(cc, text);
        if (prepend_depth_number || prepend_depth_whitespace || prepend_caller_info) {
            StackTraceElement[] st  = new Throwable().getStackTrace();
            if (prepend_caller_info) {
                StackTraceElement   ste = st[stack_frame_offset+1];
                this.caller_info_class  = ste.getClassName();
                this.caller_info_method = ste.getMethodName();
                this.caller_info_file   = ste.getFileName();
                this.caller_info_line   = ste.getLineNumber();
            }
            this.caller_info_depth  = st.length;
        }
    }

    static public Message create (CallContext cc, MessageText text) {
        return new TraceMessage(cc, text, 1);
    }

    static public Message create (CallContext cc, MessageText text, int stack_frame_offset) {
        return new TraceMessage(cc, text, stack_frame_offset + 1);
    }

    protected String getIndent (int width) {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<width; i++) { sb.append(' '); }
        return new String(sb);
    }

    public String toString(CallContext cc, AttachmentHandler attachment_handler) {
        String msg = (prepend_depth_number ? ("[" + this.caller_info_depth + "] ") : "") + (prepend_depth_whitespace ? getIndent(this.caller_info_depth) : "") + (prepend_caller_info ? (this.caller_info_class + "." + this.caller_info_method + " (" + this.caller_info_file + ":" + this.caller_info_line + ") ") : "") + this.text.getText(cc, attachment_handler);
        if (stack_trace_reg_exp != null) {
            if (msg.matches(stack_trace_reg_exp)) {
                (new Throwable()).printStackTrace();
            }
        }
        return msg;
    }
}
