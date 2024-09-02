package com.haratres.ecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


@Configuration
@EnableElasticsearchRepositories(basePackages = "com.haratres.ecommerce.elasticRepository")
public class ElasticSearchConfig {
}
