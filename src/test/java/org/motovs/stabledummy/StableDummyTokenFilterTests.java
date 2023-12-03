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

import org.elasticsearch.cluster.metadata.IndexMetadata;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.env.TestEnvironment;
import org.elasticsearch.index.IndexService;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.IndexVersion;
import org.elasticsearch.index.analysis.AnalysisRegistry;
import org.elasticsearch.index.analysis.IndexAnalyzers;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugin.analysis.TokenFilterFactory;
import org.elasticsearch.plugins.scanners.NameToPluginInfo;
import org.elasticsearch.plugins.scanners.NamedComponentReader;
import org.elasticsearch.plugins.scanners.PluginInfo;
import org.elasticsearch.plugins.scanners.StablePluginsRegistry;
import org.elasticsearch.test.ESTestCase;
import org.elasticsearch.test.IndexSettingsModule;
import org.elasticsearch.test.index.IndexVersionUtils;

import java.io.IOException;
import java.util.Map;

import static java.util.Collections.emptyList;
import static org.apache.lucene.tests.analysis.BaseTokenStreamTestCase.assertTokenStreamContents;

public class StableDummyTokenFilterTests extends ESTestCase {
    private final Settings emptyNodeSettings = Settings.builder()
        .put(Environment.PATH_HOME_SETTING.getKey(), createTempDir().toString())
        .build();

    public IndexAnalyzers getIndexAnalyzers(Settings settings) throws IOException {
        AnalysisRegistry registry = setupRegistry();

        IndexSettings idxSettings = IndexSettingsModule.newIndexSettings("test", settings);
        return registry.build(IndexService.IndexCreationContext.CREATE_INDEX, idxSettings);
    }

    public void testBasicUsage() throws Exception {
        IndexVersion version = IndexVersionUtils.randomVersion(random());
        IndexAnalyzers analyzers = getIndexAnalyzers(
            Settings.builder()
                .put("index.analysis.analyzer.token_filter_test.tokenizer", "standard")
                .put("index.analysis.analyzer.token_filter_test.filter", "test_filter")
                .put("index.analysis.filter.test_filter.type", "stabledummy")
                .put("index.analysis.filter.test_filter.size", 5)
                .put(IndexMetadata.SETTING_VERSION_CREATED, version.id())
                .build()
        );
        assertTokenStreamContents(
            analyzers.get("token_filter_test").tokenStream("", "1 12 123 1234 12345 123456"),
            new String[] { "12345" }
        );
    }

    private AnalysisRegistry setupRegistry() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();

        AnalysisRegistry registry = new AnalysisModule(
            TestEnvironment.newEnvironment(emptyNodeSettings),
            emptyList(),
            new StablePluginsRegistry(
                new NamedComponentReader(),
                Map.of(
                    TokenFilterFactory.class.getCanonicalName(),
                    new NameToPluginInfo(
                        Map.of("stabledummy", new PluginInfo("stabledummy", StableDummyTokenFilterFactory.class.getName(), classLoader))
                    )
                )
            )
        ).getAnalysisRegistry();
        return registry;
    }
}
