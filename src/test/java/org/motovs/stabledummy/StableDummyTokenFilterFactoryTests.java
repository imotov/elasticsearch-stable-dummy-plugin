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

import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class StableDummyTokenFilterFactoryTests {

    @Test
    public void exampleCharFilterIsAnnotatedWithName() {
        StableDummySettings settings = Mockito.mock(StableDummySettings.class);
        Mockito.when(settings.size()).thenReturn(4);
        StableDummyTokenFilterFactory factory = new StableDummyTokenFilterFactory(settings);
        assertThat(factory.name(), equalTo("stabledummy"));
    }
}
