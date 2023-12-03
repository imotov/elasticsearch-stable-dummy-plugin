/*
 * Copyright 2023 Igor Motov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.motovs.stabledummy;

import org.apache.lucene.analysis.FilteringTokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class StableDummyTokenFilter extends FilteringTokenFilter {
    private final CharTermAttribute term = addAttribute(CharTermAttribute.class);

    private final int size;

    public StableDummyTokenFilter(TokenStream input, int size) {
        super(input);
        this.size = size;
    }

    @Override
    public boolean accept() {
        return term.length() == size;
    }
}
