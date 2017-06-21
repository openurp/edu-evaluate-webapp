package org.openurp.edu.evaluation.student.service

import org.springframework.beans.factory.config.PropertiesFactoryBean
import org.beangle.cdi.bind.profile
import org.beangle.cdi.bind.BindModule

@profile("dev")
object DevModule extends BindModule {
  protected override def binding(): Unit = {
    bind("HibernateConfig.default", classOf[PropertiesFactoryBean]).property(
      "properties",
      props(
        "hibernate.max_fetch_depth=1", "hibernate.default_batch_fetch_size=500",
        "hibernate.jdbc.fetch_size=8", "hibernate.jdbc.batch_size=20",
        "hibernate.jdbc.batch_versioned_data=true", "hibernate.jdbc.use_streams_for_binary=true",
        "hibernate.jdbc.use_get_generated_keys=true",
        "hibernate.cache.region.factory_class=org.hibernate.cache.EhCacheRegionFactory",
        "hibernate.cache.use_second_level_cache=true", "hibernate.cache.use_query_cache=true",
        "hibernate.query.substitutions=true 1, false 0, yes 'Y', no 'N'", "hibernate.show_sql=true"))
      .description("Hibernate配置信息").nowire("propertiesArray")
  }
}
