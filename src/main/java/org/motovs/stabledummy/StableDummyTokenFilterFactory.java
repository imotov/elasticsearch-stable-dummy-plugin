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

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.plugin.Inject;
import org.elasticsearch.plugin.NamedComponent;
import org.elasticsearch.plugin.analysis.TokenFilterFactory;

@NamedComponent(value = "stabledummy")
public class StableDummyTokenFilterFactory implements TokenFilterFactory {
    private final StableDummySettings settings;

    @Inject
    public StableDummyTokenFilterFactory(StableDummySettings settings) {
        this.settings = settings;
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
        return new StableDummyTokenFilter(tokenStream, settings.size());
    }

}
