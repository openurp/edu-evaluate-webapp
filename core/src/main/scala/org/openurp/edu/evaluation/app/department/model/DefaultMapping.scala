package org.openurp.edu.evaluation.app.department.model

import scala.reflect.runtime.universe
import org.beangle.commons.model.bind.Mapping
import org.openurp.edu.evaluation.app.department.model.EvaluateSwitch

/**
 * @author xinzhou
 */
class DefaultMapping extends Mapping {

  def binding(): Unit = {
    defaultIdGenerator("auto_increment")

    //lesson result

    bind[EvaluateSwitch].on(e => declare(
      e.endOn & e.beginOn & e.opened & e.semester & e.questionnaire are notnull))

  }

}
