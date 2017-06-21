package org.openurp.edu.evaluation.app.department.model

import scala.reflect.runtime.universe
import org.beangle.data.orm.MappingModule

/**
 * @author xinzhou
 */
class DefaultMapping extends MappingModule {

  def binding(): Unit = {
    defaultIdGenerator("auto_increment")

    bind[EvaluateSwitch].on(e => declare(
      e.endOn & e.beginOn & e.opened & e.semester & e.questionnaire are notnull))

  }

}
